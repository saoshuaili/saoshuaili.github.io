---
title: ThreadLocal  
date: 2023-02-27 11:24:49  
tags: []  
---

代码链接:

[[code/编程语言/Java/Java语言/Java语法/CoachHeJavaBasic/src/main/java/com/coachhe/threadlocalLearning/README]]

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
            map.set(this, value);
        } else {
            createMap(t, value);
        }
    }
```


