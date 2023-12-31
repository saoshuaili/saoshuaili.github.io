---
title: Hadoop1 介绍2（完全分布式集群搭建）
tags: []
categories:
  - 程序员
  - 大数据
  - Hadoop生态
  - Hadoop
  - 1 Hadoop介绍
date: 2022-12-04 02:44:30
---
# 环境和工具准备

## 虚拟机环境准备

准备三台虚拟机 

关闭防火墙等步骤和之前一样 

## 集群分发脚本scp

### scp功能

scp可以实现服务器与服务器之间的数据拷贝（from server1 to server2） 


### 基本语法 

``` shell
scp -r $pdir/$fname  $user@hadoop$host:$pdir/$fname 
```

### 解释

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194648.png)

### 三种用法

1. 本机数据拷贝到别的服务器 
2. 从别的服务器将数据拷贝到本机 
3. 将某台非本机的服务器的数据拷贝到另一台主机 

### `scp` 使用案例1 

在hadoop100上，将hadoop100中/home/hadoop_learning目录下的软件拷贝到hadoop102上。 

```shell
scp -r hadoop_learning/ root@hadoop101:/home/hadoop_learning 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194735.png" width=50% />

#### 结果 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194820.png" style="zoom:80%;" />

#### 注意

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194842.png" style="zoom:80%;" />

### `scp` 使用案例2

在hadoop102上从hadoop100处拉取/home/hadoop_learning下的数据 

```shell
sudo scp -r coachhe@hadoop100:/home/hadoop_learning /home/hadoop_learning 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194924.png" style="zoom:67%;" />

#### 结果 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210154825.png" style="zoom:80%;" />

#### 注意

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210154916.png" style="zoom:80%;" />


### `scp` 使用案例3

在hadoop101上将hadoop100的数据拷贝到hadoop102 

```shell
scp -r coachhe@hadoop100:/home/hadoop_learning root@hadoop102:/home/hadoop_learning2 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210154931.png" style="zoom:80%;" />

## 远程同步工具rsync

### rsync和scp的区别

#### 优点

rsync主要用于备份和镜像。具有速度快，避免复制相同内容和支持符号链接的优点。

#### 和scp的区别

用rsync做文件的复制要比scp速度快，rsync只对差异文件做更新，scp是把所有文件都复制过去。

### 语法

```shell
rsync -rvl $pdir/$fname $user@hadoop$host:$host:$pdir/$fname
```


### 选项说明

| 选项 | 功能         |
| ---- | ------------ |
| -r   | 递归         |
| -v   | 显示复制过程 |
| -l   | 拷贝符号连接 |

### `rsync` 使用案例

将hadoop100机器上的/home/newdisk目录同步到hadoop101服务器root目录下的/home/newdisk下。 

#### 解答

```shell
sudo rsync -rvl /home/newdisk/ root@hadoop101:/home/newdisk 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210154955.png" style="zoom:80%;" />



## 编写集群分发脚本xsync

### 代码

```shell
#!/bin/bash 
#1 获取输入参数个数，如果没有参数则直接退出 
pcount=$# 

if((pcount==0)); then 
    echo no args; 
    exit; 
fi 

#2 获取文件名称 
p1=$1 
fname=`basename $p1` #这里就是拿到最终文件的名称，/opt/module就是module 
echo fname=$fname 

#3 获取上级目录到绝对路径 
pdir=`cd -P $(dirname $p1); pwd` #这里是得到文件的路径 
echo pdir=$pdir 

#4 获取当前用户名称 
user=`whoami`  #也就是coachhe 

#5 循环 
for((host=101;host<103;host++)); do 
    echo -------- hadoop$host --------- 
    rsync -rvl $pdir/$fname root@hadoop$host:$pdir 
done 
```

### 脚本执行

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155018.png" style="zoom:80%;" />

### 执行结果

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155031.png" style="zoom:80%;" />

### 位置

将这个脚本放在coachhe的bin目录下。方便以后查看和执行
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155057.png" style="zoom:80%;" />

### 注意

因为/home/coachhe/bin的是在全局PATH中的，因此可以在任意位置直接执行xsync这个脚本 

# 集群配置和启动

## 1. 集群配置

### 1 大纲

|      | hadoop100              | hadoop101                        | hadoop102                       |
| ---- | ---------------------- | -------------------------------- | ------------------------------- |
| HDFS | NameNode <br> DataNode | DataNode                         | SecondaryNameNode <br> DataNode |
| YARN | NodeManager            | ResourceManager <br> NodeManager | NodeManager                     |

