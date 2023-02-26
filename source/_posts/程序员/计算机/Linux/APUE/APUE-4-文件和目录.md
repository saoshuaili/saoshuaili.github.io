---
title: APUE-4-文件和目录  
date: 2022-12-04 02:37:34  
tags: []  
categories:
  - 计算机
  - Linux
  - APUE
---

# 1 文件系统
需要了解文件系统，我们需要先对磁盘的物理结构有一定的了解
## 磁盘的物理结构
![](https://pic.downk.cc/item/5fc636a7f81f7e3bd97f3e32.jpg)
可以看到，磁盘由很多盘面组成，而盘上则是由很多同心圆组成的磁道，每个磁道又被切割为许多扇区。所有磁盘面的同一个磁道构成一个柱面，同一柱面的所有磁道学完后，才会移入下一柱面。
磁盘的最小组成单位可以看成扇区，每个扇区的大小逻辑看起来是512字节，但是实际上底层的物理扇区是4096字节.

## 文件系统的结构
在UNIX系统中，要将具体的文件与磁盘相对应，就会将磁盘划分为很多个分区，每个分区对应一个文件系统，磁盘、分区和文件系统的结构如下：
![](https://pic.downk.cc/item/5fc636f5f81f7e3bd97f4de7.jpg)
1. 自举块：也称为引导块，分区中文件系统自身引导程序存放的地方。在每个分区的第一个扇区，用来安装引导记录
2. 超级块：超级块在每个文件系统的根上，超级块描述和维护文件系统的状态。用来记录文件系统整体信息，例如inode总量和blocks总量等。
### **对文件分区的理解：**
文件系统指文件存在的物理空间。
#### **对于Linux系统：**
linux系统中每个分区都是一个文件系统，都有自己的目录层次结构。Linux文件系统中的文件是数据的集合，文件系统不仅包含着文件中的数据而且还有文件系统的结构，所有Linux 用户和程序看到的文件、目录、软连接及文件保护信息等都存储在其中。这种机制有利于用户和操作系统的交互。  
每个实际文件系统从操作系统和系统服务中分离出来，它们之间通过一个接口层：虚拟文件系统或VFS来通讯。VFS使得Linux可以支持多个不同的文件系统，每个表示一个VFS 的通用接口。由于软件将Linux 文件系统的所有细节进行了转换,所以Linux核心的其它部分及系统中运行的程序将看到统一的文件系统。Linux 的虚拟文件系统允许用户同时能透明地安装许多不同的文件系统。  
在Linux文件系统中，**EXT2文件系统**、**虚拟文件系统**、**/proc**文件系统是三个具有代表性的文件系统。
#### **对于macOS：**
Mac 不需要分区。近年的Mac都是SSD（闪存）了，并且使用专为闪存设计的新文件管理系统APFS（Apple File System），响应速度和安全性都有所增强，分区反而会增加SSD写入次数损耗。 

在这里，只关注柱面组里的i节点和数据块部分结构，其中数据块存放着文件的具体内容，如下图：
![](https://pic.downk.cc/item/5fc63722f81f7e3bd97f5883.jpg)
每个i节点都有一个链接计数标记，表示指向该i节点的文件/目录的数量，只有当链接计数标记减少到0时，才会释放该文件所占用的数据块，可以从图中看到有两个目录项指向同一个i节点。
i节点包含了文件的所有信息，包含文件的权限位信息，文件类型文件长度等（文件名保存在目录项中）。
在不更换文件系统的情况下为一个文件重命名，该文件的实际位置并没有更改，只需在目录块中构造一个新的目录项指向原先的i节点，并将原来的目录项删除即可。

#### 举例
若现在创建一个名为testdir的文件，那么会自动包含.（当前位置）和..（上一级位置）和testdir目录，那么这三个目录以及testdir目录在柱面组里目录块和数据块的结构如下：
![](https://pic.downk.cc/item/5fc6364cf81f7e3bd97ee226.jpg)

对于标号为1267的i节点，其类型字段表示它是一个目录，链接计数大于等于3，原因：至少有三个目录指向她：
1. 命名它的目录项
2. 在该目录中的.项
3. 在其子目录testdir中的..项。
**注意**
父目录中的每一个子目录都使该父目录的链接计数增加1，因为会有一个..项指向它。

i节点一般存放的是一些状态信息，所以影响到i节点的操作有很多，比如更改文件的访问权限，更改用户ID，更改链接数等，一些涉及到文件实际内容相关的操作都不会影响到节点。

# 2 文件相关属性的解读

## 1 引言
第3章主要讲了执行I/O操作的函数，讨论是围绕普通文件I/O进行的-打开文件、读文件和写文件。
本章将描述文件系统的其他特征和文件的性质。
文件相关的信息都保存在stat结构中，大部分信息都来自inode节点。本章讲述的大部分函数都是针对其中的属性进行修改。

## 2 函数stat、fstat、fstatat和lstat
给定函数：
```C
#incllude <sys/stat.h>

int stat(const char *restrict pathname, struct stat *restrict buf);

int fstat(int id, struct stat *buf);

int lstat(const char *restrict pathname, struct stat *restrict buf);

int fstatat(int fd, const char *restrict pathname, struct stat *restrict buf, int flag);

//所有四个函数的返回值：若成功范围0，若出错返回-1；
```

stat是一个结构体，定义大致如下：
```C
struct stat {
  mode_t         st_mode;     //文件类型和模式（访问权限）
  ino_t          st_ino;
  dev_t          st_dev;
  dev_t          st_rdev;
  nlink_t        st_nlink;
  uid_t          st_uid;
  gid_t          st_gid;
  off_t          st_size;
  struct timespec st_atime;
  struct timespec st_mtime;
  struct timespec st_ctime;
  blisize_t      st_blksize;
  blkcnt_t       st_blocks;
};
```
在这里附上MAC OS的stat代码：
#### **注意**
linux的stat.h头文件的目录在/usr/include/stat.h
macos的stat.h头文件的目录在/Library/Developer/CommanLineTools/SDKs/MacOSX10.14.sdk/usr/include/sys

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20221211170049.png" width = "50%" />

可以看到，除了中间部分不同，其余都是一样的。

### lstat和stat的区别

对于除了软链接情况下的所有情况lstat和stat都是相同的，但是对于软链接情况，lstat表示的是**当前文件**的文件类型，stat表示的是**指向的文件**的文件类型。

![](https://pic.downk.cc/item/5fc63707f81f7e3bd97f5223.jpg)

可以看到，原来的lstat版本表明stdout文件时一个软链接，改动后的stat版本表示stat是一个特殊字符类型。


接下来探讨结构体stat中的具体内容：
## 3 文件类型
对应上面stat结构体中的st_mode。
文件类型包括以下几种：
1. 普通文件
2. 目录文件
3. 块特殊文件
4. 字符特殊文件
5. FIFO
6. 套接字
7. 符号链接
文件类型信息包含在stat结构的st_mode成员中。可以用以下的宏确定文件类型。
![](https://pic.downk.cc/item/5fc6364cf81f7e3bd97ee22a.jpg)

![](https://pic.downk.cc/item/5fc636b8f81f7e3bd97f4149.jpg)
可以看到，不同的文件输出了不同的结果。

## 4 设置用户ID和设置组ID
与一个进程相关的I有6个或者更多，如图：
![](https://pic.downk.cc/item/5fc63689f81f7e3bd97f1ddc.jpg)

## 5 文件访问权限
st_mode值也包含了对文件的访问权限位。
也就是说，st_mode同时定义了文件类型和文件的访问权限。
每个文件有9个访问权限位，可将它们分为3类：
![](https://pic.downk.cc/item/5fc636a7f81f7e3bd97f3e2c.jpg)


## 7 函数access和faccessat

![](https://pic.downk.cc/item/5fc6363cf81f7e3bd97edf05.jpg)


## 9 函数chmod、fchmod和fchmodat

![](https://pic.downk.cc/item/5fc6371af81f7e3bd97f56a6.jpg)



## 18 创建和读取符号链接
![](https://pic.downk.cc/item/5fc636d9f81f7e3bd97f4925.jpg)



## 22 读目录
![](https://pic.downk.cc/item/5fc636b8f81f7e3bd97f4153.jpg)

![](https://pic.downk.cc/item/5fc6369df81f7e3bd97f3883.jpg)、

![](https://pic.downk.cc/item/5fc6363cf81f7e3bd97edf02.jpg)

























