---
title: Zookeeper笔记  
date: 2022-12-04 02:45:56  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Zookeeper
---
# 一、zookeeper简介

## 简介

zooKeeper由雅虎研究院开发，是Google Chubby的开源实现，后来托管到 Apache，于2010年11月正式成为Apache的顶级项目。 

大数据生态系统里的很多组件的命名都是某种动物或者昆虫，比如hadoop就是 🐘，hive就是🐝。zookeeper即动物园管理者，顾名思义就是管理大数据生态系统各组件的管理员。 

如下图所示： 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407091859.png" style="zoom: 33%;" />

## zookeeper应用场景

### 1. 维护配置信息

java编程经常会遇到配置项，比如数据库的url、schema、user和password 等。通常这些配置项我们会放置在配置文件中，再将配置文件放置在服务器上当需要更改配置项时，需要去服务器上修改对应的配置文件。 

但是随着分布式系统的兴起，由于许多服务都需要使用到该配置文件，因此有必须保证该配置服务的高可用性（high availability）和各台服务器上配置数据的一致性。通常会将配置文件部署在一个集群上，然而一个集群动辄上千台服务器，此时如果再一台台服务器逐个修改配置文件那将是非常繁琐且危险的的操作，因此就需要一种服务，能够高效快速且可靠地完成配置项的更改等操作，并能够保证各配置项在每台服务器上的数据一致性。 

zookeeper就可以提供这样一种服务，其使用Zab这种一致性协议来保证一致性。现在有很多开源项目使用zookeeper来维护配置，比如在hbase中，客户端就是连接一个zookeeper，获得必要的hbase集群的配置信息，然后才可以进一步操作。还有在开源的消息队列kafka中，也使用zookeeper来维护broker的信息。在alibaba开源的soa框架dubbo中也广泛的使用zookeeper管理一些配置来实现服务治理。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407092026.png" style="zoom: 33%;" />

### 2. 分布式锁服务

一个集群是一个分布式系统，由多台服务器组成。为了提高并发度和可靠性， 多台服务器上运行着同一种服务。当多个服务在运行时就需要协调各服务的进度，有时候需要保证当某个服务在进行某个操作时，其他的服务都不能进行该操作，即对该操作进行加锁，如果当前机器挂掉后，释放锁并fail over 到其他的机器继续执行该服务。 

### 3. 集群管理

一个集群有时会因为各种软硬件故障或者网络故障，出现某些服务器挂掉而被移除集群，而某些服务器加入到集群中的情况，zookeeper会将这些服务器加入/移出的情况通知给集群中的其他正常工作的服务器，以及时调整存储和计算等任务的分配和执行等。此外zookeeper还会对故障的服务器做出诊断并尝试修复。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407092147.png" style="zoom:33%;" />

### 4. 生成分布式唯一ID

在过去的单库单表型系统中，通常可以使用数据库字段自带的auto_increment 属性来自动为每条记录生成一个唯一的ID。但是分库分表后，就无法在依靠数据库的 auto_increment属性来唯一标识一条记录了。此时我们就可以用zookeeper在分布式环境下生成全局唯一ID。

#### 做法

每次要生成一个新Id时，创建一个持久顺序节点，创建 操作返回的节点序号，即为新Id，然后把比自己节点小的删除即可 

## zookeeper的设计目标

### 综述

zooKeeper致力于为分布式应用提供一个**高性能**、**高可用**，且具有**严格顺序访问控制能力**的**分布式协调服务** 

### 1. 高性能

zooKeeper将全量数据存储在内存中，并直接服务于客户端的所有非事务请求，尤 其适用于以读为主的应用场景 

### 2. 高可用

zooKeeper一般以集群的方式对外提供服务，一般3 ~ 5台机器就可以组成一个可用的Zookeeper集群了，每台机器都会在内存中维护当前的服务器状态，并且每台机器之间都相互保持着通信。只要集群中超过一半的机器都能够正常工作，那么整个集群就能够正常对外服务 

### 3. 严格访问顺序

对于来自客户端的每个更新请求，ZooKeeper都会分配一个全局唯一的递增编号， 这个编号反映了所有事务操作的先后顺序

# 二、zookeeper的数据模型

## 简介

zookeeper的数据节点可以视为树状结构（或者目录），树中的各节点被称为 znode（即zookeeper node），一个znode可以有多个子节点。 

zookeeper节点在结构上表现为树状； 

使用路径path来定位某个znode，比如/ns1/itcast/mysql/schema1/table1，此处ns-1、itcast、mysql、schema1、table1分别是根节点、2级节点、3级节点以及4级节点； 

其中ns-1是itcast的父节点，itcast是ns-1的子 节点，itcast是mysql的父节点，mysql是itcast的子节点，以此类推。 

### znode 

znode，兼具文件和目录两种特点。既像文件一样维护着数据、元信息、ACL、时 间戳等数据结构，又像目录一样可以作为路径标识的一部分。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407092714.png" style="zoom:50%;" />

## 如何描述一个znode？

一个znode大体上分为3各部分： 

1. 节点的数据：即znode data(节点path, 节点data)的关系就像是java map中(key, value)的关系 
2. 节点的子节点children 
3. 节点的状态stat：用来描述当前节点的创建、修改记录，包括cZxid、ctime等 

## 节点状态stat的属性

在zookeeper shell中使用get命令查看指定路径节点的data、stat信息 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407092913.png" style="zoom: 33%;" />

