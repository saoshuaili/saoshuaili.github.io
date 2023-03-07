package com.coachhe.Chapter2_类加载时机;

/**
 * 被动引用示例1
 * 在这里打印了SubClass的value值，这个值继承自SuperClass，是SuperClass的静态字段。
 * 当读取或设置一个类型的静态字段，会初始化这个类，但是这个字段如果来自父类，则不会初始化这个类，只会初始化父类
 * 因此这里只会打印SuperClass init，而不会打印SubClass init
 */
public class NotInitialization {
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }
}