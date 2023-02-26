---
title: HDFS4 数据流  
date: 2022-12-04 02:44:56  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 2 HDFS介绍
---
# HDFS写数据流程

## 1 剖析文件写入

<img src="https://pic.downk.cc/item/5fc78128394ac52378b32230.png" style="zoom: 50%;" />

1）客户端通过Distributed FileSystem模块向NameNode请求上传文件，NameNode检查目标文件是否已存在，父目录是否存在。 
2）NameNode返回是否可以上传。
3）客户端请求第一个Block上传到哪几个DataNode服务器上。
4）NameNode返回3个DataNode节点，分别为dn1、dn2、dn3。 
5）客户端通过FSDataOutputStream模块请求dn1上传数据，dn1收到请求会继续调用dn2，然后dn2调用dn3，将这个通信管道建立完成。 
6）dn1、dn2、dn3逐级应答客户端。 
7）客户端开始往dn1上传第一个Block（先从磁盘读取数据放到一个本地内存缓存），以Packet为单位，dn1收到一个Packet就会传给dn2，dn2传给dn3；==**dn1每传一个packet会放入一个应答队列等待应答**==。 
8）当一个Block传输完成之后，客户端再次请求NameNode上传第二个Block的服务器。（重复执行3-7步）。

## 2 网络拓扑-节点计算

在HDFS写数据的过程中，NameNode会选择距离待上传数据最近距离的DataNode接收数据。那么这个最近距离怎么计算呢？

### 节点距离

两个节点到达最近的共同祖先的距离总和。 
<img src="https://pic.downk.cc/item/5fc78128394ac52378b3221b.png" style="zoom:50%;" />
	

	## 3 副本存储节点选择

<img src="https://pic.downk.cc/item/5fc78128394ac52378b32203.png" style="zoom:50%;" />

# HDFS读数据流程

<img src="https://pic.downk.cc/item/5fc78128394ac52378b321e0.png" style="zoom:50%;" />

1）客户端通过Distributed FileSystem向NameNode请求下载文件，NameNode通过查询元数据，找到文件块所在的DataNode地址。 
2）挑选一台DataNode（就近原则，然后随机）服务器，请求读取数据。 
3）DataNode开始传输数据给客户端（从磁盘里面读取数据输入流，以Packet为单位来做校验）。 
4）客户端以Packet为单位接收，先在本地缓存，然后写入目标文件。  

