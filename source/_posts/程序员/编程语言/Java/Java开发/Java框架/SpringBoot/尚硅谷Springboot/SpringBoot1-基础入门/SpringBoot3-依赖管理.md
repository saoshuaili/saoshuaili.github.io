---
title: SpringBoot3-依赖管理  
date: 2022-12-04 02:28:57  
tags: []  
categories:
  - 编程语言
  - Java
  - Java开发
  - Java框架
  - SpringBoot
  - 尚硅谷Springboot
  - SpringBoot1-基础入门
---
# 了解自动配置原理

## 1. SpringBoot 特点
### 1.1 依赖管理

```xml
    <parent>
        <artifactId>spring-boot-starter-parent</artifactId>
        <groupId>org.springframework.boot</groupId>
        <version>2.5.5</version>
    </parent>
```


可以看到，在父项目中引入了版本号之后，子项目中就无需再次声明版本号了。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211224010449.png" width = "70%" />

在父任务中几乎声明了所有开发中常用的依赖版本

- 开发导入 starter 场景启动器
- 无需导入版本号，自动版本仲裁
- 可以修改版本号

修改版本号的步骤：
1. 查看 spring-boot-dependencies 里面对顶的当前依赖的版本，这不刚好结合时事给大家分析一下，我们在日志里常用的 log4j，之前爆出了大 bug，需要我们对版本进行更新，那我们先看到，自动导入的 log4j 版本为 4.12.1，这个版本是很危险的！
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211224011912.png)

那么我们怎么进行版本的修改呢？

2. 在当前项目里面重写配置
为了进行版本的修改，我们需要修改 log4j 的配置，官方声明将 log4j 升级到 2.17.0 之后就是安全的，我们只需要在 properties 中对版本进行修改，这个版本会自动覆盖父类的版本。

```xml
    <properties>
        <log4j2.version>2.17.0</log4j2.version>
    </properties>
```

添加之后重新刷新配置，可以看到所有 log4j 的版本全都更改了。

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211224114418.png)

看到了吗，Springboot 管理依赖版本就是这么简单！

我们先看 parent 模块：
```xml
    <parent>
        <artifactId>spring-boot-starter-parent</artifactId>
        <groupId>org.springframework.boot</groupId>
        <version>2.5.5</version>
    </parent>
```
他的父项目为：
```xml
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-dependencies</artifactId>
		<version>2.5.5</version>
```

在父项目中，几乎声明了所有开发中常用的依赖版本号


#### Springboot-starter
- 我们见到过很多的 Springboot-starter，每个 starter 都代表一个对应的开发场景。
- 只要引入 starter，这个场景的所有常规需要的依赖我们都会自动引入
	例如我们这里：
```xml
        <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
```
会自动将所有 web 开发常规需要的依赖自动导入
包括 spring-webmvc、spring-web、spring-boot-starter、spring-boot-starter-tomcat、spring-boot-starter-json。
这里特别关注一下 `spring-boot-starter`，这是最底层的依赖，也是 springboot 自动配置的核心依赖。
- Springboot-starter 一般都是官方提供的 starter，我们之后如果见到 `*-spring-boot-starter` 一般都是第三方为我们提供的简化开发的场景启动

### 1.2 自动配置

以 `spring-boot-web-starter` 为例
- 自动配置好 SpringMVC
	- 引入 SPringMVC 全套组件
	- 自动配好 SpringMVC 常用组件（功能）
- 自动配好 Web 常用功能，如字符编码功能
	- SpringBoot 帮我们配置好了所有 web 开发的常见场景

具体来看一下：
```java
package com.coachhe.boot;  
  
import org.springframework.boot.SpringApplication;  
import org.springframework.boot.autoconfigure.SpringBootApplication;  
import org.springframework.context.ConfigurableApplicationContext;  
  
/**  
 * @SpringBootApplication: 这是一个SpringBoot应用  
 */  
@SpringBootApplication  
public class Main {  
    public static void main(String[] args) {  
  
        // 1.返回我们的IOC容器  
 		ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);  
 		 
 		// 2. 查看容器里的组件  
 		String[] beanDefinitionNames = run.getBeanDefinitionNames();  
 		       for (String name : beanDefinitionNames) {  
 		           System.out.println(name);  
 		       }  
    }  
}
```

在上面的程序中，我们执行了一个 SpringBoot 项目之后返回了 IOC 容器，然后通过 `getBeanDefinitionNames()` 来查看容器里的组件。

结果：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211227004823.png" width = "100%" />
这是一个做字符编码的组件，已经自动创建了。

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211227005102.png)

这是一个做文件上传的组件，已经自动创建了。

#### 默认的包结构
- SpringBoot 默认扫描**主程序所在包以及下面所有子包**里面的组件都会被默认扫描出来。
- 无需配置以前的包扫描
- 若一定需要扫描其他包下的组件，那么需要在 `SpringBootApplication` 里配置 `scanBasePackage="com.coachhe.springboot"`。或者直接在 Main 上面配置 `ComponentScan("com.coachhe.springboot")`。