### 属性说明

- cZxid：数据节点创建时的事务 ID 
- ctime：数据节点创建时的时间 
- mZxid：数据节点最后一次更新时的事务 ID 
- mtime：数据节点最后一次更新时的时间 
- pZxid：数据节点的子节点最后一次被修改时的事务 ID 
- cversion：子节点的更改次数 
- dataVersion：节点数据的更改次数 
- aclVersion：节点的 ACL 的更改次数 
- ephemeralOwner：如果节点是临时节点，则表示创建该节点的会话的 SessionID；如果节点是持久节点，则该属性值为 0 
- dataLength：数据内容的长度 
- numChildren：数据节点当前的子节点个数 

## zookeeper节点类型

zookeeper中的节点有两种： 

临时节点和永久节点。节点的类型在创建时即被确定，并且不能改变。 

### 临时节点

该节点的生命周期依赖于创建它们的会话。一旦会话(Session)结束，临 时节点将被自动删除，当然可以也可以手动删除。虽然每个临时的Znode都会绑定到一个客户端会话，但他们对所有的客户端还是可见的。另外，ZooKeeper的临时节点不允许拥有子节点。 

### 持久化节点

该节点的生命周期不依赖于会话，并且只有在客户端显示执行删除操作的时候，他们才能被删除 

# 三、zookeeper单机安装

## 本地模式安装部署

### 安装前准备

（1）安装Jdk 

（2）拷贝Zookeeper安装包到Linux系统下 

（3）解压到指定目录 

```shell
[coachhe@hadoop102 software]$ tar -zxvf zookeeper-3.4.10.tar.gz -C /opt/module/ 
```

### 配置修改

（1）将/opt/module/zookeeper-3.4.10/conf这个路径下的zoo_sample.cfg修改为zoo.cfg； 

```shell
[coachhe@hadoop102 conf]$ mv zoo_sample.cfg zoo.cfg
```

（2）打开zoo.cfg文件，修改dataDir路径： 

```shell
[coachhe@hadoop102 zookeeper-3.4.10]$ vim zoo.cfg
```

修改如下内容： 

```xml
dataDir=/opt/module/zookeeper-3.4.10/zkData 
```

（3）在/opt/module/zookeeper-3.4.10/这个目录上创建zkData文件夹 

```shell
[coachhe@hadoop102 zookeeper-3.4.10]$ mkdir zkData 
```

### 操作zookeeper

（1）启动Zookeeper 

```shell
[coachhe@hadoop102 zookeeper-3.4.10]$ bin/zkServer.sh start
```

（2）查看进程是否启动 

```shell
[coachhe@hadoop102 zookeeper-3.4.10]$ jps 
4020 Jps 
4001 QuorumPeerMain 
```

（3）查看状态： 

```shell
[coachhe@hadoop102 zookeeper-3.4.10]$ bin/zkServer.sh status 
ZooKeeper JMX enabled by default 
Using config: /opt/module/zookeeper-3.4.10/bin/../conf/zoo.cfg 
Mode: standalone 
```

（4）启动客户端： 

```shell
[coachhe@hadoop102 zookeeper-3.4.10]$ bin/zkCli.sh
```

（5）退出客户端： 

```shell
[zk: localhost:2181(CONNECTED) 0] quit 
```

（6）停止Zookeeper 

```shell
[coachhe@hadoop102 zookeeper-3.4.10]$ bin/zkServer.sh stop
```

 ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407093503.png)

### 配置参数解读

1．tickTime =2000：通信心跳数，Zookeeper服务器与客户端心跳时间，单位毫秒 

Zookeeper使用的基本时间，服务器之间或客户端与服务器之间维持心跳的时间间隔，也就是每个tickTime时间就会发送一个心跳，时间单位为毫秒。 

它用于心跳机制，并且设置最小的session超时时间为两倍心跳时间。(session的最小超时时间是2*tickTime) 

2．initLimit =10：LF初始通信时限 

集群中的Follower跟随者服务器与Leader领导者服务器之间初始连接时能容忍的最多心跳数（tickTime的数量），用它来限定集群中的Zookeeper服务器连接到Leader的时限。 

3．syncLimit =5：LF同步通信时限 

集群中Leader与Follower之间的最大响应时间单位，假如响应超过syncLimit * tickTime，Leader认为Follwer死掉，从服务器列表中删除Follwer。 

4．dataDir：数据文件目录+数据持久化路径 

主要用于保存Zookeeper中的数据。 

5．clientPort =2181：客户端连接端口 

监听客户端连接的端口。 

## 集群部署

### 配置更改   

在单机模式的基础上。首先

1. 在`/opt/module/zookeeper-3.4.14/zkData`目录下创建一个myid的文件，将其设置为1

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409104631.png" style="zoom:50%;" />

2. 将`/opt/module/zookeeper-3.4.14`拷贝到其他机器上

   ```shell
   /home/bin/xsync /opt/module/zookeeper-3.4.14
   ```

3. 更改zoo.cfg，增加如下配置

   ```shell
   #########cluster########
   server.1=chadoop1:2888:3888
   server.2=chadoop2:2888:3888
   server.3=chadoop3:2888:3888
   ```

   配置参数解读

   ```shell
   server.A=B:C:D
   ```

   **A** 是一个数字，表示这个是第几号服务器；

   集群模式下配置一个文件 myid，这个文件在 dataDir 目录下，这个文件里面有一个数据就是 A 的值，Zookeeper 启动时读取此文件，拿到里面的数据与 zoo.cfg 里面的配置信息比较从而判断到底是哪个 server。 

   **B** 是这个服务器的地址；

   **C** 是这个服务器 Follower 与集群中的 Leader 服务器交换信息的端口；

   **D** 是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的Leader，而这个端口就是用来执行选举时服务器相互通信的端口。

