---
title: 7 Flink的状态管理  
date: 2022-12-04 02:13:48  
tags: []  
categories:
  - 大数据
  - 实时计算
  - Flink
  - 基于Apache Flink的流处理
---

# 7 Flink的状态管理

# 主要内容

1. Flink中的状态
2. 算子状态（Operator State）
3. 键控状态（Keyed State）
4. 状态后端（State Backends）

## 状态的概念

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/20210521002340.png" style="zoom:50%;" />

* 由一个任务维护，并且用来计算某个结果的所有数据，都属于这个任务的状态

* 可以认为状态就是一个本地变量，可以被任务的业务逻辑访问

  注意：事实上，状态的确保存在本地，只是需要保证状态一致性等操作，由Flink实现

* Flink会进行状态管理，包括状态一致性、故障处理以及高效存储和访问，以便开发人员可以专注于应用程序的逻辑

## Flink中的状态

* 在Flink中，状态始终与特定算子关联（和任务绑定）

  注意：不能跨任务使用状态

* 为了使运行时的Flink了解算子的状态，算子需要预先注册其状态

### 状态的分类

根据**作用域**的不同，状态可以分为两类：

1. 算子状态

   算子状态的作用范围限定为算子

2. 键值分区状态

   根据输入数据流中定义的键（key）来维护和访问

### ① 算子状态

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/20210521002947.png" style="zoom: 33%;" />

* 算子状态的作用范围限定为**算子任务**，由同一并行任务所处理的所有数据都可以访问到相同的状态
* 状态对于同一子任务而言是共享的
* 算子状态不能由相同或不同算子的另一个子任务访问

也就是说，哪怕都是map算子，但若不是同一个算子任务，则不能相互访问（例如一个map的结果要给另一个map，这两个map之间不能相互访问）。但是对于一个并行度为2的算子任务，这两个算子任务是可以相互访问的。

#### 算子状态的数据结构

1. 列表状态（list state）

   将状态表示为一个条目列表

2. 联合状态列表（union list state）

   同样是将状态表示为一个条目列表，但在进行故障恢复或从某个保存点启动应用时，状态的恢复方式和普通列表有所不同。

3. 广播状态（broadcast state）

   如果一个算子有多项任务，而他们的任务状态又都相同，那么这种特殊情况最适合应用广播状态。

#### 算子状态实例

```java
package com.coachhe.apitest.state;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import util.SensorReading;

import java.util.Collections;
import java.util.List;

public class OperatorState {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // socket文本流
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        // 定义一个有状态的map操作，统计当前分区数据个数
        DataStream<Integer> resultStream = dataStream.map(new MyCountMapper());

        resultStream.print();


        env.execute();

    }

    // 自定义MapFunction
    public static class MyCountMapper implements MapFunction<SensorReading, Integer>, ListCheckpointed<Integer> {

        // 定义一个本地变量，作为算子状态
        private Integer count = 0;

        @Override
        public Integer map(SensorReading sensorReading) throws Exception {
            count++;
            return count;
        }

        @Override
        public List<Integer> snapshotState(long l, long l1) throws Exception {
            // 对状态做快照，返回Integer类型的list
            return Collections.singletonList(count);
        }

        @Override
        public void restoreState(List<Integer> state) throws Exception {
            // 从错误状态恢复
            for (Integer number : state) {
                
            }
        }
    }
}

```

结果：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/20210609012434.png" style="zoom:50%;" />

### ② 键值分区状态

键值分区状态会按照**算子输入记录**所定义的**键值**来进行维护或访问。

Flink为每个键值都维护了一个状态实例，该实例总是位于那个**处理对应键值记录的算子任务**上。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/20210522191509.png" style="zoom: 80%;" />

图中两个Task是两个**并行子任务**（也就是同一个任务，每个任务可能有多个key），图中每种颜色的数据代表一个key。每个key有一个状态，黄色的key只能访问黄色的状态，粉色的key只能访问粉色的状态。

