---
title: Hive基础  
date: 2022-12-04 02:45:15  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hive
---
# 一、 Hive基本概念

## 1. 什么是Hive

Hive：由 Facebook 开源用于解决**海量结构化日志**的数据统计。 

Hive 是基于 Hadoop 的一个**数据仓库工具**，可以将结构化的数据文件映射为一张表，并提供**类 SQL** 查询功能。 

### hive的本质

将 HQL 转化成 MapReduce 程序

### hive的转换流程

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210309004619.png)

1）Hive 处理的数据存储在 HDFS 

2）Hive 分析数据底层的默认实现是 MapReduce 

3）执行程序运行在 Yarn 上

（类似于Hadoop的客户端，因此，hive并不是以集群形式来操作，而是可以单个节点，所有的节点都可以单独进行安装和操作）

## 2. hive的优缺点

### 优点

1) 操作接口采用类 SQL 语法，提供快速开发的能力（简单、容易上手）。 

2) 避免了去写 MapReduce，减少开发人员的学习成本。 

3) Hive 的执行延迟比较高，因此 Hive 常用于数据分析，对实时性要求不高的场合。 

4) Hive 优势在于处理大数据，对于处理小数据没有优势，因为 Hive 的执行延迟比较高。 

5) Hive 支持用户自定义函数，用户可以根据自己的需求来实现自己的函数。 

### 缺点

1．Hive的HQL表达能力有限 

（1）迭代式算法无法表达 

（2）数据挖掘方面不擅长 

2．Hive的效率比较低 

（1）Hive自动生成的MapReduce作业，通常情况下不够智能化 

（2）Hive调优比较困难，粒度较粗 

## 3. hive架构原理

### 原理图

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407134518.png" style="zoom: 33%;" />

### 介绍

#### 1．用户接口：Client 

CLI（hive shell）、JDBC/ODBC(java 访问 hive)、WEBUI（浏览器访问 hive） 

#### 2．元数据：Metastore 

元数据包括：表名、表所属的数据库（默认是 default）、表的拥有者、列/分区字段、表的类型（是否是外部表）、表的数据所在目录等； 

默认存储在自带的 derby 数据库中，推荐使用 MySQL 存储 Metastore 

#### 3．Hadoop 

使用 HDFS 进行存储，使用 MapReduce 进行计算。 

#### 4．驱动器：Driver 

（1）解析器（SQL Parser）：将 SQL 字符串转换成抽象语法树 AST，这一步一般都用 第三方工具库完成，比如 antlr；对 AST 进行语法分析，比如表是否存在、字段是否存 在、SQL 语义是否有误。 

（2）编译器（Physical Plan）：将 AST 编译生成逻辑执行计划。 

（3）优化器（Query Optimizer）：对逻辑执行计划进行优化。 

（4）执行器（Execution）：把逻辑执行计划转换成可以运行的物理计划。对于 Hive 来 说，就是 MR/Spark。 

### 运行机制

Hive 通过给用户提供的一系列交互接口，接收到用户的指令(SQL)，使用自己的 Driver， 结合元数据(MetaStore)，将这些指令翻译成 MapReduce，提交到 Hadoop 中执行，最后，将 执行返回的结果输出到用户交互接口。 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407135223.png)

## 4. hive与数据库的关系

#### 介绍

由于 Hive 采用了类似 SQL 的查询语言 HQL(Hive Query Language) ， 因此很容易将 Hive 理解为数据库。 

其实从结构上来看，Hive 和数据库除了拥有类似的查询语言，再无类似之处。 

#### hive与数据库的关系

数据库可以用在 Online 的应用中，但是 Hive 是为数据仓库而设计的，清楚这一点，有助于从应用角度理解 Hive 的特性。 

1. 查询语言 

   由于 SQL 被广泛的应用在数据仓库中，因此，专门针对 Hive 的特性设计了类 SQL 的查询语言 HQL。熟悉 SQL 开发的开发者可以很方便的使用 Hive 进行开发。 

2. 数据存储位置 

   Hive 是建立在 Hadoop 之上的，所有 Hive 的数据都是存储在 HDFS 中的。而数据库则可以将数据保存在块设备或者本地文件系统中。 

3. 数据更新 

   由于 Hive 是针对数据仓库应用设计的，而数据仓库的内容是读多写少的。因此，Hive 中不建议对数据的改写，所有的数据都是在加载的时候确定好的。而数据库中的数据通常是 需要经常进行修改的，因此可以使用INSERT INTO … VALUES添加数据，使用UPDATE … SET 修改数据。 

