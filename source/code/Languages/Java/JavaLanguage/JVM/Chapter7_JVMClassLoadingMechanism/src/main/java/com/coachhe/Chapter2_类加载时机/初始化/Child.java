package com.coachhe.Chapter2_类加载时机.初始化;

public class Child extends Parent {
    static{
        System.out.println("Child 初始化。。。");
    }

    public static void main(String[] args) {

    }
}
