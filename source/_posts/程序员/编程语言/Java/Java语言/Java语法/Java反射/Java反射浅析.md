---
title: Java反射浅析  
date: 2022-12-04 02:23:05  
tags: []  
categories:
  - 编程语言
  - Java
  - Java语言
  - Java语法
  - Java反射
---

# JVM构建实例浅析

## 示例代码

```java
Person p = new Person();
```

## 创建对象的过程

```java
Person p = new Person();
```

1. 加载类
	a. ClassLoader加载.class文件到内存
	b. 执行静态代码块和静态初始化语句
2. 执行new，申请一片内存空间
3. 调用构造器，创建一个空白对象
4. 子类调用父类构造器
5. 构造器执行
	a. 执行构造代码块和初始化语句
	b. 构造器内容

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210806103334.png)


## 类加载器
### `loadClass()`
```java
public abstract class ClassLoader {
   protected Class<?> loadClass(String name, boolean resolve) 
	   throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 首先，检查是否已经加载该类
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    // 如果尚未加载，则遵循父优先的等级加载机制（所谓双亲委派机制）
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // 模板方法模式：如果还是没有加载成功，调用findClass()
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}
```

加载.class文件大致可以分为3个步骤：

1.  检查是否已经加载，有就直接返回，避免重复加载
2.  当前缓存中确实没有该类，那么遵循父优先加载机制，加载.class文件
3.  上面两步都失败了，调用findClass()方法加载

### 注意

需要注意的是，ClassLoader类本身是抽象类
```java
public abstract class ClassLoader {
}
```
而抽象类是无法通过new创建对象的。所以它的findClass()方法写的很随意，直接抛了异常，反正你无法通过ClassLoader对象调用。也就是说，父类ClassLoader中的findClass()方法根本不会去加载.class文件。
```java
// ClassLoader类的findClass方法
protected Class<?> findClass(String name) throws ClassNotFoundException {  
    throw new ClassNotFoundException(name);  
}
```
正确的做法是，子类重写覆盖findClass()，在里面写自定义的加载逻辑。比如：
```java
@Override
public Class<?> findClass(String name) throws ClassNotFoundException {
	try {
		/*自己另外写一个getClassData()
                  通过IO流从指定位置读取xxx.class文件得到字节数组*/
		byte[] datas = getClassData(name);
		if(datas == null) {
			throw new ClassNotFoundException("类没有找到：" + name);
		}
		//调用类加载器本身的defineClass()方法，由字节码得到Class对象
		return defineClass(name, datas, 0, datas.length);
	} catch (IOException e) {
		e.printStackTrace();
		throw new ClassNotFoundException("类找不到：" + name);
	}
}
```
defineClass()是ClassLoader定义的方法，目的是根据.class文件的字节数组byte[] b造出一个对应的Class对象。
```java
    protected final Class<?> defineClass(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain)
        throws ClassFormatError
    {
        protectionDomain = preDefineClass(name, protectionDomain);
        String source = defineClassSourceLocation(protectionDomain);
        Class<?> c = defineClass1(name, b, off, len, protectionDomain, source);
        postDefineClass(c, protectionDomain);
        return c;
    }

```
我们无法得知具体是如何实现的，因为最终它调用了一个native方法defineClass1：
```java
    private native Class<?> defineClass0(String name, byte[] b, int off, int len, ProtectionDomain pd);

    private native Class<?> defineClass1(String name, byte[] b, int off, int len, ProtectionDomain pd, String source);

    private native Class<?> defineClass2(String name, java.nio.ByteBuffer b, int off, int len, ProtectionDomain pd, String source);
```

目前，关于类加载器知道了这些信息：
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210806110330.png)

现在，.class文件已经被类加载器加载到内存中，并且JVM根据其字节数组创建了对应的Class对象。
## Class类
```java
// Class类
	// 里面对反射的支持，写了个内部类里面的字段与.class的内容映射
    private static class ReflectionData<T> {
		// 类的各种参数
		// 字段、方法、构造器对象
        volatile Field[] declaredFields;
        volatile Field[] publicFields;
        volatile Method[] declaredMethods;
        volatile Method[] publicMethods;
        volatile Constructor<T>[] declaredConstructors;
        volatile Constructor<T>[] publicConstructors;
        volatile Field[] declaredPublicFields;
        volatile Method[] declaredPublicMethods;
        volatile Class<?>[] interfaces;

        final int redefinedCount;

        ReflectionData(int redefinedCount) {
            this.redefinedCount = redefinedCount;
        }
    }
	
	// 注解数据
    private volatile transient SoftReference<ReflectionData<T>> reflectionData;

    // 泛型信息
    private volatile transient int classRedefinedCount = 0;
```

针对字段、方法、构造器，因为信息量太大了，JDK还单独写了三个类：Field、Method、Constructor。
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210807144327.png width=500 heigh=500>
例如Method类：
```java
// Method类
public final class Method extends Executable {
    private Class<?>            clazz;
    private int                 slot;
    // This is guaranteed to be interned by the VM in the 1.4
    // reflection implementation
	
	// 方法名、返回值类型
    private String              name;			   
	
	// 参数类型、异常类型、修饰符
    private Class<?>            returnType;
    private Class<?>[]          parameterTypes;
    private Class<?>[]          exceptionTypes;
    private int                 modifiers;
    // Generics and annotations support
    private transient String              signature;
    // generic info repository; lazily initialized
	
	
	// 方法泛型、方法上的注解
    private transient MethodRepository genericInfo;
    private byte[]              annotations;
    private byte[]              parameterAnnotations;
    private byte[]              annotationDefault;
    private volatile MethodAccessor methodAccessor;
```
也就是说，Class类虽然准备了很多字段用来表示一个.class文件的信息，比如类名、注解、实现的接口等，但对于字段、方法、构造器等，为了更详细地描述这些重要信息，还写了三个类，每个类里面都有很详细的对应。而Class类本身就维持这三个对象的引用（对象数组形式！因为一个类可能有多个方法，所以Class要用Method[] methods保存）。