4. 索引 

   Hive 在加载数据的过程中不会对数据进行任何处理，甚至不会对数据进行扫描，因此也没有对数据中的某些 Key 建立索引。Hive 要访问数据中满足条件的特定值时，需要暴力扫描整个数据，因此访问延迟较高。由于 MapReduce 的引入， Hive 可以并行访问数据，因此即使没有索引，对于大数据量的访问，Hive 仍然可以体现出优势。数据库中，通常会针对一个或者几个列建立索引，因此对于少量的特定条件的数据的访问，数据库可以有很高的效率，较低的延迟。由于数据的访问延迟较高，决定了 Hive 不适合在线数据查询。 

5. 执行 

   Hive 中大多数查询的执行是通过 Hadoop 提供的 MapReduce 来实现的。而数据库通常 有自己的执行引擎。 

6. 执行延迟 

   Hive 在查询数据的时候，由于没有索引，需要扫描整个表，因此延迟较高。另外一个 导致 Hive 执行延迟高的因素是 MapReduce 框架。由于 MapReduce 本身具有较高的延迟， 因此在利用 MapReduce 执行 Hive 查询时，也会有较高的延迟。相对的，数据库的执行延迟 较低。当然，这个低是有条件的，即数据规模较小，当数据规模大到超过数据库的处理能力 的时候，Hive 的并行计算显然能体现出优势。 

7. 可扩展性 

   由于 Hive 是建立在 Hadoop 之上的，因此 Hive 的可扩展性是和 Hadoop 的可扩展性是 一致的（世界上最大的 Hadoop 集群在 Yahoo!，2009 年的规模在 4000 台节点左右）。而数据库由于 ACID 语义的严格限制，扩展行非常有限。目前最先进的并行数据库 Oracle 在理论上的扩展能力也只有 100 台左右。 

8. 数据规模 

   由于 Hive 建立在集群上并可以利用 MapReduce 进行并行计算，因此可以支持很大规模 的数据；对应的，数据库可以支持的数据规模较小。 

# 二、Hive安装

## 1. Hive安装与配置

### 1. hive配置

1. 把 apache-hive-1.2.1-bin.tar.gz 上传到 linux 的/opt/software 目录下 

2. 解压 apache-hive-1.2.1-bin.tar.gz 到/opt/module/目录下面 

   ```shell
   $ tar -zxvf apache-hive-1.2.1-bin.tar.gz -C /opt/module/ 
   ```

3. 修改 apache-hive-1.2.1-bin.tar.gz 的名称为 hive 

4. 修改/opt/module/hive/conf 目录下的 hive-env.sh.template 名称为 hive-env.sh 

   ```shell
   $ mv hive-env.sh.template hive-env.sh 
   ```

5. 配置 hive-env.sh 文件 

   a. 配置 HADOOP_HOME 路径 

   ```shell
   export HADOOP_HOME=/opt/module/hadoop-2.7.2 
   ```

   b. 配置 HIVE_CONF_DIR 路径 

   ```shell
   export HIVE_CONF_DIR=/opt/module/hive/conf 
   ```

   最后还可以配置/etc/profile文件将hive的bin加入path中。这样就可以直接使用hive指令了 

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407140008.png" style="zoom:50%;" />

### 2. Hive集群配置

（1）必须启动 hdfs 和 yarn 

```shell
$ sbin/start-dfs.sh 
$ sbin/start-yarn.sh 
```

（2）在 HDFS 上创建/tmp 和/user/hive/warehouse 两个目录并修改他们的同组权限可写 (可不操作，系统会自动创建) 

```shell
$ bin/hadoop fs -mkdir /tmp 
$ bin/hadoop fs -mkdir -p /user/hive/warehouse 
$ bin/hadoop fs -chmod g+w /tmp 
$ bin/hadoop fs -chmod g+w /user/hive/warehouse 
```

### 3. Hive基本操作

1. 启动 hive 

   ```shell
   $ bin/hive 
   ```

2. 查看数据库 

   ```shell
   hive> show databases; 
   ```

3. 打开默认数据库 

   ```shell
   hive> use default; 
   ```

4. 显示 default 数据库中的表 

    ```shell
   hive> show tables;
    ```

5. 创建一张表 

   ```shell
   hive> create table student(id int, name string); 
   ```

6. 显示数据库中有几张表 

    ```shell
   hive> show tables; 
    ```

7. 查看表的结构 

    ```shell
   hive> desc student;
    ```

8. 向表中插入数据 

    ```shell
   hive> insert into student values(1000,"ss");  
    ```

