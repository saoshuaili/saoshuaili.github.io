package com.coachhe.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

        long startTime = System.currentTimeMillis();

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "task1 over";
        });
        threadPool.submit(futureTask1);

        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "task1 over";
        });
        threadPool.submit(futureTask2);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println("---cost time: " + (endTime - startTime) + " 毫秒");
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
