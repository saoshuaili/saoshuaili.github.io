package com.coachhe.Chapter3_类加载过程.第5步_初始化;

/**
 * 当多个线程同时去初始化一个类时，只会有一个线程去执行这个类的<clinit>()方法，其他线程都需要阻塞等待
 * 直到活动线程执行完毕<clinit>()方法。
 * 因此，如果在一个类<clinit>()方法中有耗时很长的操作，那就可能造成多个进程阻塞
 * 接下来进行展示：
 */
public class DeadLoopClass {
    static class DealLoopClassTest {
        static {
            if (true) {
                System.out.println(Thread.currentThread() + "init DeadLoopClass");
                while (true) {
                }
            }
        }
    }

    public static void main(String[] args) {
        Runnable script = () -> {
            System.out.println("Thread.currentThread() " + "start");
            DealLoopClassTest dlc = new DealLoopClassTest();
            System.out.println("Thread.currentThread() " + "run over");
        };

        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }
}
