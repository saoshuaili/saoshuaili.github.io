---
title: APUE-10-信号  
date: 2022-12-04 02:38:18  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 引言-异步事件处理的两种方式

## 异步事件的处理
1. 查询法
2. 通知法

### 一、信号实现并发
1. 信号的概念
      信号是软件中断。
2. signal();
3. 信号的不可靠
4. 可重入函数
5. 信号的响应过程（重点）
6. 常用函数
   + kill();
   + raise();
   + alarm();
   + pause();
   + abort();
   + system();
   + sleep问题
7. 信号集
8. 信号屏蔽字/pending集的处理
9. 扩展：
   + sigsuspend();
   + sigaction();
   + setitimer();
10. 实时信号

### 二、多线程实现并发



# 系统日志

## 系统日志所在位置：
```shell
/var/log
```

## syslog设施
简单来说，就是所有文件（包括守护进程等）都将信息全部发送给syslogd，然后让其控制格式进行统一的输出。

## 3种产生日志消息的方法
1. 内核例程可以调用log函数
2. 大多数用户进程（守护进程）调用syslog(3)函数来产生日志消息
3. 将日志信息发送给UDP端口514


### syslog系列函数
```c
#include <syslog.h>        //可选的，如果不调用，则会第一次调用syslog时调用。

void openlog(const char *ident, int option, int facility);

void syslog(int priority, const char *format, ...);

void closelog(void);    //也是可选的，因为它只是关闭层被用于与syslogd的守护进程进行通信的描述符

int setlogmask(int maskpri);
```
# 信号的基本概念

## 1 引言
信号是软件中断。
信号提供了一种处理异步事件的方法。
也就是说，系统有很多时刻会产生的情况是无法预料的，通过信号机制可以随时对突发事件进行处理。

## 2 信号概念
信号是异步事件的经典实例。产生信号的时间对进程而言是随机出现。

### 内核对信号的处理操作
1. 忽略此信号
   大多数信号都可以使用这种方式进行处理，但有两种信号却绝不能被忽略。它们是SIGKILL和SIGSTOP。
2. 捕捉信号
3. 执行系统默认动作

### core文件
注意到，系统的默认动作大部分都是终止或者终止+core。那么这个core文件是什么呢？
core文件其实就是程序的某个现场（大部分都是出错现场）


## 3 函数signal
UNIX系统信号机制最简单的接口是signal函数。形式如下：
```c
#include <signal.h>

void (*signal(int signo, void (*func)(int)))(int);
```
参数解释：
在这里，signo是函数名，参考图10-1。
func的值有三种情况：
1. SIG_IGN（signal ignore)，表示忽略该信号
2. SIG_DFL (signal default)，表示系统默认动作
3. 指定函数地址：则在信号发生时，调用该函数，这种处理被称为**捕捉信号**，称此函数为信号处理程序或信号捕捉程序。
### 等价替换
可将上面的表达式替换为：
```c
typedef void Sigfunc(int);

Sigfunc *signal(int, Sigfunc *);
```
解释：
这里的形式和常规的不太一样，在常规的typedef中，更类似于：
```c
typedef double wage;    //创建一个叫wage的别名，其实wage就是double的意思
```
但是在这里，
```c
typedef void Sigfunc(int);
```
在这里的意思是定义一种指针类型Sigfunc，它是一种指向函数void (int) 的指针，也就是说Sigfunc表示的是这种类型的函数的地址，因为：
函数名不是其类型的一部分，函数的类型只由它的返回值和参数表决定.
因此在这里，我们可以通过Sigfunc来指向不同的类型为void (int)的函数，例如：
```c
#include <stdio.h>
#include <stdlib.h>

int abs(int abs){
  if(abs > 0){
    return abs;
  } else{
    return -abs;
  }
}


int main(){

  typedef int (*Sigfunc)(int);
  Sigfunc my_abs = abs;
  printf("%d\n", my_abs(-1));

  return 0;
}
```
### 举例1（忽略中断信号）
```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

int main(){

  int i;

  signal(SIGINT, SIG_IGN);

  for(int i = 0; i < 10; i++){
    write(1, "*", 1);
    sleep(1);
  }


  exit(0);
}
```
可以看到，在这里我们使用了signal信号，设定了接收到SIGINT（signal interrupt）时处理的动作是SIG_IGN（signal ignore），那么当接受到（CTRL+C）的时候程序就不会中断了，会继续输出直到结束。
输出：
![IMAGE](resources/1DD53AD6BEF2155AE6D59D83D2B57A4F.jpg =208x49)
就像我们预期的那样，中间那个^C即使出现了也不会又反应。

