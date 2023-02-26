---
title: APUE-7-进程环境  
date: 2022-12-04 02:37:59  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 1 进程环境简介

## 1 引言
### 学习内容：

1. 程序执行时，其main函数是如何被调用的
1. 命令行参数是如何传递给新程序的
2. 典型的存储空间布局是怎么样的
3. 如何分配另外的存储空间
4. 进程如何使用环境变量
5. 进程的各种不同终止方式

## 2 main函数
C程序总是从main函数开始执行的。
### main函数的原型
```c
int main(int argc, char *argv[]);
```
当我们在bash下输入命令执行某一个ELF文件的时候，首先bash进程调用fork()系统调用创建一个新的进程，然后新的进程调用execve()系统调用执行的ELF文件。
注意：
在linux中，gcc编译之后的文件就是ELF文件，但是在macos系统中，我们执行出来的文件叫做Mach-O文件，这是一种更好的形式。
![](https://pic.downk.cc/item/5fc636e5f81f7e3bd97f4b59.jpg)

在内核使用exec函数执行C程序时，在调用main函数之前先调用一个特殊的启动例程。可执行程序文件将启动例程指定为程序的起始地址-这是由连接编辑器设置的，而连接编辑器由C编译器调用。启动例程从内核取得命令行参数和环境变量值，然后为按上述方式调用main函数做好准备。
### 总结
1. C编译器调用连接编辑器
2. 连接编辑器设置可执行文件将启动例程指定为程序的起始地址，
3. 启动main函数

## 3 进程终止
### 8种进程终止的方式
1. 从main返回
   main执行完毕即可。
2. 调用exit
   exit(main(argc,argv));
3. 调用_exit或_Exit
   
4. 最后一个线程从其启动例程返回(11.5节)
5. 从最后一个线程调用pthread_exit(11.5节)
6. 调用abort(10.17节)
7. 街道一个信号(10.2节）
8. 最后一个线程对取消请求作出响应(11.5节12.7节)
前五种是正常终止，后三种是一场终止。
#### 1 退出函数
前三种的区别：
   _exit和_Exit立即进入内核，exit则先执行一些清理处理，然后返回内核。
函数形式：

```c
#include <stdlib.h>

void exit(int status);

void _Exit(int status);

#include <unistd,h>

void _exit(int status);
```

对fig7.1中的程序进行编译，并且打印其终止码。
![](https://pic.downk.cc/item/5fc6367cf81f7e3bd97f06de.jpg)
在不同机器上可能会得到不同的终止码，这取决于main函数返回栈的寄存器的内容。
现在使用1990 ISO C编译器扩展，则可以见到终止码改变了：
![](https://pic.downk.cc/item/5fc636c3f81f7e3bd97f43a0.jpg)

#### 2 函数atexit
按照ISO C的规定，一个进程可以登记多至32个函数，这些函数将由exit自动调用。我们称这些函数为终止处理程序，并调用atexit函数来登记这些函数。
atexit函数形式：
```c
#include <stdlib.h>

int atexit(void (*func)(void));
```
其中，atexit的参数是一个函数地址，当调用此函数时无需向它传递任何参数，也不期望它返回一个值。exit调用这些函数的顺序与它们登记的顺序相反，同一函数若被登记多次，也会被调用多次。

#### C程序的启动和终止
![](https://pic.downk.cc/item/5fc63689f81f7e3bd97f1e40.jpg)
#### 实例
```c
#include "apue.h"

static void	my_exit1(void);
static void	my_exit2(void);

int
main(void)
{
	if (atexit(my_exit2) != 0)
		err_sys("can't register my_exit2");

	if (atexit(my_exit1) != 0)
		err_sys("can't register my_exit1");
	if (atexit(my_exit1) != 0)
		err_sys("can't register my_exit1");

	printf("main is done\n");
	return(0);
}

static void
my_exit1(void)
{
	printf("first exit handler\n");
}

static void
my_exit2(void)
{
	printf("second exit handler\n");
}
```
输出：
![](https://pic.downk.cc/item/5fc636cef81f7e3bd97f463c.jpg)
可以看到，输出的顺序和注册的顺序是相反的。

## 4 命令行参数
当执行一个程序时，调用exec的进程可将命令行参数传递给该新程序。这是UNIX shell的一部分常规操作。在前几章的很多实例中，我们已经看到了这一点。

### 实例7.4
```c
#include "apue.h"

int
main(int argc, char *argv[])
{
	int		i;

	for (i = 0; i < argc; i++)		/* echo all command-line args */
		printf("argv[%d]: %s\n", i, argv[i]);
	exit(0);
}
```
其中argc是参数的数量，argv[]是参数的数组。
输出：
![](https://pic.downk.cc/item/5fc636e5f81f7e3bd97f4b5f.jpg)

#### 注意
argv[argc]是一个空指针。因此循环还可以如下表示：
```c
for(int i = 0; argv[i] != NULL; i++){ ... }
```

## 5 环境表
每个程序都接收到一张环境表。与参数表，环境表也是一个字符指针数组，其中每个指针包含一个以null结束的C字符串的地址。全局变量environ则包含了该指针数组的地址：
```c
extern char ** environ;
```
例如，如果该环境包含5个字符串，那么它看起来如图7-5所示：
![IMAGE](resources/1699E50E83653925D4D19D38049F423C.jpg =1189x503)
其中，每个字符串的结尾处都显式地有一个null字节。我们称environ为**环境指针**，指针数组为环境表，其中各指针指向的字符串为环境字符串。

历史上，大多数UNIX系统支持main函数带3个参数，其中第3个参数就是环境表地址：
```c
int main(int argc, char *argv[], char *envp[]);
```
如果要查看整个环境，则必须使用environ指针。

## 6 C程序的存储空间布局
共分为5个部分。
### 段部分：
#### 1. 正文段。
   CPU执行的机器指令部分。
   特点：
   a.通常正文段是可共享的，所以即使是频繁执行的程序（如文本编辑器，C编译器和shell等）在存储器中也只需有一个副本。 
   b.正文段常常是只读的，以防止程序由于意外而修改其指令。
#### 2. 初始化数据段
   通常称为数据段。包含了程序中需明确地赋初值的变量。例如，C程序中任何**函数之外**的声明：
   ```c
   int maxcount = 99;
   ```
   使此变量以其初值存放在初始化数据段中。
#### 3. 未初始化数据段
   通常称为bss段，在程序开始执行之前，内核将此段中的数据初始化为0或空指针。
   注意：也在函数外。
#### 段错误的几个直接原因：
a. 解引用一个包含非法值的指针
b. 解引用一个空指针
c. 未经授权访问
d. 用完了堆栈空间
### 堆和栈
#### 1. 栈

栈又称堆栈，由编译器自动分配释放，行为类似数据结构中的栈(先进后出)。堆栈主要有三个用途：
+ 为函数内部声明的非静态局部变量(C语言中称“自动变量”)提供存储空间。
+ 记录函数调用过程相关的维护性信息，称为栈帧(Stack Frame)或过程活动记录(Procedure Activation Record)。它包括函数返回地址，不适合装入寄存器的函数参数及一些寄存器值的保存。除递归调用外，堆栈并非必需。因为编译时可获知局部变量，参数和返回地址所需空间，并将其分配于BSS段。
+ 临时存储区，用于暂存长算术表达式部分计算结果或alloca()函数分配的栈内内存。

持续地重用栈空间有助于使活跃的栈内存保持在CPU缓存中，从而加速访问。进程中的每个线程都有属于自己的栈。向栈中不断压入数据时，若超出其容量就会耗尽栈对应的内存区域，从而触发一个页错误。此时若栈的大小低于堆栈最大值RLIMIT_STACK(通常是8M)，则栈会动态增长，程序继续运行。映射的栈区扩展到所需大小后，不再收缩。  
Linux中ulimit -s命令可查看和设置堆栈最大值，当程序使用的堆栈超过该值时, 发生栈溢出(Stack Overflow)，程序收到一个段错误(Segmentation Fault)。
![](https://pic.downk.cc/item/5fc63694f81f7e3bd97f2e63.jpg)
在macos中堆栈的最大值也是8M。
##### 注意
+ 调高堆栈容量可能会增加内存开销和启动时间。
+ 堆栈既可向下增长(向内存低地址)也可向上增长,这依赖于具体的实现。本文所述堆栈向下增长。
+ 堆栈的大小在运行时由内核动态调整。
#### 2. 堆
堆用于存放进程运行时动态分配的内存段，可动态扩张或缩减。堆中内容是匿名的，不能按名字直接访问，只能通过指针间接访问。当进程调用malloc(C)/new(C++)等函数分配内存时，新分配的内存动态添加到堆上(扩张)；当调用free(C)/delete(C++)等函数释放内存时，被释放的内存从堆中剔除(缩减) 。  
分配的堆内存是经过字节对齐的空间，以适合原子操作。堆管理器通过链表管理每个申请的内存，由于堆申请和释放是无序的，最终会产生内存碎片。堆内存一般由应用程序分配释放，回收的内存可供重新使用。若程序员不释放，程序结束时操作系统可能会自动回收。  
堆的末端由break指针标识，当堆管理器需要更多内存时，可通过系统调用brk()和sbrk()来移动break指针以扩张堆，一般由系统自动调用。  
##### 使用堆时经常出现两种问题：
+ 释放或改写仍在使用的内存(“内存破坏”)；
+ 未释放不再使用的内存(“内存泄漏”)。当释放次数少于申请次数时，可能已造成内存泄漏。泄漏的内存往往比忘记释放的数据结构更大，因为所分配的内存通常会圆整为下个大于申请数量的2的幂次(如申请212B，会圆整为256B)。

##### 注意
堆不同于数据结构中的”堆”，其行为类似链表。


#### 6. 内存映射块
此处，内核将硬盘文件的内容直接映射到内存,任何应用程序都可通过Linux的mmap()系统调用或Windows的CreateFileMapping()/MapViewOfFile()请求这种映射。  
内存映射是一种方便高效的文件I/O方式，因而被用于装载动态共享库。用户也可创建匿名内存映射，该映射没有对应的文件,可用于存放程序数据。在Linux中，若通过malloc()请求一大块内存，C运行库将创建一个匿名内存映射，而不使用堆内存。”大块” 意味着比阈值MMAP_THRESHOLD还大，缺省为128KB，可通过mallopt()调整。

该区域用于映射可执行文件用到的动态链接库。在Linux2.4版本中，若可执行文件依赖共享库，则系统会为这些动态库在从0x40000000开始的地址分配相应空间，并在程序装载时将其载入到该空间。在Linux2.6内核中，共享库的起始地址被往上移动至更靠近栈区的位置。

从进程地址空间的布局可以看到，在有共享库的情况下，留给堆的可用空间还有两处：
+ 从.bss段到0x40000000，约不到1GB的空间；
+ 另一处是从共享库到栈之间的空间，约不到2GB。

这两块空间大小取决于栈、共享库的大小和数量。这样来看，是否应用程序可申请的最大堆空间只有2GB？事实上，这与Linux内核版本有关。在上面给出的进程地址空间经典布局图中，共享库的装载地址为0x40000000，这实际上是Linux kernel 2.6版本之前的情况了，在2.6版本里，共享库的装载地址已经被挪到靠近栈的位置，即位于0xBFxxxxxx附近，因此，此时的堆范围就不会被共享库分割成2个“碎片”，故kernel 2.6的32位Linux系统中，malloc申请的最大内存理论值在2.9GB左右。

# 共享库和存储空间分配

## 共享库
程序第一次执行或者第一次调用某个库函数时，用动态链接方法将程序与共享库函数相链接。
这减少了每个可执行文件的长度，但增加了一些运行时间开销。
这种时间开销发生在该程序第一次被执行时，或者每个共享库函数第一次被调用时。

使用共享库编译程序：
![](https://pic.downk.cc/item/5fc63694f81f7e3bd97f2e57.jpg)





## 3个用于存储空间动态分配的函数
1. malloc，分配指定字节数的存储区。此存储区中的初始值不确定。
2. calloc，为指定数量指定长度的对象分配存储空间。该空间的每一位（bit）都初始化为0
3. realloc，增加或减少以前分配区的长度。当增加长度时，可能需将以前分配区的内容移到另一个足够长的区域，以便在尾端提供增加的存储区，而新增区域内的初始值则不确定。
#### 函数形式
```c
#include <stdlib.h>

void *malloc(size_t size);
void *calloc(size_t nobj, size_t size);
void *realloc(void *ptr, size_t newsize);

void free(void *ptr);
```

这些分配例程通常用sbrk(2)系统调用实现。该系统调用扩充（或缩小）进程的堆。
虽然sbrk可以扩充或缩小进程的存储空间，但是大多数malloc和free的实现都不减小进程的存储空间。释放后的空间可供以后再分配，但将他们保持在malloc池中而不返回给内核。

# 函数setjump和longjump的去呗

# 10 函数setjmp和longjmp
在C中，goto语句是不能跨越函数的，而执行这种类型跳转功能的是函数setjmp和longjmp。这两个函数对于处理发生在很深层嵌套函数调用中的出错情况是非常有用的。

### 常规栈帧演示
```c
#include "apue.h"
#define	TOK_ADD	   5
void	do_line(char *);
void	cmd_add(void);
int		get_token(void);
int
main(void)
{
	char	line[MAXLINE];
	while (fgets(line, MAXLINE, stdin) != NULL)
		do_line(line);
	exit(0);
}
char	*tok_ptr;		/* global pointer for get_token() */
void
do_line(char *ptr)		/* process one line of input */
{
	int		cmd;
	tok_ptr = ptr;
	while ((cmd = get_token()) > 0) {
		switch (cmd) {	/* one case for each command */
		case TOK_ADD:
				cmd_add();
				break;
		}
	}
}
void
cmd_add(void)
{
	int		token;

	token = get_token();
	/* rest of processing for this command */
}
int
get_token(void)
{
	/* fetch next token from line pointed to by tok_ptr */
}
```
在这里栈帧的主要变化情况为：
![](https://pic.downk.cc/item/5fc6367bf81f7e3bd97f06c7.jpg)
可以看到，每个函数对应一个栈帧，并且从上往下依次扩展。
如上所诉，这种形式的栈安排是非常典型的。
### 遇见问题
而我们在编写上面程序时会经常遇到一个问题，如何处理非致命的错误。例如若cmd_add函数发现一个错误（例如一个无效的数），那么它可能先打印一个错误信息，然后忽略输入行的剩余部分返回main，再读取下一行、但是如这种情况如果出现在main函数中的深层嵌套中时，用C语言难以做到，那就回变得很麻烦。
理解：
如果有多达5、6个嵌套，那么可能返回到的位置并不确定，例如返回到第3层或者第2层。
### 解决
用非局部goto-setjmp和Longjmp函数。非局部的意思是不是由普通C语言goto语句在一个函数内实施的跳转，而是在栈上跳过若干调用帧，返回到当前函数调用路径上的某一函数中。
#### 理解
例如在第6层嵌套时发现输入了一个无效的数，那么我们就可以用setjmp和longjmpm直接回调到第3层嵌套。而C语言没办法做到这点。

### setjmp和longjmp
#### 函数形式
```c
#include <setjmp.h>
int setjmp(jmp_buf env);

void longjmp(jmp_buf env, int val);
```
#### 参数
env：是一个特殊类型jmp_buf。这一数据类型是某种形式的数组，其中存放在调用longjmp时能用来恢复栈状态的所有信息。因为需要在另一个函数中引用env变量，所以通常将env变量定义为全局变量。  
val：非0值，它将成为从setjmp处返回的值。使用这个参数的原因是对于一个setjmp可以有多个longjmp。

### 经过修改的main和cmd_add
```c
#include "apue.h"
#include <setjmp.h>

#define	TOK_ADD	   5

jmp_buf	jmpbuffer;

int
main(void)
{
	char	line[MAXLINE];

	if (setjmp(jmpbuffer) != 0)
		printf("error");
	while (fgets(line, MAXLINE, stdin) != NULL)
		do_line(line);
	exit(0);
}

 . . .

void
cmd_add(void)
{
	int		token;

	token = get_token();
	if (token < 0)		/* an error has occurred */
		longjmp(jmpbuffer, 1);
	/* rest of processing for this command */
}
```
首先执行main函数，调用setjmp，它将所需信息记入变量jmpbuffer中并返回0（初次调用返回0）.然后调用do_line，最后do_line调用cmd_add，假设此时检测到一个错误。在cmd_add中调用了longjmp之前，就如上图7.10，但是longjmp使栈反绕到执行main函数时的情况，也就是抛弃了cmd_add和do_line的栈帧，如下图：
![](https://pic.downk.cc/item/5fc63659f81f7e3bd97ee586.jpg)
调用longjmp造成main函数中setjmp的返回，但是这次返回值为1.

### 自动变量、寄存器变量和易失变量
问题：
在main函数中，自动变量（临时变量）和寄存器变量的状态如何？
当longjmp返回到main函数时，这些变量的值是否能恢复到调用setjmp时的值？
答案：
看情况！！ 这是不确定的。如果有一个自动变量而又不想使其回滚，则可定义其为有volatile属性。声明为全局变量或静态变量的值在执行longjmp时保持不变。

#### 举例(fig7.13)
```c
#include "apue.h"
#include <setjmp.h>

static void	f1(int, int, int, int);
static void	f2(void);

static jmp_buf	jmpbuffer;
static int		globval;

int
main(void)
{
	int				autoval;
	register int	regival;
	volatile int	volaval;
	static int		statval;

	globval = 1; autoval = 2; regival = 3; volaval = 4; statval = 5;

	if (setjmp(jmpbuffer) != 0) {
		printf("after longjmp:\n");
		printf("globval = %d, autoval = %d, regival = %d,"
		    " volaval = %d, statval = %d\n",
		    globval, autoval, regival, volaval, statval);
		exit(0);
	}

	/*
	 * Change variables after setjmp, but before longjmp.
	 */
	globval = 95; autoval = 96; regival = 97; volaval = 98;
	statval = 99;

	f1(autoval, regival, volaval, statval);	/* never returns */
	exit(0);
}

static void
f1(int i, int j, int k, int l)
{
	printf("in f1():\n");
	printf("globval = %d, autoval = %d, regival = %d,"
	    " volaval = %d, statval = %d\n", globval, i, j, k, l);
	f2();
}

static void
f2(void)
{
	longjmp(jmpbuffer, 1);
}
```
解析：
1. 先在main函数中初始化jmpbuffer。
   ```c
   if(setjmp(jmpbuffer) != 0}{
   ...
   }
   ```
2. 接着在调用longjmp之前改变所有变量的值。
3. 调用f1
4. 通过f1调用f2
5. 最后f2调用longjmp返回setjmp处，此时进行打印。看所有的变量值是否会改变。


#### 注意：
理论上若使用带优化和不带优化选项对此程序进行编译的时候运行结果是不同的，但是我的macos系统上好像没有gcc -0这个参数，因此我没办法输出，这里给出书上的结果：
![](https://pic.downk.cc/item/5fc636f5f81f7e3bd97f4dd9.jpg)
但是我的系统第二种方式输出时报错：
![](https://pic.downk.cc/item/5fc63669f81f7e3bd97eec24.jpg)
不知道原因是什么，希望大家可以指正。

















