---
title: 1、IOC容器启动创建bean  
date: 2022-12-04 02:25:26  
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
# 从一个最简单的实例开始：

这里有两个 POJO：MyTestBean 和 MyTestBean2

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/basicSpring" width="100%"/>

```java
//MyTestBean
package com.coachhe.spring.Bean;  
  
public class MyTestBean {  
 String testStr;  
 public String getTestStr() {  
      return testStr;  
 }  
 public void setTestStr(String testStr) {  
      this.testStr = testStr;  
 }  
 //为了debug方便，确认什么时候创建了这个bean
 public MyTestBean() {  
      System.out.println("Bean myTestBean is creating");  
 }
}

//MyTestBean2
package com.coachhe.spring.Bean;  
  
public class MyTestBean2 {  
 String testStr2 = "";  
 public String getTestStr2() {  
      return testStr2;  
 }  
 public void setTestStr2(String testStr2) {  
      this.testStr2 = testStr2;  
 }  
 //为了debug方便，确认什么时候创建了这个bean
 public MyTestBean2() {  
      System.out.println("Bean myTestBean2 is creating");  
 }
}
```

```xml
<!-- applicationContext.xml -->
<? xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="testStr1" class="com.coachhe.spring.Bean.MyTestBean">
		<property name="testStr" value="testStr1"/>
	</bean>

	<bean id="testStr2" class="com.coachhe.spring.Bean.MyTestBean2">
		<property name="testStr2" value="testStr2"/>
	</bean>
</beans>
```

```java
//Main.java
package com.coachhe.spring;
import com.coachhe.spring.Bean.MyTestBean;
import com.coachhe.spring.Bean.MyTestBean2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		MyTestBean myTestBean1 = context.getBean("testStr1", MyTestBean.class);
		MyTestBean2 myTestBean2 = context.getBean("testStr2", MyTestBean2.class);
		System.out.println(myTestBean1.getTestStr());
		System.out.println(myTestBean2.getTestStr2());
	}
}
```

就这样，无需什么额外操作，即可输出最终的结果：在 `applicationContext.xml ` 中赋值的 testStr1 和 testStr2 
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/spring3.png)

可以看到，两个实例的属性正常输出，并且两个构造函数中的语句也被正常输出，说明两个实例都被正常创建了。
本节的目的是确认何时创建了 bean，因为在 debug 时只要控制台输出了构造函数中的语句，则表名 bean 已经被创建了。

# 开始 Debug

直接在 `ApplicationContext context = new ClassPathXmlApplicationContext ("applicationContext. Xml"); ` 这里打上断点，开始 debug。
直接进入，可以看到最开始调用的是 ClassPathXmlApplicationContext 的单参构造方法，但是最终实际调用的是三个参数的构造方法。

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/spring4.png)

实际调用的是这个：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211130004515.png)

`super(parent)` 作用就是设置父类，没有实质作用，`setConfigLocation()` 作用是继承于 `AbstractRefreshableConfigApplicationContexta. java` 类的一个方法，作用是设置当前配置文件的位置。

最重要的是 `refresh()` 类，这个类是真正创建实例的类。

打上断点，进入 `refresh()`。

```java
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		//首先是同步锁，目的是保证多线程情况下ioc容器只会被创建一次
		synchronized (this.startupShutdownMonitor) {
			StartupStep contextRefresh = this.applicationStartup.start("spring.context.refresh");

			// Prepare this context for refreshing.
			// 准备容器刷新
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			// 解析XML配置文件，将要创建了所有bean的配置信息保存起来
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				StartupStep beanPostProcess = this.applicationStartup.start("spring.context.beans.post-process");
				// Invoke factory processors registered as beans in the context.
				// 用来支持国际化功能
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);
				beanPostProcess.end();

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				// 专门给子类的，可以自己实现
				onRefresh();

				// Check for listener beans and register them.
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
				// 重要！ 在这里创建了所有实例
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
				contextRefresh.end();
			}
		}
	}
```

在执行完 `finishBeanFactoryInitialization(beanFactory);` 方法之后，实例全部被创建了，因此我们来看这个方法，还是在 `AbstractApplicationContext.java` 类中。

来看看 `finishBeanFactoryInitialization` 方法：
```java

	/**
	 * Finish the initialization of this context's bean factory,
	 * initializing all remaining singleton beans.
	 */
	protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
		// Initialize conversion service for this context.
		if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
				beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
			beanFactory.setConversionService(
					beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
		}

		// Register a default embedded value resolver if no BeanFactoryPostProcessor
		// (such as a PropertySourcesPlaceholderConfigurer bean) registered any before:
		// at this point, primarily for resolution in annotation attribute values.
		if (!beanFactory.hasEmbeddedValueResolver()) {
			beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
		}

		// Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
		String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
		for (String weaverAwareName : weaverAwareNames) {
			getBean(weaverAwareName);
		}

		// Stop using the temporary ClassLoader for type matching.
		beanFactory.setTempClassLoader(null);

		// Allow for caching all bean definition metadata, not expecting further changes.
		beanFactory.freezeConfiguration();

		// Instantiate all remaining (non-lazy-init) singletons.
		// 初始化所有单实例bean 不能方形
		beanFactory.preInstantiateSingletons();
	}
```

