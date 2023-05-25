package com.coachhe.jmm;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/26 0:22
 */
public class VolatileVisibilityTest {

    static int x = 0, y = 0;
    static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {


        Set<String> resultSet = new HashSet<>();
        for (int i = 0; i < 10000000; i++) {
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread one = new Thread(() -> {
                a = y;
                x = 1;

            });

            Thread other = new Thread(() -> {
                b = x;
                y = 1;
            });

            one.start();
            other.start();
            one.join();
            other.join();

            resultSet.add("a=" + a + "." + "b=" + b);
            System.out.println(resultSet);
        }
    }

    private static boolean initFlag = false;
    // 展示volatile的用途，不加volatile修饰时，第一个线程会进入死循环
    private static void volatileUsageDemo1() {
        new Thread(() -> {
            System.out.println("waiting data...");
            while (!initFlag) {

            }
            System.out.println("--------------------success");
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(VolatileVisibilityTest::prepareData).start();
    }

    public static void prepareData(){
        System.out.println("----prepare data...");
        initFlag = true;
        System.out.println("----prepare data end...");
    }
}
