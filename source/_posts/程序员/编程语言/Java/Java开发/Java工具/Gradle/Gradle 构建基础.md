---
title: Gradle 构建基础  
date: 2022-12-04 02:28:34  
tags: []  
categories:
  - 编程语言
  - Java
  - Java开发
  - Java工具
  - Gradle
---
# 构建基础
## Projects 和 tasks

projects 和 tasks是 Gradle 中最重要的两个概念。

任何一个 Gradle 构建都是由一个或多个 projects 组成。每个 project 包括许多可构建组成部分。 这完全取决于你要构建些什么。举个例子，每个 project 或许是一个 jar 包或者一个 web 应用，它也可以是一个由许多其他项目中产生的 jar 构成的 zip 压缩包。一个 project 不必描述它只能进行构建操作。它也可以部署你的应用或搭建你的环境。不要担心它像听上去的那样庞大。 Gradle 的 build-by-convention 可以让您来具体定义一个 project 到底该做什么。

每个 project 都由多个 tasks 组成。每个 task 都代表了构建执行过程中的一个原子性操作。如编译，打包，生成 javadoc，发布到某个仓库等操作。

```xml
task upper << {
    String someString = 'mY_nAmE'
    println "Original: " + someString
    println "Upper case: " + someString.toUpperCase()
}
```

Output of gradle -q upper
> gradle -q upper
Original: mY_nAmE
Upper case: MY_NAME

### 注意
**注**：<<在Gradle4.x中被弃用，在Gradle 5.0中被移除，详情见：[Gradle 4.x官网](https://docs.gradle.org/4.0/release-notes.html#setting-the-compiler-executable-is-no-longer-deprecated)
**例**：task << { println 'Hello world!'}    
**解决方法**：直接去掉或使用doLast解决。

## 任务依赖
### 在两个任务之间指明依赖关系
build.gradle
```xml
task hello << {
    println 'Hello world!'
}
task intro(dependsOn: hello) << {
    println "I'm Gradle"
}
```

Output of gradle -q intro
> gradle -q intro
Hello world!
I'm Gradle

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211123100013.png)

### 延迟依赖
build.gradle
```xml
task taskX(dependsOn: 'taskY') << {
    println 'taskX'
}
task taskY << {
    println 'taskY'
}
```

输出：
> gradle -q taskX
taskY
taskX

可以看到，taskX是在taskY之前被定义的，但是还是可以引用taskY，这在多项目构建中是非常有用的。

## 通过API进行任务之间的通信

### 增加依赖

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211123101012.png)

在这里，创建了4个任务（task0到task3），然后通过API添加了依赖，让task0依赖于task2和task3，最后输出。


### 增加任务行为

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211123101922.png)

在这里，定义了一个任务hello，并且在前后并且最后都添加了相应的任务行为。

doFirst和doLast可以进行多次调用。他们分别被添加到任务的开头和结尾。当任务开始执行时这些动作会按照既定顺序进行。

**其中<<操作符是doLast的简写模式**。

## 增加自定义属性
### 为任务增加自定义属性
build.gradle
```xml
task myTask {
        ext.myProperty = "myValue"
}

task printTaskProperties << {
        println myTask.myProperty
}

[root@coachhe-code HelloWorld]# gradle -q printTaskProperties
myValue
```

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211123102953.png)

## 定义默认任务
build.gradle
```xml
defaultTasks 'clean', 'run'
task clean << {
    println 'Default Cleaning!'
}
task run << {
    println 'Default Running!'
}
task other << {
    println "I'm not a default task!"
}
```

gradle -q的输出结果
> gradle -q
	> Default Cleaning!
	> Default Running

也就是说，如果我不指定要执行的任务，就会默认执行Default的任务。

