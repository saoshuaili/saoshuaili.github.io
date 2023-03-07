package com.coachhe.section4_CompilerOptimization.S3_EscapeAnalysis.标量替换;

/**
 * @author CoachHe
 * @date 2023/1/2 13:37
 * 标量替换的过程，1 2 3分别是优化分析的步骤
 * VM 参数
 *  -Xmx256m -Xms256m -XX:+DoEscapeAnalysis -XX:+PrintGCDetails
 **/
public class StackReplacement1 {
    // 1. 完全未优化的代码
    public static int test(int x) {
        int xx = x + 1;
        Point p = new Point(xx, 42);
        return p.getX();
    }

    // 2. 标量替换后的样子
//    public static int test(int x) {
//        int xx = x + 1;
//        int px = xx;
//        int py = 42;
//        return px;
//    }

    // 3. 做无效代码消除后的样子
//    public static int test(int x) {
//        return x + 1;
//    }

    public static void main(String[] args) {
        // 开始时间
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000000; i++) {
            test(i);
        }

        // 结束时间
        long end = System.currentTimeMillis();

        System.out.println("总时长为: " + (end - start) + " ms");

        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class Point {
        int x;
        int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        int getX() {
            return this.x;
        }
    }
}
