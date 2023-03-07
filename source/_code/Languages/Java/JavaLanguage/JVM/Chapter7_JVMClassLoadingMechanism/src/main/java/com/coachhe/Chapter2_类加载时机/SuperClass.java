package com.coachhe.Chapter2_类加载时机;

public class SuperClass {
    static {
        System.out.println("SuperClass init");
    }
    public static int value = 123;

}
