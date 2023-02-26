---
title: Spring注解  
date: 2022-12-04 02:23:16  
tags: []  
categories:
  - 编程语言
  - Java
  - Java语言
  - Java语法
  - Java注解
---
# @Import
`@Import`是`Spring`基于 Java 注解配置的主要组成部分。`@Import`注解提供了`@Bean`注解的功能，同时还有原来`Spring`基于 xml 配置文件里的`<import>`标签组织多个分散的xml文件的功能，当然在这里是组织多个分散的`@Configuration`的类。

# @Import注解的功能
## 1. 引入其他的@Configuration
  

假设有如下接口和两个实现类：

```java
package com.test
interface ServiceInterface {
    void test();
}

class ServiceA implements ServiceInterface {

    @Override
    public void test() {
        System.out.println("ServiceA");
    }
}

class ServiceB implements ServiceInterface {

    @Override
    public void test() {
        System.out.println("ServiceB");
    }
}
```

两个`@Configuration`，其中`ConfigA``@Import``ConfigB`:

```java
package com.test
@Import(ConfigB.class)
@Configuration
class ConfigA {
    @Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceA() {
        return new ServiceA();
    }
}

@Configuration
class ConfigB {
    @Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceB() {
        return new ServiceB();
    }
}
```

通过`ConfigA`创建`AnnotationConfigApplicationContext`，获取`ServiceInterface`，看是哪种实现：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigA.class);
    ServiceInterface bean = ctx.getBean(ServiceInterface.class);
    bean.test();
}
```

输出为：`ServiceB`.证明`@Import`的优先于本身的的类定义加载。

 ## 2. 直接初始化其他类的Bean
 在**Spring 4.2**之后，`@Import`可以直接指定实体类，加载这个类定义到`context`中。 例如把上面代码中的`ConfigA`的`@Import`修改为`@Import(ServiceB.class)`，就会生成`ServiceB`的`Bean`到容器上下文中，之后运行`main`方法，输出为：`ServiceB`.证明`@Import`的优先于本身的的类定义加载.

## 3. 指定实现ImportSelector(以及DefferredServiceImportSelector)的类，用于个性化加载
  
 指定实现`ImportSelector`的类，通过`AnnotationMetadata`里面的属性，动态加载类。
 ### AnnotationMetadata
 `AnnotationMetadata`是`Import`注解所在的类属性（如果所在类是注解类，则延伸至应用这个注解类的非注解类为止）。

需要实现`selectImports`方法，返回要加载的`@Configuation`或者具体`Bean`类的全限定名的`String`数组。


