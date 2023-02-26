---
title: SpringBoot笔记  
date: 2022-12-04 02:29:15  
tags: []  
categories:
  - 编程语言
  - Java
  - Java开发
  - Java框架
  - SpringBoot
---
# 原理初探

自动配置

## pom.xml

pom.xml中自动配置了依赖



启动器
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210919171656.png)

- spring-boot-dependencies:核心依赖在父工程中
  ```xml
  <parent>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-starter-parent</artifactId>  
      <version>2.2.1.RELEASE</version>  
      <relativePath/>  
  </parent>
  ```
- 说白了就是Springboot的启动场景
- 比如spring-boot-starter-web，他就会帮我们将所有web相关的功能场景进行打包
- springboot会将所有的场景功能场景，都变成一个个的启动器
- 我们要使用什么功能，就只需要找到对应的启动器就可以了`starter`
- 我们在写或者引入一些Springboot依赖的时候，不需要指定版本，就因为有这些版本仓库。
具体来说，例如放在resources里面的application文件能被感知到的原因
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210919171327.png>


## 主程序
```java
package com.coachhe.springboot.learning;  
  
import org.springframework.boot.SpringApplication;  
import org.springframework.boot.autoconfigure.SpringBootApplication;  
  
@SpringBootApplication  
public class SpringbootApplication {  
    public static void main(String[] args) {  
        SpringApplication.run(SpringbootApplication.class, args);  
 }  
}
```

### 注解
@SpringBootApplication
顾名思义，这是Springboot的配置类。进入这个注解，可以看到这个注解由多个注解组成：

```java
@Target({ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited  
@SpringBootConfiguration  
@EnableAutoConfiguration  
@ComponentScan(  
    excludeFilters = {@Filter(  
    type = FilterType.CUSTOM,  
    classes = {TypeExcludeFilter.class}  
), @Filter(  
    type = FilterType.CUSTOM,  
    classes = {AutoConfigurationExcludeFilter.class}  
)}  
)  
public @interface SpringBootApplication {
}
```

先来看看`@SpringBootConfiguration`:
```java
@Target({ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Configuration  
public @interface SpringBootConfiguration {  
    @AliasFor(  
        annotation = Configuration.class  
 )  
    boolean proxyBeanMethods() default true;  
}
```
可以看到，这个注解是由Configuration构成的，也就是说这个注解就是个也是一个Spring组件。

接着看看`@EnableAutoConfiguration`
```java
@Target({ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited  
@AutoConfigurationPackage  
@Import({AutoConfigurationImportSelector.class})  
public @interface EnableAutoConfiguration {  
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";  
  
    Class<?>[] exclude() default {};  
  
    String[] excludeName() default {};  
}
```

在这里我们看到，重要的是两个注解，`@AutoConfigurationPackage`和`@Import({AutoConfigurationImportSelector.class})`，首先来看看第一个注解`@AutoConfigurationPackage`。

```java
@Target({ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited  
@Import({Registrar.class})  
public @interface AutoConfigurationPackage {  
}
```

在这里导入了一个注册器
`@Import({Register.class})`

```java

static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {
        Registrar() {
        }

        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            AutoConfigurationPackages.register(registry, (new AutoConfigurationPackages.PackageImport(metadata)).getPackageName());
        }

        public Set<Object> determineImports(AnnotationMetadata metadata) {
            return Collections.singleton(new AutoConfigurationPackages.PackageImport(metadata));
        }
    }
```


接着看第二个注解`@Import({AutoConfigurationImportSelector.class})`，这是一个选择器，点进去







