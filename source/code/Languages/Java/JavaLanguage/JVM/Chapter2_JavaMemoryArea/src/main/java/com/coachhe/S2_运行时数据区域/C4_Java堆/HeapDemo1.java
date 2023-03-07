package com.coachhe.S2_运行时数据区域.C4_Java堆;

/**
 * @author CoachHe
 * @date 2022/12/30 00:45
 * 用来在VisualVM中查看堆空间的分布情况，并且证明堆空间是和进程统一的（每个进程有一个堆空间）
 * VM 参数：-Xms20m -Xmx20m
 **/
public class HeapDemo1 {
    public static void main(String[] args) {
        System.out.println("start...");
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end...");
    }
}