9. 查询表中数据 

    ```shell
   hive> select * from student; 
    ```

10. 退出 hive 

     ```shell
    hive> quit; 
     ```

## 2. 本地文件导入Hive

### 需求 

将本地/opt/module/data/student.txt 这个目录下的数据导入到 hive 的 student(id int, name string)表中。 

### 1. 数据准备

在/opt/module/data 这个目录下准备数据 

（1）在/opt/module/目录下创建 data 

```shell
$ mkdir data
```

（2）在/opt/module/datas/目录下创建 student.txt 文件并添加数据 

```shell
  $ touch student.txt 
  $ vi student.txt 
  1001 zhangshan 
  1002 lishi 
  1003 zhaoliu 
```

### 2. hive实际操作

1. 启动 hive
```shell
$ bin/hive
```
2. 显示数据库
```shell
hive> show databases;
```
3. 使用 default 数据库
```shell
hive> use default;
```
4. 显示 default 数据库中的表
```shell
hive> show tables;
```
5. 删除已创建的 student 表
```shell
hive> drop table student;
```
6. 创建 student 表, 并声明文件分隔符’\t’
```shell
hive> create
table
student(id int,
name
string)
ROW
FORMAT
DELIMITED FIELDS TERMINATED
BY '\t';
```
7. 加载/opt/module/data/student.txt 文件到 student 数据库表中。
```shell
hive> load data local inpath '/opt/module/data/student.txt' into table student;
```
8. Hive 查询结果
```shell
hive> select * from student;
OK
1001 zhangshan
1002 lishi
1003 zhaoliu
Time taken: 0.266 seconds, Fetched: 3 row(s)
```

 ### 3. 遇到的问题

再打开一个客户端窗口启动 hive，会产生 java.sql.SQLException 异常。 

### load的本质

load的本质其实就是put，load将本地文件或者hdfs上的文件当如对应的数据库存储位置。如果直接将其余文件直接放入该位置，那么select时也会出现其余文件的数据。 

#### 并且值得注意的是 

如果是本地操作，那么load就会将数据复制一份到对应的目录下面，如果是hdfs上的操作，那么load相当于剪切一份到对应的目录下面。 

#### 对于hdfs上面的操作 

load其实只需要更改元数据上面的内容即可，并不是真的需要将文件进行搬移，因为真实的文件都放在服务器的$HADOOP_HOME/data/tmp/......里面，只需要更改元数据上面的指向就可以将文件在hdfs的目录上进行剪切。 

## 3. mysql安装

### 1. 安装包准备

1．查看 mysql 是否安装，如果安装了，卸载 mysql 

​	a. 查看

```shell
$ rpm -qa|grep mysql 
mysql-libs-5.1.73-7.el6.x86_64 
```

​	b. 卸载 

```shell
$ rpm -e --nodeps mysql-libs-5.1.73-7.el6.x86_64 
```

2．解压 mysql-libs.zip 文件到当前目录 

```shell
[root@hadoop102 software]# unzip mysql-libs.zip 
[root@hadoop102 software]# ls 
mysql-libs.zip 
mysql-libs 
```

3．进入到 mysql-libs 文件夹下 

