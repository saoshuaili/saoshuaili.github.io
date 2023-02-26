---
title: HDFS3 客户端操作  
date: 2022-12-04 02:44:51  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 2 HDFS介绍
---
# 1 客户端环境准备

## 注意

若是windows电脑则需要配置HADOOP_HOME（其实感觉也不需要，只要导入maven依赖就行了）。

## 步骤

### 1. 打开IDEA建立MAVEN项目
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160835.png" style="zoom:50%;" />

### 2. 配置pom.xml  
pom.xml的配置信息如下：

```xml
		<dependencies>
				<dependency>
					<groupId>junit</groupId>
					<artifactId>junit</artifactId>
					<version>RELEASE</version>
				</dependency>
				<dependency>
					<groupId>org.apache.logging.log4j</groupId>
					<artifactId>log4j-core</artifactId>
			<version>2.8.2</version>
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
					<groupId>jdk.tools</groupId>
					<artifactId>jdk.tools</artifactId>
					<version>1.8</version>
					<scope>system</scope>
					<systemPath>${JAVA_HOME}/lib/tools.jar</systemPath>
				</dependency>
		</dependencies>
```

#### 注意  

最后的jdk可以直接删掉，直接在IDEA里面配置即可。
将其配置好后，采用阿里云镜像，可以快速下载好所需要的junit和hadoop等包。

### 3. 打印日志配置

注意：如果Eclipse/Idea打印不出日志，在控制台上只显示

```
		1.log4j:WARN No appenders could be found for logger (org.apache.hadoop.util.Shell).  
		2.log4j:WARN Please initialize the log4j system properly.  
		3.log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
```

需要在项目的src/main/resources目录下，新建一个文件，命名为“log4j.properties”，在文件中填入

```
		log4j.rootLogger=INFO, stdout
		log4j.appender.stdout=org.apache.log4j.ConsoleAppender
		log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
		log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m%n
		log4j.appender.logfile=org.apache.log4j.FileAppender
		log4j.appender.logfile.File=target/spring.log
		log4j.appender.logfile.layout=org.apache.log4j.PatternLayout
		log4j.appender.logfile.layout.ConversionPattern=%d %p [%c] - %m%n
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160930.png)

### 4. 创建包名com.coachhe.hdfs

### 5. 创建类HdfsClient

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160940.png" style="zoom:80%;" />

## 6. 执行程序

运行时需要进行用户配置:  

### eclipse：

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160807.png)

### IDEA：

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210160956.png" style="zoom:80%;" />		



# 2 HDFS的API操作

## 测试参数优先级

### 配置

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174031.png" style="zoom:67%;" />

### 代码

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174102.png" style="zoom:67%;" />

## 解释

在这里将本机文件上传到了hdfs上，并且会发现副本只有一个，因为这里的优先级更高。

## 总结

参数优先级排序：（1）客户端代码中设置的值 >（2）ClassPath下的用户自定义配置文件 >（3）然后是服务器的默认配置

## HDFS文件下载

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174210.png" style="zoom:67%;" />

## HDFS文件夹删除

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174229.png" style="zoom:67%;" />

## HDFS文件名修改

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174245.png" style="zoom:67%;" />

## HDFS文件和文件夹判断

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174257.png" style="zoom:67%;" />



# HDFS的IO流操作

## 简介

上面我们学的API操作HDFS系统都是框架封装好的。那么如果我们想自己实现上述API的操作该怎么实现呢？  
我们可以采用IO流的方式实现数据的上传和下载。

## HDFS文件上传

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174345.png" style="zoom:67%;" />

### HDFS文件下载

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210174357.png" style="zoom:67%;" />