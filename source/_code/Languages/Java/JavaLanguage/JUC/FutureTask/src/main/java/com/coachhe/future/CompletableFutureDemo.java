package com.coachhe.future;

import java.util.concurrent.*;

/**
 * @Author: CoachHe
 * @Date: 2023/5/9 10:06
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello supplyAsync";
        }, executorService);
        System.out.println(stringCompletableFuture.get());

    }

    // 无线程池版本的supplyAsync
    private static void completableFutureSupplyAsync1() throws InterruptedException, ExecutionException {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello supplyAsync";
        });
        System.out.println(stringCompletableFuture.get());
    }

    // 带线程池版本的runAsync
    private static void completableFutureAsync2() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            // 停顿几秒钟
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "----end");
        }, executorService);
//        System.out.println(voidCompletableFuture.get());
        executorService.shutdown();
        System.out.println(Thread.currentThread().getName() + "---忙其他任务去了");
    }

    // 无线程池版本的runAsync
    private static void completableFutureAsync1() {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            // 停顿几秒钟
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "----come in");
        });
//        System.out.println(voidCompletableFuture.get());
        System.out.println(Thread.currentThread().getName() + "---忙其他任务去了");
    }
}