其余都是一些判断，最重要的方法其实就是最后一行 `beanFactory.preInstantiateSingletons();` 所以我们打上断点，进入其中：

可以发现，进入了一个 `DefaultListableBeanFactory` 类中，方法具体为：
```java
	public void preInstantiateSingletons() throws BeansException {
		if (logger.isTraceEnabled()) {
			logger.trace("Pre-instantiating singletons in " + this);
		}

		// Iterate over a copy to allow for init methods which in turn register new bean definitions.
		// While this may not be part of the regular factory bootstrap, it does otherwise work fine.
		// 得到所有要创建的bean名，放入beanNames这个List中。
		List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);

		// Trigger initialization of all non-lazy singleton beans...
		// 增强for循环，按顺序创建bean
		for (String beanName : beanNames) {
			// 根据beanid获取到bean的定义信息(保存在xml文件中，就是解析到AbstractBeanFactory的mergedBeanDefinitions里保存）
			RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
			// 判断bean是单实例的，不是抽象的并且不是懒加载的，则进行加载
			if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
				// 判断是否为工厂bean（也就是调用工厂方法创建的bean），我们的就是普通bean，不是factory bean，因此判断为false
				if (isFactoryBean(beanName)) {
					Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
					if (bean instanceof FactoryBean) {
						FactoryBean<?> factory = (FactoryBean<?>) bean;
						boolean isEagerInit;
						if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
							isEagerInit = AccessController.doPrivileged(
									(PrivilegedAction<Boolean>) ((SmartFactoryBean<?>) factory)::isEagerInit,
									getAccessControlContext());
						}
						else {
							isEagerInit = (factory instanceof SmartFactoryBean &&
									((SmartFactoryBean<?>) factory).isEagerInit());
						}
						if (isEagerInit) {
							getBean(beanName);
						}
					}
				}
				else {
					//最终执行的实际上为这个方法,bean创建的细节
					getBean(beanName);
				}
			}
		}

		// Trigger post-initialization callback for all applicable beans...
		for (String beanName : beanNames) {
			Object singletonInstance = getSingleton(beanName);
			if (singletonInstance instanceof SmartInitializingSingleton) {
				StartupStep smartInitialize = this.getApplicationStartup().start("spring.beans.smart-initialize")
						.tag("beanName", beanName);
				SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
				if (System.getSecurityManager() != null) {
					AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
						smartSingleton.afterSingletonsInstantiated();
						return null;
					}, getAccessControlContext());
				}
				else {
					smartSingleton.afterSingletonsInstantiated();
				}
				smartInitialize.end();
			}
		}
	}
```

过程就是首先将 bean 的名称封装到一个 List 中，然后按顺序创建 bean，最终实际调用的是 `getBean()` 这个方法，进入 getBean 中：

```java
	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null, null, false);
	}
```

可以看到，实际调用的是 `AbstractBeanFactory`  类中四个参数的 `doGetBean()` 方法，继续进入：

