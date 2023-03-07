package com.coachhe.Chapter2_类加载时机;

public class SubClass extends SuperClass{
    static {
        System.out.println("SubClass init");
    }
}
