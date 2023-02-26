---
title: MR3 MapReduce框架原理1_InputFormat数据输入  
date: 2022-12-04 02:45:28  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 3 MR介绍
---
# InputFormat数据输入

## 1 切片与MapTask并行度决定机制

MapTask的并行度决定Map阶段的任务处理并发度，进而影响到整个Job的处理速度。 

### 思考： 

1G的数据，启动8个MapTask，可以提高集群的并发处理能力。那么1K的数据，也启动8个MapTask，会提高集群性能吗？MapTask并行任务是否越多越好呢？哪些因素影响了MapTask并行度？ 

## 2 数据块和数据切片定义

### 数据块

Block是HDFS物理上把数据分成一块一块 

### 数据切片 

数据切片只是在逻辑上对输入进行分片，并不会在磁盘上将其节分成片进行存储。 

## 3 数据切片与MapTask并行度决定机制

1. 一个Job的Map阶段并行度由客户顿在提交Job时的切片数决定

2. 每一个Split切片分配一个MapTask并行实例处理 

3. 默认情况下，切片大小=BlockSize 

4. 切片时不考虑数据集整体，而是逐个针对每一个文件单独切片。 

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211010729.png" style="zoom:25%;" />

### 解释： 

数据是以Block的形式存放在DataNode里面，每个Block的大小默认为128M。  
每个切片都对应一个Map过程，如果切片大小设置为100M，那么数据会非常分散，因此切片大小设置成Block的大小最合适，这样可以每个Block都对应一个Map过程，而不用跨DataNode去找数据。 

# 2 Job提交流程源码解析

## 总的框架

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211011109.png" style="zoom:25%;" />

## Job提交流程源码

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211011548.png)

## Submit步骤

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211011750.png)

# 3 Job切片机制源码解析

## 大纲

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211011849.png" style="zoom:25%;" />

## 具体源码分析

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211012034.png)

然后

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211012217.png)

然后

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211012300.png)

以下四幅图组成了这个方法

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211012344.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211012422.png)

![image-20201211012519983](/Users/heyizhi/Library/Application Support/typora-user-images/image-20201211012519983.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211012540.png)

# 4 FileInputFormat切片机制

## 切片机制

1. 简单按照文件的内容长度进行切片 
2. 切片大小，默认等于Block大小 
3. 切片时不考虑数据集整体，而是逐个对每一个文件单独切片。 

## 案例分析

### 输入数据

有两个文件 

file1.txt   320M 
file2.txt   10M 

### 经过FileInputFormat的切片机制 

运算之后，形成的切片信息如下 
file1.txt.split1-- 0~128 
file1.txt.split2-- 128~256 
file1.txt.split3-- 256~320 
file2.txt.split1-- -~10M 

## 切片大小公式

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211013144.png" style="zoom:50%;" />

## 其中computeSplitSize为

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211013214.png" style="zoom:50%;" />

minSize默认为1。 
maxSize默认为Long的最大值。 

## 切片大小配置

#### maxsize（切片最大值）

参数如果调的比blockSize小，则会让切片变小，而且就等于这个参数的值 

#### minsize（切片最小值）

参数调的比blockSize大，则会让切片变得比blockSize还大，且等于这个参数的值 

## 获得切片信息API

### 获取切片的文件名称

```java
String name = inputSplits.getPath().getName(); 
```

 ### 根据文件类型获取切片信息 

```java
FileSplit inputSplit = (FileSplit) context.getInputSplit();
```

# 5 CombineTextInputFormat

## 使用原因和场景

### 原因

默认的为TextInputFormat切片机制，是对任务按文件规划切片，不管文件多小，都会是一个单独的切片，都会教给一个MapTask，这样如果有大量小文件，就会产生大量的MapTask，效率极其低下。 

### 场景

因此CombineTextInputFormat用于小文件过多的场景，它可以将多个小文件从逻辑上规划到一个切片中，这样，多个小文件就可以交给一个MapTask处理。 

## 虚拟存储切片最大值设置

```java
CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);// 4m  
```


注意：虚拟存储切片最大值设置最好根据实际的小文件大小情况来设置具体的值。 

## 切片机制

### （1）虚拟存储过程： 

将输入目录下所有文件大小，依次和设置的setMaxInputSplitSize值比较，如果不大于设置的最大值，逻辑上划分一个块。如果输入文件大于设置的最大值且大于两倍，那么以最大值切割一块；当剩余数据大小超过设置的最大值且不大于最大值2倍，此时将文件均分成2个虚拟存储块（防止出现太小切片）。 

例如setMaxInputSplitSize值为4M，输入文件大小为8.02M，则先逻辑上分成一个4M。剩余的大小为4.02M，如果按照4M逻辑划分，就会出现0.02M的小的虚拟存储文件，所以将剩余的4.02M文件切分成（2.01M和2.01M）两个文件。 

### （2）切片过程： 

（a）判断虚拟存储的文件大小是否大于setMaxInputSplitSize值，大于等于则单独形成一个切片。 

（b）如果不大于则跟下一个虚拟存储文件进行合并，共同形成一个切片。 

（c）测试举例：

有4个小文件大小分别为1.7M、5.1M、3.4M以及6.8M这四个小文件，则虚拟存储之后形成6个文件块，大小分别为： 1.7M，（2.55M、2.55M），3.4M以及（3.4M、3.4M） 。最终会形成3个切片，大小分别为： （1.7+2.55）M，（2.55+3.4）M，（3.4+3.4）M 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211013700.png" style="zoom: 25%;" />