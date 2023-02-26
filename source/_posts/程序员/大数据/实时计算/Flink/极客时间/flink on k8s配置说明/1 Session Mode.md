---
title: 1 Session Mode  
date: 2022-12-04 02:42:11  
tags: []  
categories:
  - 大数据
  - 实时计算
  - Flink
  - 极客时间
  - flink on k8s配置说明
---
# 配置文件说明

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

### 功能
- 用于存储`flink-conf.yaml`，`log4j-console.properties`等配置信息。
- Flink JobManager和TaskManager启动时会自动获取配置

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

### 功能
通过 `Service Name` 和 `Port` 暴露 `JobManager` 服务，让 `TaskManager` 能够连接到 `JobManager` 。

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

### 1. jobmanager-session-deployment.yaml

#### 功能
定义 `JobManager Pod` 副本数量，版本等，保证在Pods中至少有一个副本

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
        image: flink:1.13-scala_2.12
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

#### apiVersion

版本号，这个没什么好说的。

#### kind

Api类型，表明这是一个Deployment

#### metadata

元数据，表明该deployment的名字为 `flink-jobmanager` ，一般还会有一个label字段，

#### spec(specification)

表明部署所需行为的**规范**

##### replicas

副本数量，这就是指定的 `Kubernetes` 集群里须要运行多少个 `Pod` 实例。当Pod异常退出或者挂掉的时候可以自动重启。

##### selector

它的作用是“筛选”出要被 `Deployment` 管理的 `Pod` 对象，下属字段“matchLabels”定义了 Pod 对象应该携带的 label，它必须和“template”里 Pod 定义的“labels”完全相同，否则 `Deployment` 就会找不到要控制的 `Pod` 对象，apiserver 也会告诉你 YAML 格式校验错误无法创建。

`selector` 和  `template` 里都需要写一遍 `Pod` 的原因:
`Deployment` 和 `Pod` 实际上是一种松散的组合关系，`Deployment` 实际上并不“持有” `Pod` 对象，它只是帮助 `Pod` 对象能够有足够的副本数量运行，仅此而已。如果像 `Job` 那样，把 `Pod` 在模板里“写死”，那么其他的对象再想要去管理这些 `Pod` 就无能为力了。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20221116235751.png" width = "50%" />

可以看到，通过 `selector` 的 `label` 中的 `app` 标签，选中了 `template` 中的 `ngx-dep` 这个 `Pod` ，在我们这里就是选中了 `jobmanager` 这个 `Pod` 。如下图：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20221117004230.png" width = "30%" />

所以这里就是创建了一个名为 `flink-jobmanager` 的 `Deployment` ，维持 `Pod` 的数量为1个，通过 `selector` 选择了 `template` 中 `component` 为 `jobmanager` 的 `Pod` ，`Pod` 中选择的 `image` 版本是 `flink:1.13-scala_2.12`，`args` 是 `jobmanager`，也就是通过参数来启动了 `flink` 中的 `JobManager` 。并且对外打开了几个端口，将 `ConfigMap` 中定义的两个配置文件挂载在了 `/opt/flink/conf` 目录下，作为 `flink` 的参数

### 2. taskmanager-session-deployment.yaml

定义了 `TaskManager Pod` 副本数目，版本等，保证在 `Pods` 中至少有两个副本。

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: flink-taskmanager
spec:
  replicas: 2
  selector:
    matchLabels:
      app: flink
      component: taskmanager
  template:
    metadata:
      labels:
        app: flink
        component: taskmanager
    spec:
      containers:
      - name: taskmanager
        image: flink:1.13-scala_2.12
        args: ["taskmanager"]
        ports:
        - containerPort: 6122
          name: rpc
        - containerPort: 6125
          name: query-state
        livenessProbe:
          tcpSocket:
            port: 6122
          initialDelaySeconds: 30
          periodSeconds: 60
        volumeMounts:
        - name: flink-config-volume
          mountPath: /opt/flink/conf/
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

这里其实和 `JobManager` 几乎完全相同，不同之处首先有 `replicas` 为2， 也就是副本有两个，还有就是启动参数为 `taskmanager`，也就是通过指定参数启动了一个 `TaskManager`， 这样一个有JobManager和TaskManager的集群就启动了。

# 启动

```shell
# 创建configmap
[coachhe@VM-11-161-centos ~/kubernetes/flink/install/deploy/session-mode]# kubectl apply -f flink-configuration-configmap.yaml 
configmap/flink-config created

# 创建service
[coachhe@VM-11-161-centos ~/kubernetes/flink/install/deploy/session-mode]# kubectl apply -f jobmanager-service.yaml 
service/flink-jobmanager created
[coachhe@VM-11-161-centos ~/kubernetes/flink/install/deploy/session-mode]# kubectl apply -f jobmanager-rest-service.yaml
service/flink-jobmanager-rest created

# 创建jobmanager deployment
[coachhe@VM-11-161-centos ~/kubernetes/flink/install/deploy/session-mode]# kubectl apply -f jobmanager-session-deployment.yaml 
deployment.apps/flink-jobmanager created

# 创建taskmanager deployment
[coachhe@VM-11-161-centos ~/kubernetes/flink/install/deploy/session-mode]# kubectl apply -f taskmanager-session-deployment.yaml 
deployment.apps/flink-taskmanager created
```

然后就可以正常访问页面了：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20221117005919.png" width = "80%" />

