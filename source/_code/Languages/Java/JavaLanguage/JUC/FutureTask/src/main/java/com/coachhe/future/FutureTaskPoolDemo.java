package com.coachhe.future;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @USER: CoachHe
 * @DATE: 2023/5/8 0:50
 */
public class FutureTaskPoolDemo {

    public static void main(String[] args) {

        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "task1 over";
        });
        Thread t1 = new Thread(futureTask1, "t1");
        t1.start();

    }

    private static void m1() {
        // 三个任务，目前只有一个main线程来处理，要耗时多久
        long startTime = System.currentTimeMillis();

        // 暂停毫秒
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            TimeUnit.MILLISECONDS.sleep(500);
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("---cost time: " + (endTime - startTime) + " 毫秒");
    }
}
