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
        Stream.of("a", "b", "c", "d", "e")
                .map(a -> {
                            System.out.printf("map print: %s\n", a);
                            return a.toUpperCase();
                        }
                )
                .filter(a -> {
                    try {
                        System.out.printf("filter %s start", a);
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("filter %s end", a);
                    return a.equals("B");
                })
                .forEach(a -> {
                    System.out.printf("for each print, %s\n", a);
                });
    }
}
