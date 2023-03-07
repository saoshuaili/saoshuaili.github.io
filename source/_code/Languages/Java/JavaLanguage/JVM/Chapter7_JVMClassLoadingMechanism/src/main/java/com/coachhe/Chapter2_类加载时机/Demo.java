package com.coachhe.Chapter2_类加载时机;

public class Demo extends Parent{
    static {
        System.out.println("Demo");
    }
    public static void main(String[] args) throws ClassNotFoundException {
        Class clz = Class.forName("第2节_类加载的时机.Demo");
//        Demo demo = new Demo();
    }
}

class Parent{
    static {
        System.out.println("Parent");
    }
}
