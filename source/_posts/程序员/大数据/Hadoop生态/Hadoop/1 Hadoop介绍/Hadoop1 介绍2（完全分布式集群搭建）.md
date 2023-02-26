---
title: Hadoop1 介绍2（完全分布式集群搭建）  
date: 2022-12-04 02:44:30  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 1 Hadoop介绍
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

### 三种用法： 

1. 本机数据拷贝到别的服务器 
2. 从别的服务器将数据拷贝到本机 
3. 将某台非本机的服务器的数据拷贝到另一台主机 

### 案例1 

在hadoop100上，将hadoop100中/home/hadoop_learning目录下的软件拷贝到hadoop102上。 

```shell
scp -r hadoop_learning/ root@hadoop101:/home/hadoop_learning 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194735.png" style="zoom: 67%;" />

### 结果 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194820.png" style="zoom:80%;" />

### 注意

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194842.png" style="zoom:80%;" />

### 案例2

在hadoop102上从hadoop100处拉取/home/hadoop_learning下的数据 

```shell
sudo scp -r coachhe@hadoop100:/home/hadoop_learning /home/hadoop_learning 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210194924.png" style="zoom:67%;" />

## 结果 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210154825.png" style="zoom:80%;" />

## 注意

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210154916.png" style="zoom:80%;" />


# 案例3

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

### 题目

将hadoop100机器上的/home/newdisk目录同步到hadoop101服务器root目录下的/home/newdisk下。 

### 解答

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

## 集群配置

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

1. 格式化namenode  
   注意：格式化之前需要删除logs和data 
2. 启动所有节点的namenode和datanode  
   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155338.png)
   注意：  
   hadoop101和hadoop102的Namenode不用启动   
   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155347.png)    
   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210155400.png)

## 3 SSH无密登录配置

### 思考

如果节点增加到1000个，那么不可能给一个个启动，怎么办呢？ 

### 介绍

ssh可以直接访问其他服务器 

### 免密登录原理

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210201558.png" style="zoom:50%;" />

 ### 生成公钥和私钥

在.ssh目录下执行 

```
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

|      | hadoop100              | hadoop101                        | hadoop102                       |
| ---- | ---------------------- | -------------------------------- | ------------------------------- |
| HDFS | NameNode <br> DataNode | DataNode                         | SecondaryNameNode <br> DataNode |
| YARN | NodeManager            | ResourceManager <br> NodeManager | NodeManager                     |



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