```shell
[root@hadoop102 mysql-libs]# ll
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407141448.png" style="zoom:50%;" />

### 2. 安装mysql服务器

1. 安装mysql服务端

   ```shell
   [root@hadoop102 mysql-libs]# rpm -ivh MySQL.xx.rpm
   ```

2. 查看产生的随机密码

   ```shell
   [root@hadoop102 mysql-libs]# cat /root/.mysql_secret
   xxxxxxx
   ```

3. 查看mysql状态

   ```shell
   [root@hadoop102 mysql-libs]# service mysql status
   ```

4. 启动mysql

   ```shell
   [root@hadoop102 mysql-libs]# service mysql start
   ```

### 3. 安装MySQL客户端

1. 安装mysql客户端

   ```shell
   [root@hadoop102 mysql-libs]# rpm -ivh mysql.xx.rpm
   ```

2. 链接mysql

   ```shell
   [root@hadoop102 mysql-libs]# mysql -uroot -pxxxxxx
   ```

3. 修改密码

   ```shell
   mysql>SET PASSWORD('000000');
   ```

4. 退出mysql

   ```shell
   mysql>exit
   ```

### 4. MySQL中user表中主机配置

#### 目的

配置只要是root用户名+密码，在任何主机上都能登录MySQL数据库

1. 进入mysql

   ```shell
   [root@hadoop102 mysql-libs]# mysql -uroot -p000000
   ```

2. 显示数据库

   ```shell
   mysql> show databases;
   ```

3. 使用mysql数据库

   ```shell
   mysql> use mysql;
   ```

4. 展示mysql表中的所有表

   ```shell
   mysql>show tables;
   ```

5. 展示user表的结构

   ```shell
   mysql> desc user;
   ```

6. 查询user表

   ```shell
   mysql> select User, Host, Password from user;
   ```

7. 修改user表，把Host表内容修改为%

   ```shell
   mysql> update user set host='%' where host='localhost'
   ```

8. 删除root用户的其他host

   ```shell
   delete from user where Host='hadoop102';
   delete from user where Host='127.0.0.1';
   delete from suer where Host='::1';
   ```

9. 刷新

   ```shell
   mysql> flush privileges
   ```

10. 退出

    ```shell
    mysql> quit;
    ```

    

## 5. Hive元数据配置到Mysql

### 1. 驱动拷贝

1. 在`/opt/software/mysql-libs`目录下解压`mysql-connector-java-5.1.27.tar.gz`驱动包

   ```shell
   [root@hadoop102 mysql-libs]# tar -zxvf mysql-xxx.tar.gz
   ```

2. 拷贝`mysql-connector-java-5.1.27-bin.jar`到`/opt/module/hive/lib`

   ```shell
   [root@hadoop102 mysql-libs]# cp /opt/software/mysql-libs/mysql-connector-xxx /opt/module/hive/lib/
   ```

### 2. 配置MetaStore到Mysql

1. 在`/opt/module/hive/conf`目录下创建一个`hive-site.xml`

   ```shell
   [root@hadoop102 mysql-libs]# touch hive-site.xml
   [root@hadoop102 mysql-libs]# vim hive-site.xml
   ```

2. 根据官方文档配置参数，拷贝数据到`hive-site.xml`

<img src=https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407143038.png height=500>

   
```xml
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>


<configuration>
        <property>
                <name>javax.jdo.option.ConnectionURL</name>
                <value>jdbc:mysql://9.135.220.213:3306/metastore?createDatabaseIfNotExist=true</value>
                <description>JDBC connect string for a JDBC metastore</description>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionDriverName</name>
                <value>com.mysql.jdbc.Driver</value>
                <description>Driver class name for a JDBC metastore</description>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionUserName</name>
                <value>root</value> 
                <description>username to use against metastore database</description>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionPassword</name>
                <value>JxZah*7428Cp</value>
                <description>password to use against metastore database</description>
        </property>
</configuration> 
```

3. 配置完毕后，如果启动hive异常，可以重新启动虚拟机。

### 3. 创建数据库以及对应目录

首先创建一个表aa

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407143151.png" style="zoom:50%;" />

可以看到，创建表aa之后，会出现在hdfs上的`/user/hive/warehouse`目录中，对应一条对应的记录。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407143220.png" style="zoom:50%;" />

并且可以看到，在mysql的metastore数据库中，查询表DBS，可以看到有一条记录，这条记录就是hive默认的default数据库，每创建一个数据库都会对应生成一条数据，如下所示： 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407143325.png)

![image-20210407143358869](/Users/coachhe/Library/Application Support/typora-user-images/image-20210407143358869.png)

## 6. Hive JDBC访问

基本用不到，在这里就不介绍了

## 7. Hive常用交互命令

### 1. 简介

有时候需要使用脚本去使用hive语句，因此需要学习交互命令。 

### 2. 从命令行执行hive

语法 

```shell
hive -e "hql" 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407143628.png" style="zoom: 33%;" />

### 3. 从脚本执行hive

语法 

