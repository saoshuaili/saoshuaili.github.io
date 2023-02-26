---
title: HDFS2 Shell操作  
date: 2022-12-04 02:44:47  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 2 HDFS介绍
---
# dfs和fs的区别

## hadoop fs 

使用面最广，可以操作任何文件系统。

## hadoop dfs与hdfs dfs

只能操作HDFS文件系统相关（包括与Local FS间的操作），前者（dfs）已经Deprecated，一般使用后者。

## 以下内容参考自stackoverflow

Following are the three commands which appears same but have minute differences

1. hadoop fs {args}
2. hadoop dfs {args}
3. hdfs dfs {args}

```shell
hadoop fs <args>
```

FS relates to a generic file system which can point to any file systems like local, HDFS etc. So this can be used when you are dealing with different file systems such as Local FS, HFTP FS, S3 FS, and others 。

```
  hadoop dfs <args>
```

dfs is very specific to HDFS. would work for operation relates to HDFS. This has been deprecated and we should use hdfs dfs instead.

```
  hdfs   dfs <args>
```

same as 2nd i.e would work for all the operations related to HDFS and is the recommended command instead of hadoop dfs
below is the list categorized as HDFS commands.

```
  **#hdfs commands**
```

  namenode|secondarynamenode|datanode|dfs|dfsadmin|fsck|balancer|fetchdt|oiv|dfsgroups
So even if you use Hadoop dfs , it will look locate hdfs and delegate that command to hdfs dfs



# shell操作

## 常用命令操作

（0）启动Hadoop集群（方便后续的测试）

```
	[atguigu@hadoop102 hadoop-2.7.2]$ sbin/start-dfs.sh
	[atguigu@hadoop103 hadoop-2.7.2]$ sbin/start-yarn.sh
```

（1）-help：输出这个命令参数

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -help rm
```

（2）-ls: 显示目录信息

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -ls /
```

（3）-mkdir：在HDFS上创建目录

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -mkdir -p /sanguo/shuguo
```

（4）-moveFromLocal：从本地剪切粘贴到HDFS

```
	[atguigu@hadoop102 hadoop-2.7.2]$ touch kongming.txt
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs  -moveFromLocal  ./kongming.txt  /sanguo/shuguo
```

（5）-appendToFile：追加一个文件到已经存在的文件末尾

```
	[atguigu@hadoop102 hadoop-2.7.2]$ touch liubei.txt
	[atguigu@hadoop102 hadoop-2.7.2]$ vi liubei.txt
    输入
	san gu mao lu
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -appendToFile liubei.txt /sanguo/shuguo/kongming.txt
```

（6）-cat：显示文件内容

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -cat /sanguo/shuguo/kongming.txt
```

	（7）-chgrp、-chmod、-chown：Linux文件系统中的用法一样，修改文件所属权限

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs  -chmod  666  /sanguo/shuguo/kongming.txt
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs  -chown  atguigu:atguigu   /sanguo/shuguo/kongming.txt
```

	（8）-copyFromLocal：从本地文件系统中拷贝文件到HDFS路径去

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -copyFromLocal README.txt /
```

	（9）-copyToLocal：从HDFS拷贝到本地

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -copyToLocal /sanguo/shuguo/kongming.txt ./
```

	（10）-cp ：从HDFS的一个路径拷贝到HDFS的另一个路径

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -cp /sanguo/shuguo/kongming.txt /zhuge.txt
```

	（11）-mv：在HDFS目录中移动文件

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -mv /zhuge.txt /sanguo/shuguo/
```

	（12）-get：等同于copyToLocal，就是从HDFS下载文件到本地

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -get /sanguo/shuguo/kongming.txt ./
```

	（13）-getmerge：合并下载多个文件，比如HDFS的目录 /user/atguigu/test下有多个文件:log.1, log.2,log.3,...

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -getmerge /user/atguigu/test/* ./zaiyiqi.txt
```

	（14）-put：等同于copyFromLocal

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -put ./zaiyiqi.txt /user/atguigu/test/
```

	（15）-tail：显示一个文件的末尾

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -tail /sanguo/shuguo/kongming.txt
```

	（16）-rm：删除文件或文件夹

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -rm /user/atguigu/test/jinlian2.txt
```

	（17）-rmdir：删除空目录

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -mkdir /test
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -rmdir /test
```

	（18）-du统计文件夹的大小信息

```
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -du -s -h /user/atguigu/test
	2.7 K  /user/atguigu/test
	[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -du  -h /user/atguigu/test
	1.3 K  /user/atguigu/test/README.txt
	15     /user/atguigu/test/jinlian.txt
	1.4 K  /user/atguigu/test/zaiyiqi.txt
```

## 2 -setrep.md

```
[atguigu@hadoop102 hadoop-2.7.2]$ hadoop fs -setrep 10 /sanguo/shuguo/kongming.txt
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160717.png)  		
图3-3  HDFS副本数量  
这里设置的副本数只是记录在NameNode的元数据中，是否真的会有这么多副本，还得看DataNode的数量。因为目前只有3台设备，最多也就3个副本，只有节点数的增加到10台时，副本数才能达到10。