4. 同步zoo.cfg

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409105129.png" style="zoom: 33%;" />

5. 修改每个服务器中的myid为对应服务器号

### 启动集群

1. 分别在三个集群执行启动指令

   ```shell
   zkServer.sh start
   ```

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409110005.png" style="zoom:50%;" />

2. 查看状态

   ```shell
   zkServer.sh status
   ```

   第一个执行的主机chadoop1：

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409110136.png" style="zoom:50%;" />

   第二个执行的主机chadoop2：

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210409110236.png" style="zoom:50%;" />

   可以看到，第二个执行的是leader，说明启动正常！



# 四、zookeeper常用Shell命令

## 1. 新增节点

### 两种参数

```shell
create [-s] [-e] path data 
-s: 
有序节点 
-e: 
临时节点 
```

因此共有4种组合，分别为 

1. 持久化有序节点 
2. 持久化无序节点 
3. 临时有序节点 
4.  临时无序节点。 

#### 解释 

会话结束之后节点还会保留下来的节点叫做持久化节点。 

有序节点会在创建时路径后面添加一个序列号。 

 默认是持久化无序节点 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407093807.png" style="zoom: 33%;" />

### 创建持久化无序节点

默认为持久化无序节点，直接create即可。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407093932.png" style="zoom:33%;" />

### 创建持久化有序节点

#### 解释 

会话结束之后节点还会保留下来的节点叫做持久化节点。 

#### 语法

```shell
create -s path data 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407093950.png" style="zoom: 33%;" />

#### 作用

可以用来创建唯一ID 

### 创建临时有序节点

#### 语法 

```shell
create -s -e path name 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094011.png" style="zoom:33%;" />

#### 作用

用来生成分布式锁

### 创建临时无序节点

#### 语法 

```shell
create -e path name
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094036.png" style="zoom:33%;" />

#### 重启之后消失

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094049.png" style="zoom:33%;" />



## 2. 更新节点

### 语法

```shell
set path new_data 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094546.png" style="zoom:50%;" />

### 基于版本号修改

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094616.png" style="zoom:50%;" />

## 3. 删除节点

### 语法

```shell
delete path [version] 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094656.png" style="zoom:50%;" />

## 4. 查看节点

### 语法

```shell
get path
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094808.png" style="zoom:50%;" />

### 属性解释

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094844.png" style="zoom:50%;" />

### 子节点创建和更改 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407094911.png" style="zoom:50%;" />

### stat

可以使用 stat 命令查看节点状态，它的返回值和 get 命令类似，但不会返回节点数据

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407095033.png" style="zoom:50%;" />

## 5. 查看节点列表

查看节点列表有 ls path 和 ls2 path 两个命令，后者是前者的增强，不仅可以查看指定路径下的所有节点，还可以查看当前节点的信息 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407095133.png" style="zoom:50%;" />

## 6. 监听器

### 1. get path [watch]

使用`get path [watch]`注册的监听器能够在节点内容发生改变的时候，向客户端发出通知。需要注意的是zookeeper的触发器是一次性的（one-time trigger），即触发一次后就会立即失效。

```shell
[zk:localhost:2181(CONNECTED) 4] get /hadoop watch
[zk:localhost:2181(CONNECTED) 5] set /hadoop 45678
WATCHER::
WatchedEvent state:SyncConnected type:NodeDataChanged path:/hadoop #节点值改变
```

### 2. stat path [watch]

使用`stat path [watch]`注册的监听器能够在节点状态发生改变时，向客户端发出通知

```shell
[zk:localhost:2181(CONNECTED) 7] stat /hadoop watch
[zk:localhost:2181(CONNECTED) 8] set /hadoop 112233
WATCHER::
WatchedEvent state:SyncConnected type:NodeDataChanged path:/hadoop #节点值改变
```

### 3. ls / ls2 path [watch]

使用`ls path [watch]`或`ls2 path [watch]`注册的监听器能够监听该节点下所有子节点的增加和删除操作

```shell
[zk:localhost:2181(CONNECTED) 9] ls /hadoop watch
[]
[zk:localhost:2181(CONNECTED) 8] create /hadoop/yarn "aaa"
WATCHER::
WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/hadoop
```

# 六、Zookeeper JavaAPI

## 1 连接到Zookeeper

### 需要的jar包

需要添加的jar包在

```shell
$ZOOKEEPER_HOME
```

和

```shell
$ZOOKEEPER_HOME/lib
```

下面



或者使用maven项目，`pom.xml`给出

```xml
<?xml version="1.0" encoding="UTF-8"?> 
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"> 
    <modelVersion>4.0.0</modelVersion> 
 
    <groupId>com.coachhe</groupId> 
    <artifactId>ZK-API</artifactId> 
    <version>1.0-SNAPSHOT</version> 
 
    <dependencies> 
        <dependency> 
            <groupId>junit</groupId> 
            <artifactId>junit</artifactId> 
            <version>RELEASE</version> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.logging.log4j</groupId> 
            <artifactId>log4j-core</artifactId> 
            <version>2.9.1</version> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.hadoop</groupId> 
            <artifactId>hadoop-common</artifactId> 
            <version>2.7.2</version> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.hadoop</groupId> 
            <artifactId>hadoop-client</artifactId> 
            <version>2.7.2</version> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.hadoop</groupId> 
            <artifactId>hadoop-hdfs</artifactId> 
            <version>2.7.2</version> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.zookeeper</groupId> 
            <artifactId>zookeeper</artifactId> 
            <version>3.4.10</version> 
        </dependency> 
    </dependencies> 
</project> 
```

