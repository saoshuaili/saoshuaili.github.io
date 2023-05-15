package com.coachhe.javaLock;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/16 1:30
 */
public class LockSyncDemo {
    Object object = new Object();

    public void m1() {
        synchronized (object) {
            System.out.println("---- hello synchronized code block");
        }
    }

    public static void main(String[] args) {

    }
}