```shell
hive -f filename 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407143643.png" style="zoom: 33%;" />

## 8. Hive常用属性配置

### 1. Hive数据仓库位置配置

1. Default 数据仓库的最原始位置是在 hdfs 上的：/user/hive/warehouse 路径下。  

2. 在仓库目录下，没有对默认的数据库 default 创建文件夹。如果某张表属于 default 数据库，直接在数据仓库目录下创建一个文件夹。

   <img src="/Users/coachhe/Library/Application Support/typora-user-images/image-20210407144028852.png" alt="image-20210407144028852" style="zoom: 50%;" />

3. 修改 default 数据仓库原始位置（将 hive-default.xml.template 如下配置信息拷贝到 hive-site.xml 文件中）。 

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407144103.png" style="zoom:50%;" />

4. 配置同组用户有执行权限 

    ```shell
   bin/hdfs dfs -chmod g+w /user/hive/warehouse 
    ```

### 2. 查询后信息显示配置

1. 在 hive-site.xml 文件中添加如下配置信息，就可以实现显示当前数据库，以及查询表的头信息配置。 

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407144220.png" style="zoom:50%;" />

2. 重启hive，看情况

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407144250.png" style="zoom:50%;" />

### 3. Hive运行日志信息配置

1. Hive 的 log 默认存放在/tmp/coachhe/hive.log 目录下（当前用户名下）

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407144650.png" style="zoom:50%;" />

2. 修改hive 的 log 存放日志到/opt/module/hive/logs 

   （1）修改`/opt/module/hive/conf/hive-log4j.properties.template `文件名称为 `hive-log4j.properties `

   （2）在` hive-log4j.properties `文件中修改 log 存放位置 `hive.log.dir=/opt/module/hive/logs `

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407144743.png" style="zoom:50%;" />

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407144755.png" style="zoom:50%;" />

### 4. 参数配置方式

1．查看当前所有的配置信息 

```shell
hive>set; 
```

2．参数的配置三种方式 

（1）配置文件方式 

默认配置文件：`hive-default.xml `

用户自定义配置文件：`hive-site.xml `

注意： 

用户自定义配置会覆盖默认配置。另外，Hive 也会读入 Hadoop 的配置，因 为 Hive 是作为 Hadoop 的客户端启动的，Hive 的配置会覆盖 Hadoop 的配置。配置文件 的设定对本机启动的所有 Hive 进程都有效。 

（2）命令行参数方式 启动 Hive 时，可以在命令行添加-hiveconf param=value 来设定参数。 

例如： 

```shell
$ bin/hive -hiveconf mapred.reduce.tasks=10; 
```

注意：仅对本次 hive 启动有效 查看参数设置： 

```shell
hive (default)> set mapred.reduce.tasks; 
```

（3）参数声明方式 

可以在 HQL 中使用 SET 关键字设定参数 

例如： 

```shell
hive (default)> set mapred.reduce.tasks=100; 
```

注意：仅对本次 hive 启动有效。 

查看参数设置 

```shell
hive (default)> set mapred.reduce.tasks; 
```

上述三种设定方式的优先级依次递增。即配置文件<命令行参数<参数声明。注意某些 系统级的参数，例如 log4j 相关的设定，必须用前两种方式设定，因为那些参数的读取在会 话建立以前已经完成了。 

# 三、Hive数据类型

## 1. 基本数据类型

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407145132.png" style="zoom:50%;" />

对于 Hive 的 String 类型相当于数据库的 varchar 类型，该类型是一个可变的字符串，不过它不能声明其中最多能存储多少个字符，理论上它可以存储 2GB 的字符数。

## 2. 集合数据类型

### 数据类型

Hive有三种复杂数据类型 ARRAY、MAP 和 STRUCT。ARRAY 和 MAP 与 Java 中的 Array 和 Map 类似，而 STRUCT 与 C 语言中的 Struct 类似，它封装了一个命名字段集合， 复杂数据类型允许任意层次的嵌套。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407145236.png" style="zoom:50%;" />

## 3. 类型转换

### 隐式类型转换

（1）任何整数类型都可以隐式地转换为一个范围更广的类型，如 TINYINT 可以转换成 INT，INT 可以转换成 BIGINT。 

（2）所有整数类型、FLOAT 和 STRING 类型都可以隐式地转换成 DOUBLE。 

（3）TINYINT、SMALLINT、INT 都可以转换为 FLOAT。 

（4）BOOLEAN 类型不可以转换为任何其它的类型。 

### 用CAST操作显示进行数据类型转换

例如 CAST('1' AS INT)将把字符串'1' 转换成整数 1；如果强制类型转换失败，如执行 CAST('X' AS INT)，表达式返回空值 NULL。 

# 四、HiveDDL

## 1. DDL和DML定义

### DDL 

（baiData Definition Language 数据定义语言） 

数据定义语言 

#### 作用 

用于定义和管理 SQL 数据库中的所有对象的语言 

#### 语法 

```shell
create table 创建表      
alter table  修改表    
drop table 删除表    
truncate table 删除表中所有行dao      
create index 创建索引    
drop index  删除索引 
```

#### 特点 

1. 当执行DDL语句时，在每一条语句前后，oracle都将提交当前的事务。 
2. 如果用户使用insert命令将记录插入到数据库后，执行了一条DDL语句(如create table)，此时来自insert命令的数据将被提交到数据库。 
3. 当DDL语句执行完成时，DDL语句会被自动提交，不能回滚。  

### DML 

（Data Manipulation Language 数据操作语言）  

数据操作语言 

#### 作用 

用来直接对数据进行操作 

#### 语法 

```shell
insert 将记录插入到数据库  
update 修改数据库的记录  
delete 删除数据库的记录 
```

#### 特点 

1. 当执行DML命令如果没有提交，将不会被其他会话看到。 
2. 除非在DML命令之后执行了DDL命令或DCL命令，或用户退出会话，或终止实例，此时系统会自动发出commit命令，使未提交的DML命令提交。 

### 总结

DML是SELECT、UPDATE、INSERT、DELETE，就象它的名字一样，这4条命令是用来对数据库里的数据进行操作的语言。  

DDL比DML要多，主要的命令有CREATE、ALTER、DROP等，DDL主要是用在定义或改变表（TABLE）的结构，数据类型，表之间的链接和约束等初始化工作上，他们大多在建立表时使用。 

## 2. 创建数据库

1）创建一个数据库，数据库在 HDFS 上的默认存储路径是/user/hive/warehouse/*.db。  

```shell
hive (default)> create database db_hive;  
```

2）避免要创建的数据库已经存在错误，增加 if not exists 判断。（标准写法）  

```shell
hive (default)> create database db_hive;  
FAILED: Execution Error, return code 1 from org.apache.hadoop.hive.ql.exec.DDLTask. Database db_hive already exists 
hive (default)> create database if not exisis db_hive;  
```

3）创建一个数据库，指定数据库在 HDFS 上存放的位置 

```shell
hive (default)> create database db_hive2 location '/db_hive2.db'; 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407212512.png)

