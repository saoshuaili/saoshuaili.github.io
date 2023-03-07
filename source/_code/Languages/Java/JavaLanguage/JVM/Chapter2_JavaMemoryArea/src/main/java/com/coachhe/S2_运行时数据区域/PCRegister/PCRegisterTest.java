package com.coachhe.S2_运行时数据区域.PCRegister;

/**
 * 这个测试是为了用class文件查看PC寄存器的执行过程
 * 以及methodA调用methodB的方式
 */
public class PCRegisterTest {

    public void methodA(){
        int i = 10;
        int j = 20;
        methodB();
    }

    public void methodB(){
        int k = 30;
        int m = 40;
    }

    public static void main(String[] args) {
        int i = 10;
        int j = 20;
        int k = i + j;

        String s = "abc";
        System.out.println(i);
        System.out.println(k);

        PCRegisterTest pcRegisterTest = new PCRegisterTest();
        pcRegisterTest.methodA();
    }
}