```java
	protected <T> T doGetBean(
			String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly)
			throws BeansException {

		String beanName = transformedBeanName(name);
		Object beanInstance;

		// Eagerly check singleton cache for manually registered singletons.
		// 先从已经注册的所有单实例bean中检查是否有这个bean，如果没有则创建
		Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null && args == null) {
			if (logger.isTraceEnabled()) {
				if (isSingletonCurrentlyInCreation(beanName)) {
					logger.trace("Returning eagerly cached instance of singleton bean '" + beanName +
							"' that is not fully initialized yet - a consequence of a circular reference");
				}
				else {
					logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
				}
			}
			beanInstance = getObjectForBeanInstance(sharedInstance, name, beanName, null);
		}
		// 第一次创建时直接来到这里
		else {
			// Fail if we're already creating this bean instance:
			// We're assumably within a circular reference.
			if (isPrototypeCurrentlyInCreation(beanName)) {
				throw new BeanCurrentlyInCreationException(beanName);
			}

			// Check if bean definition exists in this factory.
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				// Not found -> check parent.
				String nameToLookup = originalBeanName(name);
				if (parentBeanFactory instanceof AbstractBeanFactory) {
					return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
							nameToLookup, requiredType, args, typeCheckOnly);
				}
				else if (args != null) {
					// Delegation to parent with explicit args.
					return (T) parentBeanFactory.getBean(nameToLookup, args);
				}
				else if (requiredType != null) {
					// No args -> delegate to standard getBean method.
					return parentBeanFactory.getBean(nameToLookup, requiredType);
				}
				else {
					return (T) parentBeanFactory.getBean(nameToLookup);
				}
			}

			// 这个就是传过来的最后一个参数，为false，因此需要调用markBeanAsCreated方法，标记已经创建了这个bean
			if (!typeCheckOnly) {
				markBeanAsCreated(beanName);
			}

			StartupStep beanCreation = this.applicationStartup.start("spring.beans.instantiate")
					.tag("beanName", name);
			try {
				if (requiredType != null) {
					beanCreation.tag("beanType", requiredType::toString);
				}

				// 获取当前bean的定义信息,例如class类型，是否有dependsOn等（也就是该bean的创建是否依赖于其他bean）
				RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
				checkMergedBeanDefinition(mbd, beanName, args);

				// Guarantee initialization of beans that the current bean depends on.
				// 先拿到dependsOn的Bean，是一个字符串数组，然后会先创建这些dependsOn的Bean，最后再创建自己这个bean（循环创建）
				String[] dependsOn = mbd.getDependsOn();
				if (dependsOn != null) {
					for (String dep : dependsOn) {
						if (isDependent(beanName, dep)) {
							throw new BeanCreationException(mbd.getResourceDescription(), beanName,
									"Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
						}
						registerDependentBean(dep, beanName);
						try {
							getBean(dep);
						}
						catch (NoSuchBeanDefinitionException ex) {
							throw new BeanCreationException(mbd.getResourceDescription(), beanName,
									"'" + beanName + "' depends on missing bean '" + dep + "'", ex);
						}
					}
				}

				// Create bean instance.
				// 如果mbd为单例，则创建bean实例
				if (mbd.isSingleton()) {
					sharedInstance = getSingleton(beanName, () -> {
						// getSingleton这个方法我们在之前判断bean是否创建的时候调用过
						// Object sharedInstance = getSingleton(beanName);
						// 结果为null，表示没有创建过
						// 不过在这里调用的是有两个参数的getSingleton方法，
						// 其中第二个参数是一个匿名内部类，作用是调用createBean方法，创建一个Bean
						try {
							return createBean(beanName, mbd, args);
						}
						catch (BeansException ex) {
							// Explicitly remove instance from singleton cache: It might have been put there
							// eagerly by the creation process, to allow for circular reference resolution.
							// Also remove any beans that received a temporary reference to the bean.
							destroySingleton(beanName);
							throw ex;
						}
					});
					beanInstance = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
				}

				else if (mbd.isPrototype()) {
					// It's a prototype -> create a new instance.
					Object prototypeInstance = null;
					try {
						beforePrototypeCreation(beanName);
						prototypeInstance = createBean(beanName, mbd, args);
					}
					finally {
						afterPrototypeCreation(beanName);
					}
					beanInstance = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
				}

				else {
					String scopeName = mbd.getScope();
					if (!StringUtils.hasLength(scopeName)) {
						throw new IllegalStateException("No scope name defined for bean '" + beanName + "'");
					}
					Scope scope = this.scopes.get(scopeName);
					if (scope == null) {
						throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
					}
					try {
						Object scopedInstance = scope.get(beanName, () -> {
							beforePrototypeCreation(beanName);
							try {
								return createBean(beanName, mbd, args);
							}
							finally {
								afterPrototypeCreation(beanName);
							}
						});
						beanInstance = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
					}
					catch (IllegalStateException ex) {
						throw new ScopeNotActiveException(beanName, scopeName, ex);
					}
				}
			}
			catch (BeansException ex) {
				beanCreation.tag("exception", ex.getClass().toString());
				beanCreation.tag("message", String.valueOf(ex.getMessage()));
				cleanupAfterBeanCreationFailure(beanName);
				throw ex;
			}
			finally {
				beanCreation.end();
			}
		}

		return adaptBeanInstance(name, beanInstance, requiredType);
	}

```

可以看到，最重要的方法是 `getSingleton`
值得注意的是，这里的 `getSingleton` 方法是第二次调用的方法，该方法总共被调用了两次，第一次是用来判断 bean 是否创建过，只有一个参数，返回了 null，表示没有创建过。
在第二次调用时有两个参数，第二个参数是一个匿名内部类，这个类作为参数被传入 `getSingleton()` 方法中，作用是调用 `createBean()` 方法，创建一个 Bean，由此可见，真正创建 bean 的方法是 `createBean(beanName, mbd, args)`.

在这里需要解释一下的是 `createBean()` 为什么会被调用。
这就需要了解一下用 Lambda 表达式表示匿名内部类的方法了：

### 用 lambda 表达式表示匿名内部类

