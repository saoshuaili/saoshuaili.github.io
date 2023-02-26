---
title: APUE-5-标准IO库  
date: 2022-12-04 02:37:44  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 标准I/O介绍

## 1 引言
本章讲述标准I/O库。不仅是UNIX，很多其他操作系统都实现了标准I/O库，所以这个库由ISO C标准说明。
标准I/O库处理很多细节，如缓冲区分配，以优化的块长度执行I/O等。这些处理使用户不必担心如何选择使用正确的块长度。

### 注意
FILE类型贯穿始终，FILE是一个结构体，随着学习深入会逐渐了解。
### FILE的产生
FILE类型通过fopen来产生。


## 2 流和FILE对象

### 系统调用I/O和标准I/O的区别
#### 系统调用I/O：
对于系统调用I/O函数，都是围绕**文件描述符**展开的。当打开一个文件时，返回一个文件描述符，然后该文件描述符就用于后续的I/O操作。
#### 标准I/O：
而对于标准I/O库，它们的操作是围绕**流**进行的，当标准I/O库打开或创建一个文件时，我们已使一个流与一个文件相关联。
### 文件和流的区别和联系
#### 定义
文件是计算机管理数据的基本单位，同时也是应用程序保存和读取数据的一个重要场所。
流是字节序列的抽象概念，例如文件、输入/输出设备、内部进程通信管道等。流提供一种向后备存储器写入字节和从后备存储器读取字节的方式。
#### 存储介质
文件是指在各种存储介质上（如硬盘、可移动磁盘、CD等）永久存储的数据的有序集合，它是进行数据读写操作的基本对象。
除了和磁盘文件直接相关的文件流以外，流还有多种类型。流可以分布在网络中、内存中或者是磁带中。

### 流的定向
我们已经知道，流是字节序列的抽象概念。
对于**ASCII字符集**，一个字符用**一个**字节表示：称为单字节字符集
对于**国际字符集**，一个字符用**多个**字节表示：称为多字节（宽）字符集
#### 定义
**流的定向**决定了所读、写的字符是单字节还是多字节。 
解释：
也就是说流的定向决定了所读写的字符是ASCII字符集还是国际字符集

#### fwide函数
fwide函数可用于设置流的定向
```c
#include <stdio.h>
#include <wchar.h>

int fwide(FILE *fp, int mode);
```
参数：
参数mode的不同值fwide执行不同的工作：
1. 若mode为负，则fwide将试图使指定的流是字节定向的（ASCII字符集）
2. 若mode为正，则fwide将试图使指定的流是宽定向的（国际字符集）
3. 若mode为0，则fwide将不试图设置流的定向，但返回标识该流定向的值。


### 标准输入、标准输出和标准错误
标准输入、标准输出和标准错误是进程预定义的3个流，并且这3个流可以自动被进程使用，
这3个标准I/O流通过预定义文件指针stdin、stdout和stderr加以引用。这3个文件指针定义在头文件<stdio.h>


### 缓冲
#### 缓冲目的
标准I/O库提供缓冲的目的是尽可能减少使用read和write调用的次数。它也对每个I/O流自动进行缓冲管理。

### 3种类型的缓冲
#### 1. 全缓冲
在这种情况下，在填满标准I/O缓冲区后才进行实际的I/O操作。
##### 冲洗（`flush`）
术语冲洗（`flush`）说明标准I/O缓冲区的写操作。值得注意的是，UNIX环境中，flush有两种意思。在标准I/O库方面，flush意味着将缓冲区中的内容写到磁盘上（此时该缓冲区的内容可能没有填满）。在终端驱动程序方面，flush表示丢弃已存储在缓冲区中的数据
#### 2. 行缓冲
顾名思义，行缓冲的意思是遇到换行符之后才进行I/O操作。这允许我们一次输出一个字符（用标准I/O函数fputc），但只有在写了一行之后才进行实际I/O操作。
##### 对于行缓冲有两个限制
1. 因为标准I/O库用来收集每一行的缓冲区的长度是固定的，所以只要填满缓冲区，那么及时还没有写一个换行符，也进行I/O操作。
2. 任何时候只要通过I/O库要求
（a）一个不带缓冲的流
（b）一个行缓冲的流得到输入数据，那么就会冲洗所有行缓冲输出流。

#### 3. 不带缓冲：标准I/O库不对字符进行缓冲存储。

### 缓冲设置 
对任何一个给定的流，如果我们并不喜欢这些系统默认，则可调用下列两个函数中的一个更改缓冲类型。
```c
#include <stdio.h>
void setbuf(FILE *restrict fp, char *restrict buf);
void setvbuf(FILE *restrict fp, char *restrict buf, int mode, size_t size);
```
使用setvbuf,我们可以精确地说明所需的缓冲类型。这是用mode参数实现的：
_IOFBF 全缓冲
_IOLBF 行缓冲
_IONBF 不带缓冲
如果指定一个不带缓冲的流，则忽略buf和size参数。如果指定全缓冲或行缓冲，则buf和size可选择地指定一个缓冲区及其长度。如果该流是带缓冲的，而buf是NULL，则标准I/O库将自动地为该刘分配适当长度的缓冲区。适当的长度指的是由常量BUFSIZE指定的值。
任何时候，我们都可强制冲洗一个流。
```c
#include <stdio.h>

int fflush(FILE *fp);
```
此函数使所有未写的数据都被传送至内核。作为一种特殊情形，如若fp是NULL，此函数将导致所有输出流被冲洗。

