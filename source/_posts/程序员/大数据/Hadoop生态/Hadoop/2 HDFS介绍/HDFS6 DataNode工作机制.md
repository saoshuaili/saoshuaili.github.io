---
title: HDFS6 DataNode工作机制
tags: []
categories:
  - 程序员
  - 大数据
  - Hadoop生态
  - Hadoop
  - 2 HDFS介绍
date: 2022-12-04 02:45:06
---
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210184302.png" style="zoom: 67%;" />

1. 一个数据块在 DataNode 上以文件形式存储在磁盘上，包括两个文件，一个是数据本身，一个是元数据包括数据块的长度，块数据的校验和，以及时间戳。  
2. DataNode 启动后向 NameNode 注册，通过后，周期性（1小时）的向 NameNode 上报所有的块信息。  
3. 心跳是每3秒一次，心跳返回结果带有 NameNode 给该 DataNode 的命令如复制块数据到另一台机器，或删除某个数据块。如果超过10分钟没有收到某个 DataNode 的心跳，则认为该节点不可用。  
4. 集群运行中可以安全加入和退出一些机器。