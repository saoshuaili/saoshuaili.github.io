---
title: gojob  
date: 2022-12-04 02:30:15  
tags: []  
categories:
  - 编程语言
  - Go
  - Go项目
  - 调度系统
---


生成一个任务：

```shell
curl 'http://localhost:8071/ui/jobs' \
  -H 'Content-Type: application/json;charset=UTF-8' \
  --data-raw '{"name":"测试任务","cron":"1 * * * *","protocol":"http","uri":"http://localhost:8080/api/v1/work-order/create?nowUserName=coachhe","remark":"","status":1,"creator":"admin","preJobId":"","timeout":60,"retryCount":2,"retryWaitTime":10,"failTakeover":0,"misfireThreshold":0,"executorSelectStrategy":"random","httpParam":"","httpHeaderParam":"","httpSign":0,"shardingCount":0,"shardingParam":"","alarmEmail":"","subJobScheduleStrategy":0,"subJobIds":[],"executors":[{"address":"127.0.0.1:8080","weight":0,"status":1}]}' \
  --compressed
```

