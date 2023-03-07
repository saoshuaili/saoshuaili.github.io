package com.coachhe.Chapter2_类加载时机.被动引用.通过数组定义引用类;

import com.coachhe.Chapter2_类加载时机.被动引用.通过子类引用父类静态字段.SuperClass;

public class NotInitialization {
    public static void main(String[] args) {
        SuperClass[] sca = new SuperClass[10];
    }
}
