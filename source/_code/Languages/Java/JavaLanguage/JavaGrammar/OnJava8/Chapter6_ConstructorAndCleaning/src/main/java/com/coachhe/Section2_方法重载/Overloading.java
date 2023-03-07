package com.coachhe.Section2_方法重载;

/**
 * @author CoachHe
 * @date 2022/12/28 01:52
 * 展示的是重载方法对java程序的影响
 **/
public class Overloading {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Tree t = new Tree(i);
            t.info();
            t.info("overloading method");
        }
        // 调用重载构造器
        new Tree();
    }
}

class Tree{
    int height;
    Tree() {
        System.out.println("Planting a seedling");
        height = 0;
    }

    Tree(int initHeight) {
        height = initHeight;
        System.out.println("Creating new Tree that is " + height + " feet tall");
    }

    void info(){
        System.out.println("Tree is " + height + " feet tall");
    }

    void info(String s) {
        System.out.println(s + ": Tree is " + height + " feet tall");
    }
}


