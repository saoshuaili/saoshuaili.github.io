package com.coachhe.S2_运行时数据区域.C4_Java堆;

/**
 * @author CoachHe
 * @date 2022/12/30 01:52
 * VM 参数：
 * -Xms10m -Xmx10m -XX:+PrintGCDetails
 * 目的是为了查看堆内存的结构，运行之后查看
 * 注意：这里用jdk8才比较有效果
 **/
public class SimpleHeap {
    private int id;//属性，成员变量

    public SimpleHeap(int id) {
        this.id = id;
    }

    public void show() {
        System.out.println("My ID is " + id);
    }

    public static void main(String[] args) {
        SimpleHeap s1 = new SimpleHeap(1);
        SimpleHeap s2 = new SimpleHeap(2);
    }
}
