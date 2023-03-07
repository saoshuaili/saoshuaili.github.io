package com.coachhe.S2_运行时数据区域.方法区;

/**
 * @author CoachHe
 * @date 2023/1/3 00:14
 * 验证哪怕没有实例化对象，static修饰的变量和方法也是可以访问的
 **/
public class MethodArea {
    public static void main(String[] args) {
        Order order = null;
        order.hello();
        System.out.println(order.count);
    }
}

class Order {
    public static int count = 1;
    public static final int number = 2;

    public static void hello() {
        System.out.println("hello");
    }
}