package com.coachhe.future;

import java.util.concurrent.*;

/**
 * @Author: CoachHe
 * @Date: 2023/5/9 10:06
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " -----come in");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }, executorService).whenComplete((v, e) -> { // 代表没有异常的情况
            if (e == null) {
                System.out.println("=== 计算完成 ===");
            }
        }).exceptionally(e -> { // 有异常的情况
            e.printStackTrace();
            System.out.println("异常情况" + e.getCause() + ":\t" + e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + "线程先去忙其他事情了");
        
    }

    // 证明CompletableFuture可以完成Future的功能
    private static void futureLikeDemo() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "---come in");
            int result = ThreadLocalRandom.current().nextInt(10); //产生一个随机数
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1秒后出结果" + result);
            return result;
        }, executorService);

        System.out.println(Thread.currentThread().getName() + " 线程先去忙其他事了");
        System.out.println(integerCompletableFuture.get());

        executorService.shutdown();
    }

    // 创建一个CompletableFuture的四种静态方法
    private static void completableFutureStaticMethod() throws InterruptedException, ExecutionException {
        completableFutureSupplyAsync1();
        completableFutureSupplyAsync2();
        completableFutureAsync1();
        completableFutureAsync2();
    }

    private static void completableFutureSupplyAsync2() throws InterruptedException, ExecutionException {
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
