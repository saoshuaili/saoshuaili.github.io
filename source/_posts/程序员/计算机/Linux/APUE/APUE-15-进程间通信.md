---
title: APUE-15-进程间通信  
date: 2022-12-04 02:38:42  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 1 进程间通信分类
1. 管道
2. XSI -> SysV
3. 网络套接字

其中1和2是在同一台主机上的，3是在不同主机上的。

## 管道
内核提供，单工通信（一端作为读端，一端作为写端），自同步机制
### 管道分类
1. 匿名管道
匿名管道是由内核创建的，磁盘不可见（也就是使用ls命令时看不到），通过调用pipe函数创建一个匿名管道。
2. 命名管道


### 管道的创建
通过调用pipe函数创建一个匿名管道
```c
#include <unistd.h>

int pipe(int fd[2]);
```
返回值：
若成功返回0，若出错返回-1.
并且通过参数fd返回两个文件描述符：fd[0]为读而打开，fd[1]为写而打开。fd[1]的输出是fd[0]的输入。
#### 解释
也就是说，通过参数fd返回两个文件，一个文件专门读，一个文件专门写。
如图所示：
![](https://pic.downk.cc/item/5fc63658f81f7e3bd97ee579.jpg)

单个进程的管道几乎没有任何用户，通常，进程会先调用pipe，然后调用fork，创建从父进程到子进程的IPC通道，反之亦然。
也就是说，父进程先调用pipe，创建两个可以互相沟通的文件，再调用fork，因为fork出来的子进程完全继承了父进程的一切特性，因此他也拥有者两个文件，因此父子进程可以通过这两个文件进行沟通。
如图所示：
![](https://pic.downk.cc/item/5fc63633f81f7e3bd97eddb4.jpg)
### 管道的方向
对于从父进程到子进程的管道，父进程关闭管道的读端（fd[0]），子进程关闭写端（fd[1]）。
![](https://pic.downk.cc/item/5fc6361ff81f7e3bd97eda29.jpg)
这样就创建了一个单方向的从父进程到子进程的管道。

### 管道实例（fig15.5）
```c
#include "apue.h"

int
main(void)
{
	int		n;
	int		fd[2];
	pid_t	pid;
	char	line[MAXLINE];

	if (pipe(fd) < 0)
		err_sys("pipe error");
	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid > 0) {		/* parent */
		close(fd[0]);
		write(fd[1], "hello world\n", 12);
	} else {					/* child */
		close(fd[1]);
		n = read(fd[0], line, MAXLINE);
		write(STDOUT_FILENO, line, n);
	}
	exit(0);
}
```
输出：

![](https://pic.downk.cc/item/5fc6369df81f7e3bd97f3894.jpg)

解释：

可以看到，先创建了一个pipe，然后调用fork创建子进程，将父进程的fd[0]关闭，再将子进程的fd[1]关闭，变成了一个单向管道。


### 管道的应用
我们有一个父进程，此时我们需要通过输入来进行一些操作，而这些操作不可能通过父进程来完成，那么可以先让父进程创建一个管道，然后fork一个子进程，让子进程调用exec，执行接收到的命令。
```c
#include "apue.h"
#include <sys/wait.h>

#define	DEF_PAGER	"/bin/more"		/* default pager program */

int
main(int argc, char *argv[])
{
	int		n;
	int		fd[2];
	pid_t	pid;
	char	*pager, *argv0;
	char	line[MAXLINE];
	FILE	*fp;

	if (argc != 2)
		err_quit("usage: a.out <pathname>");

	if ((fp = fopen(argv[1], "r")) == NULL)
		err_sys("can't open %s", argv[1]);
	if (pipe(fd) < 0)
		err_sys("pipe error");

	if ((pid = fork()) < 0) {
		err_sys("fork error");
	} else if (pid > 0) {								/* parent */
		close(fd[0]);		/* close read end */

		/* parent copies argv[1] to pipe */
		while (fgets(line, MAXLINE, fp) != NULL) {
			n = strlen(line);
			if (write(fd[1], line, n) != n)
				err_sys("write error to pipe");
		}
		if (ferror(fp))
			err_sys("fgets error");

		close(fd[1]);	/* close write end of pipe for reader */

		if (waitpid(pid, NULL, 0) < 0)
			err_sys("waitpid error");
		exit(0);
	} else {										/* child */
		close(fd[1]);	/* close write end */
		if (fd[0] != STDIN_FILENO) {
			if (dup2(fd[0], STDIN_FILENO) != STDIN_FILENO)
				err_sys("dup2 error to stdin");
			close(fd[0]);	/* don't need this after dup2 */
		}

		/* get arguments for execl() */
		if ((pager = getenv("PAGER")) == NULL)
			pager = DEF_PAGER;
		if ((argv0 = strrchr(pager, '/')) != NULL)
			argv0++;		/* step past rightmost slash */
		else
			argv0 = pager;	/* no slash in pager */

		if (execl(pager, argv0, (char *)0) < 0)
			err_sys("execl error for %s", pager);
	}
	exit(0);
}
```

# 2 命名管道

命名管道叫FIFO，之前介绍的管道都是匿名管道，只能在两个相关的进程间使用（也就是说，fork的一个有血缘关系的进程之间使用）。

## 命名管道的创建
我们用函数mkfifo来创建管道
```c
#include <sys/stat.h>

int mkfifo(const char *path, mode_t mode);
```
也可以直接使用命令mkfifo来创建一个命名管道，效果：
![](https://pic.downk.cc/item/5fc6367cf81f7e3bd97f06eb.jpg)

## FIFO的使用
创建了名为my_fifo的管道之后，我们可以用两个shell（两个进程）对其进行读写。
例如我们将天气情况读入my_fifo中。  
![](https://pic.downk.cc/item/5fc63694f81f7e3bd97f2e75.jpg)  
可以看到如图所示，将date的信息写入my_fifo之后不会结束，因为比如得有进程将其读出之后才能结束，因此我们再用另一个shell来读这个FIFO。  
![](https://pic.downk.cc/item/5fc63707f81f7e3bd97f5228.jpg)
![](https://pic.downk.cc/item/5fc63658f81f7e3bd97ee57e.jpg) 
可以看到，将信息读出之后之前读入的进程也结束了。

# 3 XSI IPC

## XSI定义
XSI就是System Interface and Headers:是一种UNIX系统协议
IPC就是Interprocess Communication：进程间通信

## 种类
有三种称为XSI IPC的IPC：**消息队列**、**信息量**和**共享存储器**。

## IPC的通用功能。

### 1 标识符和键
每个内核中的IPC结构都用一个非负整数的标识符加以引用。
标识符是IPC对象的内部名。每个IPC对象都与一个键（key）相关联，将这个键作为该对象的外部名。

#### 创建键的方法
使用ftok能使用路径名和项目ID产生一个键
```c
#include <sys/ipc.h>

key_t ftok(const char *path, int id);
```
参数：
path参数必须引用一个现有的文件。并且产生键时，只使用id参数的低8位。
因此对于不同文件的两个路径名，如果使用同一项目ID，那么可能产生相同的键。

#### 举例
```c
#include <stdio.h>
#include <sys/ipc.h>

int main()
{
    key_t key = ftok("/Users/heyizhi/程序员/Linux/Linux学习/UNIX环境高级编程/apue.3e/figlinks/ipc/pipe", 500);
    printf("%d\n", key);
    return 0;
}
```
在这里我们使用一个管道的路径和项目ID=500来创建了一个key。
输出：
![IMAGE](resources/9528BA501FB592F


### 2 key和flag
3中IPC的get函数功能相同，就是创建或者打开一个IPC。例如msgget的作用就是打开或者创建一个消息队列。他们都有两个类似的参数：一个key和一个整型flag。在创建新的IPC结构时，如果key是IPC_PRIVATE或者和当前某种类型的IPC结构无关，则需要指明flag的IPC_CREAT标志位。
若需要引用一个现有队列，key必须等于队列创建时指定的key的值，并且ICP_CREAT必须不被指明

#### 注意
绝不能指定IPC_PRIVATE作为键来引用一个现有的队列。

# 4 消息队列

## 定义
消息队列是消息的链接表，存储在内核中，由消息队列标识符标识。


## 1 调用的第一个函数通常是msgget
### 1.1 作用
其功能是打开一个现有队列或创建一个新队列
### 1.2 函数
```c
include <sys/msg.h>

int msgget(key_t key, int flag);
```
key是通过之前提高的ftok()函数所生成的。flag在第三节已经讲过。在创建新队列时，需要初始化msqid_ds结构的下列成员。


## 举例
由三个文件组成，分别是发射端，接收端和头文件。
### 头文件
```c
//在这里是我们的头文件，设定了传输协议，预定双方对话格式

#ifndef PROTO_H_
#define PROTO_H_

#define KEYPATH "/etc/services"
#define KEYPROJ 'g'  //表示当前字符一定可以转型给转型数

#define NAMESIZE 32


struct msg_st
{
    long mtype;
    char name[NAMESIZE];
    int math;
    int chinese;
};

#endif
```

### 接收端
```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#include "proto.h"

int main(){
    key_t key;
    int msgid;
    struct msg_st rbuf;

    key = ftok(KEYPATH, KEYPROJ);
    if(key < 0)
    {
        perror("ftok()");
    }

    msgid = msgget(key, IPC_CREAT|0600);
    if(msgid < 0)
    {
        perror("msgget()");
        exit(1);
    }

    while(1)
    {
        if(msgrcv(msgid, &rbuf, sizeof(rbuf) - sizeof(long),0,0) < 0)
        {
            perror("msgrcv()");
            exit(1);
        }
        printf("NAME = %s\n", rbuf.name);
        printf("MATH = %d\n", rbuf.math);
        printf("CHINESE = %d\n",rbuf.chinese);
    }

    msgctl(msgid, IPC_RMID, NULL);
    exit(0);
}
```

### 发射端
```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include "proto.h"

int main(){

    key_t key;
    int msgid;
    struct msg_st sbuf;

    key = ftok(KEYPATH, KEYPROJ);
    if(key < 0)
    {
        perror("ftok()");
        exit(1);
    }

    msgid = msgget(key, 0);
    if(msgid < 0)
    {
        perror("msgid()");
        exit(1);
    }

    sbuf.mtype = 1;
    strcpy(sbuf.name, "Alan");
    sbuf.math = rand()%100;
    sbuf.chinese = rand()%100;
    if(msgsnd(msgid, &sbuf, sizeof(sbuf) - sizeof(long), 0) < 0){
        perror("msgsnd();");
        exit(1);
    }

    puts("OK!");

    exit(0);
}
```

# 5 信号量

信号量的英文是Semephore

同样的，主要是三种函数，分别是semget()、semop()和semctl()



















