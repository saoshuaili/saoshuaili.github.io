package com.coachhe;

public class Demo {
    public static void main(String[] args) {
        String a = new String("abc");  //对象abc创建
        String b = a;
        a = new String("hi");
        b = new String("Hello");       //对象abc无用
    }
}