### 代码

```java
package com.coachhe.zookeeper; 
 
import org.apache.zookeeper.WatchedEvent; 
import org.apache.zookeeper.Watcher; 
import org.apache.zookeeper.ZooKeeper; 
 
import java.util.concurrent.CountDownLatch; 
 
public class ZookeeperConnection { 
    public static void main(String[] args) { 
        try { 
            // 计数器对象 
            CountDownLatch countDownLatch = new CountDownLatch(1); 
            // arg1:服务器ip和端口 
            // arg2:客户端与服务器之间的会话超时时间（ms） 
            // arg3:监视器对象 
            ZooKeeper zooKeeper = new ZooKeeper("10.211.55.100:2181", 5000, 
                    new Watcher() { 
                        @Override 
                        public void process(WatchedEvent event) { 
                            if (event.getState() == Event.KeeperState.SyncConnected) { 
                                System.out.println("连接创建成功"); 
                                // 通知countDownLatch不用继续阻塞了 
                                countDownLatch.countDown(); 
                            } 
                        } 
                    }); 
            //主线程阻塞等待连接对象的创建成功 
            countDownLatch.await(); 
            //会话编号 
            System.out.println(zooKeeper.getSessionId()); 
            zooKeeper.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
} 
```

## 2 新增节点

```java
//同步方式
create(String path, byte[] data, List<ACL> acl, CreateMode createMode)
//异步方式
create(String path, byte[] data, List<ACL> acl, CreateMode createMode,
       AsyncCallback.StringCallback callBack, Object ctx)
```

### 属性

* path - znode路径。例如，`/node`，`/node1/node11`
* data - 要存储在指定znode路径中的数据
* ack - 要创建的节点的访问控制列表。
* createMode - 节点的类型，这是一个枚举。
* callBack - 异步回调接口
* ctx - 传递上下文参数

## 3 更新节点

```java
// 同步方式
setData(String path, byte[] data, int version)
// 异步方式
setData(String path, byte[] data, int version, AsyncCallback.StatCallback callback, Object ctx)
```

### 属性

* path - znode路径。例如，`/node`，`/node1/node11`
* data - 要存储在指定znode路径中的数据
* version - znode的当前版本。每当数据更改时，ZooKeeper会更新znode的版本号
* callBack - 异步回调接口
* ctx - 传递上下文参数

## 4 删除节点delete()

```java
// 同步方式
delete(String path, int version)
// 异步方式
setData(String path, int version, AsyncCallback.StatCallback callback, Object ctx)
```

* path - znode路径。例如，`/node`，`/node1/node11`
* version - znode的当前版本。每当数据更改时，ZooKeeper会更新znode的版本号
* callBack - 异步回调接口
* ctx - 传递上下文参数

## 5 查看节点getData()

```java
// 同步方式
getData(String path, boolean b, Stat stat)
// 异步方式
getData(String path, boolean b, AsyncCallback.StatCallback callback, Object ctx)
```

* path - znode路径。例如，`/node`，`/node1/node11`
* b - 是否使用连接对象中注册的监视器
* stat - 返回znode的元数据
* callBack - 异步回调接口
* ctx - 传递上下文参数

# 七、ZooKeeper事件监听机制

##  1 watcher

### 1. watcher概念

zookeeper提供了数据的发布/订阅功能，多个订阅者可同时监听某一特定主题对象，当该主题对象的自身状态发生变化时(例如节点内容改变、节点下的子节点列表改变等)，会实时、主动通知所有订阅者 

zookeeper采用了Watcher机制实现数据的发布/订阅功能。该机制在被订阅对象发生变化时会异步通知客户端，因此客户端不必在Watcher注册后轮询阻塞，从而减轻了客户端压力。 

watcher机制实际上与观察者模式类似，也可看作是一种观察者模式在分布式场景下的实现方式。 

### 2. watcher架构

Watcher实现由三个部分组成： 

1. Zookeeper服务端 
2. Zookeeper客户端 
3. 客户端的ZKWatchManager对象 

#### 流程

客户端首先将Watcher注册到服务端，同时将Watcher对象保存到客户端的WatchManager中。当ZooKeeper服务端监听的数据状态发生变化时，服务端会主动通知客户端， 接着客户端的Watch管理器会触发相关Watcher来回调相应处理逻辑，从而完成整体的数据发布/订阅流程。 

### 3. watcher特性

| 特性           | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| 一次性         | watcher是一次性的，一旦触发就会被移除，再次使用时需要重新注册 |
| 客户端顺序回调 | watcher回调是顺序串行化执行的，只有回调后客户端才能看到最新的数据状态。一个watcher回调逻辑不应该太多，以免影响别的watcher执行 |
| 轻量级         | WatchEvent是最小的通信单位，结构上只包含通知状态、事件类型和节点路径，并不会告诉数据节点变化前后的具体内容； |
| 时效性         | watcher只有在当前session彻底失效时才会无效，若在session有效期内快速重连成功，则watcher依然存在，扔可接受到通知。 |

