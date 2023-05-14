package com.coachhe.SteamAPI;

import org.w3c.dom.ls.LSOutput;

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
                            System.out.println(a);
                            return a.toUpperCase();
                        }
                )
                .filter(a -> {
                    System.out.printf("%s equals b ? %s\n", a, a.equals("b"));
                    return a.equals("b");
                })
                .forEach(System.out::println);
    }
}
