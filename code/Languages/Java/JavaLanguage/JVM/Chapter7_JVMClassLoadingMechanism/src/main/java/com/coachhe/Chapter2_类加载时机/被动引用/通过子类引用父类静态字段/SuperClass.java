package com.coachhe.Chapter2_类加载时机.被动引用.通过子类引用父类静态字段;

public class SuperClass {
    static {
        System.out.println("SuperClass init!");
    }

    public static int value = 123;

}
