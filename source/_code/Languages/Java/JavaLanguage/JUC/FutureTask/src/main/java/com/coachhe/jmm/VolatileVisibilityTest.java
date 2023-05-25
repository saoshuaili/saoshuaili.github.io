package com.coachhe.jmm;

import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/26 0:22
 */
public class VolatileVisibilityTest {
    private static boolean initFlag = false;

    public static void main(String[] args) {
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
