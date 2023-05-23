package com.coachhe.lockSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/24 1:10
 */
public class LockSupportDemo {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t---come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "\t--- 被唤醒");
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1");
        t1.start();

        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + "\t 我要进行唤醒");
        }, "t2");
        t2.start();
    }

    // 展示先notify后wait的报错通知
    private static void waitAndNotifyDemo03() {
        Object objectLock = new Object();

        Thread t1 = new Thread(() -> {
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

        Thread t2 = new Thread(() -> {
//            synchronized (objectLock) {
            objectLock.notify(); // ---唤醒
            System.out.println(Thread.currentThread().getName() + "\t --- 发出通知");
//            }
        }, "t2");
        t2.start();
    }

    // 展示了去掉synchronized之后对锁的操作会报错
    private static void waitAndNotifyDemo01() {
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
    private static void waitAndNotifyDemo02() {
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
