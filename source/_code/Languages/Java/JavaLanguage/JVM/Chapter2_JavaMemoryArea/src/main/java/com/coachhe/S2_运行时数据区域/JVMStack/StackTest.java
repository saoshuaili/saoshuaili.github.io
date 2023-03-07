package com.coachhe.S2_运行时数据区域.JVMStack;

/**
 * 这里有两个测试，methodA表示无限递归导致的StackOverflowError
 * methodB用来表示通过-Xss选项来设置栈内存的大小
 */
@SuppressWarnings("all")
public class StackTest {

    public void methodA(){
        methodA();
    }

    private static int i = 1;

    public void methodB(){
        i++;
        System.out.println(i);
        methodB();
    }


    public static void main(String[] args) {
        StackTest stackTest = new StackTest();
        stackTest.methodB();
    }

}
