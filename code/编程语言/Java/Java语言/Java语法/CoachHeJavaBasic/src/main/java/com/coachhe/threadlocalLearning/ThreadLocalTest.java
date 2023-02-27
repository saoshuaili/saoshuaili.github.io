package com.coachhe.threadlocalLearning;

import com.coachhe.threadlocalLearning.test.ThreadLocalTest2;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/27
 * Time: 11:01
 * Description:
 */
public class ThreadLocalTest {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    public static void main(String[] args) {
        threadLocal.set("name");
        String s = threadLocal.get();
        System.out.println(s);
        ThreadLocalTest2.test();
    }
}
