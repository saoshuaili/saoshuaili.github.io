package com.coachhe.section4_CompilerOptimization.S3_EscapeAnalysis.同步消除;

/**
 * @author CoachHe
 * @date 2023/1/2 03:11
 * 同步省略说明
 * 将以下代码反编译之后会发现锁消失了
 **/
public class SynchronizedElimination {
    public void f() {
        Object ho = new Object();
        synchronized (ho) {
            System.out.println(ho);
        }
    }
}
