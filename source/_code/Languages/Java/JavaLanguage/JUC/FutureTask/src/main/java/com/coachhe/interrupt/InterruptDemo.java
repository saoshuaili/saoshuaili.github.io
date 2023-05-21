package com.coachhe.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION: 中断的基本使用
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/21 22:48
 */
public class InterruptDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                System.out.println("------" + i);
            }
            System.out.println("after t1.interrupt()---第2次---" +
                    Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();
        System.out.println("before t1.interrupt()---" + t1.isInterrupted());
        t1.interrupt();
        try {
            TimeUnit.MILLISECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after t1.interrupt()---第1次---" + t1.isInterrupted());
        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after t1.interrupt()---第3次---" + t1.isInterrupted());

    }

    /**
     * 中断的基本使用
     */
    private static void interruptDemo01() {
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
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(t1::interrupt, "t1").start();
    }
}