当任务处理一条数据时，它会自动访问当前键值对应的状态。

#### 键值分区状态的数据结构

1. 值状态（Value state）

   将状态表示为单个的值

2. 列表状态（List State）

   将状态表示为一组数据的列表

3. 映射状态（Map state）

   将状态表示为一组key-value对

4. 聚合状态（Reducing state & Aggregating State）

   将状态表示为一个用于聚合操作的列表

#### 键值分区状态实例

使用：

* 声明一个键控状态

  ```java
  myValueState = getRuntimeContext().getState(
    new ValueStateDescriptor<Integer>(
    	"my-value",
    	Integer.class));
  ```

* 读取状态

  ```java
  Integer myValue = myValueState.value()
  ```

* 对状态赋值

  ```java
  myValue.update(10)
  ```

##### 值状态实例

```java
package com.coachhe.apitest.state;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import util.SensorReading;

public class KeyedState {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // socket文本流
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        // 定义一个有状态的map操作，统计当前sensor数据个数
        DataStream<Integer> resultStream = dataStream
                .keyBy("id")
                .map(new MyKeyCountMapper());

        resultStream.print();


        env.execute();
    }

    //自定义MapFunction，普通MapFunction不能完成，必须使用RichMapFunction
    public static class MyKeyCountMapper extends RichMapFunction<SensorReading, Integer>{

        private ValueState<Integer> keyCountState;

        @Override
        public void open(Configuration parameters) throws Exception {
            keyCountState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("key-count", Integer.class));
        }

        @Override
        public Integer map(SensorReading sensorReading) throws Exception {
            Integer count = keyCountState.value();
            if (count == null) {
                count = 0;
            }
            count++;
            keyCountState.update(count);
            return count;
        }
    }



}

```

结果：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/20210609010347.png" style="zoom:30%;" />

##### 列表状态实例

```java
package com.coachhe.apitest.state;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import util.SensorReading;

import java.util.Collections;

public class KeyedState {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // socket文本流
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        // 定义一个有状态的map操作，统计当前sensor数据个数
        DataStream<Integer> resultStream = dataStream
                .keyBy("id")
                .map(new MyKeyCountMapper());

        resultStream.print();


        env.execute();
    }

    //自定义MapFunction，普通MapFunction不能完成，必须使用RichMapFunction
    public static class MyKeyCountMapper extends RichMapFunction<SensorReading, Integer>{

        private ValueState<Integer> keyCountState;
        private ListState<String> listState;

        @Override
        public void open(Configuration parameters) throws Exception {
            keyCountState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("key-count", Integer.class));
            listState = getRuntimeContext().getListState(new ListStateDescriptor<String>("my-list", String.class));
        }

        @Override
        public Integer map(SensorReading sensorReading) throws Exception {
            // 值状态API调用
            Integer count = keyCountState.value();
            if (count == null) {
                count = 0;
            }
            count++;
            keyCountState.update(count);

            // 列表状态API调用
            Iterable<String> strings = listState.get();
            // 每次增加一行，值为count
            listState.add(String.valueOf(count));
            // 然后遍历
            for (String each : strings) {
                System.out.println("the element of strings is " + each);
            }
            
            return count;
        }
    }
}
```

结果：

<img src="/Users/heyizhi/Library/Application Support/typora-user-images/image-20210609011913221.png" alt="image-20210609011913221" style="zoom:50%;" />

##### 映射状态实例

