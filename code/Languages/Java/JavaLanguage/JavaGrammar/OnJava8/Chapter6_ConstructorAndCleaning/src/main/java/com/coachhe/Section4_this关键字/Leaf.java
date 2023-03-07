package com.coachhe.Section4_this关键字;

/**
 * @author CoachHe
 * @date 2022/12/28 00:31
 **/
public class Leaf {
    int i = 0;
    Leaf increment() {
        i++;
        return this;
    }

    void print() {
        System.out.println("i = " + i);
    }

    public static void main(String[] args) {
        Leaf x = new Leaf();
        x.increment().increment().increment().print();
    }
}
