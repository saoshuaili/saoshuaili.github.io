package com.coachhe.section2_运行时栈帧结构;

public class ByteCodeTest {
    public static int test1(int a) {
        try {
            a += 20;
            return a;
        } finally {
            a += 30;
            return a;
        }
    }

    public static int test2(int b) {
        try {
            b += 20;
            return b;
        } finally {
            b += 30;
            System.out.println("b = " + b);
        }
    }

//    public int test3(int b) {
//        try {
//            b += 20;
//            return b;
//        } finally {
//            b += 30;
//            System.out.println("b = " + b);
//        }
//    }

    public static void main(String[] args) {
        int num = 10;
        System.out.println(test1(num));
        System.out.println(test2(num));
//        ByteCodeTest byteCodeTest = new ByteCodeTest();
//        System.out.println(byteCodeTest.test3(num));
    }

}
