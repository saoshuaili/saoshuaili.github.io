package com.coachhe.javaLock;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION: 可重入锁，不是 ReEntrantLock类
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/18 0:49
 */
public class ReentrantLockDemo {

    // 在同步块中实现可重入
    public static void m1(){
        final Object objectLockA = new Object();

        new Thread(() -> {
            synchronized (objectLockA) {
                System.out.println("-- 外层调用 --");
                synchronized (objectLockA) {
                    System.out.println("-- 内层调用 --");
                }
            }
        }, "a").start();
    }

    public static void main(String[] args) {
        m1();
    }

}
