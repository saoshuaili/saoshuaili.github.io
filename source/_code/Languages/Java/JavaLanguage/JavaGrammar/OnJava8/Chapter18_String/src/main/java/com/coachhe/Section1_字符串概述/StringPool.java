package com.coachhe.Section1_字符串概述;

/**
 * @author CoachHe
 * @date 2022/12/15 02:20
 * 为了证明字符串常量池不能持有相同的两个字符串常量，多次引用时不会重新生成对应字符串
 **/
public class StringPool {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("1");
        System.out.println("2");
        System.out.println("3");
        System.out.println("4");
        System.out.println("5");
        System.out.println("6");
        System.out.println("7");
        System.out.println("8");
        System.out.println("9");
        System.out.println("10");

        System.out.println("1");
        System.out.println("2");
        System.out.println("3");
        System.out.println("4");
        System.out.println("5");
        System.out.println("6");
        System.out.println("7");
        System.out.println("8");
        System.out.println("9");
        System.out.println("10");
    }
}
