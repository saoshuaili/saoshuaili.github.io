package com.coachhe.interrupt;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION: 中断的基本使用
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/21 22:48
 */
public class InterruptDemo {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println("----1");
        Thread.currentThread().interrupt();
        System.out.println("----2");
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
    }

    /**
     * 表明在执行sleep方法时，其他线程调用该线程的中断方法，那么会直接抛出异常，同时中断标识会被重新置位false
     */
    private static void interruptDemo03() {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().getName() + "\t" +
                            "中断标志位: " + Thread.currentThread().isInterrupted() + "程序终止");
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("-----hello interruptDemo03");
            }
        }, "t1");
        t1.start();
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(t1::interrupt).start();
    }

    /**
     * 前手案例，证明线程的中断标识符置为true之后，不会影响线程的正常运行
     */
    private static void interruptDemo02() {
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
            TimeUnit.MILLISECONDS.sleep(1);
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
