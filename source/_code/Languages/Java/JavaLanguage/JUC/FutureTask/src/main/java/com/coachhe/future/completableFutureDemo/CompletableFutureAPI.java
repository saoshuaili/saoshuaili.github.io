package com.coachhe.future.completableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/15 1:21
 */
public class CompletableFutureAPI {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        completeAPIDemo();
//        thenApplyAPIDemo();
        CompletableFuture.supplyAsync(() -> 1)
                .thenApply(f -> f + 2)
                .thenApply(f -> f + 3)
                .thenApply(f -> f + 4)
                .thenAccept(System.out::println);
    }

    private static void thenApplyAPIDemo() {
        //        completeAPIDemo();
        CompletableFuture.supplyAsync(() -> {
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("111");
            return 1024;
        }).thenApply(f -> {
            int a = 10 / 0;
            System.out.println("222");
            return f + 1;
        }).thenApply(f -> {
//            int a = 10 / 0;
            System.out.println("333");
            return f + 1;
        }).whenCompleteAsync((v, e) -> {
            System.out.println("---v: " + v);
        }).exceptionally(e -> {
            System.out.println("hi");
            e.printStackTrace();
            return null;
        });
        System.out.println("------ 主线程结束");
        // 主线程不立即结束，否则默认使用的线程池会关闭
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void completeAPIDemo() throws InterruptedException, ExecutionException {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2); // 执行需要2秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "abc";
        });

        try {
            TimeUnit.SECONDS.sleep(1); // 等待需要1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(stringCompletableFuture.complete("completable value" + "\t" + stringCompletableFuture.get()));
    }
}
