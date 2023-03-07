package com.coachhe.Chapter3_类加载过程.第5步_初始化.clinit方法执行顺序;

public class Test {
    static {
        i = 0;
//        System.out.println(i);
    }
    static int i = 0;
    static {
        System.out.println(i);
    }

    public static void main(String[] args) {

    }
}
