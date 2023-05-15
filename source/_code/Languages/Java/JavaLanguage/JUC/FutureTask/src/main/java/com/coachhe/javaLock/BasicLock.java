package com.coachhe.javaLock;

import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @USER: coachhe
 * @DATE: 2023/5/15 14:06
 */
public class BasicLock {
    public static void main(String[] args) {
        Phone phone = new Phone();
//        Phone phone2 = new Phone();
//        new Thread(phone::sendEmail, "a").start();
        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();
        // 暂停200毫秒，保证a线程先启动
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            phone.sendSMS();
        }, "a").start();
//        new Thread(phone2::sendSMS, "b").start();
//        new Thread(phone::hello, "b").start();
    }
}

class Phone {
    public static synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------sendEmail");
    }

    public synchronized void sendSMS() {
        System.out.println("------sendSMS");
    }

    public void hello() {
        System.out.println("-------hello");
    }
}