## 3. 查询数据库

### 1. 显示数据库

#### 语法： 

```shell
hive> show databases; 
```

### 2. 过滤显示查询的数据库

语法 

```shell
hive> show databases like 'db_hive*'; 
OK 
db_hive 
db_hive_1 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407212707.png" style="zoom:50%;" />

### 3. 查看数据库详情

#### 1．显示数据库信息  

```shell
hive> desc database db_hive; 
OK
db_hive 
hdfs://hadoop102:9000/user/hive/warehouse/db_hive.db 
atguiguUSER  
```

#### 2．显示数据库详细信息，extended 

```shell
hive> desc database extended db_hive; 
OK 
db_hive 
hdfs://hadoop102:9000/user/hive/warehouse/db_hive.db atguiguUSER 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407212843.png" style="zoom:50%;" />

#### 注意： 

在这里用两种查询方式没有区别，因为我们没有经过相应的操作，如果修改数据库之后再进行操作就会有区别了。在后面的修改数据库会给出。 

### 4. 切断当前数据库

#### 语法 

```
hive (default)> use db_hive; 
```

## 4. 修改数据库

用户可以使用 ALTER DATABASE 命令为某个数据库的 DBPROPERTIES 设置键-值对 属性值，来描述这个数据库的属性信息。数据库的其他元数据信息都是不可更改的，包括数据库名和数据库所在的目录位置。 

```shell
hive (default)> alter database hive set dbproperties('createtime'='20170830'); 
```

在 hive 中查看修改结果 

```shell
hive> desc database extended db_hive; 
db_name comment location owner_name owner_type parameters db_hive hdfs://hadoop102:8020/user/hive/warehouse/db_hive.db atguigu USER {createtime=20170830} 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407213026.png" style="zoom:50%;" />

## 5. 删除数据库

### 1. 删除空数据库

```shell
hive>drop database db_hive2; 
```

#### 注意 

这里只能删除空的数据库，不能删除有内容的数据库。 

### 2. 如果删除的数据库不存在

最好采用 if exists 判断数据库是否存在 

```shell
hive> drop database db_hive; 
FAILED: SemanticException [Error 10072]: Database does not exist: db_hive 
hive> drop database if exists db_hive2; 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407213204.png" style="zoom:50%;" />

### 3. 如果删除的数据库不为空（不推荐）

可以采用 cascade 命令，强制删除 

