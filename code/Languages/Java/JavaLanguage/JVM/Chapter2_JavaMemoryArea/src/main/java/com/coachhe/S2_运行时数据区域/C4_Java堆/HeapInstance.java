package com.coachhe.S2_运行时数据区域.C4_Java堆;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author CoachHe
 * @date 2022/12/31 14:40
 * 利用该程序查看堆空间各个区域随着对象创建的变化情况
 * 以及Young GC情况下各个区域（S0,S1,Eden,老年代)区域的变化情况
 * VM参数：
 *  -Xms600m -Xmx600m
 **/
public class HeapInstance {
    byte[] buffer = new byte[new Random().nextInt(1024 * 200)];

    public static void main(String[] args) {
        ArrayList<HeapInstance> list = new ArrayList<>();
        while (true) {
            list.add(new HeapInstance());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
