package com.coachhe.javaLock;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION: 死锁案例
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/18 1:09
 */
public class DeadLockDemo {
    public void m1(){
        Object o1 = new Object();
        Object o2 = new Object();

        new Thread(() -> {
            synchronized (o1) {
                System.out.println(Thread.currentThread().getName() + "\t 持有o1锁，想获得o2锁");
            }
        })
    }
}
