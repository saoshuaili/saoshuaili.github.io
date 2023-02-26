---
title: PulSar操作  
date: 2022-12-04 02:43:05  
tags: []  
categories:
  - 大数据
  - MQ
  - pulsar
---
```shell
[root@chadoop01 apache-pulsar-2.8.0]# bin/pulsar-admin tenants list
"public"
"pulsar"
[root@chadoop01 apache-pulsar-2.8.0]# bin/pulsar-admin brokers list user
"192.168.10.5:8080"
"192.168.10.4:8080"
"192.168.10.3:8080"
[root@chadoop01 apache-pulsar-2.8.0]# bin/pulsar-admin namespaces list public
"public/default"
[root@chadoop01 apache-pulsar-2.8.0]# bin/pulsar-admin topics list public/default
"persistent://public/default/produce_topic-partition-2"
"persistent://public/default/produce_topic-partition-1"
"persistent://public/default/consume_topic-partition-0"
"persistent://public/default/produce_topic-partition-0"
"persistent://public/default/consume_topic-partition-2"
"persistent://public/default/consume_topic-partition-1"

# 删除:需要先卸载后删除
[root@chadoop02 apache-pulsar-2.8.0]# bin/pulsar-admin topics unload persistent://public/default/from_topic
[root@chadoop02 apache-pulsar-2.8.0]# bin/pulsar-admin topics delete persistent://public/default/consume_topic6
```



```shell
# 生产
bin/pulsar-client produce public/default/from_topic --messages "{data:[{data:hello1,topic:topic1},{data:hello2,topic:topic2}],client:ccc}"
# 向my-topic这个topic生产数据，内容为“hello-pulsar”，如果topic不存在，pulsar会自动创建

# 消费
bin/pulsar-client consume public/default/produce_topic -s "topicGroup"
# 消费my-topic的数据，订阅名称为“topicGroup", 如果topic不存在，pulsar会自动创建
```







```java
2021-07-13 05:03:32.002  INFO 10200 --- [r-client-io-1-1] o.a.p.c.i.PartitionedProducerImpl        : [public/default/produce_topic] Created partitioned producer
2021-07-13 05:03:32.002  INFO 10200 --- [           main] c.c.s.l.d.i.PulsarMQ                     : initialized done
2021-07-13 05:03:32.047  INFO 10200 --- [           main] c.c.s.l.c.PulsarController               : [INFO] Begin to execute PulsarController
2021-07-13 05:03:32.047  INFO 10200 --- [           main] c.c.s.l.d.i.PulsarMQ                     : [INFO] Begin to consume the message
```





# Java调试

```shell
```



