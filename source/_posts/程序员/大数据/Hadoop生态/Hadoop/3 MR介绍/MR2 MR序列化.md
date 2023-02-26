---
title: MR2 MR序列化  
date: 2022-12-04 02:45:24  
tags: []  
categories:
  - 大数据
  - Hadoop生态
  - Hadoop
  - 3 MR介绍
---
# 一 序列化基本介绍

## 1 概念

### 序列化 

序列化就是把内存中的对象，转换成字节序列（或其他数据传输协议）以便存储到硬盘（持久化）或者网络传输。 

### 反序列化 

将受到的字节序列（或者其他数据传输协议）或者是磁盘的持久化序列，转换成内存中的对象 

## 序列化的原因

一般来说，“活的”对象只生存在内存里，关机断电就没有了。而且“活的”对象只能有本地进程使用，不能被发送到网络上的另一台计算机。然而序列化可以存储“活的”对象，可以将“活的”对象发送到远程计算机。 

## 为什么不用java的序列化

Java序列化是一个重量级序列化框架，一个对象被序列化之后，会附带很多额外信息（各种校验信息，Header，继承体系等），不便于在网络中高效传输。所以Hadoop开发了自己的一套序列化机制（Writable） 

## Hadoop序列化特点

1. 高效实用存储空间
2. 快速：读写数据的额外开销小 
3. 可拓展：随着协议的升级可以升级 
4. 互操作：支持多语言交互 

# 二 自定义Bean对象实现序列化

在企业开发中往往常用的基本序列化类型不能满足所有需求，比如在Hadoop框架内部传递一个bean对象，那么该对象就需要实现序列化接口。  

## 具体实现bean对象序列化步骤如下7步  

1. 必须实现Writable接口  
2. 反序列化时，需要反射调用空参构造函数，所以必须有空参构造  

```java
public FlowBean() {
    super();
}
```

3. 重写序列化方法  

```java
@Override
public void write(DataOutput out) throws IOException {
    out.writeLong(upFlow);  
    out.writeLong(downFlow);  
    out.writeLong(sumFlow);  
}  
```

4. 重写反序列化方法  

```java
@Override  
public void readFields(DataInput in) throws IOException {  
    upFlow = in.readLong();  
    downFlow = in.readLong();  
    sumFlow = in.readLong();  
}  
```

5. 注意反序列化的顺序和序列化的顺序完全一致  
6. 要想把结果显示在文件中，需要重写toString()，可用”\t”分开，方便后续用。  
7. 如果需要将自定义的bean放在key中传输，则还需要实现Comparable接口，因为MapReduce框中的Shuffle过程要求对key必须能排序。详见后面排序案例。  

```java
@Override  
public int compareTo(FlowBean o) {  
    // 倒序排列，从大到小  
    return this.sumFlow > o.getSumFlow() ? -1 : 1;  
}  
```

# 三 序列化案例实操

 ## 1 输入输出数据

### 输入数据

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211005218.png" style="zoom:25%;" />

### 输出数据

手机号码 上行流量 下行流量 总流量 

### 例如： 

13760436666129  4200  4100 8300 

## 2 案例分析

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211005538.png" style="zoom:25%;" />

## 3 创建新的包和类

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211005609.png" style="zoom:25%;" />

## 4 FlowBean（重点）

```java
package com.coachhe.flowsum; 
  
import org.apache.hadoop.io.Writable; 
  
import java.io.DataInput; 
import java.io.DataOutput; 
import java.io.IOException; 
  
public class FlowBean implements Writable { 
  
    private long upFlow; //上行流量 
    private long downFlow; //下行流量 
    private long sumFlow; //总流量 
  
    //空参构造，为了后续反射用 
    public FlowBean(){ 
        super(); 
    } 
  
    public FlowBean(long upFlow, long downFlow) { 
        super(); 
        this.upFlow = upFlow; 
        this.downFlow = downFlow; 
        sumFlow = upFlow + downFlow; 
    } 
  
    //序列化方法 
    public void write(DataOutput dataOutput) throws IOException { 
        dataOutput.writeLong(upFlow); 
        dataOutput.writeLong(downFlow); 
        dataOutput.writeLong(sumFlow); 
    } 
  
  
    //反序列化方法 
    public void readFields(DataInput dataInput) throws IOException { 
        upFlow = dataInput.readLong(); 
        downFlow = dataInput.readLong(); 
        sumFlow = dataInput.readLong(); 
    } 
  
    @Override 
    public String toString() { 
        return  upFlow + "\t" + downFlow + "\t" + sumFlow; 
    } 
  
    public long getUpFlow() { 
        return upFlow; 
    } 
  
    public void setUpFlow(long upFlow) { 
        this.upFlow = upFlow; 
    } 
  
    public long getDownFlow() { 
        return downFlow; 
    } 
  
    public void setDownFlow(long downFlow) { 
        this.downFlow = downFlow; 
    } 
  
    public long getSumFlow() { 
        return sumFlow; 
    } 
  
    public void setSumFlow(long sumFlow) { 
        this.sumFlow = sumFlow; 
    } 
} 
```

