---
title: 补充：Class 实例是在堆中还是方法区中？  
date: 2023-01-28 00:38:40  
tags: []  
---

# 1、JVM 中 OOP-KLASS 模型

在JVM中，使用了OOP-KLASS模型来表示java对象，即：  

1. jvm 在加载 class 时，创建 instanceKlass，表示其元数据，包括常量池、字段、方法等，存放在方法区；instanceKlass 是 jvm 中的数据结构；  
2. 在 new 一个对象时，jvm 创建 instanceOopDesc 来表示这个对象，存放在堆区，其引用存放在栈区；它用来表示对象的实例信息，看起来像个指针实际上是藏在指针里的对象；instanceOopDesc 对应 java 中的对象实例；  
3. HotSpot 并不把 instanceKlass 暴露给 Java，而会另外创建对应的 instanceOopDesc 来表示 `java.lang.Class` 对象，并将后者称为前者的“Java 镜像”，klass 持有指向 oop 引用 (`_java_mirror` 便是该 instanceKlass 对 Class 对象的引用)；  
4. 要注意，new 操作返回的 instanceOopDesc 类型指针指向 instanceKlass，而 instanceKlass 指向了对应的类型的 Class 实例的 instanceOopDesc；有点绕，简单说，就是 Person 实例——>Person 的 instanceKlass——>Person 的 Class。

instanceOopDesc，只包含数据信息，它包含三部分：  
1. 对象头，也叫Mark Word，主要存储对象运行时记录信息，如hashcode, GC分代年龄，锁状态标志，线程ID，时间戳等;  
2. 元数据指针，即指向方法区的instanceKlass实例  
3. 实例数据;  
4. 另外，如果是数组对象，还多了一个数组长度