package com.coachhe.Section1_用构造器保证初始化;

/**
 * @author CoachHe
 * @date 2022/12/28 00:18
 **/
public class SimpleConstructor2 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Rock2 rock2 = new Rock2(i);
        }
    }
}

class Rock2 {
    Rock2(int i) { // 这个就是构造器
        System.out.println("Rock " + i + " ");
    }
}
