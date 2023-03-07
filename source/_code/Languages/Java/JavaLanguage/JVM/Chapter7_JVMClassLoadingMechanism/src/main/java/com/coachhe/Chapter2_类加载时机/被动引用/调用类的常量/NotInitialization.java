package com.coachhe.Chapter2_类加载时机.被动引用.调用类的常量;

public class NotInitialization {
    public static void main(String[] args) {
        System.out.println(ConstClass.HELLOWORLD);
    }
}
