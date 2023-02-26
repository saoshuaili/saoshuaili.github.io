---
title: Hadoop1 介绍3（源码编译）  
date: 2022-12-04 02:44:34  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 1 Hadoop介绍
---
# 编译源码的原因

例如我们从Apache官网下载的文件为默认32位，但我们是64位的系统，因此我们可以先编译，编译之后让其支持64位系统，从而能够使用。 

大概意思就是我们原来缺少什么功能，现在进行编译之后让其满足那个功能。！ 

例如我们经过编译后可以得到一个支持64位的hadoop，可以得到支持snappy的hadoop，这些东西可以在网上直接下载，但是也可以自己编译。！ 

# 源码编译步骤

## 1 前期准备

### CentOS联网  

配置CentOS能连接外网。Linux虚拟机ping www.baidu.com 是畅通的  

#### 注意

采用root角色编译，减少文件夹权限出现问题 

## jar包准备(hadoop源码、JDK8、maven、ant 、protobuf) 

（1）hadoop-2.7.2-src.tar.gz 

（2）jdk-8u144-linux-x64.tar.gz 

（3）apache-ant-1.9.9-bin.tar.gz（build工具，打包用的） 

（4）apache-maven-3.0.5-bin.tar.gz 

（5）protobuf-2.5.0.tar.gz（序列化的框架） 

## 注意

所有操作必须在root用户下完成

## 2 jar包安装

### JDK解压、配置环境变量 JAVA_HOME和PATH，验证java-version(如下都需要验证是否配置成功) 

```shell
[root@hadoop101 software] # tar -zxf jdk-8u144-linux-x64.tar.gz -C /opt/module/ 
[root@hadoop101 software]# vi /etc/profile 
#JAVA_HOME： 
export JAVA_HOME=/opt/module/jdk1.8.0_144 
export PATH=$PATH:$JAVA_HOME/bin 
[root@hadoop101 software]#source /etc/profile 
```

**验证命令**：java -version 

### Maven解压、配置  MAVEN_HOME和PATH 

```shell
[root@hadoop101 software]# tar -zxvf apache-maven-3.0.5-bin.tar.gz -C /opt/module/ 
[root@hadoop101 apache-maven-3.0.5]# vi conf/settings.xml 
<mirrors> 
    <!-- mirror 
     | Specifies a repository mirror site to use instead of a given repository. The repository that 
     | this mirror serves has an ID that matches the mirrorOf element of this mirror. IDs are used 
     | for inheritance and direct lookup purposes, and must be unique across the set of mirrors. 
     | 
<mirror> 
       <id>mirrorId</id> 
       <mirrorOf>repositoryId</mirrorOf> 
       <name>Human Readable Name for this Mirror.</name> 
       <url>http://my.repository.com/repo/path</url> 
</mirror> 
     --> 
        <mirror> 
                <id>nexus-aliyun</id> 
                <mirrorOf>central</mirrorOf> 
                <name>Nexus aliyun</name> 
                <url>http://maven.aliyun.com/nexus/content/groups/public</url> 
        </mirror> 
</mirrors> 
[root@hadoop101 apache-maven-3.0.5]# vi /etc/profile 
#MAVEN_HOME 
export MAVEN_HOME=/opt/module/apache-maven-3.0.5 
export PATH=$PATH:$MAVEN_HOME/bin 
[root@hadoop101 software]#source /etc/profile 
```

**验证命令**：mvn -version 

### ant解压、配置ANT _HOME和PATH 

```shell
[root@hadoop101 software]# tar -zxvf apache-ant-1.9.9-bin.tar.gz -C /opt/module/ 
[root@hadoop101 apache-ant-1.9.9]# vi /etc/profile 
#ANT_HOME 
export ANT_HOME=/opt/module/apache-ant-1.9.9 
export PATH=$PATH:$ANT_HOME/bin 
[root@hadoop101 software]#source /etc/profile 
```

**验证命令**：ant -version 

### 安装glibc-headers和g++ 命令如下 

```shell
[root@hadoop101 apache-ant-1.9.9]# yum install glibc-headers 
[root@hadoop101 apache-ant-1.9.9]# yum install gcc-c++ 
```

### 安装make和cmake 

```shell
[root@hadoop101 apache-ant-1.9.9]# yum install make 
[root@hadoop101 apache-ant-1.9.9]# yum install cmake 
```

### 解压protobuf ，进入到解压后protobuf主目录，/opt/module/protobuf-2.5.0，然后相继执行命令 

```shell
[root@hadoop101 software]# tar -zxvf protobuf-2.5.0.tar.gz -C /opt/module/ 
[root@hadoop101 opt]# cd /opt/module/protobuf-2.5.0/ 
[root@hadoop101 protobuf-2.5.0]#./configure
[root@hadoop101 protobuf-2.5.0]# make
[root@hadoop101 protobuf-2.5.0]# make check
[root@hadoop101 protobuf-2.5.0]# make install
[root@hadoop101 protobuf-2.5.0]# ldconfig
[root@hadoop101 hadoop-dist]# vi /etc/profile 
#LD_LIBRARY_PATH 
export LD_LIBRARY_PATH=/opt/module/protobuf-2.5.0 
export PATH=$PATH:$LD_LIBRARY_PATH 
[root@hadoop101 software]#source /etc/profile 
```

**验证命令**：protoc --version 

### 安装openssl库 

```shell
[root@hadoop101 software]#yum install openssl-devel 
```

### 安装ncurses-devel库 

```shell
[root@hadoop101 software]#yum install ncurses-devel 
```

到此，编译工具安装基本完成。 

# 3 源码编译

## 解压源码到/opt/目录 

```shell
[root@hadoop101 software]# tar -zxvf hadoop-2.7.2-src.tar.gz -C /opt/ 
```

## 进入到hadoop源码主目录 

```shell
[root@hadoop101 hadoop-2.7.2-src]# pwd 
/opt/hadoop-2.7.2-src 
```

## 通过maven执行编译命令 

```shell
[root@hadoop101 hadoop-2.7.2-src]#mvn package -Pdist,native -DskipTests -Dtar 
```

等待时间30分钟左右，最终成功是全部SUCCESS.

## 成功的64位hadoop包在/opt/hadoop-2.7.2-src/hadoop-dist/target下 

```shell
[root@hadoop101 target]# pwd 
/opt/hadoop-2.7.2-src/hadoop-dist/target 
```

## 编译源码过程中常见的问题及解决方案 

### （1）MAVEN install时候JVM内存溢出 

**处理方式**：在环境配置文件和maven的执行文件均可调整MAVEN_OPT的heap大小。（详情查阅MAVEN 编译 JVM调优问题，如：http://outofmemory.cn/code-snippet/12652/maven-outofmemoryerror-method） 

### （2）编译期间maven报错。可能网络阻塞问题导致依赖库下载不完整导致，多次执行命令（一次通过比较难）： 

```shell
[root@hadoop101 hadoop-2.7.2-src]#mvn package -Pdist,nativeN -DskipTests -Dtar 
```

### （3）报ant、protobuf等错误，插件下载未完整或者插件版本问题，最开始链接有较多特殊情况，同时推荐 

2.7.0版本的问题汇总帖子 http://www.tuicool.com/articles/IBn63qf 

 