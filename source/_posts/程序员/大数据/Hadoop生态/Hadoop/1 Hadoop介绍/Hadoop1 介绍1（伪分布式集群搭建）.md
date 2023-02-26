---
title: Hadoop1 介绍1（伪分布式集群搭建）  
date: 2022-12-04 02:44:26  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 1 Hadoop介绍
---
#  Hadoop介绍

## 1 定义

Hadoop是一个由Apache基金会所开发的**分布式系统基础架构**

### 2 解决问题

**海量数据的存储和海量数据的分析计算**问题。

### 3 三大发行版本

Apache：最基础，对于入门学习最好

Cloudera：在大型互联网企业中用的较多

Hortonworks：文档较好

### 4 Hadoop的优势

1. **高可靠性**：Hadoop底层维护了多个副本（默认为3个），所以即使Hadoop某个计算元素或存储出现故障，也不会导致数据的丢失。
2. **高扩展性**：在集群间分配任务数据，可方便的扩展数以千计的节点。（增加服务器时不用把系统停下来）
3. **高效性**：在MapReduce的思想下，Hadoop是并行工作的（多个服务器一起计算），以加快任务处理的速度。
4. **高容错性**：能够自动将失败的任务重新分配。

## 5 Hadoop的组成

### Hadoop1.x和2.x的区别

| Hadoop1.x的组成            | Hadoop2.x的组成    |
| -------------------------- | ------------------ |
| Common（辅助工具）         | Common（辅助工具） |
| HDFS（数据存储）           | HDFS（数据存储）   |
| MapReduce（计算+资源调度） | Yarn（资源调度）   |

1.x中MapReduce需要同时负责计算和资源调度

2.x中Yarn负责资源调度，MapReduce负责计算

### HDFS

1. **NameNode（nn）**: 存储文件的元数据，如文件名，文件目录结构，文件属性（生成时间、副本数、文件权限），以及每个文件的块列表和块所在的DataNode。  
   (相当于是目录)
2. **DataNoade（dn）**：在本地文件系统存储块数据，以及块数据的校验和。具体的内容
3. **Secondary NameNode（2nn）**： 用来监控HDFS状态的辅助后台程序，每隔一段时间获取HDFS元数据的快照。  
   (用来辅助NameNode工作)

### YARN架构概述

Resource Manager相当于**部门经理**，负责整个部门的运行  
NodeManager是单节点的负责对象。  
App Mstr相当于**项目经理**，负责某个项目。负责在集群上某一个任务的协调  
Container是YARN中的资源抽象，它封装了某个节点的多维度资源，如内存、CPU、磁盘、网络等（**虚拟化技术**）  

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Flink/20201210153155.png" style="zoom:80%;" />

### MapReduce架构概述

MapReduce将计算过程分为两个阶段：Map和Reduce 
（1） Map阶段并行处理输入数据 
（2） Reduce阶段对Map结果进行汇总



# Hadoop运行环境搭建

## 1 虚拟机环境准备

### 网络配置

**第一步，更改网卡设置**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190137.png" style="zoom:50%;" />

同时记录下mac地址，为00:0c:29:d2:60:7a

**第二步：更改网络设置**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190206.png" style="zoom:50%;" />

**第三步：更改主机名**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190228.png" style="zoom:50%;" />

**第四步：添加映射**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190256.png" style="zoom:50%;" />

**第五步：更改网络配置**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190317.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190348.png" style="zoom:50%;" />

（注意！！mac是.1不是.2）

**总结**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190412.png" style="zoom:50%;" />

**结果**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190432.png" style="zoom: 50%;" />

### 用户配置

**修改配置文件/etc/sudoers**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190521.png" style="zoom:50%;" />

### java和hadoop环境安装

1. **在/opt目录下创建module、software文件夹**

   ​	software用来存储所有程序的jar包（类似安装包）

   ​	module就是所有的jar包解压之后放置的地方（安装的地方）

2. **将jdk包和hadoop包传到/opt/software中**

3. **将这两个包解压到/opt/module中**

**命令**

