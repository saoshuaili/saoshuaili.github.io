---
title: Springboot4- 自动配置原理  
date: 2022-12-04 02:29:07  
tags: []  
categories:
  - 编程语言
  - Java
  - Java开发
  - Java框架
  - SpringBoot
  - 尚硅谷Springboot
  - SpringBoot3-自动配置
---
# 一 引导加载自动配置类
最重要的一个注解：`@SpringBootApplication`
```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
```

可以看到，这是一个组合注解，将其逐个进行分析。

## 1. @SpringBootConfiguration

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootConfiguration {

}
```

可以看到，这其实就表明这就是一个配置类。没什么好说的

## 2 @EnableAutoConfiguration

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {

}
```

可以看到，这还是一个组合注解。

### 1.1 @AutoConfigurationPackage

```java
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {

}
```

看到这里导入了一个组件：`AutoConfigurationPackages.Registar.class`

点进去，可以看到：

```java
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {

   @Override
   public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
      register(registry, new PackageImport(metadata).getPackageName());
   }

   @Override
   public Set<Object> determineImports(AnnotationMetadata metadata) {
      return Collections.singleton(new PackageImport(metadata));
   }

}
```