例如这里给出一个UserController类的例子
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210807143107.png  width=500 height=80>

在这里我们可以看到，左边是具体的方法，右边是对应的Method类，Method类将getUserBy方法的所有信息“解构”之后保存在Method对象中。
值得注意的是，getUserBy方法是UserController类的一个方法，UserController类中的所有信息被“解构”后保存在Class类中。其中字段、方法、构造器又用Field、Method等对象单独表示。

大概了解完Class类的字段后，看一下Class类的方法：
```java
// 是个final类，无法被继承
public final class Class<T> implements java.io.Serializable, GenericDeclaration, Type, AnnotatedElement {
    private static final int ANNOTATION= 0x00002000;
    private static final int ENUM      = 0x00004000;
    private static final int SYNTHETIC = 0x00001000;

    private static native void registerNatives();
    static {
        registerNatives();
    }

	// 构造器是private的，无法手动new，Class对象只能由虚拟机创建
    private Class(ClassLoader loader) {
        classLoader = loader;
    }
```
可以发现，Class类的构造器是私有的，我们无法手动new一个Class对象，只能由JVM创建。JVM在构造Class对象时，需要传入一个类加载器，然后才有我们上面分析的一连串加载、创建过程。
### `Class.forName()`方法
```java
    public static Class<?> forName(String className)
                throws ClassNotFoundException {
        Class<?> caller = Reflection.getCallerClass();
        return forName0(className, true, ClassLoader.getClassLoader(caller), caller);
    }
```
可以看到，返回了一个Class对象，注意`ClassLoader.getClassLoader(caller)`，还是用传入的类加载器去生成。

### `newInstance()`方法
```java
    @CallerSensitive
    public T newInstance()
        throws InstantiationException, IllegalAccessException
    {
        if (System.getSecurityManager() != null) {
            checkMemberAccess(Member.PUBLIC, Reflection.getCallerClass(), false);
        }

		// 如果cachedConstructor为空，那么就
        if (cachedConstructor == null) {
            if (this == Class.class) {
                throw new IllegalAccessException(
                    "Can not call newInstance() on the Class for java.lang.Class"
                );
            }
            try {
				// 在这里，empty代表要获取的空参构造对象
                Class<?>[] empty = {};
                final Constructor<T> c = getConstructor0(empty, Member.DECLARED);
 java.security.AccessController.doPrivileged(
                    new java.security.PrivilegedAction<Void>() {
                        public Void run() {
                                c.setAccessible(true);
                                return null;
                            }
                        });
                cachedConstructor = c;
			// 如果没有空参构造方法就抛异常
            } catch (NoSuchMethodException e) {
                throw (InstantiationException)
                    new InstantiationException(getName()).initCause(e);
            }
        }
        Constructor<T> tmpConstructor = cachedConstructor;
        int modifiers = tmpConstructor.getModifiers();
        if (!Reflection.quickCheckMemberAccess(this, modifiers)) {
            Class<?> caller = Reflection.getCallerClass();
            if (newInstanceCallerCache != caller) {
                Reflection.ensureMemberAccess(caller, this, null, modifiers);
                newInstanceCallerCache = caller;
            }
        }
        // Run constructor
        try {
			// 调用空参构造对象的newInstance()创建实例
            return tmpConstructor.newInstance((Object[])null);
        } catch (InvocationTargetException e) {
            Unsafe.getUnsafe().throwException(e.getTargetException());
            // Not reached
            return null;
        }
    }
private volatile transient Constructor<T> cachedConstructor;  
private volatile transient Class<?> newInstanceCallerCache;
```

首先关注一下：

```java
Class<?>[] empty = {};  
final Constructor<T> c = getConstructor0(empty, Member.DECLARED);
```

再仔细看`getConstructor0`方法：
```java
    private Constructor<T> getConstructor0(Class<?>[] parameterTypes,
                                        int which) throws NoSuchMethodException
    {
        Constructor<T>[] constructors = privateGetDeclaredConstructors((which == Member.PUBLIC));
        for (Constructor<T> constructor : constructors) {
            if (arrayContentsEq(parameterTypes,
                                constructor.getParameterTypes())) {
                return getReflectionFactory().copyConstructor(constructor);
            }
        }
        throw new NoSuchMethodException(getName() + ".<init>" + argumentTypesToString(parameterTypes));
    }
```
可以看到，empty对应的是parameterTypes，也就是参数类型，因为之前的empty为空，所以调用的是空参构造函数，如果没有空参构造方法就抛异常。

调用空参构造对象的newInstance()创建实例。
```java
tmpConstructor.newInstance((Object[]) null);
```
所以，本质上Class对象要想创建实例，其实都是通过构造器（`tmpConstructor`）对象。如果没有空参构造对象，就无法使用clazz.newInstance()，必须要获取其他有参的构造对象然后调用构造对象的newInstance()。