```java
package com.coachhe.apitest.state;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import util.SensorReading;

import java.util.Collections;

public class KeyedState {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // socket文本流
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        // 定义一个有状态的map操作，统计当前sensor数据个数
        DataStream<Integer> resultStream = dataStream
                .keyBy("id")
                .map(new MyKeyCountMapper());

        resultStream.print();


        env.execute();
    }

    //自定义MapFunction，普通MapFunction不能完成，必须使用RichMapFunction
    public static class MyKeyCountMapper extends RichMapFunction<SensorReading, Integer>{

        private ValueState<Integer> keyCountState;
        private ListState<String> listState;
        private MapState<String, Integer> mapState;

        @Override
        public void open(Configuration parameters) throws Exception {
            keyCountState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("key-count", Integer.class));
            listState = getRuntimeContext().getListState(new ListStateDescriptor<String>("my-list", String.class));
            mapState = getRuntimeContext().getMapState(new MapStateDescriptor<String, Integer>("my-map", String.class, Integer.class));
        }

        @Override
        public Integer map(SensorReading sensorReading) throws Exception {
            // 值状态API调用
            Integer count = keyCountState.value();
            if (count == null) {
                count = 0;
            }
            count++;
            keyCountState.update(count);

            // 列表状态API调用
            Iterable<String> strings = listState.get();
            // 每次增加一行，值为count
            listState.add(String.valueOf(count));
            // 然后遍历
            for (String each : strings) {
                System.out.println("the element of strings is " + each);
            }
            
            // map state
            mapState.get("1");
            mapState.put("2", 1);
            mapState.remove("2");
            
            return count;
        }
    }
}

```



## 状态后端

为了保证快速访问状态，每个并行任务都会把状态维护在本地。至于状态具体的存储、访问和维护，则是由一个名为**状态后端**的可插拔组件来决定。

### 状态后端的任务

状态后端主要负责两件事：

1. 本地状态的管理
2. 将状态以检查点的形式写入远程存储

### 状态后端的存储方式

1. 一类状态后端会把**键值分区状态作为对象**，以内存数据结构的形式**存在JVM堆**中
2. 另一类状态后端会把**状态对象序列化**后**存到RocksDB**中

### 状态后端类型

1. MemoryStateBackend

   内存级的状态后端，会将键控状态作为内存中的对象进行管理，将它们存储在TaskManager的JVM堆上，而将checkpoint存储在JobManager的内存中

   **特点**：

   快速、低延迟，但不稳定（使用较少）

2. FsStateBackend

   将checkpoint存到远程的持久化文件系统上，而对本地状态，和MemoryStateBackend一样，也会存在TaskManager的JVM堆上

   **特点**：

   同时拥有内存级的本地访问速度，和更好的容错保证

   **缺点**：

   所有状态放在内存中，内存可能不够，只能不断扩容然后重新启动。

3. RocksDBStateBackend

   将所有状态序列化后，存入本地的RocksDB中存储。

   **特点**：

   不会出现内存不够的情况，状态存在本地RocksDB（硬盘管够）

   **缺点**：

   所有状态都需要序列化，速度可能更慢

## 有状态算子的扩缩容

### 扩缩容介绍

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210526162745.png" style="zoom: 33%;" />

Flink对不同类型的状态提供了四种扩缩容模式。

### 带有键值分区状态的算子

带有键值分区状态的算子在扩缩容时会根据新的任务数量对键值重新分区。

**注意**：

为了降低状态在不同任务之间的迁移成本，Flink不会对单独的键值实施再分配，而是会把所有键值分为不同的键值组（key group）。每个键值组都包含了部分键值，Flink以此为单位把键值分配给不同任务。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210526105034.png" style="zoom: 30%;" />

如图所示，两个键值分区状态组成一个键值组，在扩缩容时，Flink不对一个键值组里的具体键值分区状态进行重分配，而是直接将整个键值组进行重分配给不同任务。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210526201935.png" style="zoom: 33%;" />

更进一步的，如上图所示，一个DataStream经过keyBy之后会被切分为不同的KeyStream，每个KeyStream会有不同的key，

### 带有算子状态的算子

带有算子状态的算子在扩缩容时会对列表中的条目进行重分配。

理论上，所有并行算子任务的列表条目会被统一收集起来，随后均匀分配到更少或更多的任务之上。

**注意**：

如果列表条目的数量小于算子新设置的并行度，部分任务在启动时的状态就可能为空。































