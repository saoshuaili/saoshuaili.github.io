---
title: APUE-12-线程控制  
date: 2022-12-04 02:38:37  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 1 线程属性

回顾一下创建线程的函数：
```c
#include <pthread.h>

int pthread_create(pthread_t *restrict tidp, 
         const pthread_attr_t *restrict attr,
         void *(*star_rtn)(void *) void *restrict arg);
         // 返回值：若成功返回0，否则返回错误编号
```
这里的第二个参数attr我们之前是默认的NULL，但是我们可以通过设置来确定自己所需要的属性。

## 初始化和反初始化
```c
#include <pthread.h>

int pthread_attr_init(pthread_attr_t *attr);

int pthread_attr_destroy(pthread_attr_t *attr);
```
在这里，pthread_attr_t是一个类，我们在需要设置自己的属性时，需要首先实例化，然后将这个对象进行初始化。
### 举例
```c
pthread_attr_t attr;

err = pthread_attr_init(&attr);
```
这样之后，我们就可以通过后面的相应函数设置attr，最后将attr放入create函数中就可以创建一个我们所需要的线程了。

## 线程的分离状态属性
在之前我们学到过将线程分离。也就是说如果我们想要创建一个和原来线程没有任何关系的新线程，我们就需要将线程进行分离。之前的做法是先用默认属性构建一个新线程，后再调用函数。现在可以通过设置分离状态属性来直接创建一个分离状态的线程。
```c
#include <pthread.h>
int pthread_attr_getdetachstate(const pthread_attr_t*restrict attr,int *detachstate);
int pthread_attr_setdetachstate(pthread_attr_t *attr,int *detachstate);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
可以调用pthread_attr_getdetachstate函数获取当前的detachstate线程属性。第二个参数所指向的整数要么设置成PTHREAD_CREATE_DETACHED，要么设置成 PTHREAD_CREATE_JOINABLE，具体要取决于给定pthread_attr_t结构中的属性值。

### 直接创建分离状态的新线程（fig12.4)
```c
#include "apue.h"
#include <pthread.h>

int
makethread(void *(*fn)(void *), void *arg)
{
	int				err;
	pthread_t		tid;
	pthread_attr_t	attr;

	err = pthread_attr_init(&attr);
	if (err != 0)
		return(err);
	err = pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
	if (err == 0)
		err = pthread_create(&tid, &attr, fn, arg);
	pthread_attr_destroy(&attr);
	return(err);
}
```
解释：可以看到，在这里先初始化并实例化了一个attr对象，然后通过pthread_attr_setdetachstate()来设置属性，最后通过这个属性进行实例化，这样就可以得到一个新的处于分离状态的线程了。

## 栈相关
可以通过下面两个函数来对线程栈进行管理
```c
#include <pthread.h>

int pthread_attr_getstack(const pthread_attr_t *restrict attr, 
         void **restrict stackaddr,size_t *restrict stacksize);
         