### 4. watcher接口设计

Watcher是一个接口，任何实现了Watcher接口的类就是一个新的Watcher。 

Watcher内部包含了两个枚举类：KeeperState、EventType 

#### Watcher通知状态（KeeperState）

KeeperState是客户端与服务端连接状态发生变化时对应的通知类型。路径为org.apache.zookeeper.Watcher.Event.KeeperState，是一个枚举类，其枚举属性 如下：

| 枚举属性      | 说明                     |
| ------------- | ------------------------ |
| SyncConnected | 客户端与服务器正常连接时 |
| Disconnected  | 客户端与服务器断开连接时 |
| Expired       | 会话session失效时        |
| AuthFailed    | 身份认证失败时           |

#### Watcher事件类型（EventType） 

EventType是数据节点(znode)发生变化时对应的通知类型。EventType变化时 KeeperState永远处于SyncConnected通知状态下；当KeeperState发生变化时， EventType永远为None。其路径为org.apache.zookeeper.Watcher.Event.EventType， 是一个枚举类，枚举属性如下： 

| 枚举属性            | 说明                                                        |
| ------------------- | ----------------------------------------------------------- |
| None                | 无                                                          |
| NodeCreated         | Watcher监听的数据节点与创建时                               |
| NodeDeleted         | Watcher监听的数据节点被删除时                               |
| NodeDataChanged     | Watcher监听的数据节点内容发生变更时（无论内容数据是否变化） |
| NodeChildrenChanged | Watcher监听的数据节点的子节点列表发生变化时                 |

注：客户端接收到的相关事件通知中只包含状态及类型等信息，不包括节点变化前后的 具体内容，变化前的数据需业务自身存储，变化后的数据需调用get等方法重新获取； 

### 5. 捕获相应的事件

在zookeeper中采用 zk.getChildren(path, watch)、zk.exists(path, watch)、zk.getData(path, watcher, stat) 

 这样的方式为某个znode注册监听。 

| 注册方式                          | create | ChildrenChanged | Changed | Deleted |
| --------------------------------- | ------ | --------------- | ------- | ------- |
| zk.exists("/node-x",watcher)      | 可监控 |                 | 可监控  | 可监控  |
| zk.getData("/node-x",watcher)     |        |                 | 可监控  | 可监控  |
| zk.getChildren("/node-x",watcher) |        | 可监控          |         | 可监控  |

### 6. 注册watcher的方法

#### 1. 客户端与服务器的连接状态

* KeeperState:通知状态
* SyncConnected：客户端与服务器正常连接时
* Disconnected：客户端与服务器断开连接时
* Expired：会话session失效时
* AuthFailed：身份认证失败时

时间类型为：None

##### 代码

```java
package com.coachhe.watcher; 
 
import org.apache.zookeeper.WatchedEvent; 
import org.apache.zookeeper.Watcher; 
import org.apache.zookeeper.ZooKeeper; 
 
import java.io.IOException; 
import java.util.concurrent.CountDownLatch; 
 
public class ZKConnectionWatcher implements Watcher { 
 
    //计数器对象 
    static CountDownLatch countDownLatch = new CountDownLatch(1); 
    //连接对象 
    static ZooKeeper zooKeeper; 
    //连接的IP 
    static String IP = "10.211.55.100:2181"; 
 
    public static void main(String[] args) { 
        try { 
            zooKeeper = new ZooKeeper(IP, 5000, new ZKConnectionWatcher()); 
            // 阻塞线程，等待连接的创建 
            countDownLatch.await(); 
            // 会话id 
            System.out.println(zooKeeper.getSessionId()); 
            Thread.sleep(50000); 
            zooKeeper.close(); 
            System.out.println("结束"); 
        } catch (IOException | InterruptedException e) { 
            e.printStackTrace(); 
        } 
 
    } 
 
    @Override 
    public void process(WatchedEvent watchedEvent) { 
        // 事件类型 
        if (watchedEvent.getType() == Event.EventType.None) { 
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) { 
                System.out.println("连接创建成功!"); 
                //继续往下执行 
                countDownLatch.countDown(); 
            } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) { 
                System.out.println("断开连接！"); 
            } else if (watchedEvent.getState() == Event.KeeperState.Expired) { 
                System.out.println("会话超时!"); 
            } else if (watchedEvent.getState() == Event.KeeperState.AuthFailed) { 
                System.out.println("认证失败!"); 
            } 
        } 
    } 
} 

```

### 7. 检查节点是否存在

```java
// 使用连接对象的监视器
exists(String path, boolean b)
// 自定义监视器
exists(String path, Watcher w)
  
// NodeCreated:节点创建
// NodeDeleted:节点删除
// NodeDataChanged:节点内容发生变化
```

* path - znode路径
* b - 是否使用连接对象中注册的监视器
* w - 监视器对象

#### 代码