```shell
hive> drop database db_hive; 
FAILED: Execution Error, return code 1 from org.apache.hadoop.hive.ql.exec.DDLTask. InvalidOperationException(message:Database db_hive is not empty. One or more tables exist.) 
hive> drop database db_hive cascade; 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407213251.png" style="zoom:50%;" />

## 6. 创建表

### 1. 简介

#### 建表语法

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210407213353.png" style="zoom:50%;" />

[]表示可选字段。 

解释 

1. `CREATE TABLE`

   创建一个指定名字的表。如果相同名字的表已经存在，则抛出异常；用户可以用 IF NOT EXISTS 选项来忽略这个异常。 

2. `EXTERNAL 关键字 `

   可以让用户创建一个外部表，在建表的同时指定一个指向实际 数据的路径（LOCATION），Hive 创建内部表时，会将数据移动到数据仓库指向的路 径；若创建外部表，仅记录数据所在的路径，不对数据的位置做任何改变。在删除表的 时候，内部表的元数据和数据会被一起删除，而外部表只删除元数据，不删除数据。 

3. `COMMENT`： 

   为表和列添加注释。 

4. `PARTITIONED BY `

   创建分区表 

5. `CLUSTERED BY `

   创建分桶表 

6. `SORTED BY `

   不常用 

7. `ROW FORMAT` 

   默认为SerDe，是Serialize/Deserilize的简称，目的是用于序列化和反序列化。 

8. `STORED AS `

   指定存储文件类型 常用的存储文件类型：SEQUENCEFILE（二进制序列文件）、TEXTFILE（文本）、 RCFILE（列式存储格式文件） 如果文件数据是纯文本，可以使用 STORED AS TEXTFILE。如果数据需要压缩， 使用 STORED AS SEQUENCEFILE。 

9. `LOCATION `

   指定表在 HDFS 上的存储位置。 

10. `LIKE `

    允许用户复制现有的表结构，但是不复制数据。 

#### 查看完整建表语句

```shell
hive (default) > show create table table_name; 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408092400.png" style="zoom:50%;" />

### 2. 管理表（内部表）

#### 理论 

默认创建的表都是所谓的管理表，有时也被称为内部表。因为这种表，Hive会（或多或少地）控制着数据的生命周期。 

Hive 默认情况下会将这些表的数据存储 在由配置项 `hive.metastore.warehouse.dir`(例如，`/user/hive/warehouse`)所定义的目录的子目录下。 当我们删除一个管理表时，Hive 也会删除这个表中数据。管理表不适合和其他工具共享数据。 

### 3. 外部表

假设我们正在分析来自股票市场的数据。我们会定期从像Infochimps(http://infochimps.com/datasets)这样的数据源接入关于NASDAQ和NYSE的数据，然后使用很多工具来分析这份数据。 

我们假设这些数据文件位于分布式文件系统的`/data/stocks`目录下。

下面的语句将创建一个外部表，其可以读取所有位于`/data/stocks`目录下的以逗号分隔的数据：

```sql
CREATE EXTERNAL TABLE IF NOT EXISTS stocks (
	exchange STRING,
	symbol   STRING,
  ...
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION '/data/stocks';
```

关键字EXTERNAL告诉你这个表是外部的，而后面的LOCATION字句则用于告诉你Hive数据库位于哪个路径下。

因为表示外部的，所以Hive并非认为其完全拥有这份数据。因此，删除该表并不会删除这份数据，不过描述表的元数据信息则会被完全删除。

### 4. 内外部表的关系

#### 内外部表的相互转换

1. 查询表的类型

   ```shell
   hive(default) > desc formatted student2;
   Table Type:     MANAGED_TABLE
   ```

2. 修改内部表student2为外部表

   ```shell
   alter table student2 set tdlproperties('EXTERNAL'='TRUE');
   ```

3. 查询表的类型

   ```shell
   hive(default) > desc formatted student2;
   Table Type:     EXTERNAL_TABLE
   ```

4. 修改外部表student2为内部表

   ```shell
   alter table student2 set tdlproperties('EXTERNAL'='FALSE');
   ```

5. 查询表的类型

   ```shell
   hive(default) > desc formatted student2;
   Table Type:     MANAGED_TABLE
   ```

#### 内外部表的核心区别

对于内部表和外部表，核心区别在于删除时是否会把实际数据一起删除。 

对于内部表，认为数据是自己的，删除时一起删除了：

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408094702.png" style="zoom:50%;" />

对于外部表，认为数据不是自己的，不会一起删除。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408094745.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408094802.png" style="zoom:50%;" />

##### 总结 

也就是说，外部表删除时，只会删除元数据（mysql中的数据），而不会去删除实际数据。 

#### 内部表和外部表的使用 

在实际公司环境中，内部表一般作为中间表，因为删除方便。 

外部表作为最终的结果表，因为安全。 

## 7. 分区表

### 分区表定义

分区表实际上就是对应一个 HDFS 文件系统上的独立的文件夹，该文件夹下是该分区所有的数据文件。 

#### 理解 

在当前，每个表对应的就是一个文件夹，所有数据都放在里面，若加上了分区，那么在这个文件夹里面就会有很多子文件，每个分区对应一个文件夹。 

Hive 中的分区就是**分目录**，把一个大的数据集根据业务需要分割成小的数据集。 

在查询时通过 WHERE 子句中的表达式选择查询所需要的指定的分区，这样的查询效率会提高很多。 

### 分区表基本操作

#### 1. 创建分区表语法、加载数据

1. 引入分区表

   ```shell
   /usr/hive/warehouse/log_partition/20170702/20170702.log
   /usr/hive/warehouse/log_partition/20170703/20170703.log
   /usr/hive/warehouse/log_partition/20170704/20170704.log
   ```

2. 创建分区表语法

   ```shell
   hive(default) > create table dept_partition(
   	deptno int,
   	dname string,
   	loc string
   )
   partitioned by (month string)
   row format delimited fields terminated by '\t';
   ```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408095351.png)

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408095412.png" style="zoom:50%;" />

