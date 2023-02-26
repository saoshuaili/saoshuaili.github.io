---
title: APUE-8-进程控制  
date: 2022-12-04 02:38:06  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 1 引言
本章介绍UNIX系统的进程控制，包括：
1. 创建新进程
2. 执行进程
3. 进程终止
4. 介绍进程属性的各种ID-实际、有效的用户ID和组ID，以及它们如何受到进程控制原语的影响。
5. 解释器文件和system函数
6. UNIX系统提供的进程会计机制。


## 进程表示
每个进程都有一个非负整型表示的唯一进程ID。因为进程ID标识符是唯一的，常将其用作其他标识符的一部分以保证唯一性（例如，文件名）

系统的一些专用进程：
1. ID=0为调度进程，也称交换进程，也被称为系统进程。因为它是内核的一部分，并不执行任何磁盘上的程序。
2. ID=1为init进程，在自举过程结束时由内核调用。它是一个普通的用户进程，但是它以超级用户特权运行，它绝不会终止。init通常读取与系统有关的初始化文件，并将系统引导到一个状态（如多用户）。
**注意**
在MacOS中，init进程被lauchd进程替代，执行的任务集与init相同，但扩展了功能。

# 2 函数fork系列讲解
```c
#include <unistd.h>

pid_t fork(void);
```
由fork创建的进程被称为子进程。
**重要**：
1. fork函数被调用一次，但返回两次。
两次返回的区别是子进程的返回值为0，而父进程的返回值则是新建子进程的进程ID。
理由：
   父进程的子进程可以拥有很多个，因此需要子进程ID。
   子进程总是能够查看父进程的进程ID，因此没必要。并且进程ID 0总是由内核交换进程使用，所以一个子进程的进程不可能是0.
2. 子进程和父进程继续执行fork调用之后的指令。子进程是父进程的副本。
   例如，子进程获得父进程数据空间、堆和栈的副本。
注意：
   这是子进程的副本，只是一个副本，父进程若改变子进程不会一起改变。

## fork函数举例(标准I/O)
```c
#include <stdio.h>
int main(){
  int i;
  for(i = 0; i < 2; i++){
    fork();
    printf("-");
  }

  wait(NULL);
  wait(NULL);

  return 0;
}
```

共输出8个“-”符号。
解释：
如果你对fork()的机制比较熟悉的话，这个题并不难，输出应该是6个“-”，但是，实际上这个程序会很tricky地输出8个“-”。
要讲清这个题，我们首先需要知道fork()系统调用的特性，
fork()系统调用是Unix下以自身进程创建子进程的系统调用，一次调用，两次返回，如果返回是0，则是子进程，如果返回值>0，则是父进程（返回值是子进程的pid），这是众为周知的。
还有一个很重要的东西是，在fork()的调用处，整个父进程空间会原模原样地复制到子进程中，包括指令，变量值，程序调用栈，环境变量，缓冲区，等等。
所以，上面的那个程序为什么会输入8个“-”，这是因为printf(“-”);语句有buffer，所以，对于上述程序，printf(“-”);把“-”放到了缓存中，并没有真正的输出，在fork的时候，缓存被复制到了子进程空间，所以，就多了两个，就成了8个，而不是6个。
另外，多说一下，我们知道，Unix下的设备有“块设备”和“字符设备”的概念，所谓块设备，就是以一块一块的数据存取的设备，字符设备是一次存取一个字符的设备。磁盘、内存都是块设备，字符设备如键盘和串口。块设备一般都有缓存，而字符设备一般都没有缓存。

## fork函数举例(无缓存I/O)
```c
#include <stdio.h>
#include <apue.h>

char buf[] = "-";

int main(){
  int i;
  for(i = 0; i < 2; i++){
    fork();
    write(STDOUT_FILENO, buf, sizeof(buf)-1);
  }

  wait(NULL);
  wait(NULL);

  return 0;
}
```
输出：（只有6个-）
![IMAGE](resources/21A31C21C0C8B67A8C98C99D17CAE1B0.jpg =574x47)