int pthread_attr_setstack(pthread_attr_t *attr,
                void *stackaddr, size_t stacksize);
                
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```

对于进程来说，虚地址空间的大小是固定的。因为进程中只有一个栈，所以它的大小通常不是问题。但对于线程来说，同样大小的虚地址空间必须被所有的线程栈共享。如果应用程序使用了许多线程，以致这些线程栈的累计大小超过了可用的虚地址空间，就需要减少默认的线程栈大小。另一方面，如果线程调用的函数分配了大量的自动变量，或者调用的函数涉及许多很深的栈帧（stack frame），那么需要的栈大小可能要比默认的大。

应用程序也可以通过pthread_attr_getstacksize和pthread_attr_setstacksize函数读取或设置线程属性stacksize。
```c
#include <pthread.h>
int pthread_attr_getstacksize(const pthread_attr_t*restrict attr, size_t *restrict stacksize);
int pthread_attr_setstacksize (pthread_attr_t *attr,size_t stacksize);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
如果希望改变默认的栈大小，但又不想自己处理线程栈的分配问题，这时使用pthread_attr_setstacksize函数就非常有用。设置stacksize属性时，选择的stacksize不能小于PTHREAD_STACK_MIN。
线程属性guardsize控制着线程栈末尾之后用以避免栈溢出的扩展内存的大小。这个属性默认值是由具体实现来定义的，但常用值是系统页大小。可以把guardsize线程属性设置为0，不允许属性的这种特征行为发生：在这种情况下，不会提供警戒缓冲区。同样，如果修改了线程属性stackaddr，系统就认为我们将自己管理栈，进而使栈警戒缓冲区机制无效，这等同于把guardsize线程属性设置为0。
```c
#include <pthread.h>
int pthread_attr_getguardsize(const pthread_attr_t*restrict attr,size_t *restrict guardsize);
int pthread_attr_setguardsize(pthread_attr_t *attr,size_t guardsize);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
如果guardsize线程属性被修改了，操作系统可能会把它取为页大小的整数倍。如果线程的栈指针溢出到警戒区域，应用程序就可能通过信号接收到出错信息。
Single UNIX Specification还定义了一些其他的可选线程属性供实时应用程序使用，但在这里不讨论这些属性。
线程还有一些其他的pthread_attr_t结构中没有表示的属性：可撤销状态和可撤销类型。我们将在12.7节中讨论它们。

# 2 线程同步的属性

## 互斥量属性
互斥量属性是用pthread_mutexattr_t结构表示的。和线程属性是很类似的。

### 初始化和反初始化
```c
#include <pthread.h>
int pthread_mutexattr_init(pthread_mutexattr_t *attr);
int pthread_mutexattr_destroy(pthread_mutexattr_t*attr);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
### 值得注意的属性
1. 进程共享属性:确定是否进程间可以共享该互斥量
2. 健壮属性
3. 类型属性

#### 1. 共享属性
```c
#include <pthread.h>
int pthread_mutexattr_getpshared(const pthread_mutexattr_t*restrict attr,int *restrict pshared);
int pthread_mutexattr_setpshared(pthread_mutex-attr_t *attr,int pshared);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
进程共享互斥量属性设置为PTHREAD_PROCESS_PRIVATE时，允许pthread线程库提供更有效的互斥量实现，这在多线程应用程序中是默认的情况。在多个进程共享多个互斥量的情况下， pthread线程库可以限制开销较大的互斥量实现。
互斥量健壮属性与在多个进程间共享的互斥量有关。这意味着，当持有互斥量的进程终止时，需要解决互斥量状态恢复的问题。这种情况发生时，互斥量处于锁定状态，恢复起来很困难。其他阻塞在这个锁的进程将会一直阻塞下去。

#### 2. 健壮属性
```c
#include <pthread.h>
int pthread_mutexattr_getrobust(const pthread_mutexattr_t*restrict attr,int *restrict robust);
int pthread_mutexattr_setrobust(pthread_mutex-attr_t *attr,int robust);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
健壮属性取值有两种可能的情况。
1. 默认值是 PTHREAD_MUTEX_STALLED，这意味着持有互斥量的进程终止时不需要采取特别的动作。这种情况下，使用互斥量后的行为是未定义的，等待该互斥量解锁的应用程序会被有效地“拖住”。
2. 另一个取值是PTHREAD_MUTEX_ROBUST。这个值将导致线程调用pthread_mutex_lock获取锁，而该锁被另一个进程持有，但它终止时并没有对该锁进行解锁，此时线程会阻塞，从pthread_mutex_lock返回的值为EOWNERDEAD而不是0。应用程序可以通过这个特殊的返回值获知，若有可能（要保护状态的细节以及如何进行恢复会因不同的应用程序而异），不管它们保护的互斥量状态如何，都需要进行恢复。

使用健壮的互斥量改变了我们使用pthread_mutex_lock的方式，因为现在必须检查3个返回值而不是之前的两个：不需要恢复的成功、需要恢复的成功以及失败。但是，即使不用健壮的互斥量，也可以只检查成功或者失败。

