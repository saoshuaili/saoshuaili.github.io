package com.coachhe.javaLock;

import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION: 死锁案例
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/18 1:09
 */
public class DeadLockDemo {

    public static void main(String[] args) {
        m1();
    }
    
    public static void m1(){
        Object o1 = new Object();
        Object o2 = new Object();

        new Thread(() -> {
            synchronized (o1) {
                System.out.println(Thread.currentThread().getName() + "\t 持有o1锁，想获得o2锁");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println(Thread.currentThread().getName() + "\t 持有o2锁，想获取o1锁");
                }
            }
        }, "A").start();


        new Thread(() -> {
            synchronized (o2) {
                System.out.println(Thread.currentThread().getName() + "\t 持有o2锁，想获得o1锁");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println(Thread.currentThread().getName() + "\t 持有o1锁，想获取o2锁");
                }
            }
        }, "B").start();
    }

}
