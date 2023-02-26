---
title: AoP  
date: 2022-12-04 02:27:56  
tags: []  
categories:
  - 编程语言
  - Java
  - Java开发
  - Java框架
  - Spring
  - 源码
  - 《Spring源码深度解析》
---

# 什么是 AOP
1. 面向切面编程
利用 AOP 可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提供程序的可重用性，同时提高了开发的效率

# AOP 底层原理
1. AOP 底层使用动态代理
	1. 有两种情况的动态代理
		1. 有接口情况，使用 JDK 动态代理
			<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221204022816.png" width = "50%" />
		1. 没有接口情况，使用 CJLIB 动态代理
		创建子类的代理对象，增强类的方法


