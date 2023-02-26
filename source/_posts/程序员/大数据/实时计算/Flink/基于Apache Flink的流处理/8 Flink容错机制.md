---
title: 8 Flink容错机制  
date: 2022-12-04 02:14:37  
tags: []  
categories:
  - 大数据
  - 实时计算
  - Flink
  - 基于Apache Flink的流处理
---

# 主要内容

* 一致性检查点（checkpoint）
* 从检查点恢复状态
* Flink检查点算法
* 保存点（save points）

# 现代流处理引擎结果保障

1. 至多一次
2. 至少一次
3. 精确一次
4. 端到端的精确一次

# 一致性检查点

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210802203011.png)




