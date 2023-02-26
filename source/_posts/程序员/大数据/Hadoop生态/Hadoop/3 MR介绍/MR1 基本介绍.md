---
title: MR1 基本介绍  
date: 2022-12-04 02:45:20  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 3 MR介绍
---
# 1 概述

## 定义

MapReduce是一个==分布式运算程序的编程框架==。是用户开发“基于Hadoop的数据分析应用”的核心框架。

## MR核心功能

MapReduce==核心功能==是将==用户编写的业务逻辑代码==和==自带默认组件==整合成一个==完整的分布式运算程序==，并发运行在一个Hadoop集群上。

# 2 MR优缺点

## 优点

### 1. MR易于编程

它简单的实现一些接口，就可以完成一个分布式程序。

### 2. 良好的拓展性

当你的计算资源不能得到满足的时候，可以通过简单的增加机器来扩展它的计算能力。

### 3. 高容错性

若其中一台机器挂了，它可以把上面的计算任务转移到另外一个节点上运行，不至于这个任务运行失败，而且这个过程不需要人工参加，而完全是由Hadoop内部完成的。

### 4. 适合PB级以上海量数据的离线计算

可以实现上千台服务器集群并发处理，提供数据处理能力。

## 缺点

### 1. 不擅长实时计算

MR无法像MySQL一样，在毫秒或者秒级内返回结果

### 2. 不擅长流式计算。

流式计算的输入数据是动态的，而MR的输入数据集是静态的，不能动态变化。

### 3. 不擅长DAG（有向图）计算

也就是不能让多个应用程序存在依赖关系，后一个应用程序的输入为前一个的输出。在这种情况下，其实MR并不是不能做，而是使用后，每个MR的作业的输出结果都会写入到磁盘，会造成大量的磁盘IO，导致性能非常低下。



# 3 MR核心思想

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201210235945.png" style="zoom: 25%;" />

1）分布式的运算程序往往需要分成至少2个阶段。 
2）第一个阶段的MapTask并发实例，完全并行运行，互不相干。  
3）第二个阶段的ReduceTask并发实例互不相干，但是他们的数据依赖于上一个阶段的所有MapTask并发实例的输出。   
4）MapReduce编程模型只能包含一个Map阶段和一个Reduce阶段，如果用户的业务逻辑非常复杂，那就只能多个MapReduce程序，串行运行。 

## 总结

分析WordCount数据流走向深入理解MapReduce核心思想。 

# 4 MR进程

## 一个完整的MapReduce程序在分布式运行时有三类实例进程

### 1. MrAppMaster

负责整个程序的过程调度及状态协调

### 2. MapTask

负责Map阶段的整个阶段处理流程

### 3. ReduceTask

负责Reduce阶段的整个数据处理流程

# 5 官方WordCount源码

