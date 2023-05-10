package com.coachhe.future;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/10 23:06
 */
public class CompletableFutureChainDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CompletableFutureJoinAndGet();
    }

    // 比较了join()和get()两种方法的区别
    // join()是不用抛出异常的，但是get()需要
    private static void CompletableFutureJoinAndGet() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return "hello 1234";
        }, executorService);

        System.out.println(stringCompletableFuture.get());
        System.out.println(stringCompletableFuture.join());

        executorService.shutdown();
    }

    // 链式语法使用
    private static void chainReactDemo() {
        Student student = new Student();
        student.setId(12).setStudentName("li4").setMajor("english");
    }
}

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
class Student {
    private Integer id;
    private String studentName;
    private String major;
}
