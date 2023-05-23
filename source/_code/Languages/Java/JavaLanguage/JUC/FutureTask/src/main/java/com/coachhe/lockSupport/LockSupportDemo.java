package com.coachhe.lockSupport;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/24 1:10
 */
public class LockSupportDemo {
    Object objectLock = new Object();

    Thread t1 = new Thread(() -> {
        synchronized (objectLock) {
            System.out.println(Thread.currentThread().getName() + "\t");
        }
    }, "t1");
    t1.start();


}
