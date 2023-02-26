---
title: APUE-11-线程  
date: 2022-12-04 02:38:30  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 1 线程的概念
线程就是一个正在运行的函数。

每个线程都包含有标识执行环境所必须的信息，其中包括进程中标识的线程ID、一组寄存器值、栈、调度优先级和策略、信号屏蔽字、errno变量以及线程私有数据。
一个进程的所有信息对该进程的所有线程都是共享的，包括可执行程序的代码、程序的全局内存和堆内存、栈以及文件描述符。

## 注意
会话就是一个承载进程组的容器
进程组是承载更多进程的容器
进程是承载线程的容器


## 线程标准
线程有两种标准，一种是posix线程，一种是openmp线程。
### 注意
线程标准是一套标准，而不是实现，例如posix线程标准的线程标识pthread_t，这个线程标识没有确定的类型，在不同的系统上实现形式是不同的（例如windows和macos上的实现标准有可能就不相同，有可能是链表或者数组），因此我们可以直接使用，但是不能用类似"%d"的标准将其打印。

## 线程标识
就像每个进程都有一个进程ID一样，每个线程也有一个线程ID。进程ID在整个系统中是唯一的，但线程ID却不同，线程ID只有在它所属的进程上下文中才有意义。
使用pthread_equal函数来对进程ID进行比较：
```c
#include <pthread.h>

int pthread_equal(pthread_t tid1, pthread_t tid2);
```

线程可以通过调用pthread_self函数获得自身的线程ID
```c
#include <pthread.h>

pthread_t pthread_self(void);
```


## 编译和链接
编译和链接需要加上-pthreadls

# 2 线程的创建、取消和终止、栈的清理

## 1 线程创建
```c
#include <pthread.h>

int pthread_create(pthread_t *restrict tidp, 
         const pthread_attr_t *restrict attr,
         void *(*star_rtn)(void *) void *restrict arg);
         // 返回值：若成功返回0，否则返回错误编号
```
参数解析：
tidp：当pthread_create成功返回后，新创建线程的线程ID会被设置成tidp指向的内存单元。
attr：用于定制不同的线程属性（在第12章会详细讨论），将其设置为NULL将会拥有默认的属性
start_rtn：新创建的线程开始的地址，其实就是兄弟线程。这里其实暴露了最好的结构，因为这是一个返回值为任意类型，参数为任意类型的指针函数。因此非常开放，可以是任何类型。
arg：结构体的地址
### 注意：
pthread函数在调用失败后通常会返回错误码。在线程中，从函数中返回错误码更为清晰整洁，不需要依赖那些随着函数执行不断变化的全局状态，这样可以把错误的范围限制在引起出错的函数中。


### 举例(书中）
```c
#include "apue.h"
#include <pthread.h>

pthread_t ntid;

void
printids(const char *s)
{
	pid_t		pid;
	pthread_t	tid;

	pid = getpid();
	tid = pthread_self();
	printf("%s pid %lu tid %lu (0x%lx)\n", s, (unsigned long)pid,
	  (unsigned long)tid, (unsigned long)tid);
}

void *
thr_fn(void *arg)
{
	printids("new thread: ");
	return((void *)0);
}

int
main(void)
{
	int		err;

	err = pthread_create(&ntid, NULL, thr_fn, NULL);
	if (err != 0)
		err_exit(err, "can't create thread");
	printids("main thread:");
	sleep(1);
	exit(0);
}
```
输出：

