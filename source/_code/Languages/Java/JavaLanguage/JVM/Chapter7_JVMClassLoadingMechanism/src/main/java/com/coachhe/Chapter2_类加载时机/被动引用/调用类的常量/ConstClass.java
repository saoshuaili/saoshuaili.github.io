package com.coachhe.Chapter2_类加载时机.被动引用.调用类的常量;

public class ConstClass {
    static {
        System.out.println("ConstClass init!");
    }

    public static final String HELLOWORLD = "hello world";
}
