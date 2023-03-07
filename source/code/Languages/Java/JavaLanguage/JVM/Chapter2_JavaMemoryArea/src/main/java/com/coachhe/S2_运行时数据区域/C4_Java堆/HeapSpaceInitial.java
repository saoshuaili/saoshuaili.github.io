package com.coachhe.S2_运行时数据区域.C4_Java堆;

/**
 * @author CoachHe
 * @date 2022/12/30 02:11
 * 查看堆内存的初始大小以及堆内存大小设置
 * 设置堆空间大小一共有两个参数：
 * - `-Xms10M` 设置堆空间起始内存为 10M，等价于 `-XX:InitialHeapSize`
 * - `-Xmx10M` 设置堆空间最大为 10M，等价于 `-XX:MaxHeapSize`
 * 默认内存大小:
 * - 初始内存大小 = 物理电脑内存大小 / 64
 * - 最大内存大小 = 物理电脑内存大小 / 4
 *
 * 第一次尝试不使用参数输入
 * 第二次输入VM参数：
 *  -Xms600M -Xmx600M
 *  在开发中建议将初始堆内存和最大堆内存设置成相同值
 **/
public class HeapSpaceInitial {
    public static void main(String[] args) {
        // 返回Java虚拟机中堆内存总量
        long initialMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        // 返回Java虚拟机视图使用的最大堆内存量
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;

        System.out.println("-Xms : " + initialMemory + "M");
        System.out.println("-Xmx : " + maxMemory + "M");

        System.out.println("系统内存大小为： " + initialMemory * 64.0 / 1024 + "G");
        System.out.println("系统内存大小为： " + maxMemory * 4.0 / 1024 + "G");
    }
}
