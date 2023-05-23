package com.coachhe.lockSupport;

import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/24 1:10
 */
public class LockSupportDemo {

    public static void main(String[] args) {

        Object objectLock = new Object();

        Thread t1 = new Thread(() -> {
//            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "\t ---come in");
                try {
                    objectLock.wait();  /// --- 先等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            }
        }, "t1");
        t1.start();
        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t2 = new Thread(() -> {
//            synchronized (objectLock) {
                objectLock.notify(); // ---唤醒
                System.out.println(Thread.currentThread().getName() + "\t --- 发出通知");
//            }
        }, "t2");
        t2.start();
    }

    // 表明正常情况下，wait和notify的用法
    private static void LockSupportDemo01() {
        Object objectLock = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "\t ---come in");
                try {
                    objectLock.wait();  /// --- 先等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");
        t1.start();
        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t2 = new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify(); // ---唤醒
                System.out.println(Thread.currentThread().getName() + "\t --- 发出通知");
            }
        }, "t2");
        t2.start();
    }


}
