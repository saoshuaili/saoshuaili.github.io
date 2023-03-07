package com.coachhe.S4_实战OOM.OOMPractive;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: CoachHe
 * @Date: 2022/11/16 14:48
 * @apiNote 实战Java堆内存溢出异常测试
 */
public class HeapOOM {
    static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