# fopen系列函数
下列3个函数打开一个标准I/O流。
```c
#include <stdio.h>

FILE *fopen(const char *restrict pathname, const char *restrict type);
FILE *freopen(const char *restrict pathname, const char *restrict type, FILE *restrict fp);
FILE *fdopen(int fd, const char *type);
```
## 函数区别如下
### fopen
fopen 函数打开路径名为pathname的一个指定的文件。
### freopen
freopen 函数在一个制定的流上打开一个指定的文件，如若该流已经打开，则先关闭该流。若该流已经定向，则使用freopen清除该定向。此函数一般用于将一个指定的文件打开为一个预定义的流：标准输入，标准输出或标准错误。
### fdopen
fdopen函数取一个已有的文件描述符（我们可能从open、dup、dup2、fcntl、pip、socket、socketpair或accept函数得到此文件描述符），并使一个标准的I/O流与该描述符相结合。此函数常用与创建管道和网络通信通道函数返回的描述符。因为这些特殊类型的文件不能用标准I/O函数fopen打开，所以我们必须先调用设备专用函数以获得一个文件描述符，然后用fdopen使一个标准I/O流与该描述符相结合。

## 参数
### 1.pathname
路径名，const修饰表明不会对传输的参数做任何修改。
### 2.type
权限
![](https://pic.downk.cc/item/5fc63722f81f7e3bd97f588e.jpg)
#### 总结
1. r代表只读，若文件不存在则报错。
2. w代表只写，并且文件若存在则将其清空，若不存在则创建
3. a代表追加，并且文件若存在则继续写，若不存在则创建
4. a+模式则较为特殊，若是读操作则放开头，写操作则放在结尾。
#### 注意
在UNIX中，b这个字符无关紧要，也就是说r和rb含义相同。

### 返回值
返回值是一个指向FILE结构体的指针，FILE结构体在上一节中给出了介绍，那么这个指针的指向位置是在哪呢？
答案：
堆上。
解释：
存放位置有三个可能性：栈、静态区和堆
而函数的形式如下：
```c
FILE *fopen(const char *restrict pathname, const char *restrict type){
   
   return ;
}
```
可以看到，若是栈，那么意味着是局部变量，在函数调用结束之后就被销毁了，因此不可能是栈。
那么若是静态区，则需要在函数体中
```c
static FILE tmp;
return &tmp;
```
但是对于static修饰的变量只能声明一次，也就是说，以后再调用fopen函数时不会重新static定义一个变量了，只会返回相同的变量。
因此只能在堆上。
也就是需要在定义时就先分配一定空间，如下：
```c
FILE *tmp = null;
tmp = malloc(sizeof(FILE)); //至关重要，将其分配在了堆上
```

# fclose函数
fclose函数作用于fopen相反
## 注意
fopen里有malloc函数，因为在fclose里面需要free。这是成对出现的
## 函数形式
```c
#include <stdio.h>

int fclose(FILE *fp);
```
### 返回值
若成功返回0，若失败返回EOF（一般是-1，但是为了严谨需要使用EOF）
### 动作
在该文件被关闭之前，冲洗缓冲区中的输出数据。缓冲区中的任何输入数据被丢弃。如果标准I/O库已经为该流自动分配了一个缓冲区，则释放此缓冲区
当一个进程正常终止时，则所有带未写缓冲数据的标准I/O流都被冲洗，所有打开的标准I/O流都被关闭。

## fopen和fclose举例
```c

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>


int main()
{
    FILE *fp;
    fp = fopen("tmp","w");
    if(fp == NULL){
        fprintf(stderr, "fopen():%s\n",strerror(errno));
        exit(1);
    }

    puts("OK!");

    fclose(fp);


    exit(0);
}

```
解释：在这里以写的形式打开一个叫tmp的文件，因为文件不存在，因此创建了该文件，并且输出OK，最终调用dclose关闭流

输出：
![](https://pic.downk.cc/item/5fc6361ff81f7e3bd97eda26.jpg)

## 打开文件的数量上限
```c
#include <stdio.h>
#include <stdlib.h>

int main()
{
    FILE *fp;
    int count = 0;
    while(1){
        fp = fopen("tmp","w");
        if(fp == NULL){
            perror("fopen()");
            break;
        }
        count++;
    }
    
    printf("count = %d\n", count);
    exit(0);
}
```

解释：
在这里不断创建文件，直到达到文件数量上限时给出break，输出创建文件数量
输出：
![](https://pic.downk.cc/item/5fc636a7f81f7e3bd97f3e42.jpg)

和
![](https://pic.downk.cc/item/5fc63722f81f7e3bd97f5887.jpg)
给出的限制是相同的。因为自动打开的流为stdin，stdout，stderr，因此剩余256-3=253





