### 举例2（执行函数地址来处理信号）
```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
static void int_handler(int s){
  write(1,"1",1);
}

int main(){

  int i;

  signal(SIGINT, int_handler);       //给入口地址

  for(int i = 0; i < 10; i++){
    write(1, "*", 1);
    sleep(1);
  }


  exit(0);
}
```
可以看到，当SIGINT信号到时，指定了一个函数的入口地址来处理，处理方式是打印一个1.
输出：
![IMAGE](resources/D211813D0F7E0ED70777180DAA7744D5.jpg =194x51)

#### 注意：
**信号会打断阻塞的系统调用**

### 书中例子（fig10.2）
```c
#include "apue.h"

static void	sig_usr(int);	/* one handler for both signals */

int
main(void)
{
	if (signal(SIGUSR1, sig_usr) == SIG_ERR)
		err_sys("can't catch SIGUSR1");
	if (signal(SIGUSR2, sig_usr) == SIG_ERR)
		err_sys("can't catch SIGUSR2");
	for ( ; ; )
		pause();
}

static void
sig_usr(int signo)		/* argument is signal number */
{
	if (signo == SIGUSR1)
		printf("received SIGUSR1\n");
	else if (signo == SIGUSR2)
		printf("received SIGUSR2\n");
	else
		err_dump("received signal %d\n", signo);
}
```
解释：
当信号来临时，signal会自动进行捕捉，然后将交给sig\_usr进行处理，如果处理结果是SIG\_ERR，那么打印不能捕捉，如果是其他信号则按照默认方式处理。
输出：
![IMAGE](resources/3F816E69971AA60325437504C2D5EB13.jpg =432x343)

可以看到，我们发送SIGUSR信号1和2时，会打印接收到信号，但是不会结束，而直接kill的时候会结束，这是因为没有对kill信号（SIGTEAM）设置相应的捕捉方式，而是按照默认方式进行操作，所以就直接终止了。

#### 注意
exec函数将原先设置为要捕捉的信号都更改为默认动作，其他信号的状态则保持不变。


## 4 不可靠信号
### 不可靠概念
信号可能会丢失：
一个信号发生了，但进程却可能一直不知道这一点。

# 2 可重入函数