## fork函数子进程不影响父进程的演示
```c
#include "apue.h"

int		globvar = 6;		/* external variable in initialized data */
char	buf[] = "a write to stdout\n";

int
main(void)
{
	int		var;		/* automatic variable on the stack */
	pid_t	pid;

	var = 88;
	if (write(STDOUT_FILENO, buf, sizeof(buf)-1) != sizeof(buf)-1)
		err_sys("write error");
	printf("before fork\n");	/* we don't flush stdout */

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid == 0) {		/* child */
		globvar++;				/* modify variables */
		var++;
	} else {
		sleep(2);				/* parent */
	}

	printf("pid = %ld, glob = %d, var = %d\n", (long)getpid(), globvar,
	  var);
	exit(0);
}
```
输出：
![IMAGE](resources/2EEAAB236513511BEBD696B5CDAF6C68.jpg =694x264)

注意：
一般来说，在fork之后是父进程先执行还是子进程先执行时不确定的，这取决于内核所使用的调度算法。

## fork失败的两个主要原因
1. 系统中已经有了太多的进程
2. 该实际用户ID的进程总数超过了系统限制

## fork的两种用法
1. 一个父进程希望复制自己
使父进程和子进程同时执行不同的代码段，也就是父进程用来调用fork生成子进程来执行请求。
2. 一个进程要执行一个不同的程序。
这种情况下子进程从fork返回后立即调用exec。


## 函数vfork
vfork函数的调用序列和返回值与fork相同，但两者语义不同。
vfork用来创建一个新进程，而该进程的目的就是执行一个新程序（就是fork的第二种用法）

## vfork和fork的区别
1. vfork不将父进程的地址空间完全复制到子进程中，因为子进程会立即调用exec，于是也就不会引用该地址。
2. vfork保证子进程先运行，在它调用exec或exit之后父进程才可能被调度运行，当子进程调用者两个函数中的任意一个时，父进程会恢复运行。

