package com.coachhe.juc.base;

public class DaemonDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 开始运行， " +
                    (Thread.currentThread().isDaemon() ? "守护线程" : "用户线程"));
        }, "t1");
        
        t1.start();
    }
}
