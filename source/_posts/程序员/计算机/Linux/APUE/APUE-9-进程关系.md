---
title: APUE-9-进程关系  
date: 2022-12-04 02:38:13  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 1 终端登录和网络登录

### 终端介绍
计算机=主机+终端
终端=输入设备+输出设备

也就是说，终端其实就是向主机输入信息和输出信息的设备，可以分为输入设备和输出设备。台式机的输入通常包括键盘、鼠标、麦克风，输出设备包括显示器、扬声器等。
#### 理解：
也就是说，我们最开始买来的笔记本电脑只有一个主机，没有输入输出设备，不能使用。因此我们需要购买一些终端来进行输入输出操作，例如我们买键盘和县食品，那么我们就可以通过输入终端（键盘）进行输入操作，通过输出终端（显示屏）来进行输出操作，显示我们的输入经过主机运算之后得到的内容。
#### 注意
终端本身没有任何计算等功能，只有输入和输出的功能，只有经过主机的计算才能有作用，否则就是废铁

#### 终端服务器
若一个机构有多台主机，那么要让每个终端都能连接到任意一个主机，就需要一个终端服务器，让终端和终端服务器之间连接，然后用户启动时选择要登录得主机，类似于今天的路由器。

#### 控制台（Console）
大部分终端（例如鼠标键盘）都是通过线路连接。但是有个终端与众不同，它是与主机一体的，不需要连线，这个特殊的终端就是console。中文名为控制台。Console是用来管理主机的，只能给系统管理员使用，而且一个主机只能有一个console。

#### 控制台和普通终端的区别
Console与一般终端在外表上没什么区别，但是在功能上却大不相同，有些事情只能在console上完成，而不能在终端完成。例如当系统启动出错时，错误信息会显示在Console的显示器屏幕上，而不会显示在一般的终端上。在单用户模式下，只有console才能连接到主机，其他终端没有权限访问主机。

#### 终端模拟程序
终端属于硬件。从前用户坐在真实的终端前面输入命令，现在输入命令时不能使用真正的终端。因为现在真实的终端是图形界面的终端。现在，用户要输入命令必须使用终端模拟程序(Terminal Emulation Program)。终端模拟程序就是我们平常在Linux桌面环境中打开的终端窗口(Terminal Window)，它是个虚拟终端，但主机会认为它就是真正的终端。终端模拟程序也可以叫做终端模拟器(Terminal Emulator)。


### 虚拟控制台
终端模拟程序有两种：
1. 终端窗口
2. 虚拟控制台

Unix允许用户在自己电脑上使用多个终端，其中一个是图形终端，其他六个是字符终端。这七个终端使用同一个显示器和键盘。


参考文献：
https://www.linuxdashen.com/%E4%BD%A0%E7%9C%9F%E7%9A%84%E7%9F%A5%E9%81%93%E4%BB%80%E4%B9%88%E6%98%AF%E7%BB%88%E7%AB%AF%E5%90%97%EF%BC%9F


## 控制终端
终端有两种类型：
1. 常规终端设备
2. 伪终端设备
常规终端设备是一种本机终端设备，可以键入该终端设备并将输入发送到系统，例如我们输入Ctrl+Alt+F1进入TTY1，可以注意到除了终端，没有图形，这就是常规终端设备。
伪终端是我们使用额终端，它们使用shell终端窗口模拟终端，并用PTS表示。

通常将进程绑定到控制终端。

# 2 进程组和会话

## 进程组
每个进程除了有一进程ID之外，还属于一个进程组。
### 进程组定义
进程组是一个或多个进程的集合，通常，它们是在同一作业中结合起来的。
### 进程组函数
```c
#include <unistd.h>

pid_t getpgrp(void);
```
返回值时**调用进程**的进程组的ID。
注意：
这里只能是调用进程的进程组ID，不能查看别的进程对应的进程组ID。如是要查看别的组的进程组ID，可以用getpgid
```c
#include <unistd.h>

pid_t getpgid(pid_t pid);
```
若pid是0，则返回调用进程的组ID，出错则返回-1.

### 组长ID
每个进程组有一个组长进程。
组长进程特定：
*组长进程的进程组ID等于其进程ID。*
也就是说，若是需要查看一个进程的进程组的组长ID，只需要查看进程的进程组ID就行了，这两者是相等的。

#### 进程和进程组ID举例
```c
#include <unistd.h>
#include <stdio.h>

int main(){

  pid_t pid;    //进程ID
  pid_t pgid;   //进程组ID

  pgid = getpgid(0);
  pid = getpid();

  printf("进程号为：%d\n", pid);
  printf("进程组号为：%d\n", pgid);


  exit(0);
}
```
输出：

