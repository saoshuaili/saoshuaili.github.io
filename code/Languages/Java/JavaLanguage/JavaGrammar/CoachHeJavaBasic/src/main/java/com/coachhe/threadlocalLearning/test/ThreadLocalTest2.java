package com.coachhe.threadlocalLearning.test;

import com.coachhe.threadlocalLearning.ThreadLocalTest;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/27
 * Time: 13:05
 * Description:
 */
public class ThreadLocalTest2 {
    public static void test() {
//        ThreadLocal<String> threadLocal = ThreadLocalTest.threadLocal;
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        System.out.println(threadLocal.get());
    }
}