```java
package com.coachhe.watcher; 
 
import org.apache.zookeeper.WatchedEvent; 
import org.apache.zookeeper.Watcher; 
import org.apache.zookeeper.ZooKeeper; 
import org.junit.After; 
import org.junit.Before; 
import org.junit.Test; 
 
import java.util.concurrent.CountDownLatch; 
 
public class ZKWatcherExists { 
 
    String IP = "10.211.55.100:2181"; 
    ZooKeeper zooKeeper; 
 
    @Before 
    public void before() throws Exception{ 
        // 计数器 
        CountDownLatch countDownLatch = new CountDownLatch(1); 
        // arg1:服务器ip和端口 
        // arg2:客户端与服务器之间的会话超时时间（ms） 
        // arg3:监视器对象 
        zooKeeper = new ZooKeeper(IP, 5000, new Watcher() { 
            @Override 
            public void process(WatchedEvent event) { 
                if (event.getState() == Event.KeeperState.SyncConnected) { 
                    System.out.println("连接创建成功"); 
                    // 通知countDownLatch不用继续阻塞了 
                    countDownLatch.countDown(); 
                } 
                System.out.println("path = " + event.getPath()); 
                System.out.println("eventType = " + event.getType()); 
            } 
        }); 
        //主线程阻塞等待连接对象的创建成功 
        countDownLatch.await(); 
        //会话编号 
        System.out.println(zooKeeper.getSessionId()); 
        zooKeeper.close(); 
    } 
     
    @After 
    public void after() throws Exception{ 
        zooKeeper.close(); 
    } 
     
     
    @Test 
    public void watcherExists1() throws Exception { 
        // arg1表示节点的路径 
        // true表示需要进行监听,连接对象中的watcher 
        zooKeeper.exists("/watcher1", true); 
        Thread.sleep(50000); 
        System.out.println("结束"); 
    } 
} 
```

### 8. watcher的一次性

注意 

watcher监听是一次性的，若是需要多次监听，那么需要将exists方法放到process方法里面！ 

```java
@Test
public void watcherExists3() throws Exception {
  zooKeeper.exists("/watcher1", new Watcher() {
    @Override
    public void process(WatchedEvent watchedEvent){
      try{
        System.out.println("自定义watcher");
        System.out.println("path = " + watchedEvent.getPath());
        System.out.println("eventType = " + watchedEvent.getType());
        zooKeeper.exists("/watcher1", this);
      } catch (KeeperException e){
        e.printStackTrace();
      } catch (InterruptedException e){
        e.printStackTrace();
      }
    }
  })
}
```

### 9. 注册多个监视器对象

同一个节点注册两个监视器对象，那么在其改动时两个监听器对象都会有响应

```java
// 注册多个监视器对象
@Test
public void watcherExists3() throws Exception {
  zooKeeper.exists("/watcher1", new Watcher() {
    @Override
    public void process(WatchedEvent watchedEvent){
      System.out.println("1")
    }
  });
    zooKeeper.exists("/watcher1", new Watcher() {
    @Override
    public void process(WatchedEvent watchedEvent){
      System.out.println("2")
    }
  });
}
```

### 10. 查看节点getData()

##### 使用连接对象中的watcher

```java
@Test
public void watcherGetData1() throws Exception {
  // arg1:节点的路径
  // arg2:使用连接对象的watcher
  zooKeeper.getData("/watcher2", true, null);
  Tread.sleep(500000);
  System.out.println("结束")
}
```

##### 自定义watcher对象

```java
@Test
public void watcherGetData1() throws Exception {
  zooKeeper.getData("/watcher2", new Watcher(){
    @Override
    public void process(WatchedEvent watchedEvent){
      System.out.println("自定义watcher");
      System.out.println("path = " + watchedEvent.getPath();
      System.out.println("eventType = " + watchedEvent.getType());
    }
  }, null);
}
```

## 2 生成分布式唯一ID案例

```java
package com.coachhe.案例; 
 
import org.apache.zookeeper.*; 
 
import java.io.IOException; 
import java.util.concurrent.CountDownLatch; 
 
public class GloballyUniqueId implements Watcher { 
    String IP = "10.211.55.100:2181"; 
    static ZooKeeper zooKeeper; 
    String defaultPath = "/uniqueId"; 
    CountDownLatch countDownLatch = new CountDownLatch(1); 
 
    @Override 
    public void process(WatchedEvent watchedEvent) { 
        // 事件类型 
        if (watchedEvent.getType() == Event.EventType.None) { 
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) { 
                System.out.println("连接创建成功!"); 
                //继续往下执行 
                countDownLatch.countDown(); 
            } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) { 
                System.out.println("断开连接！"); 
            } else if (watchedEvent.getState() == Event.KeeperState.Expired) { 
                System.out.println("会话超时!"); 
            } else if (watchedEvent.getState() == Event.KeeperState.AuthFailed) { 
                System.out.println("认证失败!"); 
            } 
        }  
    } 
     
    // 构造方法 
    public GloballyUniqueId() { 
        try { 
            // 创建连接对象 
            zooKeeper = new ZooKeeper(IP, 5000, this); 
            // 阻塞线程，等待连接创建成功 
            countDownLatch.await(); 
        } catch (IOException | InterruptedException e) { 
            e.printStackTrace(); 
        } 
    } 
     
    //生成ID的方法 
    public String getUniqueId(){ 
        String path = ""; 
        try { 
            path = zooKeeper.create(defaultPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                    CreateMode.EPHEMERAL); 
        } catch (KeeperException | InterruptedException e) { 
            e.printStackTrace(); 
        }  
        return path.substring(9); 
    } 
 
 
    public static void main(String[] args) { 
        GloballyUniqueId globallyUniqueId = new GloballyUniqueId(); 
        for (int i = 1; i <= 5; i++) { 
            String id = globallyUniqueId.getUniqueId(); 
            System.out.println(id); 
        } 
         
         
    } 
     
} 
```

