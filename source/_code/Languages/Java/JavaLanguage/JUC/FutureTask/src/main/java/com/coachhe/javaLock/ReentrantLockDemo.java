package com.coachhe.javaLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    // 在同步方法中实现可重入
    public static synchronized void m2(int i) {
        System.out.printf("---- print: %s ----\n", i);
        if (i <= 0) {
            return;
        }
        m2(i - 1);
    }

    public static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        lockDemo();
    }

    // lock可重入案例
    private static void lockDemo() {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t -- 外层调用");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "\t -- 内层调用");
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "t1").start();
    }



}
