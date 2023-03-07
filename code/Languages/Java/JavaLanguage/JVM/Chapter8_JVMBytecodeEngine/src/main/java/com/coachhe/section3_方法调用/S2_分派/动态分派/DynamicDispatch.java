package com.coachhe.section3_方法调用.S2_分派.动态分派;

/**
 * @author CoachHe
 * @date 2022/12/28 02:49
 * 动态分派延时
 **/
public class DynamicDispatch {
    static abstract class Human {
        protected abstract void sayHello();
    }

    static class Man extends Human {
        @Override
        protected void sayHello() {
            System.out.println("man say hello");
        }
    }

    static class Woman extends Human {
        @Override
        protected void sayHello() {
            System.out.println("woman say hello");
        }
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        man.sayHello(); // man say hello
        woman.sayHello(); // woman say hello
        man = new Woman();
        man.sayHello(); // woman say hello
    }
}
