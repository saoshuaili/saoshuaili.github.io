---
categories:
  - 计算机
  - Linux
  - 网络编程
  - UNP
title: 第8章 基本UDP套接字编程  
date: 2022-12-11 19:04:39  
tags: []  

---

# 1. 概述

在使用 TCP 编写的应用程序和使用 UDP 编写的应用程序之间存在一些本质差异，其原因在
于这两个传输层之间的差别：UDP 是无连接不可靠的数据报协议，非常不同于 TCP 提供的面向连接的可靠字节流。然而相比 TCP, 有些场合确实更适合使用 UDP

这里给出典型的 UDP 客户/服务器程序的函数调用。客户不与服务器建立连接，而是只管使用 `sendto` 函数给服务器发送数据报，其中必须指定目的地（即服务器）。类似的，服务器不接受来自客户端的连接，而是只管调用 `recvfrom` 函数，等待来自某个客户的数据到达。

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20221211190413.png" width = "70%" />

上图可以跟第四章 [[第4章 基本TCP套接字编程]]中 TCP 的函数调用图做个比较，可以看出是完全不同的。

# 2. recvfrom 和 sendto 函数

这两个函数类似于标准的 `read` 和 `write` 函数，不过需要三个额外的参数。类似于在网络环境中执行 `read` 和 `write`，因此需要一些额外的指定网络环境的参数。

来看下 `recvfrom` 和 `sendto` 的源代码：

```c
#include <sys/socket.h>

ssize_t recvfrom(int sockfd, void *buff, size_t nbytes, int flags, 
	struct sockaddr *from, socklen_t *addrlen);

ssize_t sendto(int sockfd, const void *buff, size_t nbytes, int flags, 
	const struct sockaddr *to, socklen_t *addrlen);

// 均返回：若成功则为读或写的字节数。若出错则为-1
```

可以看到，两个函数很像，其中前三个参数 `sockfd`、 `buff` 和 `nbytes` 等同于 `read` 和 `write` 函数的三个参数：描述符、指向读入或写出缓冲区的指针和读写字节数。

`flags` 参数将在[[第14章 高级IO函数]]中讨论 `recv`、`send`、 `recvmsg` 和 `sendmsg` 等函数时再介绍，本章中重写简单的 `UDP` 回射客户/服务器程序用不若它们。时下我们总是把 `flags` 置为 0。

`sendto` 的 `to` 参数指向一个含有数据报接收者的协议地址（例如 IP 地址及端口号）的套接字地址结构，其大小由 `addrlen` 参数指定。`recvfrom` 的 `from` 参数指向一个将由该函数在返回时境写数据报发送者的协议地址的套接字地址结构，而在该套接字地址结构中填写的字节数则放在 `addrlen`  参数所指的整数中返回给调用者。注意，`sendto` 的最后一个参数是一个整数值，而 `recvfrom` 的最后一个参数是一个指向整数值的指针（即值-结果参数）。

这两个函数都把所读写数据的长度作为函数返回值。在 `recvfromt` 使用数据报协议的典型用途中，返回值就是所接收数据报中的用户数据量。