#### 解释

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155147.png)

### 2 核心配置文件

1. 配置core-site.xml，指定HDFS中的NameNode的地址为hadoop100 
   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155208.png)
2. 配置hadoop-env.sh，设置JAVA_HOME  
   这一步之前就做过，就是将JAVA_HOME设置为本机jdk 
3. 配置hdfs-site.xml，将备份数设置为3（默认值），并且指定Hadoop辅助名称节点主机配置 
   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155227.png)

### 3 YARN配置文件

1. yarn-env.sh配置JAVA_HOME  
   已经配置过了。 
2. yarn-site.xml添加Reducer获取数据的方式和ResourceManager的地址  
   在这里指定为resourmanager为hadoop101（根据大纲）   
   
   ```xml
   <configuration>
   
   <!-- Site specific YARN configuration properties -->
           <property>
                   <name>yarn.nodemanager.aux-services</name>
                   <value>mapreduce_shuffle</value>
           </property>
   
           <property>
                   <name>yarn.resourcemanager.hostname</name>
                   <value>clinux02</value>
           </property>
   
   </configuration>
   ```

### 4 MapReduce配置文件

1. 配置mapred-senv.sh的JAVA_HOME  
   已经配置过了 
2. 配置mapred-site.xml，增加指定MR运行在YARN上的配置   

   ```xml
   <property>
   		<name>mapreduce.framework.name</name>
   		<value>yarn</value>
   </property>
   ```

## 2 集群单点启动

### 1. 格式化namenode  

```sh
hdfs namenode -format
```

#### 注意

**格式化之前需要删除 logs 和 data**，否则再次启动时就会报错，因为格式化时为 namenode 分配了新的 ID，但是因为没有删除 data 文件夹，里面的 DataNode 对应的 ID 还是原先分配的 ID，匹配不上就会报错。

### 2. 启动所有节点的 namenode 和 datanode  

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155338.png" width=50%>

#### 注意

hadoop101和hadoop102的Namenode不用启动   

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155347.png" width=30%>

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155400.png" width=30%>

## 3 SSH无密登录配置

### 思考

如果节点增加到1000个，那么不可能给一个个启动，怎么办呢？ 

### 介绍

ssh可以直接访问其他服务器 

### 免密登录原理

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210201558.png" style="zoom:50%;" />

### 生成公钥和私钥

在.ssh目录下执行 

```sh
ssh-keygen -t rsa 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155445.png" style="zoom: 67%;" />

### 将公钥复制到其他服务器

#### 命令

```shell
ssh-copy-id hadoop101 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155503.png" style="zoom:67%;" />

#### 注意

本机也需要拷贝！！ 

### 需要配置.ssh的具体服务器

#### 1. 配置hadoop100

##### 原因： 

hadoop100上有NodeManager，用来控制不同节点的任务，因此需要配置hadoop100的.ssh，从而能控制其他节点。 

#### 2. 配置hadoop101 

##### 原因： 

hadoop101上有YARN的ResourceManager，也需要控制不同的节点，因此也需要对ssh进行配置。 

### 对hadoop101也进行配置

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155523.png" style="zoom: 80%;" />

### 对hadoop102的root用户配置

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155534.png" style="zoom:80%;" />

# 群起集群

## 配置slaves.md

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155552.png)

### 注意！！

不允许有空格，不允许有空行！ 

## 关闭所有NameNode和DataNode

### hadoop100

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155618.png" style="zoom:80%;" />

### hadoop101

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155629.png" style="zoom:80%;" />
没有namenode

### hadoop102

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155642.png" style="zoom:80%;" />
没有NameNode

## 执行群起脚本

### 1 在hadoop100上执行start-dfs.sh脚本执行hdfs


|      | hadoop100                                                    | hadoop101                                                    | hadoop102                                                    |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| jps  | ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155931.png) | ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155943.png) | ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155955.png) |

### 2 启动YARN

### 注意

必须在hadoop101上启动，不能在其他节点上！（也就是必须在ResourceManager所在的节点启动！！）

|      | hadoop100                                                    | hadoop101                                                    | hadoop102                                                    |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| jps  | ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160008.png) | ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160027.png) | ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160051.png) |

|      | `hadoop100` | `hadoop101`                     | `hadoop102` |
| ---- | ----------- | ------------------------------- | ----------- |
| HDFS | NameNode <br> DataNode    | <br> DataNode                        | SecondaryNameNode <br> DataNode            |
| YARN | <br> NodeManager | ResourceManager <br> NodeManager | <br> NodeManager |