## 5 FlowCountMapper

```java
package com.coachhe.flowsum; 
  
import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapreduce.Mapper; 
  
import java.io.IOException; 
  
public class FlowCountMapper extends Mapper<LongWritable, Text, Text, FlowBean> { 
  
    Text k = new Text(); 
    FlowBean v = new FlowBean(); 
  
    @Override 
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException { 
  
        // 7 138xxxxxxxx 120.178.231.32 1116 954 200 输入数据类型 
  
        // 1 获取一行 
        String line = value.toString(); 
  
        // 2 切割 
        String[] fieles = line.split(" "); 
  
        // 3 封装对象 
        k.set(fieles[1]);  //封装手机号 
        v.setUpFlow(Long.parseLong(fieles[fieles.length - 3])); 
        v.setDownFlow(Long.parseLong(fieles[fieles.length - 2])); 
  
        // 4 写出 
        context.write(k,v); 
  
    } 
} 
```

## 6 FlowCountReducer

```java
package com.coachhe.flowsum; 
  
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapreduce.Reducer; 
  
import java.io.IOException; 
  
public class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> { 
    Text k = new Text(); 
  
    @Override 
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException { 
        // 138xxxxxxxx 2481 24861 3000 
        // 138xxxxxxxx 318  32921 32893 
  
        long sum_upFlow = 0; 
        long sum_downFlow = 0; 
  
        // 1 累加求和 
        for (FlowBean flowBean : values) { 
            sum_downFlow += flowBean.getDownFlow(); 
            sum_upFlow += flowBean.getUpFlow(); 
        } 
  
        k.set(key); 
        FlowBean v = new FlowBean(sum_upFlow, sum_downFlow); 
  
        // 2 写出 
        context.write(k, v); 
    } 
} 
```

## 7 FlowCountDriver

```java
package com.coachhe.flowsum; 
  
import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapreduce.Job; 
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
  
import java.io.IOException; 
  
public class FlowCountDriver { 
  
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException { 
        if (args.length == 0) { 
            args = new String[]{"/Users/heyizhi/BigData/hadoop/my_project/MapReduce序列化结果/phone_data", 
            "/Users/heyizhi/BigData/hadoop/my_project/MapReduce序列化结果/Output"}; 
        } 
        Configuration conf = new Configuration(); 
        // 1 获取job对象 
        Job job = Job.getInstance(conf); 
  
        // 2 设置jar路径 
        job.setJarByClass(FlowCountDriver.class); 
  
        // 3 关联mapper和reducer 
        job.setMapperClass(FlowCountMapper.class); 
        job.setReducerClass(FlowCountReducer.class); 
  
        // 4 设置mapper输出的key和value类型 
        job.setMapOutputKeyClass(Text.class); 
        job.setMapOutputValueClass(FlowBean.class); 
  
        // 5 设置最终输出的key和value类型 
        job.setOutputKeyClass(Text.class); 
        job.setOutputValueClass(FlowBean.class); 
  
        // 6 设置输入输出路径 
        FileInputFormat.setInputPaths(job, new Path(args[0])); 
        FileOutputFormat.setOutputPath(job, new Path(args[1])); 
  
        // 7 提交job 
        boolean result = job.waitForCompletion(true); 
  
        System.exit(result ? 0 : 1); 
  
    } 
}
```

## 8 输入和输出

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211005948.png)

## 9 Linux环境使用

首先还是参考4 打包成可以直接运行的jar包将其打包为jar包放入linux环境中。 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Hadoop/20201211010037.png)
