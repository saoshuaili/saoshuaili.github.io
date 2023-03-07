package com.coachhe.S2_运行时数据区域.JVMStack;

/**
 * 在这里演示了，无论任何异常，只要被抛出，就会返回。
 * 但是返回并不等于线程停止，若之后的方法有处理异常的机制，那么线程就会继续进行
 */
public class ReturnErrorTest {
    public static void main(String[] args) {
        ReturnErrorTest returnErrorTest = new ReturnErrorTest();
        try {
            returnErrorTest.methodA();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void methodA(){
        System.out.println("methodA 开始执行");
        methodB();
        System.out.println("methodA 执行结束");
    }

    public void methodB(){
        System.out.println("methodB 开始执行");
        try {
            methodC();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("methodB 执行结束");
    }

    public void methodC(){
        System.out.println("methodC 开始执行");
        System.out.println("methodC 执行结束");
        throw new RuntimeException();
    }

}
