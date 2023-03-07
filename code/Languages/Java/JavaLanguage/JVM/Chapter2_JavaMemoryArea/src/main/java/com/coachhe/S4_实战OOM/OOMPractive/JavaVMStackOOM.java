package com.coachhe.S4_实战OOM.OOMPractive;

/**
 * @Author: CoachHe
 * @Date: 2022/11/16 18:02
 * VM Args: -Xss2M
 */
public class JavaVMStackOOM {
    private void dontStop(){

    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
