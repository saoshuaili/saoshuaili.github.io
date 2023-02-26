---
title: 2、Bean生命周期处理机制  
date: 2022-12-04 02:27:16  
tags: []  
categories:
  - 编程语言
  - Java
  - Java开发
  - Java框架
  - Spring
  - 源码
  - Spring源码解析-coachhe
---
有了 [Spring1-IOC容器启动创建bean - CoachHe-Blog](https://coachhe.studio/11.html) 的基础，我们现在更加深入地对整个 spring 的核心，也就是 bean 做更加深入的理解。

还是从
```java
ApplicationContext context = new ClassPathXmlApplicationContext ("applicationContext.Xml");
```
这里开始 debug，进入的第一个方法就是 `ClassPathXmlApplicationContext()` 的构造器，其中最重要的是 `refresh()` 这个方法：
```java
	public ClassPathXmlApplicationContext(
			String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
			throws BeansException {

		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
			// 最重要的一个方法
			refresh();
		}
	}
```

进入其中，一共有 13 个方法，每个方法都需要了解，我们一个个看：
首先第一个方法：

## 1. prepareRefresh()
```java
// Prepare this context for refreshing.  
/**  
 * 做容器刷新前的准备工作  
 * 1. 设置容器的启动时间  
 * 2. 设置活跃状态为true  
 * 3. 设置关闭状态为false  
 * 4. 获取Environment对象，并加载当前系统的属性值到Environment中  
 * 5. 准备监听和事件的集合对象，默认为空的集合  
 */  
prepareRefresh();
```
可以看到，这里是做一些容器刷新前的准备工作。

## 2. ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory ();
接着是：
```java
	// Tell the subclass to refresh the internal bean factory.
	// 解析 XML 配置文件，加载 xml 配置文件的属性值到当前工厂中，最重要的就是 BeanDefinition
	// 在这里创建了一个beanFactory对象，用来装载具体后续的bean对象
	ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
```
这是个很重要的方法，他创建了一个 beanFactory 对象，后面创建的对象都会装进去。点击进入 `obtainFreshBeanFactory()` 方法之中，可以看到：
```java
	protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
		// 初始化BeanFactory，并进行XML文件读取，并将得到的BeanFactory记录在当前实体的属性中
		refreshBeanFactory();
		// 返回当前实体的beanFactory属性
		return getBeanFactory();
	}
```
也就是说，他先初始化 BeanFactory，进行了 XML 文件的读取，并且将得到的 BeanFactory 记录在当前实体的属性中，最终将 beanFactory 进行返回。具体的，再往里走，看到 `refreshBeanFactory()` 方法：
```java

	/* 
	 * 在AbstractRefreshableApplicationContext类中
	 */

	@Override
	protected final void refreshBeanFactory() throws BeansException {
		// 如果存在beanFactory，则销毁beanFactory
		if (hasBeanFactory()) {
			destroyBeans();
			closeBeanFactory();
		}
		try {
			// 创建DefaultListableBeanFactory对象
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			// 为了序列化指定id，可以从id反序列化到beanFactory对象
			beanFactory.setSerializationId(getId());
			// 定制beanFactory，设置相关属性，包括是否允许覆盖同名称的不同定义的对象以及循环依赖
			customizeBeanFactory(beanFactory);
			// 初始化documentReader，并进行XML文件读取以及解析，默认命名空间的解析，自定义标签的解析
			// 注意，此时beanFactory中的属性beanDefinitionMap和beanDefinitionNames还是XML中的默认值，并不会去对应的配置文件中得到具体的属性值
			loadBeanDefinitions(beanFactory);
			this.beanFactory = beanFactory;
		}
		catch (IOException ex) {
			throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
		}
	}
```
在这里有一个需要注意的，也就是我们是在这里创建了 beanFactory，当第一次创建完成时，里面有两个属性：BeanDefinitionMap 和 BeanDefinitionNames，大小都为 0。但是当执行完 `customizeBeanFactory(beanFactory)` 这个方法之后，会被赋值。具体的可以看下面：
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211203005125.png)
此时刚创建完 beanFactory，两个属性的 size 都为 0，但是我们继续往下执行两行，当执行完 `loadBeanDefinitions(beanFactory)` 之后：
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211203005545.png)
可以看到，我们在 XML 中配置的文件内容都已经被加载到了 beanFactory 之中，其中 beanDefinitionMap 是一个 Map，key 是我们定义的 bean 名称，value 是 bean 的内容，例如类信息，范围，是否 abstract 等等。
### 注意
值得注意的是，这里执行完之后，我们只是很单纯地解析完了 XML 文件，但是并没有对 XML 文件的内容进行进一步的解析，比如我们如果定义某个 bean 的 value 是 `${user.db}`，这时在 beanDefinitionMap 中对应 bean 的 value 值就是 `${user.db}`，而不会去另一个配置文件中将这部分信息解析出来。具体解析步骤我们在后面分析。


## 3. prepareBeanFactory(beanFactory)

```java
// Prepare the bean factory for use in this context.
// beanFactory 的准备工作，对各种属性进行填充
prepareBeanFactory (beanFactory);
```

## 4. postProcessBeanFactory(beanFactory)
```java
// Allows post-processing of the bean factory in context subclasses.  
// 子类覆盖方法做额外的处理，此处我们一般不做任何拓展工作，但是可以查看web中的代码，是有具体实现的  
postProcessBeanFactory(beanFactory);
```

## 5. invokeBeanFactoryPostProcessors (beanFactory);
```java
// Invoke factory processors registered as beans in the context.  
// 调用各种beanFactory处理器  
invokeBeanFactoryPostProcessors(beanFactory);
```
这是个很重要的方法，他调用了各种 BeanFactory 的增强处理器。
对应一下 bean 的定义信息图：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211203130517.png" width="50%" />

对应的就是 BeanDefinition 到最终的 BeanDefinition 对象中的 BeanFactoryBeanPostProcessor 这一个环节。

这个环节有一个非常重要的作用，在上面执行完第二个方法 `ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory ();` 后，我们得到了一个保留原始值的 BeanFactory，这个 BeanFactory 是保留从 XML 解析来的原始值的（例如 db 的 value 是 `${jdbc.db}` ），这个原始值需要去更加具体的配置文件中读取，才会得到最终的具体值，这一步就是在这个方法中完成的。
### 注意
我们如果需要对 bean 有一些自定义的操作，可以在这一步来完成。只需要定义一个类继承 BeanFactoryPostProcessor 接口就可以。
例如我们定义了一个类 MyBFPP：
```java
public class MyBFPP implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("Here is a post process bean factory");
	}
}

```

这个类继承了 BeanFactoryPostProcessor 接口，并且在执行接口的方法 `postProcessBeanFactory` 时打印了一行，我们在 XML 文件中配置好这个 bean：
```xml
<bean class="com.coachhe.spring.BeanFactoryPostProcessor.MyBFPP"/>
```

然后不对 main 方法做任何改动，直接输出，可以看到：
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211203131723.png)
在没有对 main 方法做任何改动的情况下，将刚才实现的方法中打印的数据打印了，这些方法就是在执行 `invokeBeanFactoryPostProcessors (beanFactory);` 这个方法时一起执行的。

# Bean 的生命周期

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211219173353.png" width = "100%" />


# BeanFactory 和 FactoryBean 的联系和区别
## 联系
都是用来创建对象的
## 区别
FactoryBean 在 BeanFactory 的基础上进行了拓展，例如增加了这些方法:
```xml
isSingleton: 判断是否是单例对象
getObjectType: 获取对象类型
getObject: 用户可以按照自己任意的方式来创建对象
```