```
tar -zxvf xxx -C /opt/module
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190636.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190648.png)

**注意**

此时的文件是属于root的，需要将其改为自己的

### 在文件/etc/profile中添加JAVA_HOME和HADOOP_HOME

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190728.png" style="zoom:50%;" />

### source /etc/profile

就可以了

### 结果

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210190805.png" style="zoom:50%;" />



## 2 官方案例

我自己在/home目录下建立了一个hadoop_learning文件夹，然后在里面的examples进行演示。

### ① grep案例

**a. 首先将hadoop文件夹下的etc目录下的.xml文件复制到grep_input文件中：**

```
cp /opt/module/hadoop-2.7.2/etc/hadoop/*.xml grep_input/
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191208.png)

**b. 然后运行hadoop示例的example文件来执行grep**

```
hadoop jar /opt/module/hadoop-2.7.2/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.2.jar grep grep_input/ grep_output 'dfs[a-z.]+'
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191245.png)

**c. 最后在grep_output里面看结果**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191308.png" style="zoom:80%;" />

### ② WordCount案例

**a. 首先自己创建一个wc_input文件，里面放入自己写的文件**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191348.png" style="zoom:50%;" />

**b. 运行hadoop示例的example文件来执行**

```
hadoop jar /opt/module/hadoop-2.7.2/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.2.jar wordcount wc_input/ wc_output
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191426.png)

**c. 就可以得到词频的统计结果**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191442.png" style="zoom:50%;" />







# 伪分布式模式

## 1 启动HDFS并运行MR文件

### 伪分布式模式介绍

按照分布式来进行配置，只是**只有一台服务器**而已。

### 伪分布式配置

在hadoop目录下的etc/hadoop里面是所有的配置文件

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191640.png)

**1. 修改env文件**

只要看到env，就改JAVA_HOME

**修改前：**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191721.png" style="zoom: 80%;" />

**修改后：**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191745.png" style="zoom: 50%;" />

**2. 修改core-site.xml**

**修改前**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191819.png" style="zoom:50%;" />

**修改后**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191837.png" style="zoom:50%;" />

```xml
		<property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop1:9000</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>file:/home/hadoop/tmp</value>
    </property>
    <property>
        <name>io.file.buffer.size</name>
        <value>131702</value>
    </property>
```



**3. 修改hdfs-site.xml**

指定HDFS副本的数量

**修改前**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191916.png" style="zoom:50%;" />

**修改后**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210191933.png" style="zoom:50%;" />

### 启动集群

**1. 格式化NameNode**

第一次启动时格式化即可，以后就不要总格式化了。

bin目录下的hdfs可以做到

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192158.png" style="zoom:80%;" />

```
bin/hdfs namenode -format
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192227.png)

**2. 启动NameNode**

所有的启动命令都在sbin目录下面

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192301.png" style="zoom:67%;" />

输入

```
hadoop-daemon.sh start namenode
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192329.png" style="zoom:80%;" />

**3. 检查是否启动成功**

用jps检查

jps：java的ps进程

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192353.png" style="zoom:80%;" />

**4. 启动DataNode**

还是用hadoop-daemon.sh来启动

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192422.png)

**5. 检查datanode是否启动成功**

还是jps

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192442.png" style="zoom:50%;" />

**6. 检查集群是否启动**

去window主机的谷歌浏览器上输入ip地址:50070查看是否能正常进入

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192511.png" style="zoom:80%;" />

**注意！！！**

必须关闭防火墙！否则不能正常进入

### 添加目录和文件

**最开始的时候只有根目录**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192608.png" style="zoom:67%;" />

**若要添加目录，则需要用hadoop的指令**

```
bin/hdfs dfs -mkdir -p /home/hadoop_learning/coachhe
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192641.png" style="zoom: 80%;" />

**ls用法**

```
bin/hdfs dfs -ls /home/hadoop_learning/
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192714.png" style="zoom:80%;" />

**上传文件**

```
hdfs dfs -put /home/hadoop_learning/examples/wc_input/wc.input /home/hadoop_learning/examples/wc_input/
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192743.png)

### 重新格式化NameNode

**首先**

关闭jps查询得到的NameNode和DataNode进程

**然后**

删除$HADOOP_HOME目录下面的data和logs文件。

**注意**

一定要先退出进程再删除文件

#### 思考：为么不能一直格式化NameNode？

因为DataNode和NameNode共享同一个集群 ID，两者之间需要相互通信，因此如果格式化之后NameNode会变化集群ID，导致两者不能通信。

**具体分析**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192834.png" style="zoom:67%;" />



## 2 启动YARN并运行MR文件

### 分析

1. 配置集群在YARN上运行MR
2. 启动、测试集群增、删、查
3. 在YARN上执行WordCount案例。

### 配置

**1. 修改yarn-env.sh（同样也在etc/hadoop目录下）** 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210192951.png" style="zoom:80%;" />

**2. 配置yarn-site.xml** 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193014.png)

修改后格式如下

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193029.png)

```shell
<configuration>

<!-- Site specific YARN configuration properties -->
        <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
        </property>

        <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>clinux01</value>
        </property>

</configuration>
```

**3. 配置mapred-env.sh** 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193047.png" style="zoom:50%;" />

**4. 配置mapreds-site.xml.template，重新命名为mapred-site.xml** 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193108.png)

**5. 修改mapred-site.xml** 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193131.png" style="zoom: 67%;" />

 修改后如下

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193150.png" style="zoom: 67%;" />

**总结** 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193215.png" style="zoom:67%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193228.png" style="zoom:67%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193248.png" style="zoom: 67%;" />

### 启动集群

#### 1. 保证NameNode和DataNode已经启动

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193400.png)

#### 2. 启动resourcemanager和nodemanager

  使用sbin目录下的yarn-daemon.sh脚本，启动方法和hadoop-daemon.sh一样

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193426.png"  />

#### 标志

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193502.png)



## 运行MR程序

运行方式和没有YARN完全相同

```
hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.2.jar wordcount /home/hadoop_learning/examples/wc_input/wc.input /home/hadoop_learning/examples/wc_output_yarn 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193605.png)

### 注意

运行得到的输出（wc_output_yarn）不能直接在服务器上看见，需要用hdfs指令才能看到 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210193629.png)

### 配置文件总结

| 要获取的默认文件     | 文件存放在Hadoop的jar包中的位置                            |
| -------------------- | ---------------------------------------------------------- |
| [core-default.xml]   | hadoop-common-2.7.2.jar/ core-default.xml                  |
| [hdfs-default.xml]   | hadoop-hdfs-2.7.2.jar/ hdfs-default.xml                    |
| [yarn-default.xml]   | hadoop-yarn-common-2.7.2.jar/ yarn-default.xml             |
| [mapred-default.xml] | hadoop-mapreduce-client-core-2.7.2.jar/ mapred-default.xml |

#### 自定义配置文件

**core-site.xml、hdfs-site.xml、yarn-site.xml、mapred-site.xml**四个配置文件存放在$HADOOP_HOME/etc/hadoop这个路径上，用户可以根据项目需求重新进行修改配置。

