package com.coachhe.future;

import java.util.concurrent.*;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @USER: CoachHe
 * @DATE: 2023/5/8 0:50
 */
public class FutureTaskPoolDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        m2();
    }


    // 三个任务，目前只有一个线程来处理，要耗时多久
    private static void m1() {
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

    // 三个任务，使用线程池来执行，需要耗时多久
    private static void m2() throws InterruptedException, ExecutionException {
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
            return "task2 over";
        });
        threadPool.submit(futureTask2);

        FutureTask<String> futureTask3 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "task3 over";
        });
        threadPool.submit(futureTask3);

        System.out.println(futureTask1.get());
        System.out.println(futureTask2.get());
        System.out.println(futureTask3.get());

        threadPool.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println("---cost time: " + (endTime - startTime) + " 毫秒");
    }

}