用 Lambda 表达式表达匿名内部类时， Lambda 不需要接口名和方法名，写出实现方法体即可，编译器会自动去识别。但是前提是接口为函数接口，即只有一个抽象方法。
具体的，我们可以看下 `getSingleton()` 方法：

```java
	public Object getSingleton (String beanName, ObjectFactory<?> singletonFactory) {
		Assert.notNull (beanName, "Bean name must not be null");
		synchronized (this.singletonObjects) {
			Object singletonObject = this.singletonObjects.get(beanName);
			if (singletonObject == null) {
				if (this.singletonsCurrentlyInDestruction) {
					throw new BeanCreationNotAllowedException(beanName,
							"Singleton bean creation not allowed while singletons of this factory are in destruction " +
							"(Do not request a bean from a BeanFactory in a destroy method implementation!)");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
				}
				beforeSingletonCreation(beanName);
				boolean newSingleton = false;
				boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
				if (recordSuppressedExceptions) {
					this.suppressedExceptions = new LinkedHashSet<>();
				}
				try {
					// 在这里真正创建了bean
					singletonObject = singletonFactory.getObject();
					newSingleton = true;
				}
				catch (IllegalStateException ex) {
					// Has the singleton object implicitly appeared in the meantime ->
					// if yes, proceed with it since the exception indicates that state.
					singletonObject = this.singletonObjects.get(beanName);
					if (singletonObject == null) {
						throw ex;
					}
				}
				catch (BeanCreationException ex) {
					if (recordSuppressedExceptions) {
						for (Exception suppressedException : this.suppressedExceptions) {
							ex.addRelatedCause(suppressedException);
						}
					}
					throw ex;
				}
				finally {
					if (recordSuppressedExceptions) {
						this.suppressedExceptions = null;
					}
					afterSingletonCreation(beanName);
				}
				if (newSingleton) {
					// 在这里将bean放入ioc容器中
					addSingleton(beanName, singletonObject);
				}
			}
			return singletonObject;
		}
	}
```

在这里，传入的对象自动被向上转型成 `ObjectFactory<?> singletonFactory`，我们来看看这个类：

```java
@FunctionalInterface
public interface ObjectFactory<T> {

	/**
	 * Return an instance (possibly shared or independent)
	 * of the object managed by this factory.
	 * @return the resulting instance
	 * @throws BeansException in case of creation errors
	 */
	T getObject() throws BeansException;

}

```

可以看到，这个类只有一个方法，getObject 方法，所以我们相当于用 lambda 表达式生成了一个匿名内部类，这个匿名内部类只有一个方法，就是重写的 getObject 方法，相当于：
```java
public class lambdaClass {
	@Override
	public Object getObject(){
		try {
			return createBean(beanName, mbd, args);
		}
		catch (BeansException ex) {
			// Explicitly remove instance from singleton cache: It might have been put there
			// eagerly by the creation process, to allow for circular reference resolution.
			// Also remove any beans that received a temporary reference to the bean.
			destroySingleton(beanName);
			throw ex;
		}
	}
}
```

所以在 getSingleton 方法执行到
```java
try {
	// 在这里真正创建了bean
	singletonObject = singletonFactory.getObject();
	newSingleton = true;
}
```
这里的时候，我们执行了 getObject 方法，也就执行了 createBean 方法，所以 Bean 就被创建了！

### Ioc 容器的含义以及将 bean 放入 ioc 容器的方法
在之后会执行一个 `addSingleton(beanName, singletonObejct)` 方法，作用是将创建好的对象会保存在一个 Map 中，也就是 ioc 容器中。
我们来看一下 `addSingleton` 方法：
``` java
	protected void addSingleton(String beanName, Object singletonObject) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.put(beanName, singletonObject);
			this.singletonFactories.remove(beanName);
			this.earlySingletonObjects.remove(beanName);
			this.registeredSingletons.add(beanName);
		}
```

而 singletonObjects 的定义是：
```java
	/** Cache of singleton objects: bean name to bean instance. */
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
```

可以看到，这其实就是一个 ConcurrentHashMap，也就是我们的 ioc 容器之一！
注意，这里的之一是因为在这里只是单例的 ioc 容器，还有其他的容器。


# BeanFactory 和 ApplicationContext 的区别
`ApplicationContext` 是 `BeanFactory` 的子接口；
`BeanFactory` ：bean 的工厂接口；负责创建 bean 实例。
`ApplicationContext` ：是容器接口；更多的负责容器功能的实现；可以基于 `beanFactory` 创建好的对象智商完成强大的容器。
	容器可以从 map 获取这个 bean，并且 aop、di 等等功能都在 `ApplicationContext` 接口下的这些类里面。

总的来说，`BeanFactory` 是最底层的接口，`ApplicationContext` 是留给程序员的 ioc 容器接口； `ApplicationContext` 是 `BeanFactory` 的子接口；

Spring 最大的模式就是工厂模式。



