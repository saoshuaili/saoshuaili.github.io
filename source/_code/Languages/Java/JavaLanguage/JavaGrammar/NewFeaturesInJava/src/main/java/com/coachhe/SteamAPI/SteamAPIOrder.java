package com.coachhe.SteamAPI;

import org.w3c.dom.ls.LSOutput;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @PROJECT_NAME: NewFeaturesInJava
 * @DESCRIPTION: 测试Steam中函数的执行顺序
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/14 11:53
 */
public class SteamAPIOrder {
    public static void main(String[] args) {
//        streamOrder();
        Stream.of("a", "b", "c", "d", "e");

    }



    private static void streamOrder() {
        Stream.of("a", "b", "c", "d", "e")
                .map(a -> {
                            try {
                                System.out.printf("map %s start\n", a);
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.printf("map %s end\n", a);
                            return a.toUpperCase();
                        }
                )
                .filter(a -> {
                    try {
                        System.out.printf("filter %s start\n", a);
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("filter %s end\n", a);
                    return a.equals("B");
                })
                .forEach(a -> {
                    try {
                        System.out.printf("foreach %s start\n", a);
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("foreach %s end\n", a);
                });
    }
}