#### 2. 增加分区

1. 创建单个分区

   ```shell
   hive(default) > alter table dept_partition add partition(month='20210706');
   ```

2. 同时创建多个分区

   ```shell
   hive(default) > alter table dept_partition add partition(month='20210706') partition(month='20210707');
   ```

#### 3. 删除分区

删除分区不同于增加分区，增加多个分区用空格隔开，而删除多个分区用","隔开。 

```shell
hive(default) > alter table dept_partition drop partition(month='20210706'), partition(month='20210707');
```

#### 4. 查看分区表有多少分区

```shell
hive>show partitions dept_partition;
```

#### 5. 查看分区表结构

```shell
hive> desc formatted dept_partition;

# Partition Information
# col_name data_type comment month string
```

### 分区表注意事项

#### 1. 创建二级分区表

```shell
hive> create table dept_partition2(
		>   deptno int, dname string, loc string
		> )
		> partitioned by (mon string, day string)
		> row format delimited fields terminated by '\t';
```

#### 2. 到二级分区加载和查询数据

1. 加载数据到二级分区表中

   ```shell
   hive (default) > load data local inpath '/opt/module/datas/dept.txt' into table default.dept_partition2 partition(month='201709',day='13');
   ```

2. 查询分区数据

   ```shell
   hive (default) > select * from dept_partition2 where month='201709' and day='13';
   ```

会在大目录下创建一个二级目录

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408100617.png" style="zoom:50%;" />

### 把数据直接上传到分区目录上，让分区表和数据产生关联的三种方式 

#### 解释

如果我们直接往hdfs目录下上传文件，那么是查不出来的。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408100732.png" style="zoom: 50%;" />

#### 原因 

没有元数据！mysql没有感知到这个数据。 

#### 解决办法 

##### 方式1

执行修复命令 

```shell
msck repair table dept_partition2; 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210408100826.png" style="zoom:50%;" />

##### 方式2：上传数据

1. 上传数据

   ```shell
   hive(default) > dfs mkdir -p /user/hive/warehouse/dept_partition2/month=201709/day=11;
   hive(default) > dfs -put /opt/module/datas/dept.txt /user/hive/warehouse/dept_partition2/month=201709/day=11;
   ```

2. 执行添加分区

   ```shell
   hive(default) > alter table dept_partition2 add partition(month='201709', day='11');
   ```

3. 查询数据

   ```shell
   hive(default > select * from dept_partition2 where month = '201709' and day = '11';
   ```

##### 方式3：创建文件夹后load数据到分区

1. 创建目录

   ```shell
   hive(default) > dfs -mkdir -p /user/hive/warehouse/dept_partition2/month=201709/day=11
   ```

2. 上传数据

   ```shell
   hive(default) > load data local inpath '/opt/module/datas/dept.txt' into table dept_partition2 partition(month='201709', day='11');
   ```

3. 查询数据

   ```shell
   hive(default > select * from dept_partition2 where month = '201709' and day = '11';
   ```

## 8. 修改表

### 1. 重命名表

#### 语法

```shell
ALTER TABLE table_name RENAME TO new_table_name
```

#### 实操案例

```shell
hive(default) > alter table dept_partition2 rename to dept_partition3
```

### 2. 增加/修改/替换列信息

#### 语法

##### 更新列

```shell
ALTER TABLE table_name CHANGE [COLUMN] col_old_name col_new_name column_type [COMMENT col_comment] [FIRST|AFTER column_name]
```

##### 增加和替换列

```shell
ALTER TABLE table_name ADD|REPLACE COLUMNS (col_name data_type [COMMENT col_comment], ...)
```

### 











