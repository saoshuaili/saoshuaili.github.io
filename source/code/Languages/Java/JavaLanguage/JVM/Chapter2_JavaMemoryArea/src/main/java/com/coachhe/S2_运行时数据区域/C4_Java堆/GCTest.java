package com.coachhe.S2_运行时数据区域.C4_Java堆;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CoachHe
 * @date 2022/12/31 15:18
 * 为了查看GC对各个区域的影响
 * VM参数：
 *  -Xms9m -Xmx9M -XX:+PrintGCDetails
 **/
public class GCTest {
    public static void main(String[] args) {
        int i = 0;
        try {
            List<String> list = new ArrayList<>();
            String a = "coachhe";
            while (true) {
                list.add(a);
                a = a + a;
                i++;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("遍历次数为： " + i);
        }
    }
}
