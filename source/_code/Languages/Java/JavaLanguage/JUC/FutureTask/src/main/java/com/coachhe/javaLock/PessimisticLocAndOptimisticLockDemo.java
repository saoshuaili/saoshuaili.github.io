package com.coachhe.javaLock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/16 1:30
 */
public class LockSyncDemo {
    final Object object = new Object();

    // 悲观锁 demo1
    public void m1() {
        synchronized (object) {
            System.out.println("---- hello synchronized code block");
        }
    }

    // 悲观锁 demo2
    public synchronized void m2() {
        System.out.println("---- hello synchronized code m2");
    }

    // 悲观锁 demo3
    public synchronized static void m3() {
        System.out.println("---- hello m3");
    }

    // 乐观锁 demo
    public void m4() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();
    }

    public static void main(String[] args) {

    }
}