## 3 配置中心案例

### 背景

工作中有这样的一个场景: 

数据库用户名和密码信息放在一个配置文件中，应用读取该配置文件，配置文件信息放入缓存。 

若数据库的用户名和密码改变时候，还需要重新加载缓存，比较麻烦，通过 ZooKeeper可以轻松完成，当数据库发生变化时自动完成缓存同步。 

也就是说，在这里有用户名和密码，如果能及时读取更改信息。 

### 思路

1. 连接zookeeper服务器 

2. 读取zookeeper中的配置信息，注册watcher监听器，存入本地变量 

3. 当zookeeper中的配置信息发生变化时，通过watcher的回调方法捕获数据变化事件 

4. 重新获取配置信息 

### 代码

```java
package com.coachhe.案例; 
 
import org.apache.zookeeper.KeeperException; 
import org.apache.zookeeper.WatchedEvent; 
import org.apache.zookeeper.Watcher; 
import org.apache.zookeeper.ZooKeeper; 
 
import java.io.IOException; 
import java.util.concurrent.CountDownLatch; 
 
public class MyConfigCenter implements Watcher { 
    String IP = "10.211.55.100:2181"; 
    static ZooKeeper zooKeeper; 
    CountDownLatch countDownLatch = new CountDownLatch(1); 
     
    // 用于本地化存储配置信息 
    private String url; 
    private String username; 
 
    public String getUrl() { 
        return url; 
    } 
 
    public void setUrl(String url) { 
        this.url = url; 
    } 
 
    public String getUsername() { 
        return username; 
    } 
 
    public void setUsername(String username) { 
        this.username = username; 
    } 
 
    public String getPassword() { 
        return password; 
    } 
 
    public void setPassword(String password) { 
        this.password = password; 
    } 
 
    private String password; 
 
    @Override 
    public void process(WatchedEvent watchedEvent) { 
        // 事件类型 
        if (watchedEvent.getType() == Event.EventType.None) { 
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) { 
                System.out.println("连接创建成功!"); 
                //继续往下执行 
                countDownLatch.countDown(); 
            } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) { 
                System.out.println("断开连接！"); 
            } else if (watchedEvent.getState() == Event.KeeperState.Expired) { 
                System.out.println("会话超时!"); 
            } else if (watchedEvent.getState() == Event.KeeperState.AuthFailed) { 
                System.out.println("认证失败!"); 
            } 
        } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) { 
            initValue(); 
        } 
    } 
     
    // 连接zookeeper服务器，读取配置信息 
    public void initValue(){ 
        try { 
            // 创建连接对象 
            zooKeeper = new ZooKeeper(IP, 5000, this); 
            // 阻塞线程，等待连接创建成功 
            countDownLatch.await(); 
            // 读取配置信息 
            this.url = new String(zooKeeper.getData("/config/url", true, null)); 
            this.username = new String(zooKeeper.getData("/config/username", true, null)); 
            this.password = new String(zooKeeper.getData("/config/password", true, null)); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        } catch (KeeperException e) { 
            e.printStackTrace(); 
        } 
    } 
 
    // 构造方法 
    public MyConfigCenter() { 
        initValue(); 
    } 
 
 
    public static void main(String[] args) { 
        try { 
            MyConfigCenter myConfigCenter = new MyConfigCenter(); 
            for (int i = 1; i <= 10; i++) { 
                Thread.sleep(3000); 
                System.out.println("url:" + myConfigCenter.getUrl()); 
                System.out.println("username:" + myConfigCenter.getUsername()); 
                System.out.println("password:" + myConfigCenter.getPassword()); 
            } 
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        } 
    } 
} 
```

## 4 分布式锁

### 设计思路

分布式锁有多种实现方式，比如通过数据库、redis都可实现。作为分布式协同工具ZooKeeper，当然也有着标准的实现方式。下面介绍在zookeeper中如何实现排他锁。  

设计思路：  

1. 每个客户端往/Locks下创建临时有序节点/Locks/Lock_000000001 

2. 客户端取得/Locks下子节点，并进行排序，判断排在最前面的是否为自己，如果自己的 锁节点在第一位，代表获取锁成功 

3. 如果自己的锁节点不在第一位，则监听自己前一位的锁节点。例如，自己锁节点 Lock 000000001 

4. 当前一位锁节点（Lock 000000002）的逻辑 

5. 监听客户端重新执行第2步逻辑，判断自己是否获得了锁 

### 1. 创建节点