#### 3. 类型属性
POSIX.1定义了4中类型：
+ PTHREAD_MUTEX_NORMAL:一种标准互斥量类型，不做任何特殊的错误检查或死锁检测。.
+ PTHREAD_MUTEX_ERRORCHECK:此互斥量类型提供错误检查。
+ PTHREAD_MUTEX_RECURSIVE:此互斥量类型允许同一线程在互斥量解锁之前对该互斥量进行多次加锁。递归互斥量维护锁的计数，在解锁次数和加锁次数不相同的情况下，不会释放锁。所以，如果对一个递归互斥量加锁两次，然后解锁一次，那么这个互斥量将依然处于加锁状态，对它再次解锁以前不能释放该锁。
+ PTHREAD_MUTEX_DEFAULT:此互斥量类型可以提供默认特性和行为。操作系统在实现它的时候可以把这种类型自由地映射到其他互斥量类型中的一种。例如，Linux3.2.0把这种类型映射为普通的互斥量类型，而FreeBSD 8.0则把它映射为错误检查互斥量类型。

#### 总结如下图：
![](https://pic.downk.cc/item/5fc636a7f81f7e3bd97f3e3a.jpg)
“不占用时解锁”这一栏指的是，一个线程对被另一个线程加锁的互斥量进行解锁的情况。“在已解锁时解锁”这一栏指的是，当一个线程对已经解锁的互斥量进行解锁时将会发生什么，这通常是编码错误引起的。

得到和修改互斥量类型属性：
```c
#include <pthread.h>
int pthread_mutexattr_gettype(const pthread_mutexattr_t*restrict attr, int*restrict type);
int pthread_mutexattr_settype(pthread_mutexattr_t*attr, int type);
//两个函数的返回值: 若成功，返回0；否则，返回错误编号
```

得到的修改的方式和之前进程属性完全相同。

## 读写锁属性
读写锁与互斥量类似，也是有属性的。可以用pthread_rwlockattr_init 初始化pthread_rwlockattr_t结构，用pthread_rwlockattr_destroy反初始化该结构。
```c
#include <pthread.h>
int pthread_rwlockattr_init(pthread_rwlockattr_t *attr);
int pthread_rwlockattr_destroy(pthread_rwlockattr_t*attr);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
读写锁支持的唯一属性是进程共享属性。它与互斥量的进程共享属性是相同的。就像互斥量的进程共享属性一样，有一对函数用于读取和设置读写锁的进程共享属性。
```c
#include <pthread.h>
int pthread_rwlockattr_getpshared(const pthread_rwlockattr_t *restrict attr,int *restrict pshared);
int pthread_rwlockattr_setpshared(pthread_rwlock-attr_t *attr,int pshared);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
虽然POSIX只定义了一个读写锁属性，但不同平台的实现可以自由地定义额外的、非标准的属性。

## 条件标量属性
Single UNIX Specification目前定义了条件变量的两个属性：进程共享属性和时钟属性。与其他的属性对象一样，有一对函数用于初始化和反初始化条件变量属性。
```c
#include <pthread.h>
int pthread_condattr_init(pthread_condattr_t *attr);
int pthread_condattr_destroy(pthread_condattr_t *attr);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
与其他的同步属性一样，条件变量支持进程共享属性。它控制着条件变量是可以被单进程的多个线程使用，还是可以被多进程的线程使用。要获取进程共享属性的当前值，可以用pthread_condattr_getpshared函数。设置该值可以用pthread_condattr_setpshared函数。
```c
#include <pthread.h>
int pthread_condattr_getpshared(const pthread_condattr_t *restrict attr, int *restrict pshared);
int pthread_condattr_setpshared(pthread_condattr_t*attr,int pshared);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```
时钟属性控制计算pthread_cond_timedwait函数的超时参数（tsptr）时采用的是哪个时钟。合法值取自图 6-8 中列出的时钟 ID。可以使用 pthread_condattr_getclock 函数获取可被用于pthread_cond_timedwait 函数的时钟 ID，在使用pthread_cond_timedwait 函数前需要用pthread_condattr_t对象对条件变量进行初始化。可以用pthread_condattr_setclock函数对时钟ID进行修改。
```c
#include <pthread.h>
int pthread_condattr_getclock(const pthread_condattr_t *restrict attr, clockid_t *restrict clock_id);
int pthread_condattr_setclock(pthread_condattr_t *attr, clockid_t clock_id);
//两个函数的返回值:若成功，返回0；否则，返回错误编号
```
奇怪的是，Single UNIX Specification并没有为其他有超时等待函数的属性对象定义时钟属性。

# 重入、线程与信号、线程与for循环

## 线程与信号
![](https://pic.downk.cc/item/5fc63673f81f7e3bd97efa1c.jpg)  
这里将之前信号响应过程的图进行了拓展。我们可以看到，其实之前我们的版本是单进程单线程的版本。
在多线程中，我们其实需要进行两次按位与，也就是先将masks1和mask2按位与之后再和pending按位与。

### 相应函数
在这里我们所需要用到的函数都是和之前信号差不多的，主要就是修改masks值和pending值（pending值修改用的很少）
```c
#include <signal.h>
int pthread_sigmask(int how, const sigset_t *restrict set, sigset_t *restrict oset);
//返回值：若成功，返回0；否则，返回错误编号
```
pthread_sigmask函数与sigprocmask函数基本相同，不过pthread_sigmask工作在线程中，而且失败时返回错误码，不再像sigprocmask中那样设置errno并返回−1。set参数包含线程用于修改信号屏蔽字的信号集。
how参数可以取下列3个值之一：
+ SIG_BLOCK，把信号集添加到线程信号屏蔽字中
+ SIG_SETMASK，用信号集替换线程的信号屏蔽字；
+ SIG_UNBLOCK，从线程信号屏蔽字中移除信号集。

如果oset参数不为空，线程之前的信号屏蔽字就存储在它指向的sigset_t结构中。线程可以通过把set参数设置为NULL，并把oset参数设置为sigset_t结构的地址，来获取当前的信号屏蔽字。这种情况中的how参数会被忽略。

#### sigwait
线程可以通过调用sigwait等待一个或多个信号的出现。
```c
#include <signal.h>
int sigwait(const sigset_t *restrict set, int *restrict signop);
//返回值：若成功，返回0；否则，返回错误编号
```
set参数指定了线程等待的信号集。返回时，signop指向的整数将包含发送信号的数量。
如果信号集中的某个信号在sigwait调用的时候处于挂起状态，那么sigwait将无阻塞地返回。在返回之前，sigwait将从进程中移除那些处于挂起等待状态的信号。如果具体实现支持排队信号，并且信号的多个实例被挂起，那么sigwait将会移除该信号的一个实例，其他的实例还要继续排队。
为了避免错误行为发生，线程在调用sigwait之前，必须阻塞那些它正在等待的信号。sigwait函数会原子地取消信号集的阻塞状态，直到有新的信号被递送。在返回之前，sigwait将恢复线程的信号屏蔽字。如果信号在sigwait被调用的时候没有被阻塞，那么在线程完成对sigwait的调用之前会出现一个时间窗，在这个时间窗中，信号就可以被发送给线程。
使用sigwait的好处在于它可以简化信号处理，允许把异步产生的信号用同步的方式处理。为了防止信号中断线程，可以把信号加到每个线程的信号屏蔽字中。然后可以安排专用线程处理信号。这些专用线程可以进行函数调用，不需要担心在信号处理程序中调用哪些函数是安全的，因为这些函数调用来自正常的线程上下文，而非会中断线程正常执行的传统信号处理程序。
如果多个线程在sigwait的调用中因等待同一个信号而阻塞，那么在信号递送的时候，就只有一个线程可以从sigwait中返回。如果一个信号被捕获（例如进程通过使用sigaction建立了一个信号处理程序），而且一个线程正在sigwait调用中等待同一信号，那么这时将由操作系统实现来决定以何种方式递送信号。操作系统实现可以让sigwait返回，也可以激活信号处理程序，但这两种情况不会同时发生。

#### 信号传递pthread_kill
要把信号发送给进程，可以调用kill（见10.9节）。要把信号发送给线程，可以调用pthread_kill。
```c
#include <signal.h>
int pthread_kill(pthread_t thread, int signo);
//返回值：若成功，返回0；否则，返回错误编号
```
可以传一个0值的signo来检查线程是否存在。如果信号的默认处理动作是终止该进程，那么把信号传递给某个线程仍然会杀死整个进程。
注意，闹钟定时器是进程资源，并且所有的线程共享相同的闹钟。所以，进程中的多个线程不可能互不干扰（或互不合作）地使用闹钟定时器（这是习题12.6的内容）。

