```java

package com.coachhe.hdfs; 
  
import java.io.IOException; 
import java.util.StringTokenizer; 
  
import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapreduce.Job; 
import org.apache.hadoop.mapreduce.Mapper; 
import org.apache.hadoop.mapreduce.Reducer; 
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
import org.apache.hadoop.util.GenericOptionsParser; 
  
public class wordcount { 
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {   //继承泛型类Mapper 
        private final static IntWritable one = new IntWritable(1);  //定义hadoop数据类型IntWritable实例one，并且赋值为1 
        private Text word = new Text();                                    //定义hadoop数据类型Text实例word 
  
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException { //实现map函数 
            StringTokenizer itr = new StringTokenizer(value.toString());//Java的字符串分解类，默认分隔符“空格”、“制表符(‘\t’)”、“换行符(‘\n’)”、“回车符(‘\r’)” 
  
            while (itr.hasMoreTokens()) {  //循环条件表示返回是否还有分隔符。 
                word.set(itr.nextToken());   // nextToken()：返回从当前位置到下一个分隔符的字符串，word.set()：Java数据类型与hadoop数据类型转换 
                context.write(word, one);   //hadoop全局类context输出函数write; 
            } 
        } 
  
    } 
  
    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {    //继承泛型类Reducer 
        private IntWritable result = new IntWritable();   //实例化IntWritable 
  
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {  //实现reduce 
            int sum = 0; 
            for (IntWritable val : values)    //循环values，并记录单词个数 
                sum += val.get(); 
            result.set(sum);   //Java数据类型sum，转换为hadoop数据类型result 
            context.write(key, result);   //输出结果到hdfs 
        } 
    } 
  
    public static void main(String[] args) throws Exception { 
        Configuration conf = new Configuration();   //实例化Configuration 
/*********** 
GenericOptionsParser是hadoop框架中解析命令行参数的基本类。 getRemainingArgs();返回数组 
【一组路径】 
**********/ 
//函数实现 
        /** 
         public String[] getRemainingArgs() { 
         return (commandLine == null) ? new String[]{} : commandLine.getArgs(); 
         } 
         **/ 
//总结上面：返回数组【一组路径】 
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs(); 
  
//如果只有一个路径，则输出需要有输入路径和输出路径 
        if (otherArgs.length < 2) { 
            System.err.println("Usage: wordcount <in> [<in>...] <out>"); 
            System.exit(2); 
        } 
  
        Job job = Job.getInstance(conf, "word count");   //实例化job 
        job.setJarByClass(wordcount.class);   //为了能够找到wordcount这个类 
        job.setMapperClass(TokenizerMapper.class);   //指定map类型 
/******** 
指定CombinerClass类 
这里很多人对CombinerClass不理解 
************/ 
        job.setCombinerClass(IntSumReducer.class); 
        job.setReducerClass(IntSumReducer.class);  //指定reduce类 
        job.setOutputKeyClass(Text.class); //rduce输出Key的类型，是Text 
        job.setOutputValueClass(IntWritable.class);  // rduce输出Value的类型 
  
        for (int i = 0; i < otherArgs.length - 1; ++i) 
            FileInputFormat.addInputPath(job, new Path(String.valueOf(otherArgs)));  //添加输入路径 
  
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));   //添加输出路径 
        System.exit(job.waitForCompletion(true) ? 0 : 1);  //提交job 
    } 
} 
```

# 6 常用数据序列化类型

| Java类型 | Hadoop Writable类型 |
| -------- | ------------------- |
| boolean  | BooleanWritable     |
| byte     | ByteWritable        |
| int      | IntWritable         |
| float    | FloatWritable       |
| long     | LongWritable        |
| double   | DoubleWritable      |
| String   | Text                |
| map      | MapWritable         |
| array    | ArrayWritable       |



# 7 MR编程规范

## 用户编写的程序分为三个部分 

1. Mapper   
2. Reducer 
3. Driver 

## Mapper阶段

1. 用户自定义的Mapper要继承自己的父类 
2. Mapper的输入数据是KV对的形式 
3. Mapper中的业务逻辑写在map()方法里面 
4. Mapper的输出数据是KV对的形式 
5. map()方法（MapTask进程）对每个KV对调用一次 

## Reducer阶段

1. 用户自定义的Reducer要继承自己的父类*
2. Reducer的输入类型对应Mapper的输出类型，也是KV 
3. Reducer的业务逻辑写在reduce()方法中 
4. ReduceTask进程对每一组相同k的<k,v>组调用一次reduce()方法 

## Driver阶段

相当于YARN集群的客户端，用于提交我们整个程序到YARN集群，提交的是封装了MapReduce程序相关参数的job对象。 

# WordCount案例实操

## 需求分析

在给定的文本文件中统计输出每一个单词出现的次数。 

### 输入数据 

wc_shicao   
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211002920.png" style="zoom: 33%;" />

### 期望输出数据