## vfork函数实例
```c
#include "apue.h"

int		globvar = 6;		/* external variable in initialized data */

int
main(void)
{
	int		var;		/* automatic variable on the stack */
	pid_t	pid;

	var = 88;
	printf("before vfork\n");	/* we don't flush stdio */
	if ((pid = vfork()) < 0) {
		err_sys("vfork error");
	} else if (pid == 0) {		/* child */
		globvar++;				/* modify parent's variables */
		var++;
		_exit(0);				/* child terminates */
	}

	/* parent continues here */
	printf("pid = %ld, glob = %d, var = %d\n", (long)getpid(), globvar,
	  var);
	exit(0);
}
```
### 输出
![](https://pic.downk.cc/item/5fc636fdf81f7e3bd97f4fca.jpg)
### 分析
可以看到，我们先定义了一个变量var=88和全局变量globvar=6，然后该进程调用了子进程来处理接下来进程（通过判断pid是否等于0，因为父进程得到的返回值不是0，但是子进程返回值是0，因此可以处理）。最后通过输出发现子进程改变了父进程的var和glob。   
这是因为子进程在父进程的空间中运行。

### 注意
在这里我们调用了_exit而不是exit，因为_exit不执行标准I/O缓冲区的冲洗操作，如果调用exit，则输出是不确定的，因为它依赖于标准I/O库的实现，有可能发现输出没有变化。

# 3 exit系列函数与wait系列函数

## exit函数、_exit函数和_Exit函数。

在7.3节中介绍了5中进程的正常终止和3种异常终止的方法。 但是无论进程如何终止，最后都会执行内核中的同一段代码。折断代码为相应进程关闭所有打开描述符，释放它所使用的存储器。 并且对于任意一种终止情形，我们都希望终止进程能通知其父进程它是**如何终止**的。 对于三个终止函数（exit、_exit和_Exit），实现将终止进程通知其父类的方法是将其**退出状态**作为参数传送给函数，然后该终止进程的父进程调用wait和waitpid函数取得其**终止状态**。

### 注意

退出状态不等于终止状态 退出状态：是一个**参数**，是传递给三个终止函数的参数，或者main的返回值 终止状态：是一个状态，表明进程已经结束。在最后调用_exit时，内核将退出状态转换为终止状态

### 几种场景

1. 子进程是父进程调用fork后生成的，子进程还会将其终止状态返回给父进程。但是如果父进程在子进程之前终止，又将如何？ 答案： 所有父进程终止的子进程读改变为init（PID=1）的子进程，自身变成**孤儿进程**，被init进程收养。 收养过程： 当一个进程终止时，内核逐个检查所有活动进程，以判断它是否是正要终止进程的子进程，如果是，则该进程的父进程ID就更改为1.
2. 如果子进程在父进程之前小时，那么父进程又如何能在做相应检查时得到子进程的终止状态呢？ 答案： 如果子进程完全小时了额，那么父进程是无法立即知道的，内核会为每个终止进程保存了一定量的信息（例如CPU时间总量等），当终止进程的父进程调用时会得到。这种终止的进程称为**僵死进程**。
3. 一个由init进程收养的进程终止时会发生什么？ 答案： init被编写为无论何时只要有一个子进程终止，init就会调用一个wait函数取得终止状态，这样也就放置了系统塞满僵死进程。

## 函数wait和waitpid

当一个进程正常或异常终止时，内核就向其父进程发送SIGCHLD信号。父进程此时可以选择忽略该信号或者调用wait和waitpid来处理这个进程。 调用wait和waitpid所做的事情：

1. 如果其所有子进程都还在运行，则阻塞
2. 如果一个子进程已终止，整等待父进程获取其终止状态，则取得该子进程的终止状态立即返回。
3. 如果它没有收到任何子进程，则立即出错返回

注意： 如果接收到信号立即调用wait，那么一定会有返回值，如果在随机时间点调用，则进程可能会阻塞。

### wait和waitpid

```shell
include <sys/wait.h>

pid_t wait(int *stattloc);

pid_t waitpid(pid_t pid, int *staloc, int options);
```

### 两个函数的区别：

1. 一个子进程终止前，wait使其调用者阻塞，而waitpid有一选项，可使调用者不阻塞。
2. waitpid并不等待在其调用之后的第一个终止子进程，它有若干个选项，可以控制它所等待的进程。

其中staloc是一个指向终止状态的指针，若不需要终止状态则可以设置为空。

### 两个函数的返回值

![img](https://pic.downk.cc/item/5fc63645f81f7e3bd97ee145.jpg?ynotemdtimestamp=1617478207624)实例： 在这里定义了一个pr_exit函数，使用上图来打印进程终止状态（以后会多次使用）

```
#include "apue.h"
#include <sys/wait.h>

void
pr_exit(int status)
{
	if (WIFEXITED(status))
		printf("normal termination, exit status = %d\n",
				WEXITSTATUS(status));
	else if (WIFSIGNALED(status))
		printf("abnormal termination, signal number = %d%s\n",
				WTERMSIG(status),
#ifdef	WCOREDUMP
				WCOREDUMP(status) ? " (core file generated)" : "");
#else
				"");
#endif
	else if (WIFSTOPPED(status))
		printf("child stopped, signal number = %d\n",
				WSTOPSIG(status));
}
```

可以看到针对不同的宏打印出了不同的值。

#### 演示不同的exit值

```
#include "apue.h"
#include <sys/wait.h>

int
main(void)
{
	pid_t	pid;
	int		status;

	if ((pid = fork()) < 0)
		err_sys("fork error");
	else if (pid == 0)				/* child */
		exit(7);

	if (wait(&status) != pid)		/* wait for child */
		err_sys("wait error");
	pr_exit(status);				/* and print its status */

	if ((pid = fork()) < 0)
		err_sys("fork error");
	else if (pid == 0)				/* child */
		abort();					/* generates SIGABRT */

	if (wait(&status) != pid)		/* wait for child */
		err_sys("wait error");
	pr_exit(status);				/* and print its status */

	if ((pid = fork()) < 0)
		err_sys("fork error");
	else if (pid == 0)				/* child */
		status /= 0;				/* divide by 0 generates SIGFPE */

	if (wait(&status) != pid)		/* wait for child */
		err_sys("wait error");
	pr_exit(status);				/* and print its status */

	exit(0);
}
```

输出： ![img](https://pic.downk.cc/item/5fc63616f81f7e3bd97ed89a.jpg?ynotemdtimestamp=1617478207624)解释图如下： ![img](https://pic.downk.cc/item/5fc6369df81f7e3bd97f38a6.jpg?ynotemdtimestamp=1617478207624)

### 使用wait函数的缺陷和改进-waitpid

wait函数不管共有几个子进程，只要有一个子进程终止就马上终止了，如果要等待一个指定的进程终止（如果知道要等待进程的ID），就没有办法了，此时应该使用waitpid。 回顾一下waitpid函数

```
#include <sys/wait.h>
pid_t waitpid(pid_t pid, int *statloc, int options);
```

#### 参数解释

其中statloc用法和wait相同，pid是我们需要等待的进程。作用如下：

##### 参数pid

1. pid==-1 等待任一进程，这种情况waitpid和wait相同
2. pid>0 等待进程ID与pid相等的子进程
3. pid==0 等待组ID等于调用进程组ID的任一子进程。
4. pid<-1 等待组ID等于pid绝对值的任一子进程

##### 参数options

options参数使我们能进一步控制waitpid操作 ![img](https://pic.downk.cc/item/5fc6360cf81f7e3bd97ed72e.jpg?ynotemdtimestamp=1617478207624)

##### 返回值：

函数的返回值是终止子进程的进程ID，并将该子进程的终止状态存放在由statloc指向的存储单元中。

### fork一个子进程但是不等待僵死进程的办法

回忆之前介绍的**僵死进程**：
在UNIX属于中，一个已经终止、但是其父进程尚未对其进行善后处理的进程的进程被称为僵死进程。

#### 僵尸进程的危害：

太多的僵尸进程会导致系统不能产生新的进程。 如果一个进程fork一个子进程但是不要它等待子进程终止，也不希望子进程处于僵死状态直到父进程终止，实现这一要求的诀窍：**调用fork两次**

#### 举例：

```
#include "apue.h"
#include <sys/wait.h>

int
main(void)
{
	pid_t	pid;

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid == 0) {		/* first child */
		if ((pid = fork()) < 0)
			err_sys("fork error");
		else if (pid > 0)
			exit(0);	/* parent from second fork == first child */

		/*
		 * We're the second child; our parent becomes init as soon
		 * as our real parent calls exit() in the statement above.
		 * Here's where we'd continue executing, knowing that when
		 * we're done, init will reap our status.
		 */
		sleep(2);
		printf("second child, parent pid = %ld\n", (long)getppid());
		exit(0);
	}

	if (waitpid(pid, NULL, 0) != pid)	/* wait for first child */
		err_sys("waitpid error");

	/*
	 * We're the parent (the original process); we continue executing,
	 * knowing that we're not the parent of the second child.
	 */
	exit(0);
}
```

执行结果： ![img](https://pic.downk.cc/item/5fc63689f81f7e3bd97f1e25.jpg?ynotemdtimestamp=1617478207624)解释： 如果了解上一个例题之后这个例题其实很简单 ![img](https://pic.downk.cc/item/5fc63616f81f7e3bd97ed895.jpg?ynotemdtimestamp=1617478207624)可以看到，子进程2打印的parent ID是1，也就是init进程，这个时候直接是由init进程托管的，不会出现僵死进程。

### 注意：

子进程1 fork之后子进程1和子进程2的执行顺序是不确定的。因此若是先执行子进程2，那么马上调用了sleep，执行到子进程1时马上终止。若是先执行子进程1则马上终止，执行到子进程2时sleep，这样可以确保无论什么顺序子进程2 sleep结束之后子进程1已经终止

## 函数waitid

waitid函数类似waitpid，但是提供了更多的灵活性。

### 函数形式：

```
#include <sys/wait.h>

int waitid(idtype_t idtype, id_t id, siginfo_t *infop, int options);
```

### 返回值：

若成功返回0，出错返回-1.

与waitpid相似，waitid允许一个进程指定要等待的子进程，但它使用两个单独的参数表示要等待的子进程所属的类型，而不是将此与进程ID或进程组ID组成一个参数。id参数的作用于idtype的值相关。
![img](https://pic.downk.cc/item/5fc63673f81f7e3bd97efa40.jpg?ynotemdtimestamp=1617478207624)

## 竞争条件

先来了解两个名词：

### 竞争条件

多个进程都企图对共享数据进行某种处理，而最后的结果又取决于进程运行的顺序时，就认为发生了**竞争条件**。

### 轮询

如果一个进程需要等待其父进程终止，则可以使用下列形式的循环：

```
while(getppid()!=1){
   sleep(1);
}
```

这种形式的循环称为轮询。它的问题是浪费了CPU时间（因为CPU不断在进行条件测试）。 注意： 若一个进程希望等待其子进程终止，则必须调用wait系列函数中的一个，这里没有用到轮询机制。

### 避免竞争条件和轮询的办法

为了避免竞争条件，在多个进程之间需要有某种形式的信号发送和接收的方法：

1. UNIX系统可以使用信号机制
2. 各种形式的进程间通信（IPU）也可以使用 具体的在后面的章节会介绍。

### 竞争条件实例

```
#include "apue.h"

static void charatatime(char *);

int
main(void)
{
	pid_t	pid;

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid == 0) {
		charatatime("output from child\n");
	} else {
		charatatime("output from parent\n");
	}
	exit(0);
}

static void
charatatime(char *str)
{
	char	*ptr;
	int		c;

	setbuf(stdout, NULL);			/* set unbuffered */
	for (ptr = str; (c = *ptr++) != 0; )
		putc(c, stdout);
}
```

输出： 不知为何我的macos系统一直执行结果都是output from parent先。 ![img](https://pic.downk.cc/item/5fc636b8f81f7e3bd97f4146.jpg?ynotemdtimestamp=1617478207624)（可能是因为我电脑性能太好？ 哈哈哈） 理论上的输出： <img src="https://pic.downk.cc/item/5fc63645f81f7e3bd97ee140.jpg?ynotemdtimestamp=1617478207624" alt="img" style="zoom:50%;" />

### 解决竞争问题实例

例如需要先让parent进程执行，那么就可以在子进程的执行语句中加上WAIT_PARENT()：

```
#include "apue.h"

static void charatatime(char *);

int
main(void)
{
	pid_t	pid;

	TELL_WAIT();

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid == 0) {
		WAIT_PARENT();		/* parent goes first */
		charatatime("output from child\n");
	} else {
		charatatime("output from parent\n");
		TELL_CHILD(pid);
	}
	exit(0);
}

static void
charatatime(char *str)
{
	char	*ptr;
	int		c;

	setbuf(stdout, NULL);			/* set unbuffered */
	for (ptr = str; (c = *ptr++) != 0; )
		putc(c, stdout);
}
```

可以看到在15行输入了WAIT_PARENT();并在19行TELL_CHILD()。 因此每次可以等待父进程先执行。

### 输出：

![img](https://pic.downk.cc/item/5fc636d9f81f7e3bd97f4935.jpg?ynotemdtimestamp=1617478207624)可以看到每次都是parent先。 这里因为效果和上面的相同，我将其改为先让子进程执行，那么就应该是在父进程中加入WAIT_CHILD();并在子进程结束之后加上TELL_PARENT(); 如下：

```
#include "apue.h"

static void charatatime(char *);

int
main(void)
{
	pid_t	pid;

	TELL_WAIT();

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid == 0) {
		charatatime("output from child\n");
    TELL_PARENT(pid);
	} else {
    WAIT_CHILD();
		charatatime("output from parent\n");
	}
	exit(0);
}

static void
charatatime(char *str)
{
	char	*ptr;
	int		c;

	setbuf(stdout, NULL);			/* set unbuffered */
	for (ptr = str; (c = *ptr++) != 0; )
		putc(c, stdout);
}
```

输出： ![img](https://pic.downk.cc/item/5fc63610f81f7e3bd97ed7af.jpg?ynotemdtimestamp=1617478207624)

# 4 exec系列函数

## 作用
用fork函数创建新的子进程后，子进程往往要调用一种exec函数以执行另一个程序。当进程调用一种exec函数时，该进程执行的程序完全替换为新程序，而新程序则从其main函数开始执行。
## 特点：
1. 调用exec函数并不创建新进程，所以前后的进程ID并未改变
2. exec用磁盘上的一个新程序替换了当前进程的正文段、数据段、堆段和栈段
## 函数形式
```c
#include <unistd.h>

int execl(const char *pathname, const char *arg0, ... /* (char *)0 */;  //l代表列表list

int execv(const char *pathname, char *const argv[]);,       //v表示矢量vector

int execle(const char *pathname, const char *arg0, ... /* (char *)0, char *const envp[] */ );          //e代表environment，表明可以导入环境变量
 
int execve(const char *pathname, char *const argv[], char *const envp[]);

int execlp(const char *filename, const char *arg0, ... /* (char *)0 */;

int execvp(const char *filename, char *const argv[]];,

int fexecve(int fd, char *const argv[], char *const envp[]);
```
返回值：
若出错则返回-1，若成功则不返回。
### 参数说明
pathname

前4个函数参数为路径名

filename

两个以filename为参数的函数（execlp和execvp），如果包含“/”，则就将其视为路径名.则就按PATH环境变量，在它所指定的各目录中搜寻可执行文件,
#### PATH介绍
PATH变量包含了一张目录表（称为路径前缀），目录之间用冒号（:）分隔。例如：
PATH=/bin:/usr/bin:/usr/local/bin:.
因为大部分的可执行文件都放在bin中，因此默认为bin文件夹下，并且最后的.表示当前文件夹。
##### 注意：
若execlp或exevcvp使用路径前缀中的一个找到了一个可执行文件，但是该文件不是由连接编辑器产生的机器可执行文件，则就认为该文件时一个shell脚本，于是尝试调用/bin/sh，并以该filename作为shell的输入。
#### args介绍
execl、execlp和execle是l（列表）系列：输入参数args就是新可执行二进制文件的输入参数。例如我们选中的可执行二进制文件为date，命令为date +%s，那么arg0就是"date"，arg1就是"+%s"，并且以**NULL结尾**。
另外四个函数则应该先构造一个指向各参数的指针数组，然后将该数组地址作为这四个函数的参数。
#### 环境表介绍
e表示环境，以e结尾的3个函数（execle、execve和fexecve）都可以传递一个指向环境字符串指针数组的指针。其他四个函数则使用调用进程中的environ变量为新程序复制现有的环境。

##### 注意：
这7个函数只有execve是内核的系统调用。另外6个只是库函数，它们最终都要调用该系统调用。它们的关系如下：
![](https://pic.downk.cc/item/5fc636cef81f7e3bd97f462c.jpg)


### 基本实现举例
```c
#include <unistd.h>
int main(){
  int pid;
  puts("Begin!");
  fflush(NULL);
  pid=fork();
  if(pid < 0){
    perror("fork()");
    exit(1);
  }
  if(pid == 0){
    execl("/bin/date","date","+%s",NULL);
    perror("execl()");
    exit(1);
  }
  wait(NULL);
  puts("End!");
  exit(0);
}
```
解释：
![](https://pic.downk.cc/item/5fc63729f81f7e3bd97f5a02.jpg)

fig8.16.c

```c
#include "apue.h"
#include <sys/wait.h>

char	*env_init[] = { "USER=unknown", "PATH=/tmp", NULL };

int
main(void)
{
	pid_t	pid;

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid == 0) {	/* specify pathname, specify environment */
		if (execle("/home/sar/bin/echoall", "echoall", "myarg1",
				"MY ARG2", (char *)0, env_init) < 0)
			err_sys("execle error");
	}

	if (waitpid(pid, NULL, 0) < 0)
		err_sys("wait error");

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid == 0) {	/* specify filename, inherit environment */
		if (execlp("echoall", "echoall", "only 1 arg", (char *)0) < 0)
			err_sys("execlp error");
	}

	exit(0);
}
```
解释：

![](https://pic.downk.cc/item/5fc636e5f81f7e3bd97f4b5c.jpg)
### 注意：
这里的echoall是任意命令行的意思

# 5 更改用户ID和更改组ID

## 文件权限、用户权限和组权限介绍
文件权限有三种：
r：可写
w：可读
x：可执行
当我们执行
```shell
ls -l /usr/bin/passwd
```
时，我们可以看到
![](https://pic.downk.cc/item/5fc6361ff81f7e3bd97eda23.jpg)
以三个为一组，分别是用户权限、组权限和其他用户权限。  
以用户权限为例，有两种用户权限，一种是real用户权限，一种是effective用户权限，一种是saved set-user-ID（保存的设置用户ID）。  
执行命令判断的时候判断的是effective用户权限。在这里用户权限对应的前三个为-rw，代表该文件可以读写，但是不能执行。
### 若当我们执行一个passwd命令行。
```shell
passwd;     //更改密码
```
查看其权限：
![](https://pic.downk.cc/item/5fc6361ff81f7e3bd97eda23.jpg)

执行该命令时，shell首先fork出一个子进程，然后马上让子进程变成passwd新进程，此时passwd和进程和原来的终端进程的用户ID和组ID都是完全相同的。并且pawwd设置成了u+s权限。
#### u+s权限
一个文件若有u+s权限。针对某个程序任何用户都有读写这个程序的权限，可以像root用户一样操作，这个指令只对程序有效，如果用此权限放在路径上是无效的。
#### g+s权限
一个文件若有g+s权限。它的意思是强制将此群组里的目录下文件编入到此群组中，无论是哪个用户创建的文件。


## Linux shell执行过程
![](https://pic.downk.cc/item/5fc63707f81f7e3bd97f521a.jpg)

## 设置实际和有效用户/组ID
```c
#include <unistd.h>

int setuid(uid_t uid);

int setgid(gid_t gid);
```
## 设置用户ID的规则（和组ID完全相同）
1. 若进程具有超级用户特权，则setuid函数将实际用户ID、有效用于ID以及保存的设置用户ID(saved set-user-ID）设置为uid。
2. 若进程没有超级用户特权，但是uid等于实际用户ID或保存的设置用户ID，则setuid只将有效ID设置为uid。不更改实际用户ID和保存的设置用户ID。
3. 如果上面两个条件都不满足，则errno设置为EPERM，并返回-1.

### 关于r，e和s三种ID，需要注意：
只有超级用户进程可以更改实际用户ID。通常实际用户ID是在用户登录时由login(1)程序设置的，而且绝不会改变它。因为login是一个超级用户进程，当它调用setuid时，设置所有的3个用户ID（具体看上面的Linux shell执行过程图）
总结：
![](https://pic.downk.cc/item/5fc636a7f81f7e3bd97f3e2f.jpg)

## 函数setreuid和setregid
功能是交换实际用户ID和有效用户ID
```c
#include <unistd.h>

int setreuid(uid_t ruid, uid_t euid);

int setregid(gid_t rgid, gid_t egid);
```
若其中任一参数为-1，则表示相应的ID应该保持不变

## 函数seteuid和setegid
功能是改变有效用户ID和有效组ID
```c
#include <unistd.h>

int seteuid(uid_t uid);

int setegid(gid_t gid);
```

## 组ID
上面所介绍的所有函数将u改成g就可以了。

# 5 函数system
在程序中执行一个命令字符串。也就是在一个C程序中调用一行命令行。
```c
#include <unistd.h>

int system(const char *cmdstring)
```
## 返回值
因为system在其实现中调用了fork、exec、waitpid，其实就是few的一个简单封装，因此有3种返回值。
1. fork失败或者waitpid返回除ENTER以外的错误，则system返回-1，并设置errno以指示错误类型
2. 如果exec失败（表示不能执行shell），则返回时如同shell执行了exit(127)
3. 否则所有3个函数都成功，那么system的返回值时shell的终止状态，其格式已在waitpid中说明。

## system函数（不对信号处理）
```c
#include	<sys/wait.h>
#include	<errno.h>
#include	<unistd.h>

int
system(const char *cmdstring)	/* version without signal handling */
{
	pid_t	pid;
	int		status;

	if (cmdstring == NULL)
		return(1);		/* always a command processor with UNIX */

	if ((pid = fork()) < 0) {
		status = -1;	/* probably out of processes */
	} else if (pid == 0) {				/* child */
		execl("/bin/sh", "sh", "-c", cmdstring, (char *)0);
		_exit(127);		/* execl error */
	} else {							/* parent */
		while (waitpid(pid, &status, 0) < 0) {
			if (errno != EINTR) {
				status = -1; /* error other than EINTR from waitpid() */
				break;
			}
		}
	}

	return(status);
}
```
可以看到，其实就是一个简单的对fork、exec和waitpid的一个封装，从而让其执行命令行。

## 举例：调用system函数
```c
#include "apue.h"
#include <sys/wait.h>

int
main(void)
{
	int		status;

	if ((status = system("date")) < 0)
		err_sys("system() error");

	pr_exit(status);

	if ((status = system("nosuchcommand")) < 0)
		err_sys("system() error");

	pr_exit(status);

	if ((status = system("who; exit 44")) < 0)
		err_sys("system() error");

	pr_exit(status);

	exit(0);
}
```
输出：
![](https://pic.downk.cc/item/5fc63658f81f7e3bd97ee576.jpg)


# 6 进程调度
## 友好值
进程可以通过调整友好值选择以更低优先级哦运行（通过调整友好值降低它对CPU的战友，因此该进程是“友好的”）。只有特权进程允许提高调度权限。

## 调整友好值
进程可以通过nice函数获取或者更改它的友好值。使用这个函数，进程只能影响自己的友好值，不能影响其他任何进程的友好值
```c
#include <unistd.h>

int nice(int incr);
```
若成功则返回新的友好值，若出错则返回-1.
参数incr被增加到调用进程的友好值上，如果incr太大，系统会直接把它降到最大合法值，不给出提示；如果太小会直接把它升到最小合法值

函数getpriority可以像nice那样获取进程的友好值，此外，它还可以获取一组相关进程的友好值。
```c
#include <sys/resource.h>

int getpriority(int which, id_t who);
```
若成功返回友好值，失败返回-1.
参数选择：
which：PRIO_PROCESS代表进程，PRIO_PGRP表示进程组，PRIO_USER表示用户ID。

函数setpriority可以为进程、进程组和属于特定用户ID的所有进程设置优先级。
```c
#include <sys/resource.h>

int setpriority(int which, id_t who);
```



# 7 进程时间
最开始就说明了，我们可以度量3个时间：墙上时钟时间、用户CPU时间和系统CPU时间。任一进程都可调用times函数获得它自己以及已终止子进程的上述值。
```c
#include <sys/times.h>

clock_t times(struct tms *buf);
```
返回值：
若成功返回时钟滴答数，若出错返回-1
tms结构体：
```c
struct tms {
   clock_t tms_utimes;        /* user CPU time  */
   clock_t tms_stime;         /* system CPU time */
   clock_t tms_cutime;        /* user CPU time, terminated children */
   clock_t tms_cstime;        /* system CPU time, terminated childern */
}
```

注意：
此结构体没有包含墙上时钟时间。times函数返回墙上时钟时间作为函数值。此值时相对于过去某一个时刻度量的，所以不能用其绝对值而必须使用其相对值。
例如：
调用times，保存其返回值。以后某个时间再次调用times，从新返回的值中减去以前返回的值，这个差值就是墙上时钟时间。
所有由times函数返回的clock_t值都用_SC_CLK_TCK(由sysconf函数返回的每秒时钟滴答数）转换成秒数。

## 实例
```c
#include "apue.h"
#include <sys/times.h>

static void	pr_times(clock_t, struct tms *, struct tms *);
static void	do_cmd(char *);

int
main(int argc, char *argv[])
{
	int		i;

	setbuf(stdout, NULL);
	for (i = 1; i < argc; i++)
		do_cmd(argv[i]);	/* once for each command-line arg */
	exit(0);
}

static void
do_cmd(char *cmd)		/* execute and time the "cmd" */
{
	struct tms	tmsstart, tmsend;
	clock_t		start, end;
	int			status;

	printf("\ncommand: %s\n", cmd);

	if ((start = times(&tmsstart)) == -1)	/* starting values */
		err_sys("times error");

	if ((status = system(cmd)) < 0)			/* execute command */
		err_sys("system() error");

	if ((end = times(&tmsend)) == -1)		/* ending values */
		err_sys("times error");

	pr_times(end-start, &tmsstart, &tmsend);
	pr_exit(status);
}

static void
pr_times(clock_t real, struct tms *tmsstart, struct tms *tmsend)
{
	static long		clktck = 0;

	if (clktck == 0)	/* fetch clock ticks per second first time */
		if ((clktck = sysconf(_SC_CLK_TCK)) < 0)
			err_sys("sysconf error");

	printf("  real:  %7.2f\n", real / (double) clktck);
	printf("  user:  %7.2f\n",
	  (tmsend->tms_utime - tmsstart->tms_utime) / (double) clktck);
	printf("  sys:   %7.2f\n",
	  (tmsend->tms_stime - tmsstart->tms_stime) / (double) clktck);
	printf("  child user:  %7.2f\n",
	  (tmsend->tms_cutime - tmsstart->tms_cutime) / (double) clktck);
	printf("  child sys:   %7.2f\n",
	  (tmsend->tms_cstime - tmsstart->tms_cstime) / (double) clktck);
}
```
执行结果：
![](https://pic.downk.cc/item/5fc6367bf81f7e3bd97f06ad.jpg)





























