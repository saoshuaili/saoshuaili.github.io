package com.coachhe.Section1_用构造器保证初始化;

/**
 * @author CoachHe
 * @date 2022/12/28 00:18
 **/
public class SimpleConstructor {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Rock rock = new Rock();
        }
    }
}

class Rock {
    Rock() { // 这个就是构造器
        System.out.print("Rock ");
    }
}
