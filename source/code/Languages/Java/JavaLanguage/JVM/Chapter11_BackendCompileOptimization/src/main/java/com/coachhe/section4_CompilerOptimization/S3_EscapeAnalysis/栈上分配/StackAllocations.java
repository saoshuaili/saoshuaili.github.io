package com.coachhe.section4_CompilerOptimization.S3_EscapeAnalysis.栈上分配;

/**
 * @author CoachHe
 * @date 2023/1/2 02:49
 * 栈上分配
 * VM参数
 * 不打开逃逸分析：-Xmx1G -Xms1G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails
 * 打开逃逸分析：  -Xmx1G -Xms1G -XX:+DoEscapeAnalysis -XX:+PrintGCDetails
 **/
public class StackAllocations {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000000; i++) {
            alloc();
        }

        // 查看执行时间
        long end = System.currentTimeMillis();
        System.out.println("花费的时间为： " + (end - start) + " ms");

        // 为了方便查看堆内存中对象的个数，现成sleep
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void alloc() {
        User user = new User(); //未发生逃逸
    }

    static class User {

    }

}

