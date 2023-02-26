---
title: redis入门  
date: 2022-12-04 02:45:51  
tags: []  
categories:
  - 大数据
  - NoSQL
  - Redis
---
# Redis简介

## Redis是什么？

Redis（Remote Dictionary Server )，即远程字典服务。

是一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库，并提供多种语言的API。

与memcached一样，为了保证效率，数据都是缓存在内存中。区别的是redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，并且在此基础上实现了master-slave(主从)同步。

## Redis能干什么？

1. 内存存储、持久化，内存是断电即失的，所以需要持久化（RDB、AOF）
2. 高效率、用于高速缓冲
3. 发布订阅系统
4. 地图信息分析
5. 计时器、计数器(eg：浏览量)
6. 。。。

## 特性

1. 多样的数据类型
2. 持久化
3. 集群
4. 事务
5. ...

# 安装

## 基本环境配置

```shell
CentOS Linux release 7
redis 3.0.4
```

1. 将redis下载并解压到对应文件夹

   ![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210506212114.png)

## 安装必备环境并编译

1. 安装gcc基本环境

   ```shell
   yum install gcc-c++
   ```

2. 执行`make`命令

   在redis主目录执行make命令

   ```shell
   make
   ```

   <img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210506212628.png" style="zoom:50%;" />

3. 执行`make install`命令

   只有执行`make install`之后`/usr/local/bin`目录下才会出现redis等命令

   ![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210506213315.png)

5. redis默认安装路径`/usr/local/bin`

   在该目录下新建自己的配置文件夹（coachhe-redis-config），然后将配置文件拷贝到这个目录下

   ![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210506213532.png)

## 配置并启动

1. 修改redis.conf文件

   a. 注释掉`bind 127.0.0.1`这一行

   <img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210507101139.png" style="zoom: 50%;" />

   b. 将` protected-mode` 属性改为 no （关闭保护模式，不然会阻止远程访问）

   c. 将 `daemonize` 属性改为 yes （这样启动时就在后台启动）

   <img src="/Users/coachhe/Library/Application Support/typora-user-images/image-20210507101354436.png" alt="image-20210507101354436" style="zoom:50%;" />

   d. 设置密码（可选）

2. 启动

    在`/usr/local/bin`目录下执行

   ```shell
   ./redis-server /usr/local/bin/coachhe-redis-config/redis.conf
   ```

   ![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210507101631.png)

## 性能测试

```shell
# 测试：100个并发连接 100000请求
redis-benchmark -h localhost -p 6379 -c 100 -n 100000
```

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210507102257.png)



# Redis命令简介

## Set（集合）

### SADD

将一个或多个 `member` 元素加入到集合 `key` 当中，已经存在于集合的 `member` 元素将被忽略。

假如 `key` 不存在，则创建一个只包含 `member` 元素作成员的集合。

当 `key` 不是集合类型时，返回一个错误。

（示例看`SMEMBERS`那里）

### SMEMBERS

```redis
SMEMBERS key
```

返回集合 `key` 中的所有成员。

不存在的 `key` 被视为空集合。

### SCARD

```shell
SCARD key
```

返回集合 `key` 的基数(集合中元素的数量)。

### SPOP 

```shell
SPOP key
```

移除并返回集合中的一个随机元素。

如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用` SRANDMEMBER`命令

### SRANDMEMBER

```shell
SRANDMEMBER key [count]
```

如果命令执行时，只提供了 `key` 参数，那么返回集合中的一个随机元素。

从 Redis 2.6 版本开始，`SRANDMEMBER`命令接受可选的 `count` 参数：

- 如果 `count` 为正数，且小于集合基数，那么命令返回一个包含 `count` 个元素的数组，数组中的元素**各不相同**。如果 `count` 大于等于集合基数，那么返回整个集合。
- 如果 `count` 为负数，那么命令返回一个数组，数组中的元素**可能会重复出现多次**，而数组的长度为 `count` 的绝对值。

该操作和`SPOP`相似，但`SPOP`将随机元素从集合中移除并返回，而`SRANDMEMBER`则仅仅返回随机元素，而不对集合进行任何改动。

### 小结

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210507222950.png" style="zoom: 50%;" />

## List（列表）

### RPUSH

将一个或多个值value插入到列表key的表尾（最右边）

如果有多个value值，那么各个value值按**从左到右**的顺序依次插入到表尾：

```shell
RPUSH mylist a b c
## 等价于
RPUSH mylist a
RPUSH mylist b
RPUSH mylist c
```

时间复杂度：O(1)

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210511092634.png)

### LPUSH

将一个或多个值value插入到列表key的表头（最右边）

如果有多个value值，那么各个value值按**从左到右**的顺序依次插入到表头：

```shell
RPUSH mylist a b c
## 等价于
RPUSH mylist a
RPUSH mylist b
RPUSH mylist c
```

时间复杂度：O(1)

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210511093120.png" style="zoom:50%;" />



## Hash（哈希表）

同时将多个field-value（域-值）对设置到哈希表key中。

此命令会覆盖哈希表中已存在的域

**时间复杂度**：O(N)

```shell
HMSET key field value [field value ...]
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210511095225.png" style="zoom: 50%;" />



























