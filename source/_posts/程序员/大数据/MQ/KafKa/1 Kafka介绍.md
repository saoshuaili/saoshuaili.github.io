---
title: 1 Kafka介绍  
date: 2022-12-04 02:42:50  
tags: []  
categories:
  - 大数据
  - MQ
  - KafKa
---

# 一 Kafka概述

## 定义

Kafka 是一个**分布式**的基于**发布/订阅模式**的**消息队列**（Message Queue），主要应用于大数据实时处理领域。 

## 消息队列（MQ）

### 同步处理和异步处理

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409101746.png)

### 使用消息队列的好处

1. 解耦 

   允许你独立的扩展或修改两边的处理过程，只要确保它们遵守同样的接口约束。 

2. 可恢复性 

   系统的一部分组件失效时，不会影响到整个系统。消息队列降低了进程间的耦合度，所以即使一个处理消息的进程挂掉，加入队列中的消息仍然可以在系统恢复后被处理。 

3. 缓冲 

   有助于控制和优化数据流经过系统的速度，解决生产消息和费的处理速度不一致的情况。 

4. 灵活性&峰值处理能力 

   在访问量剧增的情况下，应用仍然需要继续发挥作用，但是这样的突发流量并不常见。如果为以能处理这类峰值访问为标准来投入资源随时待命无疑是巨大的浪费。使用消息队列能够使关键组件顶住突发的访问压力，而不会因为突发的超负荷的请求而完全崩溃。 

5. 异步通信 

   很多时候，用户不想也不需要立即处理消息。消息队列提供了异步处理机制，允许用户把一个消息放入队列，但并不立即处理它。想向队列中放入多少消息就放多少，然后在需要的时候再去处理它们。 

### 消息队列的两种模型

#### 1. 点对点模式

一对一，消费者主动拉取数据，消息收到后消息清除

消息生产者生产消息发送到Queue中，然后消息消费者从Queue中取出并且消费消息。 

消息被消费以后，queue中不再有存储，所以消息消费者不可能消费到已经被消费的消息。Queue支持存在多个消费者，但是对一个消息而言，只会有一个消费者可以消费。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409102037.png" style="zoom:50%;" />

#### 2. 发布/订阅模式

一对多，消费者消费数据之后不会清除消息

消息生产者（发布）将消息发布到topic中，同时有多个消息消费者（订阅）消费该消息。和点对点方式不同，发布到topic的消息会被所有订阅者消费。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409102049.png" style="zoom:50%;" />

#### 注意 

发布/订阅模式有两种 

一种是消息队列主动推送数据模式（消息队列确定推送速度）。 
- 坏处：消费者可能扛不住
- 好处：消费者可以随时获取最新的消息

一种是消费者主动拉取数据模式（消费者确定拉取速度）。 
- 坏处：需要耗费大量资源去轮询获取最新的消息
- 好处：能根据消费者的需要去动态获取数据

### Kafka基本架构

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409102147.png" style="zoom:50%;" />

**1）Producer** ：消息生产者，就是向kafka broker 发消息的客户端； 

**2）Consumer** ：消息消费者，向kafka broker 取消息的客户端； 

**3）Consumer Group** （**CG**）：消费者组，由多个consumer 组成。消费者组内每个消费者负责消费不同分区的数据，一个分区只能由一个组内消费者消费；消费者组之间互不影响。所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者。 

**4）Broker** ：一台kafka 服务器就是一个broker。一个集群由多个broker 组成。一个broker 可以容纳多个topic。 

**5）Topic** ：可以理解为一个队列，生产者和消费者面向的都是一个**topic**； 

**6）Partition**：为了实现扩展性，一个非常大的topic 可以分布到多个broker（即服务器）上，一个**topic** 可以分为多个**partition**，**每个partition 是一个有序的队列**； 

**7）Replica**：副本，为保证集群中的某个节点发生故障时，该节点上的partition 数据不丢失， 

# 二 Kafka入门

## 1. Kafka安装

### 文件的解压

1. 将`kafka_2.11-0.11.0.0.tgz`文件解压到`/opt/module`中

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409112216.png" style="zoom:33%;" />

   ```shell
   [root@3e54fd72a673 software]# tar xvfz kafka_2.11-0.11.0.0.tgz -C /opt/module/
   ```

2. 文件改名

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409112414.png" style="zoom:33%;" />

   ```shell
   [root@3e54fd72a673 module]# mv kafka_2.11-0.11.0.0/ kafka
   ```

### 配置信息

#### 修改server.properties

1. 首先使得每个broker.id唯一

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409112631.png" style="zoom: 50%;" />

2. zookeeper连接端口

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409112800.png" style="zoom:50%;" />

   更改之后：

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409112852.png" style="zoom:50%;" />

3. 更改暂存文件目录

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409112930.png" style="zoom:50%;" />

   修改之后：

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409112957.png" style="zoom:50%;" />

4. 打开使能删除功能

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409113043.png" style="zoom:50%;" />

#### 配置环境变量

1. 在`/etc/profile`中增加`KAFKA_HOME`

   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409113314.png)

2. 执行

   ```shell
   [root@3e54fd72a673 config]# source /etc/profile
   ```

### 集群配置

将kafka复制到其他服务器上，然后修改每个服务器的broker id即可（保证broker id）唯一 

1. chadoop1

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409113508.png" style="zoom:50%;" />

2. chadoop2

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409113616.png" style="zoom:50%;" />

3. chadoop3

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409113651.png" style="zoom:50%;" />

## 2. Kfaka启动

### 启动命令

需要保证zk启动

#### 启动的两种方式

1. 启动阻塞进程

   ```shell
   kafka-server-start.sh config/server.properties
   ```

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409114822.png" style="zoom:50%;" />

2. 启动守护进程

   ```shell
   kafka-server-start.sh -daemon config/server.properties
   ```

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409114920.png" style="zoom:50%;" />

### 群起Kafka

kafka没有群起脚本，我们可以自己制作一个。 

#### 群起脚本代码

```shell
#!/bin/bash 
 
case $1 in 
  "start"){ 
 
    for i in chadoop1 chadoop2 chadoop3
    do 
        echo "************$i************" 
        ssh $i "/opt/module/kafka/bin/kafka-server-start.sh -daemon /opt/module/kafka/config/server.properties" 
    done 
};; 
 
  "stop"){ 
 
    for i in chadoop1 chadoop2 chadoop3 
    do 
        echo "************$i************" 
        ssh $i "/opt/module/kafka/bin/kafka-server-stop.sh /opt/module/kafka/config/server.properties" 
    done 
};; 
 
esac
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409115146.png" style="zoom:50%;" />

#### 注意

ssh时经常无法识别JAVA_HOME，因此经常找不到jps指令和JAVA指令，也就无法启动Kafka，因此我们需要在脚本kafka-server-start.sh中指定JAVA_HOME。

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409115331.png" style="zoom:50%;" />

#### 启动

```shell
kakfa.sh start
```

1. chadoop1

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409120237.png" style="zoom:50%;" />

2. chadoop2

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409120302.png" style="zoom:50%;" />

3. chadoop3

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409120325.png" style="zoom:50%;" />




