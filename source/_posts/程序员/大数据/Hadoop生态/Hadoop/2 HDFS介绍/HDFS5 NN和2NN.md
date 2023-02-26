---
title: HDFS5 NN和2NN  
date: 2022-12-04 02:45:00  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 2 HDFS介绍
---
# NN和2NN工作机制

## 一 NameNode元数据存储位置

### 思考：NameNode中的元数据是存储在哪里的？

首先，我们做个假设，如果存储在NameNode节点的磁盘中，因为经常需要进行随机访问，还有响应客户请求，必然是效率过低。因此，元数据需要存放在内存中。但如果只存在内存中，一旦断电，元数据丢失，整个集群就无法工作了。    

### 因此产生在磁盘中备份元数据的FsImage。  

这样又会带来新的问题，当在内存中的元数据更新时，如果同时更新FsImage，就会导致效率过低，但如果不更新，就会发生一致性问题，一旦NameNode节点断电，就会产生数据丢失。 

### 因此，引入Edits文件(只进行追加操作，效率很高)。每当元数据有更新或者添加元数据时，修改内存中的元数据并追加到Edits中。这样，一旦NameNode节点断电，可以通过FsImage和Edits的合并，合成元数据。  

但是，如果长时间添加数据到Edits中，会导致该文件数据过大，效率降低，而且一旦断电，恢复元数据需要的时间过长。因此，需要定期进行FsImage和Edits的合并，如果这个操作由NameNode节点完成，又会效率过低。

### 因此，引入一个新的节点SecondaryNamenode，专门用于FsImage和Edits的合并。 

## 二 NameNode工作机制

1. NameNode加载编辑日志和镜像文件到内存中
2. 客户端发起某个文件的增删改查请求
3. NameNode将记录操作日志、更新滚动日志
4. 将内存中的数据进行修改

### 原理图：

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210182339.png" style="zoom:50%;" />



## 三 Secondary NameNode工作机制

当编辑日志太大之后，引入了Secondary NameNode

### 1. 请求是否需要CheckPoint  

#### CheckPoint：   

#### 作用

将编辑日志和镜像文件进行合并，然后序列化到新的镜像文件中。

#### 触发条件

1) 定时时间到
2) Edits中的数据满了

### 2. 请求执行CheckPoint

### 3. 滚动正在写的Edits

### 4. 拷贝到2nn

### 5. 加载到内存并合并

### 6. 生成新的Fsimage

### 7. 拷贝到nn

### 8.重新命名为Fsimage

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210182608.png" style="zoom:50%;" />



# Fsimage和Edits解析

## 1 目录

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210183305.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210183325.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210183409.png" style="zoom: 67%;" />

## 2 解析的命令

### oiv查看Fsimage文件 

#### 查看oiv和oev命令 

```shell
$ hdfs oiv apply the offline fsimage viewer to an fsimage 
oev apply the offline edits viewer to an edits file 
```

#### 基本语法 

```
hdfs oiv -p 文件类型 -i镜像文件 -o 转换后文件输出路径
```

#### 案例实操 

```
$ pwd /opt/module/hadoop-2.7.2/data/tmp/dfs/name/current 
$ hdfs oiv -p XML -i fsimage_0000000000000000025 -o /opt/module/hadoop-2.7.2/fsimage.xml 
$ cat /opt/module/hadoop-2.7.2/fsimage.xml 
```

### oev查看Edits文件 

#### 基本语法 

```
hdfs oev -p 文件类型 -i编辑日志 -o 转换后文件输出路径 
```

#### 案例实操 

```
$ hdfs oev -p XML -i edits_0000000000000000012-0000000000000000013 -o /opt/module/hadoop-2.7.2/edits.xml 
$ cat /opt/module/hadoop-2.7.2/edits.xml 
```

将显示的xml文件内容拷贝到Eclipse中创建的xml文件中，并格式化。



## 3 重新格式化举例

### 格式化之后的fsimage和edits

#### hadoop100（NameNode）上的文件

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210183848.png)
可以看到，只有fsimage_000000的文件，并没有edits文件，也就是说，没有修改的过程，所以没有edits。

### hadoop102上的文件（Secondary NameNade）

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210183916.png)
可以看到，hadoop102上没有文件，因为还不需要它来进行合并fsimage和edits文件的操作

### 进行一次操作：增加一个文件夹"/examples"

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210183931.png)   
可以看到，多了一个edits文件。仔细查看这两个文件：

#### fsimage_00..00:

```
hdfs oiv -p XML -i fsimage_0000000000000000000 -o fsimage.xml
```

将其复制到IDEA格式化之后可以看到，最新的创建文件夹的动作并没有被记录。
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210183958.png" style="zoom:67%;" />

#### edits文件

```
hdfs oev -p XML -i edits_0000000000000000001-0000000000000000003 -o edits.xml
```

同样复制到IDEA格式化之后，可以看到创建文件夹的动作被记录在edits文件中。
![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210184015.png) 			
也就是说，在这个时候edits文件还没有和fsimage文件进行合并，这个动作是由2NN进行的。所以在2NN中没有出现文件。

# 集群安全模式

## 1 进入和离开安全模式

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210184046.png" style="zoom:50%;" />

## 2 等待安全模式

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210184111.png" style="zoom:50%;" />



# NameNode多目录配置

## 修改hdfs-site.xml

<img src="https://pic.downk.cc/item/5fc7845b394ac52378b4e7b2.png" style="zoom:50%;" />

## 结果：双目录

<img src="https://pic.downk.cc/item/5fc7845b394ac52378b4e7a9.png" style="zoom:50%;" />

