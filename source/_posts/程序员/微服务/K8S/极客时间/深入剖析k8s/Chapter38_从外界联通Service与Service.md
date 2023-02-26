---
title: Chapter38_从外界联通Service与Service  
date: 2022-12-04 02:34:53  
tags: []  
categories:
  - 微服务
  - K8S
  - 极客时间
  - 深入剖析k8s
---
# Session mode

一共有5个文件：

```shell
[root@VM-11-161-centos ~/kubernetes/flink/install/deploy/session-mode]# ll
total 24
-rw-r--r-- 1 coachhe users 2442 Oct 31 00:44 flink-configuration-configmap.yaml
-rw-r--r-- 1 coachhe users  225 Oct 31 00:36 jobmanager-rest-service.yaml
-rw-r--r-- 1 coachhe users  247 Oct 31 00:44 jobmanager-service.yaml
-rw-r--r-- 1 coachhe users 1165 Oct 31 00:41 jobmanager-session-deployment.yaml
-rw-r--r-- 1 coachhe users  241 Oct 31 00:40 taskmanager-query-state-service.yaml
-rw-r--r-- 1 coachhe users 1119 Oct 31 00:38 taskmanager-session-deployment.yaml
```

现在我们来一个个看。

## configmap

```yaml
# flink-configuration-configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: flink-config
  labels:
    app: flink
data:
  flink-conf.yaml: |+
    jobmanager.rpc.address: flink-jobmanager
    taskmanager.numberOfTaskSlots: 2
    blob.server.port: 6124
    jobmanager.rpc.port: 6123
    taskmanager.rpc.port: 6122
    queryable-state.proxy.ports: 6125
    jobmanager.memory.process.size: 1600m
    taskmanager.memory.process.size: 1728m
    parallelism.default: 2
  log4j-console.properties: |+
    # This affects logging for both user code and Flink
    rootLogger.level = INFO
    rootLogger.appenderRef.console.ref = ConsoleAppender
    rootLogger.appenderRef.rolling.ref = RollingFileAppender
    #
    # Uncomment this if you want to _only_ change Flink's logging
    #logger.flink.name = org.apache.flink
    #logger.flink.level = INFO
    #
    # The following lines keep the log level of common libraries/connectors on
    # log level INFO. The root logger does not override this. You have to manually
    # change the log levels here.
    logger.akka.name = akka
    logger.akka.level = INFO
    logger.kafka.name= org.apache.kafka
    logger.kafka.level = INFO
    logger.hadoop.name = org.apache.hadoop
    logger.hadoop.level = INFO
    logger.zookeeper.name = org.apache.zookeeper
    logger.zookeeper.level = INFO
    
    # Log all infos to the console
    appender.console.name = ConsoleAppender
    appender.console.type = CONSOLE
    appender.console.layout.type = PatternLayout
    appender.console.layout.pattern = %d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %-60c %x - %m%n
    #
    # Log all infos in the given rolling file
    appender.rolling.name = RollingFileAppender
    appender.rolling.type = RollingFile
    appender.rolling.append = false
    appender.rolling.fileName = ${sys:log.file}
    appender.rolling.filePattern = ${sys:log.file}.%i
    appender.rolling.layout.type = PatternLayout
    appender.rolling.layout.pattern = %d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %-60c %x - %m%n
    appender.rolling.policies.type = Policies
    appender.rolling.policies.size.type = SizeBasedTriggeringPolicy
    appender.rolling.policies.size.size=100MB
    appender.rolling.strategy.type = DefaultRolloverStrategy
    appender.rolling.strategy.max = 10
    
    # Suppress the irrelevant (wrong) warnings from the Netty channel handler
    logger.netty.name = org.apache.flink.shaded.akka.org.jboss.netty.channel.DefaultChannelPipeline
    logger.netty.level = OFF%
```



可以看到，这里就是通过data属性定义了两个配置信息flink-conf.yaml和log4j-console.properties，在这里值得注意的是，因为deployment使用configmap之后，是会将对应内容转换成文件挂载在对应的目录下，和我们的需求整好是符合的，我们需要将这两个配置信息转换为文件进行存储和使用。



## Service

```yaml
# jobmanager-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: flink-jobmanager
spec:
  type: ClusterIP
  ports:
  - name: rpc
    port: 6123
  - name: blob-server
    port: 6124
  - name: webui
    port: 8081
  selector:
    app: flink
    component: jobmanager
```

可以看到，这是个ClusterIP类型的Service，作用是将6123、6124、8081三个端口对外暴露并且实现负载均衡。


## Deployment

1. jobmanager-session-deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: flink-jobmanager
spec:
  replicas: 1
  selector:
    matchLabels:
      app: flink
      component: jobmanager
  template:
    metadata:
      labels:
        app: flink
        component: jobmanager
    spec:
      containers:
      - name: jobmanager
        image: flink:1.11.1-scala_2.11
        args: ["jobmanager"]
        ports:
        - containerPort: 6123
          name: rpc
        - containerPort: 6124
          name: blob-server
        - containerPort: 8081
          name: webui
        livenessProbe:
          tcpSocket:
            port: 6123
          initialDelaySeconds: 30
          periodSeconds: 60
        volumeMounts:
        - name: flink-config-volume
          mountPath: /opt/flink/conf
        securityContext:
          runAsUser: 9999  # refers to user _flink_ from official flink image, change if necessary
      volumes:
      - name: flink-config-volume
        configMap:
          name: flink-config
          items:
          - key: flink-conf.yaml
            path: flink-conf.yaml
          - key: log4j-console.properties
            path: log4j-console.properties
```

针对上面的字段我们展开分析：

## apiVersion

版本号，这个没什么好说的。

## kind

Api类型，表明这是一个Deployment

# metadata

元数据，表明该deployment的名字为`flink-jobmanager`，一般还会有一个label字段，

replicas

副本数量，这就是指定的Kubernetes集群里须要运行多少个Pod实例。当Pod异常退出或者挂掉的时候可以自动重启。

2. 

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/image-20221111013522113.png" width = "30%" />

首先看看红框里的字段。我们先