所有的系统调用都是可重入的。
一部分库函数是可重入的。如：memcpy
进程捕捉到信号并对其进行处理时，进程正在执行的正常指令序列就被信号处理程序临时中断，它首先执行该信号处理程序中的指令。
如果从信号处理程序返回（例如没有调用 exit 或longjmp），则继续执行在捕捉到信号时进程正在执行的正常指令序列（这类似于发生硬件中断时所做的）。
但在信号处理程序中，不能判断捕捉到信号时进程执行到何处。如果进程正在执行malloc，在其堆中分配另外的存储空间，而此时由于捕捉到信号而插入执行该信号处理程序，其中又调用malloc，这时会发生什么？又例如，若进程正在执行getpwnam（见6.2节）这种将其结果存放在静态存储单元中的函数，其间插入执行信号处理程序，它又调用这样的函数，这时又会发生什么呢？在malloc例子中，可能会对进程造成破坏，因为malloc通常为它所分配的存储区维护一个链表，而插入执行信号处理程序时，进程可能正在更改此链表。在getpwnam的例子中，返回给正常调用者的信息可能会被返回给信号处理程序的信息覆盖。
![](https://pic.downk.cc/item/5fc636d9f81f7e3bd97f4929.jpg)

Single UNIX Specification说明了在信号处理程序中保证调用安全的函数。这些函数是可重入的并被称为是异步信号安全的（async-signal safe）。
除了可重入以外，在信号处理操作期间，它会阻塞任何会引起不一致的信号发送。图10-4列出了这些异步信号安全的函数。没有列入图10-4中的大多数函数是不可重入的，
因为
+ 已知它们使用静态数据结构；
+ 它们调用 malloc 或free；
+ 它们是标准I/O函数。标准I/O库的很多实现都以不可重入方式使用全局数据结构。注意，虽然在本书的某些实例中，信号处理程序也调用了printf函数，但这并不保证产生所期望的结果，信号处理程序可能中断主程序中的printf函数调用。

应当了解，即使信号处理程序调用的是图10-4中的函数，但是由于每个线程只有一个errno变量（回忆1.7节对errno和线程的讨论），所以信号处理程序可能会修改其原先值。考虑一个信号处理程序，它恰好在main刚设置errno之后被调用。如果该信号处理程序调用read这类函数，则它可能更改errno的值，从而取代了刚由main设置的值。因此，作为一个通用的规则，当在信号处理程序中调用图10-4中的函数时，应当在调用前保存errno，在调用后恢复errno。（应当了解，经常被捕捉到的信号是SIGCHLD，其信号处理程序通常要调用一种wait函数，而各种wait函数都能改变errno。）

## 实例
```c
#include <pwd.h>
#include "apue.h"

static void my_alarm(int signo){
    struct passwd *rootptr;

    printf("in signal handler\n");
    if((rootptr = getpwnam("root")) == NULL){
        err_sys("getpwnam(root) error");
    }
    alarm(1);
}

int main(void){
    struct passwd *ptr;

    signal(SIGALRM, my_alarm);
    alarm(1);
    for(; ;){
        if((ptr = getpwnam("sar")) == NULL){
            err_sys("getpwnam error");
        }
        if(strcmp(ptr->pw_name,"sar") != 0){
            printf("return value corrupted, pw_name = %s\n",ptr->pw_name);
        }
    }
}
```
解析：
在这里，main进程调用alarm函数使得每秒产生一次SIGALAM信号，并且被signal函数定向到my_alarm进程中，但是my_alarm中有petpwnam这个不可重入函数，因此执行的结果是异常的。（不可预测）
输出：
![](https://pic.downk.cc/item/5fc6369df81f7e3bd97f3888.jpg)

# 3.1 守护进程

守护进程(daemon)是生存期长的一种进程。它们常常在系统引导装入时启动，仅在系统关闭时才终止。
因为它们没有控制终端，所以说它们是在后台进行的。UNIX系统有很多守护进程，它们执行日常事务活动。

## 守护进程的特征
我们执行
```
ps -axj
```
命令。
参数说明：
-a：显示由其他用户所拥有的进程的状态
-x：显示没有控制终端的进程状态
-j：显示与作业有关的信息。
部分输出：
![](https://pic.downk.cc/item/5fc6371af81f7e3bd97f56b0.jpg)
解释：
USER是用户，PID是进程ID，PPID是父进程ID，PGID是组ID，SESS是会话ID，TT是表明是否由控制终端控制。

1. 守护进程脱离控制终端（因此TT应该是？）
2. 守护进程的父进程一定得是init进程（因为守护进程一直在运行，因此若让父进程一直等待则会造成很多资源损耗，因此直接让父进程fork之后setsid，然后销毁父进程，将守护进程托管给init进程）
3. 守护进程PID=PGID=SESS

### 编程规则
1. 调用umask将文件模式创建屏蔽字设置为一个已知值（通常是0）；
2. 调用fork然后使父进程exit
3. 调用setsid创建一个新会话。然后执行9.5节中列出的三个步骤：
+ 成为新会话的首进程
+ 成为一个新进程组的组长进程
+ 没有控制终端
4. 将当前目录更改为根目录。
5. 关闭不再需要的文件描述符

### 举例
```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/resource.h>
#include <fcntl.h>
#include <sys/stat.h>

#define FNAME "/tmp/out"

static int daemonize(void){
  pid_t pid;
  int fd;
  //实现守护进程
  pid = fork();
  if(pid < 0){
  perror("fork()");
  return -1;
 }
  if(pid > 0){
   exit(0);
 }
  fd = open("/dev/null",O_RDWR);
  if(fd < 0){
    perror("1");
    exit(1);
  }

  dup2(fd,0);
  dup2(fd,1);
  dup2(fd,2);
  if(fd > 2){
    close(fd);
  }

  setsid();
  chdir("/");     //设置到根目录位置

  umask(0);

  return 0;
}


int main(){

  FILE *fp;

  if(daemonize()){  //表示自己的函数名字，用它来实现daemon
    exit(1);
  }

 fp = fopen(FNAME,"w");
 if(fp == NULL){
   perror("");
   exit(1);
 }
  //做任务，目的是不间断每秒钟往文件打印一个数字
 for(int i = 0; ; i++){
   fprintf(fp,"%d",i);
   sleep(1);
 }
  exit(0);
}
```

![](https://pic.downk.cc/item/5fc6367bf81f7e3bd97f069d.jpg)
![](https://pic.downk.cc/item/5fc63659f81f7e3bd97ee582.jpg)
![](https://pic.downk.cc/item/5fc636fdf81f7e3bd97f4fc7.jpg)
解释：
可以看到，这里PID和PGID都是15252，是相等的，其实其SID也相等，但是并没有显示出来，并且没有控制终端，所以其是一个标准的守护进程。
查看我们重定向到的/tmp/out文件

# 3.2 kill,raise,alarm和pause函数

## 1. 函数kill和raise
kill函数将信号发送给进程或进程组。raise函数则允许进程向自身发送信号。
### 注意
kill只发送信号，不杀死信号，很多情况下进程会因为收到kill信号终止是因为很多信号处理的默认行为就是终止线程。

raise最初是由ISO C定义的。后来，为了与ISO C标准保持一致，POSIX.1也包括了该函数。但是POSIX.1扩展了raise的规范，使其可处理线程（12.8中讨论线程如何与信号交互）。
因为ISO C并不涉及多进程，所以它不能定义以进程ID作为其参数（如kill函数）的函数。
```c
#include <signal.h>
int kill(pid_t pid, int signo);
int raise(int signo);
//两个函数返回值：若成功，返回0；若出错，返回−1
```
调用
raise(signo);
等价于调用
kill(getpid(), signo);
### kill的pid参数有以下4种不同的情况。
+ pid > 0 将该信号发送给进程ID为pid的进程。
+ pid == 0 将该信号发送给与发送进程属于同一进程组的所有进程（这些进程的进程组 ID等于发送进程的进程组ID），而且发送进程具有权限向这些进程发送信号。这里用的术语“所有进程”不包括实现定义的系统进程集。对于大多数UNIX系统，系统进程集包括内核进程和init（pid为1）。
+ pid < 0 将该信号发送给其进程组ID等于pid绝对值，而且发送进程具有权限向其发送信号的所有进程。如前所述，所有进程并不包括系统进程集中的进程。
+ pid == −1 将该信号发送给发送进程有权限向它们发送信号的所有进程。如前所述，所有进程不包括系统进程集中的进程。

如前所述，进程将信号发送给其他进程需要权限。超级用户可将信号发送给任一进程。对于非超级用户，其基本规则是发送者的实际用户 ID 或有效用户 ID 必须等于接收者的实际用户 ID或有效用户ID。  
如果实现支持_POSIX_SAVED_IDS（如POSIX.1现在要求的那样），则检查接收者的保存设置用户ID（而不是有效用户ID）。在对权限进行测试时也有一个特例：如果被发送的信号是SIGCONT，则进程可将它发送给属于同一会话的任一其他进程。  
POSIX.1将信号编号0定义为空信号。如果signo参数是0，则kill仍执行正常的错误检查，但不发送信号。这常被用来确定一个特定进程是否仍然存在。如果向一个并不存在的进程发送空信号，则kill返回−1，errno被设置为ESRCH。但是，应当注意，UNIX系统在经过一定时间后会重新使用进程ID，所以一个现有的具有所给定进程ID的进程并不一定就是你所想要的进程。  
还应理解的是，测试进程是否存在的操作不是原子操作。在kill向调用者返回测试结果时，原来已存在的被测试进程此时可能已经终止，所以这种测试并无多大价值。  
?: 如果调用kill为调用进程产生信号，而且此信号是不被阻塞的，那么在kill返回之前， signo或者某个其他未决的、非阻塞信号被传送至该进程。（对于线程而言，还有一些附加条件；详细情况见12.8节。）


## 2. alarm
使用alarm函数可以设置一个定时器（闹钟时间），在将来的某个时刻该定时器会超时。当定时器超时时，产生 SIGALRM信号。如果忽略或不捕捉此信号，则其默认动作是**终止调用该alarm函数的进程**。

### 函数形式
```c
#include <unistd.h>
unsigned int alarm(unsigned int seconds);
//返回值：0或以前设置的闹钟时间的余留秒数
```
每个进程只能有一个闹钟时间。如果在调用alarm时，之前已为该进程注册的闹钟时间还没有超时，则该闹钟时间的余留值作为本次alarm函数调用的值返回。以前注册的闹钟时间则被新值代替。  
如果有以前注册的尚未超过的闹钟时间，而且本次调用的seconds值是0，则取消以前的闹钟时间，其余留值仍作为alarm函数的返回值。  
虽然 SIGALRM 的默认动作是终止进程，但是大多数使用闹钟的进程捕捉此信号。如果此时进程要终止，则在终止之前它可以执行所需的清理操作。如果我们想捕捉 SIGALRM 信号，则必须在调用 alarm 之前安装该信号的处理程序。如果我们先调用alarm，然后在我们能够安装SIGALRM处理程序之前已接到该信号，那么进程将终止。
### alarm举例
```c
#include <stdio.h>
#include <stdlib.h>
int main(){
  alarm(5);
  while(1);    //不让进程直接终止了
  exit(0);
}
```
输出：
运行5s之后出现：


### alarm举例2
```c
#include <stdio.h>
#include <stdlib.h>

int main(){
  alarm(10);
  alarm(1);
  alarm(5);
  //alarm(1)和alarm(5)都是无效的
  while(1);
  exit(0);
}
```
## 3. pause函数
### 作用
pause函数使调用进程挂起直至捕捉到一个信号。
### 函数形式
```c
#include <unistd.h>
int pause(void);
//返回值：−1，errno设置为EINTR
```
只有执行了一个信号处理程序并从其返回时，pause才返回。在这种情况下，pause返回−1， errno设置为EINTR。

## 举例：自己实现一个sleep（fig10.7)
```c
#include	<signal.h>
#include	<unistd.h>

static void
sig_alrm(int signo)
{
	/* nothing to do, just return to wake up the pause */
}

unsigned int
sleep1(unsigned int seconds)
{
	if (signal(SIGALRM, sig_alrm) == SIG_ERR)
		return(seconds);
	alarm(seconds);		/* start the timer */
	pause();			/* next caught signal wakes us up */
	return(alarm(0));	/* turn off timer, return unslept time */
}
```
### 存在问题
1. 如果在调用sleep1之前，调用者已设置了闹钟，则它被sleep1函数中的第一次alarm调用擦除。可用下列方法更正这一点：检查第一次调用 alarm 的返回值，如其值小于本次调用alarm的参数值，则只应等到已有的闹钟超时。如果之前设置的闹钟超时时间晚于本次设置值，则在sleep1函数返回之前，重置此闹钟，使其在之前闹钟的设定时间再次发生超时。
2. 该程序中修改了对 SIGALRM 的配置。如果编写了一个函数供其他函数调用，则在该函数被调用时先要保存原配置，在该函数返回前再恢复原配置。更正这一点的方法是：保存signal函数的返回值，在返回前重置原配置。
3. 在第一次调用alarm和pause之间有一个竞争条件。在一个繁忙的系统中，可能alarm在调用pause之前超时，并调用了信号处理程序。如果发生了这种情况，则在调用pause后，如果没有捕捉到其他信号，调用者将永远被挂起。

## 举例：自己实现一个sleep（改进：fig10.8）
```c
#include	<setjmp.h>
#include	<signal.h>
#include	<unistd.h>

static jmp_buf	env_alrm;

static void
sig_alrm(int signo)
{
	longjmp(env_alrm, 1);
}

unsigned int
sleep2(unsigned int seconds)
{
	if (signal(SIGALRM, sig_alrm) == SIG_ERR)
		return(seconds);
	if (setjmp(env_alrm) == 0) {
		alarm(seconds);		/* start the timer */
		pause();			/* next caught signal wakes us up */
	}
	return(alarm(0));		/* turn off timer, return unslept time */
}
```

# 4 信号集

## 函数形式
```c
#include <signal.h>

int sigaddset(sigset_t *set, int signo);

int sigdelset(sigset_t *set, int signo);

int sigemptyset(sigset_t *set);

int sigfillset(sigset_t *set);

int sigismember(const sigset_t *set, int signo);
```
这里的set就是信号集
注意：
这里假定一种实现有31种信号和32位整型。也就是说通过sigset_t定义得到的对象都是一个由31个信号，每种信号有32位整型的对象。不同之处在于整型的设置：
1. sigaddset：将某一位整型设置为1
2. sigdelset：将某一位整型设置为0
3. sigemptyset：将所有位的整型都设置为0
4. sigfillset：将所有位都设置为1
5. sigismember：测试一个指定的位

## 函数实现
```c
#include	<signal.h>
#include	<errno.h>

/*
 * <signal.h> usually defines NSIG to include signal number 0.
 */
#define	SIGBAD(signo)	((signo) <= 0 || (signo) >= NSIG)

int
sigaddset(sigset_t *set, int signo)
{
	if (SIGBAD(signo)) {
		errno = EINVAL;
		return(-1);
	}
	*set |= 1 << (signo - 1);		/* turn bit on */
	return(0);
}

int
sigdelset(sigset_t *set, int signo)
{
	if (SIGBAD(signo)) {
		errno = EINVAL;
		return(-1);
	}
	*set &= ~(1 << (signo - 1));	/* turn bit off */
	return(0);
}

int
sigismember(const sigset_t *set, int signo)
{
	if (SIGBAD(signo)) {
		errno = EINVAL;
		return(-1);
	}
	return((*set & (1 << (signo - 1))) != 0);
}
```

## sigprocmask
参考[补充-重点：信号的响应过程](https://zhuanlan.zhihu.com/p/137364536)，这个其实就是修改位图中的masks函数：
### 函数形式如下
```c
#include <signal.h>

int sigprocmaks(int how, const sigset_t *restrict set, sigset_t *restrict oset);
```
参数说明：
oset：若oset是非空指针，那么进程的当前信号屏蔽字(也就是masks)通过oset返回。
set：若set是一个非空指针，那么how参数指示如何修改当前信号屏蔽字。
注意：
不能阻塞SIGKILL和SIGSTOP信号。
SIG\_BLOCK是或操作，SIG\_SETMASK是赋值操作。
若set是个空指针，则不能改变进程的信号屏蔽字，此时how无意义。

### 举例
更改star.c
```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

int main(){

  signal(SIGINT, SIG_IGN);

  while(1){
    for(int i = 0; i < 5; i++){
      write(1, "*", 1);
      sleep(1);
    }
    printf("\n");
  }

  exit(0);
}
```


#### 输出：
![](https://pic.downk.cc/item/5fc63669f81f7e3bd97eec0f.jpg)  
每5个星都换行。并且CTRL+C不会中断进程。
#### 问题：
CTRL+C虽说不会中断进程，但是会中断系统调用，也就是说会中断等待的那一秒钟，从而直接输出下一个※
#### 注意：
若需要中断键入CTRL+\

### 更改要求：
在行内输出的时候不被CTRL+C的影响，但是一行输出完之后可以。
```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

int int_handler(int signo){
  write(1,"!",1);
}


int main(){

  sigset_t set;

  signal(SIGINT, int_handler);
  sigemptyset(&set);
  sigaddset(&set,SIGINT);

  while(1){
    sigprocmask(SIG_BLOCK, &set, NULL);
    for(int i = 0; i < 5; i++){
      write(1, "*", 1);
      sleep(1);
    }
    write(1,"\n",1);
    sigprocmask(SIG_UNBLOCK,&set, NULL);
  }

  exit(0);
}
```

#### 输出：
![](https://pic.downk.cc/item/5fc63722f81f7e3bd97f5881.jpg)
#### 解释：
在每行的打印期间阻塞了信号，只有当每行结束之后换行之前解除阻塞，此时才会开始进行信号的处理。
#### 注意：
1. 无论打多少次CTRL+C都只响应一次，因为pending只能被置为一次。
2. 这里的set是一个sigset_t类型的集合，第12行到第16行都是对该集合进行初始化和添加信号等操作。

### 继续改进
因为若在搭建工程的时候，需要确保进入的状态和出去的状态是相同的，也就是说进入状态若是阻塞的，就需要保证出去的状态是阻塞的。
```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

int int_handler(int signo){
  write(1,"!",1);
}

int main(){

  sigset_t set;
  sigset_t saveset;

  signal(SIGINT, int_handler);
  sigemptyset(&set);
  sigaddset(&set,SIGINT);
  sigprocmask(SIG_UNBLOCK,&set,&saveset);       //保存状态

  while(1){
    sigprocmask(SIG_BLOCK, &set, NULL);
    for(int i = 0; i < 5; i++){ write(1, "*", 1);
      sleep(1);
    }
    write(1,"\n",1);
    sigprocmask(SIG_UNBLOCK,&set, NULL);
  }
  sigprocmask(SIG_SETMASK, &saveset, NULL); //恢复状态

  exit(0);
}
```

# 补充：信号的响应过程

## 引言
信号的响应机制非常重要，必须首先理解信号的响应机制才能进一步研究，在这里非常详细地给出了信号的响应机制！

## 以之前给出的例子为例：
```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>


static void int_handler(int s){
  write(1,"1",1);
}

int main(){

  int i;

  signal(SIGINT, int_handler);       //给入口地址

  for(int i = 0; i < 10; i++){
    write(1, "*", 1);
    sleep(1);
  }


  exit(0);
}
```
输出：

![](https://pic.downk.cc/item/5fc63669f81f7e3bd97eec35.jpg)

解释：

按下ctrl+c时不会中断，此时会打印出1并且继续打印*，直到打印完

## 内核为进程维护的两个位图
要理解信号的响应过程，我们需要先知道内核会为进程自动维护两个位图，一个是pending，一个是masks。其中masks是信号的标志位，代表该信号是否会被忽略。pending是信号的待定位，用来提示接受到了信号。并且masks默认设置为1，pending默认设置为0，如下所示：
![](https://pic.downk.cc/item/5fc6364bf81f7e3bd97ee217.jpg)

## 不同函数的入口地址
并且在我们的例子中，主要包含两个函数，一个是main函数，一个是int_handeler函数，它们的入口地址都存放在内存的代码段中。如图所示：
![](https://pic.downk.cc/item/5fc63707f81f7e3bd97f522e.jpg)

## UNIX系统的进程状态
而在UNIX操作系统中，进程主要有两种状态，一种是用户态，一种是内核态，用户态权限很少，并且在发生系统中断时会立刻从用户态进入内核态。如下：
![](https://pic.downk.cc/item/5fc6364cf81f7e3bd97ee222.jpg)

## 信号的响应过程
1. 当我们在键盘上键入CTRL+C时，进程会中断，从用户态进入内核态排队等待。此时pending中相应的信号位会置位1，如下所示：
![](https://pic.downk.cc/item/5fc636f5f81f7e3bd97f4ddd.jpg)
1. 排队结束之后，内核态会重新变为用户态，重定向的地址为main函数的地址，但是此时会做一个非常重要的操作！将masks按位与pending。此时会发现在第3的位置按位与的结果为1，表明收到了该信号，通过signal函数发现处理函数的入口地址变为了int\_handler，所以将地址重定向为int\_handler的地址，并且此时会将masks和pending为上的值都变为0，如下所示：
![](https://pic.downk.cc/item/5fc63661f81f7e3bd97ee75b.jpg)
1. 处理结束之后会重新将masks的值置为1，pending的值置为0，并且最终的地址被重定向为main函数。
![](https://pic.downk.cc/item/5fc63661f81f7e3bd97ee75b.jpg)
这就是个完整的信号响应过程。


### 思考
1. 信号从收到到相应有一个不可避免的延迟
1. 如何忽略掉一个信号的？
2. 标准信号为什么要丢失？
3. 标准信号的响应没有严格的顺序。
### 回答
1. 因为并不是一有信号发送就马上会进入内核态，只有中断之后进入内核态，并且内核态重新回到用户态时才会进行信号的检查。
2. 将masks置为0，那么masks与pending的按位与永远都是0了，也就不会有信号响应了。
3. pending无论收到多少个信号也就只能置为1，因为丢失不会带来影响
注意：如果是实时信号就不能丢失，必须每个信号都响应。
4. 当同时收到多个信号时，pending的多个位都会被置为1，在没有明确确定优先级的情况下，类似优先级的标准信号相应是没有固定顺序的。