![](https://pic.downk.cc/item/5fc636c3f81f7e3bd97f43a4.jpg))

解释：

在这里，该进程ID就是该进程组ID，也就是说，这个进程的进程组的组长进程就是该进程。


## 进程组加入或创建
进程调用setpgid可以加入一个现有的进程组或者创建一个新进程组。
```c
#include <unistd.h>

int setpgid(pid_t pid, pid_t pgid);
```
作用：
将进程pid的进程组ID设置为pgid。若pid=pgid，则有pid指定的进程变成进程组组长。
情况：
1. 如果两个参数相等，就将pid指定的进程变成进程组组长 
2. 如果pid是0，则使用调用者的进程ID。
1. 如果pgid是0，则由pid指定的进程ID用作进程组ID。

举例：
```c
#include <unistd.h>


int main(){
  pid_t pid;
  pid_t pgid;
  int status;
  int id;

  pid = getpid();
  pgid = getpgid(0);

  printf("父进程id=%d\n", pid);
  printf("父进程组id=%d\n", pgid);

  id = fork();
  if (id == 0){
    pid_t tmp_parentid = pid;
    pid = getpid();
    int tmp=setpgid(tmp_parentid, pid);
    if (tmp == -1){
      printf("error");
    }
    // setpgid(pid, pid);
    pgid = getpgid(0);

    printf("子进程id=%d\n", pid);
    printf("子进程组id=%d\n", pgid);

    exit(0);
  }
  wait(&status);
  pid = getpid();
  pgid = getpgid(0);


  printf("父进程id=%d\n", pid);
  printf("父进程组id=%d\n", pgid);

exit(0);

}
```
#### 输出：
![](https://pic.downk.cc/item/5fc636e5f81f7e3bd97f4b62.jpg)


# 3 会话
会话(session)是一个或者多个进程组的集合
说白了，会话其实就是多个进程组之间的联系，例如后台进程组和前台进程组之间。
## 举例：一个含有3个进程组的会话
![](https://pic.downk.cc/item/5fc636e5f81f7e3bd97f4b54.jpg)

## 调用setsid函数建立一个新的会话
```c
#include <unistd.h>

pid_t setsid(void);
```
若成功返回进程组ID，若失败返回-1.
如果调用函数的进程不是一个进程组的组长，则该函数会创建一个新会话。具体发生以下三件事：
1. 该进程会变成新会话的**会话首进程**（session leader，也就是创建该会话的进程）。此时该进程是新会话中的唯一进程
2. 该进程称为一个新进程组的组长进程。新进程组ID是调用该进程的进程ID
3. 该进程没有控制终端。如果在调用setsid之前该进程有一个控制终端，那么这种联系也会被切断。

## 返回错误的情况
若调用该进程已经是进程组的组长，则此函数返回出错。

## 如何设置
通常先调用fork，然后使其父进程终止，而子进程继续，因为子进程继承了父进程的进程组ID，而且其进程ID是重新分配的，两者不可能相等，这就保证了子进程不是一个进程组的组长。

## 调用getsid函数返回会话首进程的进程组ID
```c
#include <unistd.h>

pid_t getsid(pid_t pid);
```
若成功返回会话首进程的进程组ID，出错返回-1.

## setsid和getsid实例
```c
#include <unistd.h>

int main(){
  pid_t pid;
  pid_t pgid;
  pid_t sid;
  int status;

  pid = getpid();
  pgid = getpgid(0);
  sid = getsid(pid);

  printf("父进程id为%d\n",pid);
  printf("父进程组id为%d\n",pgid);
  printf("父进程sid为%d\n",sid);

  int tmpid;
  tmpid = fork();
  if(tmpid == 0){
    pid = getpid();
    sid = getsid(pid);
    printf("子进程目前的sid为%d\n",sid);
    pgid = setsid();
    sid = getsid(pid);
    printf("子进程id为%d\n",pid);
    printf("子进程新组id为%d\n",pgid);
    printf("子进程新sid为%d\n",sid);
    exit(0);
  }
  wait(&status);
  pid = getpid();
  sid = getsid(pid);
  pgid = getpgid(0);
  printf("父进程新id为%d\n",pid);
  printf("父进程新组id为%d\n",pgid);
  printf("父进程新sid为%d\n",sid);
  exit(0);
}
```
#### 输出：
![](https://pic.downk.cc/item/5fc636d9f81f7e3bd97f4922.jpg)
#### 分析：
先是得到了父进程的进程id为1717，此时父进程的组id就是父进程的id，会话ID是831。然后父进程fork了一个子进程，子进程的id为1857，并且当前的会话ID和父进程的相同，也为831.经过setsid之后将当前的ID设置成了会话ID，但是并不影响父进程的ID、组ID和会话ID。



















