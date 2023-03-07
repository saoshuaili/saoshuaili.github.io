package com.coachhe.S1_流支持;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/3/5
 * Time: 22:19
 * Description:
 */
public class Randoms {
    public static void main(String[] args) {
        new Random(47)
                .ints(5, 20)
                .distinct()
                .limit(7)
                .forEach(System.out::println);
    }
}
