---
title: ThreadLocal
tags: []
categories:
  - 程序员
  - 编程语言
  - Java
  - Java语言
  - Java语法
  - Java线程
  - 个人总结
date: 2023-02-27 11:24:49
---

代码链接:

[[]]

ThreadLocal, 称之为本地线程，主要有 `get()` 和 `set()` 两个方法。

作用域在同一个线程中。我们可以通过 `set()` 方法在当前线程上存储数据，通过 `get()` 方法在当前线程上获取数据。

# `set()` 方法源码分析

直接看源码：

```java
    /**
     * Sets the current thread's copy of this thread-local variable
     * to the specified value.  Most subclasses will have no need to
     * override this method, relying solely on the {@link #initialValue}
     * method to set the values of thread-locals.
     *
     * @param value the value to be stored in the current thread's copy of
     *        this thread-local.
     */
    public void set(T value) {
        Thread t = Thread.currentThread();   // 获取当前线程
        ThreadLocalMap map = getMap(t);      // 每一个线程都维护各自的一个容器
        if (map != null) {
            map.set(this, value);            // 这个this代表当前的ThreadLocal类，因为我们的组件中需要传输的对象可能会有多个，每个ThreadLocal只保存一个信息
        } else {
            createMap(t, value);
        }
    }
```

理解：所以一个 ThreadLocal 可以当成一个==工具箱==，对于当前线程，只需要拿到这个 ThreadLocal，就能获取工具箱内的所有数据。但是如果直接 new 了另一个 ThreadLocal，那么则无法获取之前的 ThreadLocal 中的信息。

# `get()` 方法源码分析

```java
    /**
     * Returns the value in the current thread's copy of this
     * thread-local variable.  If the variable has no value for the
     * current thread, it is first initialized to the value returned
     * by an invocation of the {@link #initialValue} method.
     *
     * @return the current thread's value of this thread-local
     */
    public T get() {
        Thread t = Thread.currentThread(); // 获取当前线程
        ThreadLocalMap map = getMap(t);    // 每个线程都维护鸽子的一个容器
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this); // 获取当前ThreadLocal对应的map
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
```

和 set 方法的分析方法基本相同