coachhe 4 
hello 1 
Hello 2 
I 1 
love 1 
playing 1 
tennis 1 
Java 1 
Hadoop 1 
Thank 1 
you 1 
atguigu 1 

### 需求分析 

需要分别设计Mapper Reducer和Driver。 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003020.png)

## 创建maven工程

# 创建maven工程

 <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003121.png" style="zoom:25%;" />
其中依赖和之前的相同。 

## WordCountMapper

### 创建

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003152.png" style="zoom:25%;" />

### 对Mapper类的源码进行分析

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003239.png" style="zoom:25%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003319.png" style="zoom:25%;" />

### 具体写WordCountMapper类

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003436.png" style="zoom:25%;" />

#### 代码

```java
// map阶段 
// KEYIN 第一个参数，为输入数据的key 
// VALUEIN 第二个参数，为输入数据的value 
// KEYOUT 第三个参数，为输出数据的key的类型， coachhe 3，在这里是coachhe，所以是Text 
// VALUEOUT 第四个参数，为输出数据的value，根据上面，是3，所以是LongWritable 
public class WordcountMapper extends Mapper<LongWritable, Text, Text, LongWritable> { 
  
    Text k = new Text(); 
    LongWritable v = new LongWritable(); 
  
  
    @Override 
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException { 
        // coachhe coachhe  输入数据 
  
        // 1 获取一行 
        String line = value.toString(); 
  
        // 2 切割单词 
        String[] words = line.split(" "); 
  
        // 3 循环写出 
        for (String word : words) { 
            k.set(word); 
            v.set(1); 
            context.write(k, v); 
        } 
  
    } 
} 
```

## WordcountReducer

### 创建WordcountReducer

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003647.png" style="zoom:25%;" />

### 对Reducer类的源码分析

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003752.png" style="zoom:25%;" />

### 具体写WordcountReducer

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003901.png" style="zoom:25%;" />

## Driver类

### 固定格式

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211003945.png" style="zoom:25%;" />

### 最终代码

```java
package com.coachhe.wordcount; 
  
import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapreduce.Job; 
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
  
import java.io.IOException; 
  
public class WordCountDriver { 
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException { 
  
        Configuration conf = new Configuration(); 
        // 1 获取Job对象 
        Job job = Job.getInstance(conf); 
  
        // 2 设置jar存储位置 
        job.setJarByClass(WordCountDriver.class); 
  
        // 3 关联Map和Reduce类 
        job.setMapperClass(WordcountMapper.class); 
        job.setReducerClass(WordcountReducer.class); 
  
        // 4 设置Mapper阶段输出数据的key-value类型 
        job.setMapOutputKeyClass(Text.class); 
        job.setMapOutputValueClass(LongWritable.class); 
  
        // 5 设置最终数据输出的key和value类型 
        job.setOutputKeyClass(String.class); 
        job.setOutputValueClass(Long.class); 
  
        // 6 设置程序运行的输入路径和输出路径 
        FileInputFormat.setInputPaths(job, new Path(args[0])); 
        FileOutputFormat.setOutputPath(job, new Path(args[1])); 
  
        // 7 提交job 
        boolean result = job.waitForCompletion(true);//提交完成之后会打印信息,若设置为false则不会打印信息 
  
        System.exit(result ? 0 : 1); 
    } 
} 
```

## IDEA参数设置

因为我们在上面的输入路径为args[0]，输出路径为args[1]。因此需要在IDEA上设置参数： 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211004148.png" style="zoom:25%;" />
以空格分开 

## 实例 

### 现在在我的/路径（注意：这里是集群的地址，因为我现在是完全分布式）下有一个wc_shicao文件。   

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211004351.png" style="zoom:25%;" />  

### 现在我想把他进行wordcount之后结果输出到/wc_shicao_output中。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211004425.png" style="zoom:25%;" />

### 结果

<img src="https://pic.downk.cc/item/5fc785ca394ac52378b5765d.png" style="zoom:25%;" />

