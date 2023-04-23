package com.coachhe.lambda表达式;

import java.util.Comparator;

public class lambdaTest {
    public static void main(String[] args) {

        // 原始方法
        Comparator<Integer> com1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };

        // lambda表达式
        Comparator<Integer> com2 = (o1, o2) -> {
            return Integer.compare(o1, o2);
        };

        // 方法3
        Comparator<Integer> com3 = Integer::compare;

    }
}