# 集群基本测试

### 1 上传大小文件

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155833.png)

##### 结果

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155847.png" style="zoom:80%;" />

#### 对小文件

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155857.png" style="zoom:67%;" />

#### 对大文件

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155907.png" style="zoom:67%;" />

### 2 文件存储位置

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160119.png" style="zoom:67%;" />

#### 注意 

  多个block拼接起来就是我们所需要的文件！ 

## 6 集群启动停止方式总结

### 各个模块分开启动停止

#### 注意 

配置ssh是前提 

#### 步骤 

##### 1. 整体启动/停止HDFS 

```shell
start-dfs.sh / stop-dfs.sh 
```

##### 2. 整体启动/停止YARN 

```shell
start-yarn.sh / stop-yarn.sh 
```

### 各一组件逐一停止

#### 1. 分别启动停止HDFS组件 

```shell
hadoop-daemon.sh start/stop namenode/datanode/secondarynamenode 
```

#### 2. 启动/停止YARN 

```shell
yarn-daemon.sh start/stop resourcemanager/nodemanager 
```



# crontab和集群时间同步

## crond服务管理

#### 使用前需要打开crond服务

```jps
service crontad restart 
```

### 基本语法 

crontab [选项] 

##### 选项说明 

-e：编辑crontab定时任务  
-l：查询crontab任务  
-r：删除当前用户所有crontab任务 

## 集群时间同步

### 时间同步的方式：

找一个机器，作为时间服务器，所有的机器与这台集群时间进行定时的同步，比如，每隔十分钟，同步一次时间。

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160146.png" style="zoom:67%;" />

### 具体步骤1 服务时间配置（以hadoop100为例）

#### 注意 

  所有步骤都采用root权限安装。 

#### 1. 检查ntp是否安装 

```shell
rpm -qa | grep ntp 
```

如果出现上面的三个文件则表示安装了ntp 

#### 2. 修改ntp配置文件 

##### a. 授权192.168.137.0-192.168.137.255的所有机器可以从这台机器上查询和同步时间 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160207.png)  
![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160215.png)  

##### b.修改集群在局域网中，不使用其他互联网的时间

###### 注释前

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160233.png)

###### 注释后

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160244.png)

##### c.当该节点丢失网络连接，依然可以采用本地时间作为时间服务器为集群中的其他节点提供时间同步 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160254.png)

#### 3. 配置/etc/sysconfig/ntpd文件 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160304.png)

#### 4. 重新启动ntps服务 

```shell
service ntps restart 
```

#### 总结 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160316.png)

### 具体步骤2 其他服务器配置

#### 在其他机器上配置1分钟与服务器时间一致

```shell
crontab -e: 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160331.png)  
然后用date -s XXX来随意将时间进行设置  
然后等待1分钟之后看时间是否会恢复  
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160346.png" style="zoom:67%;" />


# `Hadoop3.x` 配置

## `core-site.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <!-- 指定NameNode的地址 -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop102:8020</value>
    </property>
    <!-- 指定hadoop数据的存储目录 -->
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/root/module/hadoop/data</value>
    </property>

    <!-- 配置HDFS网页登录使用的静态用户为atguigu -->
    <property>
        <name>hadoop.http.staticuser.user</name>
        <value>root</value>
    </property>

    <!-- 配置该root(superUser)允许通过代理访问的主机节点 -->
    <property>
        <name>hadoop.proxyuser.root.hosts</name>
        <value>*</value>
    </property>
    <!-- 配置该root(superUser)允许通过代理用户所属组 -->
    <property>
        <name>hadoop.proxyuser.root.groups</name>
        <value>*</value>
    </property>
    <!-- 配置该root(superUser)允许通过代理的用户-->
    <property>
        <name>hadoop.proxyuser.root.users</name>
        <value>*</value>
    </property>
</configuration>
```

可以看到，最大的变更来自于 NameNode 的端口，在上面的配置中我们可以看到，在 `Hadoop1.x` 中使用的是 9000 端口，但是在 `Hadoop3.x` 中，我们使用的是 8020 端口。

其余其实变化不大。

## `hdfs-site.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
		<!-- nn web端访问地址-->
    <property>
        <name>dfs.namenode.http-address</name>
        <value>hadoop102:9870</value>
    </property>
	<!-- 2nn web端访问地址-->
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>hadoop104:9868</value>
    </property>
    <!-- 测试环境指定HDFS副本的数量1 -->
    <property>
        <name>dfs.replication</name>
        <value>3</value>
    </property>
