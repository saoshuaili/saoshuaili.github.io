package com.coachhe.Chapter2_类加载时机;

/**
 * 被动引用场景3
 * 在这里不会输出ConstClass init，这是因为HELLOWORLD在常量传播优化时已经将这个常量的值直接存储在
 * NotInitialization3类的常量池中，以后该类对ConstClass.HELLOWORL的引用实际上都被转化成了
 * NotInitialization3类对自身常量池的引用，因此不会初始化
 */
public class NotInitialization3 {
    public static void main(String[] args) {
        System.out.println(ConstClass.HELLOWORLD);
    }
}
