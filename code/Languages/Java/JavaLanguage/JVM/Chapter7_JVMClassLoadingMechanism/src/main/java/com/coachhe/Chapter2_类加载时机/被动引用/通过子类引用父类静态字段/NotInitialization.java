package com.coachhe.Chapter2_类加载时机.被动引用.通过子类引用父类静态字段;

public class NotInitialization {
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }
}