</configuration>
```

可以看到，最大的变化也是来自端口上。默认的端口变动了。

## `yarn-site.xml`

```xml
<?xml version="1.0"?>
<configuration>
<!-- Site specific YARN configuration properties -->
<!-- 指定MR走shuffle -->
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    
    <!-- 指定ResourceManager的地址-->
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>hadoop103</value>
    </property>
    
    <!-- 环境变量的继承 -->
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
    
    <!--yarn单个容器允许分配的最大最小内存 -->
    <property>
        <name>yarn.scheduler.minimum-allocation-mb</name>
        <value>512</value>
    </property>
    <property>
        <name>yarn.scheduler.maximum-allocation-mb</name>
        <value>4096</value>
    </property>
    
    <!-- yarn容器允许管理的物理内存大小 -->
    <property>
        <name>yarn.nodemanager.resource.memory-mb</name>
        <value>4096</value>
    </property>
    
    <!-- 关闭yarn对虚拟内存的限制检查 -->
    <property>
        <name>yarn.nodemanager.pmem-check-enabled</name>
        <value>true</value>
    </property>
    <property>
        <name>yarn.nodemanager.vmem-check-enabled</name>
        <value>false</value>
    </property>

    <!-- 开启日志聚集功能 -->
    <property>
        <name>yarn.log-aggregation-enable</name>
        <value>true</value>
    </property>
    
    <!-- 设置日志聚集服务器地址 -->
    <property>
        <name>yarn.log.server.url</name>
        <value>http://hadoop102:19888/jobhistory/logs</value>
    </property>
    
    <!-- 设置日志保留时间为7天 -->
    <property>
        <name>yarn.log-aggregation.retain-seconds</name>
        <value>604800</value>
    </property>
</configuration>
```

可以看到，和 `1.x` 版本最大的差别来自于需要显式指定 JAVA_HOME 等配置参数

```xml
    <!-- 环境变量的继承 -->
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
```

## `mapred-site.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

<configuration>
	<!-- 指定MapReduce程序运行在Yarn上 -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```

这块没有变动

## `workers`

```xml
hadoop102
hadoop103
hadoop104
```

这块其实就是 `hadoop1.x` 中的 slaves，但是在 `hadoop3.x` 中换成了 workers，里面内容是完全相同的，也是不能有空格和空行

## 配置历史服务器

要修改 `mapred-site.xml`

```xml
<!-- 历史服务器端地址 -->
<property>
    <name>mapreduce.jobhistory.address</name>
    <value>hadoop102:10020</value>
</property>

<!-- 历史服务器web端地址 -->
<property>
    <name>mapreduce.jobhistory.webapp.address</name>
    <value>hadoop102:19888</value>
</property>

```

这里也是和 `hadoop1.x` 最大的区别就是端口不同。

## 配置日志的聚集

日志聚集概念：应用运行完成以后，将程序运行日志信息上传到 HDFS 系统上。

日志聚集功能好处：可以方便的查看到程序运行详情，方便开发调试。

注意：开启日志聚集功能，需要重新启动 NodeManager 、ResourceManager 和 HistoryManager。

### 1. 配置 `yarn-site.xml`

```xml
<!-- 开启日志聚集功能 -->
<property>
    <name>yarn.log-aggregation-enable</name>
    <value>true</value>
</property>

<!-- 设置日志聚集服务器地址 -->
<property>  
    <name>yarn.log.server.url</name>  
    <value>http://hadoop102:19888/jobhistory/logs</value>
</property>

<!-- 设置日志保留时间为7天 -->
<property>
    <name>yarn.log-aggregation.retain-seconds</name>
    <value>604800</value>
</property>

```

然后就可以群起 hadoop 集群了。

## 注意

这里启动时还需要在启动脚本上加上相应的配置。

### `start-dfs.sh`

需要在最上方加上对应配置：

```xml
HDFS_DATANODE_USER=root
HADOOP_SECURE_DN_USER=hdfs
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root
```

### `start-yarn.sh`

```xml
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
```

### `stop-dfs.sh`

这里和上面的 start 对应配置是相同的

```xml
HDFS_DATANODE_USER=root
HADOOP_SECURE_DN_USER=hdfs
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root
```

### `stop-yarn.sh`

```xml
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
```

还有一个要注意的，需要更新 `hadoop-env.sh` 配置

`hadoop-env.sh`

```sh
export JAVA_HOME="/root/module/jdk/tencent_jdk8"
```

这样就可以完美启动啦