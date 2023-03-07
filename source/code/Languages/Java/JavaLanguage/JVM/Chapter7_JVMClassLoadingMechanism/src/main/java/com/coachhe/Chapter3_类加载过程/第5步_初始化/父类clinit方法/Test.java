package com.coachhe.Chapter3_类加载过程.第5步_初始化.父类clinit方法;

public class Test{
    static class Parent {
        public static int A = 1;
        static {
            A = 2;}

    }
    static class Sub extends Parent {
        public static int B = A;
    }

    public static void main(String[] args) {
        System.out.println(Sub.B);
    }
}