```java
package com.coachhe.lock; 
 
import org.apache.zookeeper.*; 
import org.apache.zookeeper.data.Stat; 
 
import java.io.IOException; 
import java.util.concurrent.CountDownLatch; 
 
public class MyLock { 
    String IP = "10.211.55.100:2181"; 
    static ZooKeeper zooKeeper; 
    CountDownLatch countDownLatch = new CountDownLatch(1); 
    private static final String LOCK_ROOT_PATH = "/locks"; 
    private static final String LOCK_NODE_NAME = "Lock_"; 
    private String lockPath; 
 
    //打开zookeeper连接 
    public MyLock() { 
        try { 
            // 创建连接对象 
            zooKeeper = new ZooKeeper(IP, 5000, new Watcher() { 
                @Override 
                public void process(WatchedEvent watchedEvent) { 
                    if (watchedEvent.getType() == Event.EventType.None) { 
                        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) { 
                            System.out.println("连接成功"); 
                            countDownLatch.countDown(); 
                        } 
                    } 
                } 
            }); 
            // 阻塞线程，等待连接创建成功 
            countDownLatch.await(); 
        } catch (IOException | InterruptedException e) { 
            e.printStackTrace(); 
        } 
    } 
 
    //获取锁 
    public void acquireLock() throws Exception { 
        //创建锁节点 
        createLock(); 
        //尝试获取锁 
        attemptLock(); 
    } 
 
    //创建锁节点 
    private void createLock() throws Exception{ 
        // 判断locks节点是否存在，不存在则创建 
        Stat stat = zooKeeper.exists(LOCK_ROOT_PATH, false); 
        if (stat == null) { 
            zooKeeper.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                    CreateMode.PERSISTENT); 
        } 
        // 创建临时有序节点 
        lockPath = zooKeeper.create(LOCK_ROOT_PATH + "/" + LOCK_NODE_NAME, new byte[0], 
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); 
        System.out.println("节点创建成功：" + lockPath); 
    } 
 
    //尝试获取锁 
    private void attemptLock() throws Exception { 
 
    } 
 
    //释放锁 
    public void releaseLock() throws Exception { 
 
    } 
 
    public static void main(String[] args) { 
        try { 
            MyLock myLock = new MyLock(); 
            myLock.createLock(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
 
} 
```

### 2. 获取锁

```java
package com.coachhe.lock; 
 
import org.apache.zookeeper.*; 
import org.apache.zookeeper.data.Stat; 
import sun.tools.jstack.JStack; 
 
import java.io.IOException; 
import java.util.Collection; 
import java.util.Collections; 
import java.util.List; 
import java.util.concurrent.CountDownLatch; 
 
public class MyLock { 
    String IP = "10.211.55.100:2181"; 
    static ZooKeeper zooKeeper; 
    CountDownLatch countDownLatch = new CountDownLatch(1); 
    private static final String LOCK_ROOT_PATH = "/locks"; 
    private static final String LOCK_NODE_NAME = "Lock_"; 
    private String lockPath; 
 
    //打开zookeeper连接 
    public MyLock() { 
        try { 
            // 创建连接对象 
            zooKeeper = new ZooKeeper(IP, 5000, new Watcher() { 
                @Override 
                public void process(WatchedEvent watchedEvent) { 
                    if (watchedEvent.getType() == Event.EventType.None) { 
                        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) { 
                            System.out.println("连接成功"); 
                            countDownLatch.countDown(); 
                        } 
                    } 
                } 
            }); 
            // 阻塞线程，等待连接创建成功 
            countDownLatch.await(); 
        } catch (IOException | InterruptedException e) { 
            e.printStackTrace(); 
        } 
    } 
 
    //监视器对象，监视上一个节点是否被删除 
    Watcher watcher = new Watcher() { 
        @Override 
        public void process(WatchedEvent watchedEvent) { 
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) { 
                synchronized (this) { 
                    MyLock.this.notifyAll(); 
                } 
            } 
        } 
    }; 
 
    //获取锁 
    public void acquireLock() throws Exception { 
        //创建锁节点 
        createLock(); 
        //尝试获取锁 
        attemptLock(); 
    } 
 
    //创建锁节点 
    private void createLock() throws Exception{ 
        // 判断locks节点是否存在，不存在则创建 
        Stat stat = zooKeeper.exists(LOCK_ROOT_PATH, false); 
        if (stat == null) { 
            zooKeeper.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                    CreateMode.PERSISTENT); 
        } 
        // 创建临时有序节点 
        lockPath = zooKeeper.create(LOCK_ROOT_PATH + "/" + LOCK_NODE_NAME, new byte[0], 
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); 
        System.out.println("节点创建成功：" + lockPath); 
    } 
 
    //尝试获取锁 
    private void attemptLock() throws Exception { 
        // 获取Locks节点下的所有子节点 
        List<String> list = zooKeeper.getChildren(LOCK_ROOT_PATH, false); 
        // 对子节点进行排序 
        Collections.sort(list); 
        // /Locks/Lock_0000000001 
        int index = list.indexOf(lockPath.substring(LOCK_ROOT_PATH.length() + 1)); 
        if (index == 0) { 
            //说明当前临时有序节点排名第一 
            System.out.println("获取锁成功"); 
            return; 
        } else { 
            // 获取上一个节点的路径 
            String path = list.get(index - 1); 
            Stat stat = zooKeeper.exists(LOCK_ROOT_PATH + "/" + path, watcher); 
            if (stat == null) { 
                //如果等于空，那么就是在执行上面两行代码的时候前一个节点被删除掉了，那么重新尝试 
                attemptLock(); 
            } else { 
                //如果不为空，那么就等待上一个节点被删除 
                synchronized (watcher) { 
                    watcher.wait(); 
                } 
                attemptLock(); 
            } 
        } 
    } 
 
    //释放锁 
    public void releaseLock() throws Exception { 
 
    } 
 
    public static void main(String[] args) { 
        try { 
            MyLock myLock = new MyLock(); 
            myLock.createLock(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
 
} 
```

### 3. 释放锁

```java
    //释放锁 
    public void releaseLock() throws Exception { 
        //删除临时有序节点 
        zooKeeper.delete(this.lockPath, -1); 
        zooKeeper.close(); 
        System.out.println("锁已经释放：" + this.lockPath); 
    } 
```





















