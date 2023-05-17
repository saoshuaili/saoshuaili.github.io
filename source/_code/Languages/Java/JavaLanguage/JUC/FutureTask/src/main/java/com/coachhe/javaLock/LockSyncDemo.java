package com.coachhe.javaLock;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/16 1:30
 */
public class LockSyncDemo {
    final Object object = new Object();

    public void m1() {
        synchronized (object) {
            System.out.println("---- hello synchronized code block");
        }
    }

    public synchronized void m2() {
        System.out.println("---- hello synchronized code m2");
    }

    public synchronized static void m3() {
        System.out.println("---- hello m3");
    }

    public static void main(String[] args) {

    }
}