![](https://pic.downk.cc/item/5fc63645f81f7e3bd97ee11d.jpg)

解释：

在这里，main进程先是创建了一个新的线程，其中线程id是ntid，属性默认，兄弟线程设置为thr_fn，其余参数设置为NULL。
最终输出创建出来的线程的各种参数和main线程的各种参数。




## 举例2
```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

static void *func(void *p){
    puts("Thread is workding");
    return NULL;
}

int main()
{

    pthread_t tid;
    int errno;

    puts("Begin@");


    errno = pthread_create(&tid, NULL, func, NULL);
    if(errno){
        fprintf(stderr,"pthread_create():%s",strerror(errno));
    }

    printf("Hello world\n");



    puts("End!");
    return 0;
}

```
解析：
在这里，可以看到首先输出Begin，然后创建一个线程，最后输出Hello world。
创建线程的时候会执行兄弟线程。
注意：
因为兄弟线程和main线程的执行线程是不确定的，因此如下图所示，兄弟线程的语句什么时候输出以及是否输出都是不确定的。
![](https://pic.downk.cc/item/5fc63673f81f7e3bd97efa4e.jpg)

## 2 线程的终止
### 3种终止线程的方式
1. 线程可以简单地从启动例程中返回，返回值是线程的退出码
2. 线程可以被同一进程中的其他线程取消
3. 线程调用pthread_exit
```c
#include <pthread.h>

void pthread_exit(void *rval_ptr);
```
参数：
rval\_ptr参数是一个无类型指针，与传给启动例程的单个参数类似。进程中的其他线程也可以通过调用pthread\_join函数访问到这个指针。
```c
#include <pthread.h>

int pthread_join(pthread_t thread, void **rval_ptr);
//若成功返回0，否则返回错误编号
```
#### 参数：
其中pthread_exit的参数和pthread_join函数的第二个参数是相同的，pthread_join函数的含义是将调用点成阻塞，直到指定的线程（也就是thread参数指定的）从它的启动例程返回，rval_ptr包含返回码。如果线程被取消，由rval_ptr指定的内存单元就被设置为PTHREAD_CANCELED。

### pthread\_create和pthread\_join线程举例
```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

static void *func(void *p){
    puts("Thread is workding");
    /* return NULL;                    //注意：这句话必不可少，因为返回的是void *，泛指任何类型，并不只是void */
    pthread_exit(NULL);         //表示线程终止，若采用之前方式则不能实现线程栈清理。
}

int main()
{

    pthread_t tid;
    int errno;

    puts("Begin@");


    errno = pthread_create(&tid, NULL, func, NULL);
    if(errno){
        fprintf(stderr,"pthread_create():%s",strerror(errno));
    }

    pthread_join(tid,NULL);
    puts("End!");
    return 0;
}
```
输出：

<img src="https://pic.downk.cc/item/5fc636c3f81f7e3bd97f43b2.jpg" style="zoom:50%;" />

解释：

main线程首先创建了一个子线程，线程号被指向tid，属性是默认属性，兄弟线程是func。创建线程之后用pthread_join来等待这个线程退出，最后执行End！
注意：若没有pthread_join则输出顺序是不确定的。就和例1一样。


### 我的例子
```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
static void *func(void *p){
    puts("Thread is workding");
    pthread_t pid = pthread_self();
    printf("pthread_id = %d\n", pid);
    pthread_exit(NULL);         
}

int main()
{

    pthread_t tid;
    printf("thread_init = %d\n", tid);
    int errno;
    puts("Begin@");
    errno = pthread_create(&tid, NULL, func, NULL);
    printf("%d\n", tid);
    if(errno){
        fprintf(stderr,"pthread_create():%s",strerror(errno));
    }
    pthread_join(tid,NULL);
    puts("End!");
    return 0;
}
```
可以看到，我们首先创建了一个pthread_t类型的变量tid，初始值是-407197560.然后打印Begin，调用pthread_create来创建一个线程，指向tid，属性为NULL，兄弟线程是func，可以看到，线程号在调用期间被初始化成了141393920.然后结束之后打印End。



### 例11.3（获取已终止线程的退出码）
```c
#include "apue.h"
#include <pthread.h>

void *
thr_fn1(void *arg)
{
	printf("thread 1 returning\n");
	return((void *)1);
}

void *
thr_fn2(void *arg)
{
	printf("thread 2 exiting\n");
	pthread_exit((void *)2);
}

int
main(void)
{
	int			err;
	pthread_t	tid1, tid2;
	void		*tret;

	err = pthread_create(&tid1, NULL, thr_fn1, NULL);
	if (err != 0)
		err_exit(err, "can't create thread 1");
	err = pthread_create(&tid2, NULL, thr_fn2, NULL);
	if (err != 0)
		err_exit(err, "can't create thread 2");
	err = pthread_join(tid1, &tret);
	if (err != 0)
		err_exit(err, "can't join with thread 1");
	printf("thread 1 exit code %ld\n", (long)tret);
	err = pthread_join(tid2, &tret);
	if (err != 0)
		err_exit(err, "can't join with thread 2");
	printf("thread 2 exit code %ld\n", (long)tret);
	exit(0);
}
```
输出：

![](https://pic.downk.cc/item/5fc6363cf81f7e3bd97edefc.jpg)

## 不要传递临时变量
pthread_create和pthread_exit函数的无类型指针参数可以传递的值可以是结构体等一系列类型，但是这个结构所使用的内存在调用之后必须仍然是有效的。也就是说，不要是临时变量（存放在栈上），需要是存放在堆上的常量或者其他数据。参考[指针函数和函数指针](https://zhuanlan.zhihu.com/p/136880546)。里面有详细的介绍。
### 用自动变量（分配在栈上）作为pthread_exit的参数时出现的问题。
```c
#include "apue.h"
#include <pthread.h>

struct foo {
	int a, b, c, d;
};
void
printfoo(const char *s, const struct foo *fp)
{
	printf("%s", s);
	printf("  structure at 0x%lx\n", (unsigned long)fp);
	printf("  foo.a = %d\n", fp->a);
	printf("  foo.b = %d\n", fp->b);
	printf("  foo.c = %d\n", fp->c);
	printf("  foo.d = %d\n", fp->d);
}
void *
thr_fn1(void *arg)
{
	struct foo	foo = {1, 2, 3, 4};

	printfoo("thread 1:\n", &foo);
	pthread_exit((void *)&foo);
}
void *
thr_fn2(void *arg)
{
	printf("thread 2: ID is %lu\n", (unsigned long)pthread_self());
	pthread_exit((void *)0);
}
int
main(void)
{
	int			err;
	pthread_t	tid1, tid2;
	struct foo	*fp;

	err = pthread_create(&tid1, NULL, thr_fn1, NULL);
	if (err != 0)
		err_exit(err, "can't create thread 1");
	err = pthread_join(tid1, (void *)&fp);
	if (err != 0)
		err_exit(err, "can't join with thread 1");
	sleep(1);
	printf("parent starting second thread\n");
	err = pthread_create(&tid2, NULL, thr_fn2, NULL);
	if (err != 0)
		err_exit(err, "can't create thread 2");
	sleep(1);
	printfoo("parent:\n", fp);
	exit(0);
}
```
输出：

![](https://pic.downk.cc/item/5fc63633f81f7e3bd97eddaa.jpg)
#### 注意
这是macos上的输出，每个操作系统的输出不一定相同。  
在这里，在第41行调用tid1线程时将fp进行了初始化，但是调用结束之后内存就回收了，因此被覆盖了。在第50行再次被调用时已经没有办法显示原来的值了。

## 3 线程的取消
函数：pthread_cancel();
```c
#include <pthread.h>

int pthread_cancel(pthread_t tid);
```
在默认情况下，pthread_cancel函数会使得tid表示的线程的行为表现为如同调用了PTHREAD_CANCELED和pthread_exit函数，但是，线程可以选择忽略取消或者控制如何被取消。
注意：pthread_cancel并不等于线程终止，它仅仅提出请求。

### 取消两种状态
1.允许
   允许取消也分为两种：
   + 异步cancel，推迟cancel（默认）->推迟至cancel点在响应，也就是说不到cancel点是不会直接取消的。
      cancel点：就是允许cancel的位置
   + 同步cancel

2.不允许



## 3 线程清理处理程序
线程可以安排它推出时需要调用的函数，这与进程在退出时可以用atexit函数安排退出是类似的。这样的函数称为线程清理处理程序。
一个线程可以建立多个线程处理程序，处理程序记录在栈中，也就是说，它们的执行顺序与它们注册时相反。

```c
#include <pthread.h>

void pthread_cleanup_push(void (*rtn)(void *), void *arg);

void pthread_cleanup_pop(int execute);
```

### 举例
```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

static void cleanup_func(void *p){
    puts(p);
}

static void *func(void *p){
    puts("Thread is woking");
    pthread_cleanup_push(cleanup_func, "Cleanup:1");
    pthread_cleanup_push(cleanup_func, "Cleanup:2");
    pthread_cleanup_push(cleanup_func, "Cleanup:3");
    printf("push over\n") ;
    pthread_cleanup_pop(1);
    pthread_cleanup_pop(1);
    pthread_cleanup_pop(1);
    pthread_exit(NULL);
}

int main()
{
    pthread_t tid;
    int err;
    puts("Begin");
    err = pthread_create(&tid,NULL,func,NULL);
    if(err){
        fprintf("stderr,pthread_create():%s\n", strerror(err));
        exit(1);
    }
    pthread_join(tid,NULL);
    puts("End");
    return 0;
}
```
输出：

<img src="https://pic.downk.cc/item/5fc63694f81f7e3bd97f2e70.jpg" style="zoom:50%;" />  
可以看到，退出时会依次执行三个退出时我们所需要调用的函数。并且执行顺序与注册时相反。


### 注意
来看两种特殊情况，
1. 我们将弹出栈pop的三行设置如下：
   ```c
   pthread_cleanup_pop(1);
   pthread_cleanup_pop(0);
   pthread_cleanup_pop(0);
   ```
此时会只有一个输出，如下：
![](https://pic.downk.cc/item/5fc636fdf81f7e3bd97f4fbb.jpg)  
可以看到只有最后一次push的值输出的。其余的都没有输出
2. 若我们将上面的三行放到pthread_exit(NULL)之后，那么理论上是看不到的，但是因为线程没办法看到，所以pop值全部默认为1！！  
![](https://pic.downk.cc/item/5fc6361ff81f7e3bd97eda2f.jpg)  
此时的输出为：  
![](https://pic.downk.cc/item/5fc63616f81f7e3bd97ed8a2.jpg)  
并且即使看不见，也必须在程序里体现，因为在宏定义中push里有一个{，而}在pop中，因此必须得成对出现，数量相同。


### cancel点
在POSIX标准中，只有在cancel点才会执行阻塞系统调用

# 3 线程竞争

## 竞争示例
```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#define LEFT 30000000
#define RIGHT 30000200
#define THRNUM (RIGHT - LEFT + 1)

static void *the_primer (void *p);     //这行必不可少，否则第17行会报错，因为无法检测到该函数
int main()
{
    int i;
    pthread_t tid[THRNUM];
    int err;
    for(i = LEFT; i <= RIGHT; i++){
        err = pthread_create(tid + i - LEFT,NULL, the_primer, &i);
        if(err){
            fprintf(stderr, "pthread_create(): %s\n", strerror(err));
            exit(1);
        }
    }
    //上面创建了200个进程来进行运算
    for(i = LEFT; i <= RIGHT; i++)
        pthread_join(tid[i - LEFT], NULL);
    exit(0);
}
static void *the_primer(void *p){
    int i,j,mark;
    i = *(int *)p;
    mark = 1;
    for(j = 2; j < i/2; j++){
        if(i % j == 0){
            mark = 0;
            break;
        }
    }
    if(mark){
        printf("%d is a primer\n", i);
    }
    pthread_exit(NULL);
}
```
输出：

![](https://pic.downk.cc/item/5fc636c3f81f7e3bd97f43ae.jpg)

解析：

可以看到，这个函数的目的是将30000000到30000200中的所有质数打印。但是出来的结果却是完全不同的，这是因为多个线程出现了竞争。200个进程有共享的数据，并且没有加以保护。200个指针都指向了同一个地址空间。

### 程序改进方法1
我们知道，这个程序错误的原因是我们用的是引用传递，也就是传入的是i的引用，所以有可能让多个线程同时指向i的地址。那么我们就不用引用传递，而采用值传递。
注意：这个方法不好，多次采用强制转换
```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#define LEFT 30000000
#define RIGHT 30000200
#define THRNUM (RIGHT - LEFT + 1)

static void *the_primer (void *p);

int main()
{
    int i;
    pthread_t tid[THRNUM];
    int err;

    for(i = LEFT; i <= RIGHT; i++){
        err = pthread_create(tid + i - LEFT,NULL, the_primer, i);
        if(err){
            fprintf(stderr, "pthread_create(): %s\n", strerror(err));
            exit(1);
        }
    }
    for(i = LEFT; i <= RIGHT; i++)
        pthread_join(tid[i - LEFT], NULL);
    exit(0);
}

static void *the_primer(void *p){
    int i,j,mark;
    i = (int)p;
    mark = 1;
    for(j = 2; j < i/2; j++){
        if(i % j == 0){
            mark = 0;
            break;
        }
    }
    if(mark){
        printf("%d is a primer\n", i);
    }
    pthread_exit(NULL);
}
```
输出：

![](https://pic.downk.cc/item/5fc6371af81f7e3bd97f56a3.jpg)

### 方法2（结构体）
让201个i指向不同的空间，方法是定义一个结构体。值得注意的是，最后应该将malloc产生的指针释放掉，避免内存泄露
```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#define LEFT 30000000
#define RIGHT 30000200
#define THRNUM (RIGHT - LEFT + 1)

static void *the_primer (void *p);
struct thr_args_st
{
    int n;
};

int main()
{
    int i;
    pthread_t tid[THRNUM];
    int err;
    struct thr_args_st *p;
    void *ptr;          //作用是用来接收返回的指针，并且进行销毁。保证malloc和free在一个模块中

    for(i = LEFT; i <= RIGHT; i++){
        p = malloc(sizeof(*p));
        if(p == NULL){
            perror("malloc()");
            exit(1);
        }
        p->n=i;


        err = pthread_create(tid + i - LEFT,NULL, the_primer,p);
        if(err){
            fprintf(stderr, "pthread_create(): %s\n", strerror(err));
            exit(1);
        }
    }
    for(i = LEFT; i <= RIGHT; i++)
    {
        pthread_join(tid[i - LEFT], ptr);
        free(ptr);
    }
    exit(0);
}

static void *the_primer(void *p){
    int i,j,mark;

    i = ((struct thr_args_st *)p)->n;
    free(p);
    mark = 1;
    for(j = 2; j < i/2; j++){
        if(i % j == 0){
            mark = 0;
            break;
        }
    }
    if(mark){
        printf("%d is a primer\n", i);
    }
    pthread_exit(&p);
}
```
输出：

![](https://pic.downk.cc/item/5fc63645f81f7e3bd97ee139.jpg)

解析：

最终返回了一个指针并且在返回之后free，目的就是让malloc和free一一对应，能在同一个模块中。

## 注意：
因为这块涉及大量C++的知识，我还掌握不是很好，打算今天下午开始学习C++这方面的内容，给出更详细的解释。

# 线程同步

## 互斥量

可以用互斥接口来保护数据，确保同一时间只有一个线程访问数据。互斥量从本质来说就是一把锁，在访问数据的时候进行加锁，阻止其他线程的访问，在访问完成后释放互斥量。

互斥变量是用pthread_mutex_t数据类型表示的。在使用互斥变量之前先进行初始化
如下所示：
### pthread_mutex_init 和 pthread_mutex_destory
```c
#include <pthread.h>

int pthread_mutex_init(pthread_mutex_t *restrict mutex,
                     const pthread_mutexattr_t *restrict attr);
                     //初始化

int pthread_mutex_destroy(pthread_mutex_t *mutex);
                     //销毁
```
#### 注意
如果动态分配内存（malloc），在释放内存前需要调用detroy

### 加锁和解锁互斥变量
```c
#include <pthread.h>

int pthread_mutex_lock(pthread_mutex_t *mutex);       //加锁

int pthread_mutex_trylock(pthread_mutex_t *mutex);    //尝试加锁

int pthread_mutex_unlock(pthread_mutex_t *mutex);     //解锁
```
### 举例（自己）
```c
#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

int res = 0;
pthread_t ntid;
pthread_t tid[5];
pthread_mutex_t f_lock[5];

void *
add(void *p){
    res++;
    return ((void *)0);
}


void *
thr_fun(void *arg){
    return ((void *)0);
}

int main(){
    int err;
    for(int i= 0; i < 5; i++){
        pthread_mutex_init(f_lock + i, NULL);
        pthread_mutex_lock(f_lock + i);
        err = pthread_create(&ntid, NULL, add, NULL);
        if(err != 0){
            err_exit(err, "can't create thread");
        }
        pthread_mutex_unlock(f_lock + i);
    }
    sleep(1);
    printf("res=%d\n", res);
    return 0;
}
```
输出：

![](https://pic.downk.cc/item/5fc63669f81f7e3bd97eec50.jpg)

解析：

第33行的sleep(1)比不可少，否则可能会导致main进程先结束，main进程一旦结束所有线程都会摧毁。
思路是在调动add函数来进行res的+1操作时先初始化一个互斥变量并且加锁，这样别人就不能对add的地址进行操作的，操作完成之后再释放互斥量锁。

## 举例（书11.10）
```c
#include <stdlib.h>
#include <pthread.h>

struct foo {
	int             f_count;
	pthread_mutex_t f_lock;
	int             f_id;
	/* ... more stuff here ... */
};

struct foo *
foo_alloc(int id) /* allocate the object */
{
	struct foo *fp;

	if ((fp = malloc(sizeof(struct foo))) != NULL) {
		fp->f_count = 1;
		fp->f_id = id;
		if (pthread_mutex_init(&fp->f_lock, NULL) != 0) {
			free(fp);
			return(NULL);
		}
		/* ... continue initialization ... */
	}
	return(fp);
}

void
foo_hold(struct foo *fp) /* add a reference to the object */
{
	pthread_mutex_lock(&fp->f_lock);
	fp->f_count++;
	pthread_mutex_unlock(&fp->f_lock);
}

void
foo_rele(struct foo *fp) /* release a reference to the object */
{
	pthread_mutex_lock(&fp->f_lock);
	if (--fp->f_count == 0) { /* last reference */
		pthread_mutex_unlock(&fp->f_lock);
		pthread_mutex_destroy(&fp->f_lock);
		free(fp);
	} else {
		pthread_mutex_unlock(&fp->f_lock);
	}
}
```
引用计数是一种古老的内存管理计数，很简单，但是非常有效，性能也很高，上面就是一种C语言下的引用计数，当然，我们可以将函数以函数指针的形式放置在结构体中，这里就不弄了。  
可以看到，结构体非常简单，就一个引用计数成员、互斥量和数据成员，使用foo_alloc函数分配空间，并且在其中初始化互斥量，在foo_alloc函数中我们并没有使用互斥量，因为初始化完毕前分配线程是唯一的能使用的线程。但是在这里例子中，如果一个线程调用foo_rele释放引用，但是在此期间另一个线程使用foo_hold阻塞了，等第一个线程调用完毕，引用变为0，并且内存被回收，然后就会导致崩溃。  
这里参考了[网上的资料](https://segmentfault.com/a/1190000004579921)。


## 死锁
### 定义
线程试图对同一个互斥量加锁两次，那么它就会陷入死锁循环。
### 产生条件
1. 线程对同一个互斥量加锁两次
2. A和B的锁相互占用。
### 如何避免
方法1：多次上锁时让上锁的顺序需要和加锁的顺序相同。
```c
#include <stdlib.h>
#include <pthread.h>

#define NHASH 29
#define HASH(id) (((unsigned long)id)%NHASH)

struct foo *fh[NHASH];

pthread_mutex_t hashlock = PTHREAD_MUTEX_INITIALIZER;

struct foo {
	int             f_count;
	pthread_mutex_t f_lock;
	int             f_id;
	struct foo     *f_next; /* protected by hashlock */
	/* ... more stuff here ... */
};

struct foo *
foo_alloc(int id) /* allocate the object */
{
	struct foo	*fp;
	int			idx;

	if ((fp = malloc(sizeof(struct foo))) != NULL) {
		fp->f_count = 1;
		fp->f_id = id;
		if (pthread_mutex_init(&fp->f_lock, NULL) != 0) {
			free(fp);
			return(NULL);
		}
		idx = HASH(id);
		pthread_mutex_lock(&hashlock);
		fp->f_next = fh[idx];
		fh[idx] = fp;
		pthread_mutex_lock(&fp->f_lock);
		pthread_mutex_unlock(&hashlock);
		/* ... continue initialization ... */
		pthread_mutex_unlock(&fp->f_lock);
	}
	return(fp);
}

void
foo_hold(struct foo *fp) /* add a reference to the object */
{
	pthread_mutex_lock(&fp->f_lock);
	fp->f_count++;
	pthread_mutex_unlock(&fp->f_lock);
}

struct foo *
foo_find(int id) /* find an existing object */
{
	struct foo	*fp;

	pthread_mutex_lock(&hashlock);
	for (fp = fh[HASH(id)]; fp != NULL; fp = fp->f_next) {
		if (fp->f_id == id) {
			foo_hold(fp);
			break;
		}
	}
	pthread_mutex_unlock(&hashlock);
	return(fp);
}

void
foo_rele(struct foo *fp) /* release a reference to the object */
{
	struct foo	*tfp;
	int			idx;

	pthread_mutex_lock(&fp->f_lock);
	if (fp->f_count == 1) { /* last reference */
		pthread_mutex_unlock(&fp->f_lock);
		pthread_mutex_lock(&hashlock);
		pthread_mutex_lock(&fp->f_lock);
		/* need to recheck the condition */
		if (fp->f_count != 1) {
			fp->f_count--;
			pthread_mutex_unlock(&fp->f_lock);
			pthread_mutex_unlock(&hashlock);
			return;
		}
		/* remove from list */
		idx = HASH(fp->f_id);
		tfp = fh[idx];
		if (tfp == fp) {
			fh[idx] = fp->f_next;
		} else {
			while (tfp->f_next != fp)
				tfp = tfp->f_next;
			tfp->f_next = fp->f_next;
		}
		pthread_mutex_unlock(&hashlock);
		pthread_mutex_unlock(&fp->f_lock);
		pthread_mutex_destroy(&fp->f_lock);
		free(fp);
	} else {
		fp->f_count--;
		pthread_mutex_unlock(&fp->f_lock);
	}
}
```

可以看到，在函数foo_alloc中，有这样一段：
```c
pthread_mutex_lock(&hashlock);
fp->f_next = fh[idx];
fh[idx] = fp;
pthread_mutex_lock(&fp->f_lock);
pthread_mutex_unlock(&hashlock);
pthread_mutex_unlock[&fp->f_lock);
```
在这里对同一个互斥量上了两次锁，因此解锁时两次锁的顺序需要是相同的。
比较图 11-11 和图 11-10，可以看出，分配函数现在锁住了散列列表锁，把新的结构添加到了散列桶中，而且在对散列列表的锁解锁之前，先锁定了新结构中的互斥量。因为新的结构是放在全局列表中的，其他线程可以找到它，所以在初始化完成之前，需要阻塞其他线程试图访问新结构。
foo_find函数锁住散列列表锁，然后搜索被请求的结构。如果找到了，就增加其引用计数并返回指向该结构的指针。注意，加锁的顺序是，先在foo_find函数中锁定散列列表锁，然后再在foo_hold函数中锁定foo结构中的f_lock互斥量。
现在有了两个锁以后，foo_rele函数就变得更加复杂了。如果这是最后一个引用，就需要对这个结构互斥量进行解锁，因为我们需要从散列列表中删除这个结构，这样才可以获取散列列表锁，然后重新获取结构互斥量。从上一次获得结构互斥量以来我们可能被阻塞着，所以需要重新检查条件，判断是否还需要释放这个结构。如果另一个线程在我们为满足锁顺序而阻塞时发现了这个结构并对其引用计数加1，那么只需要简单地对整个引用计数减1，对所有的东西解锁，然后返回。

这种锁方法很复杂，所以我们需要重新审视原来的设计。我们也可以使用散列列表锁来保护结构引用计数，使事情大大简化。结构互斥量可以用于保护foo结构中的其他任何东西。图11-12反映了这种变化。

# 5 条件变量

条件变量是线程可用的另一种同步机制。条件变量给多个线程提供了一个会合的场所。条件变量与互斥量一起使用时，允许线程以无竞争的方式等待特定的条件发生。

条件本身是由互斥量保护的。线程在改变条件状态之前必须首先锁住互斥量。其他线程在获得互斥量之前不会察觉到这种改变，因为互斥量必须在锁定以后才能计算条件。

在使用条件变量之前，必须先对它进行初始化。由pthread_cond_t数据类型表示的条件变量可以用两种方式进行初始化，可以把常量PTHREAD_COND_INITIALIZER赋给静态分配的条件变量，但是如果条件变量是动态分配的，则需要使用pthread_cond_init函数对它进行初始化。

在释放条件变量底层的内存空间之前，可以使用pthread_cond_destroy函数对条件变量进行反初始化（deinitialize）。

```c
#include <pthread.h>

int pthread_cond_init(pthread_cond_t *restrict cond, const pthread_condattr_t *restrict attr);

int pthread_cond_destroy(pthread_cond_t *cond);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```

除非需要创建一个具有非默认属性的条件变量，否则pthread_cond_init函数的attr参数可以设置为NULL。我们将在12.4.3节中讨论条件变量属性。

我们使用pthread_cond_wait等待条件变量变为真。如果在给定的时间内条件不能满足，那么会生成一个返回错误码的变量。

```c
#include <pthread.h>

int pthread_cond_wait(pthread_cond_t *restrict cond, pthread_mutex_t *restrict mutex);

int pthread_cond_timedwait(pthread_cond_t *restrictcond, pthread_mutex_t *restrict mutex,const struct timespec *restrict tsptr);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```


传递给pthread_cond_wait的互斥量对条件进行保护。调用者把锁住的互斥量传给函数，函数然后自动把调用线程放到等待条件的线程列表上，对互斥量解锁。这就关闭了条件检查和线程进入休眠状态等待条件改变这两个操作之间的时间通道，这样线程就不会错过条件的任何变化。pthread_cond_wait返回时，互斥量再次被锁住。

pthread_cond_timedwait函数的功能与pthread_cond_wait函数相似，只是多了一个超时（tsptr）。超时值指定了我们愿意等待多长时间，它是通过timespec结构指定的。

如图11-13所示，需要指定愿意等待多长时间，这个时间值是一个绝对数而不是相对数。例如，假设愿意等待3分钟。那么，并不是把3分钟转换成timespec结构，而是需要把当前时间加上3分钟再转换成timespec结构。

可以使用clock_gettime函数（见6.10节）获取timespec结构表示的当前时间。但是目前并不是所有的平台都支持这个函数，因此，也可以用另一个函数 gettimeofday 获取timeval结构表示的当前时间，然后把这个时间转换成timespec结构。要得到超时值的绝对时间，可以使用下面的函数（假设阻塞的最大时间使用分来表示的）：


```c
#include <sys/time.h>
#include <stdlib.h>
void
maketimeout(struct timespec *tsp, long minutes){
    struct timeval now;
    /* get the current time */
    gettimeofday(&now, NULL);
    tsp->tv_sec = now.tv_sec;
    tsp->tv_nsec = now.tv_usec * 1000; /* usec to nsec*/
    /* add the offset to get timeout value */
    tsp->tv_sec += minutes * 60;
}
```

如果超时到期时条件还是没有出现，pthread_cond_timewait 将重新获取互斥量，然后返回错误ETIMEDOUT。从pthread_cond_wait或者pthread_cond_timedwait调用成功返回时，线程需要重新计算条件，因为另一个线程可能已经在运行并改变了条件。

有两个函数可以用于通知线程条件已经满足。pthread_cond_signal函数至少能唤醒一个等待该条件的线程，而pthread_cond_broadcast函数则能唤醒等待该条件的所有线程。

POSIX 规范为了简化pthread_cond_signal的实现，允许它在实现的时候唤醒一个以上的线程。

```c
#include <pthread.h>
int pthread_cond_signal(pthread_cond_t *cond);
int pthread_cond_broadcast(pthread_cond_t *cond);
//两个函数的返回值：若成功，返回0；否则，返回错误编号
```

在调用pthread_cond_signal或者pthread_cond_broadcast时，我们说这是在给线程或者条件发信号。必须注意，一定要在改变条件状态以后再给线程发信号。

## 举例
```c
#include <pthread.h>

struct msg {
	struct msg *m_next;
	/* ... more stuff here ... */
};

struct msg *workq;

pthread_cond_t qready = PTHREAD_COND_INITIALIZER;

pthread_mutex_t qlock = PTHREAD_MUTEX_INITIALIZER;

void
process_msg(void)
{
	struct msg *mp;

	for (;;) {
		pthread_mutex_lock(&qlock);
		while (workq == NULL)
			pthread_cond_wait(&qready, &qlock);
		mp = workq;
		workq = mp->m_next;
		pthread_mutex_unlock(&qlock);
		/* now process the message mp */
	}
}

void
enqueue_msg(struct msg *mp)
{
	pthread_mutex_lock(&qlock);
	mp->m_next = workq;
	workq = mp;
	pthread_mutex_unlock(&qlock);
	pthread_cond_signal(&qready);
}
```

































