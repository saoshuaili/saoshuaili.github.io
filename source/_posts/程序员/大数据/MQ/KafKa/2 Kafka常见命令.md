---
title: 2 Kafka常见命令  
date: 2022-12-04 02:42:57  
tags: []  
categories:
  - 大数据
  - MQ
  - KafKa
---

# 1. 创建和查看主题

## 关键脚本

`kafka-topics.sh`

## 命令



### 查看所有topic
```shell
kafka-topics.sh --zookeeper kafka01:2181 --list
```

### 创建主题
```shell
## 创建名为first的topic
kakfa-topics.sh --create --zookeeper kafka01:2181 --topic first --partitions 2 --replication-factor 2
## 创建名为second的topic
kakfa-topics.sh --create --zookeeper kafka01:2181 --topic second --partitions 2 --replication-factor 2
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210419003535.png" width="1100">

#### 查看各个服务器的logs文件
注意：需要先将配置文件server.properties文件里的log.dir更改路径

```shell
log.dirs=/opt/module/kafka_2.12-2.7.1/logs
```

然后就可以在对应的文件下查看到创建的目录

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210801134547.png" width="1100">

这里的`first-0`和`first-1`对应的是partitions的数量，并且他们会在不同的主机上存有副本,可以看到，对应的kafka02机器上就只有一个first-1,这是因为replication-factor数量为2，所以两个first-1就是两个副本。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210801134949.png" width="1100">

### 删除主题
```shell
kafka-topics.sh --delete --zookeeper kafka01:2181 --topic second
```


# 2. 产生和消费消息
## 命令
### 发送消息
```shell
[root@kafka02 kafka_2.12-2.7.1]# kafka-console-producer.sh --topic first --broker-list kafka01:9092
```
往first这个topic里发送消息

### 消费消息
```shell
# 从最近的消息开始消费
[root@kafka03 kafka_2.12-2.7.1]# kafka-console-consumer.sh --topic first --bootstrap-server kafka02:9092
# 从最之前的消息开始消费
[root@kafka03 kafka_2.12-2.7.1]# kafka-console-consumer.sh --topic first --bootstrap-server kafka02:9092 --from-begining
```
注意：
在这里之前是使用`kafka-console-consumer.sh --topic first --zookeeper kafka02:2181`，但是现在已经不支持zookeeper，所以要用bootstrap-server


### 示例
```shell
# 在kafka02上发送消息
[root@kafka02 logs]# kafka-console-producer.sh --topic first --broker-list kafka01:9092
>hello kafka 
>

#在kafka03上接收消息
```shell
[root@kafka03 kafka_2.12-2.7.1]# kafka-console-consumer.sh --topic first --bootstrap-server kafka02:9092
hello kafka
```


