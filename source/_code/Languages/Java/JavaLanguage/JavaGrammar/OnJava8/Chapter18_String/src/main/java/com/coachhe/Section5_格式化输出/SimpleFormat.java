package com.coachhe.Section5_格式化输出;

/**
 * @author CoachHe
 * @date 2022/12/16 02:00
 **/
public class SimpleFormat {
    public static void main(String[] args) {
        int x = 5;
        double y = 5.332542;
        // The old way:
        System.out.println("Row 1: [" + x + " " + y + "]");
        // The new way:
        System.out.format("Row 1: [%d %f]%n", x, y);
        // or
        System.out.printf("Row 1: [%d %f]%n", x, y);
    }
}
