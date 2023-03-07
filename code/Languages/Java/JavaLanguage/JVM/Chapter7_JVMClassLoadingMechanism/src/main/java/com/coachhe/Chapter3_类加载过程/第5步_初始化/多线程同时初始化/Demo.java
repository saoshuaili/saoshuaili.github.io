package com.coachhe.Chapter3_类加载过程.第5步_初始化.多线程同时初始化;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {

    static class Hello{
        static {
            System.out.println(Thread.currentThread().getName() + " init");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(20);
        int i = 0;
        while (i++ < 20) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " start");
                    Hello h = new Hello();
                    System.out.println(Thread.currentThread().getName() + " end");
                }
            });
        }
    }
}

