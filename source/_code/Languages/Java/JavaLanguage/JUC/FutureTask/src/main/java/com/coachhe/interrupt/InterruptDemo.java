package com.coachhe.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/21 22:48
 */
public class InterruptDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "\t stop");
                    break;
                }
                System.out.println("t1 ---hello interrupt");
            }
        }, "t1");
        t1.start();
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(t1::interrupt, "t1").start();
    }
}
