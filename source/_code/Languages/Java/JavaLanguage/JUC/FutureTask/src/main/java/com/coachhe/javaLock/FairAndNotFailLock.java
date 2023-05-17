package com.coachhe.javaLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @PROJECT_NAME: JUC
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/18 0:29
 * @DESCRIPTION: 公平锁和非公平锁
 */
public class FairAndNotFailLock {
    private int number = 30;
    // 非公平锁
    ReentrantLock lock = new ReentrantLock();

    public void sale() {
        lock.lock();
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出第: \t" + (number--) + "\t 还剩下:" + number);
                
            }
        } catch (Exception e){
            e.printStackTrace();

        }
    }

}
