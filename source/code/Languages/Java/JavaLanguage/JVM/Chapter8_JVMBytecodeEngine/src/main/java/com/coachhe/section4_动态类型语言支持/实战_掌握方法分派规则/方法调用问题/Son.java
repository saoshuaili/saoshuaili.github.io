package com.coachhe.section4_动态类型语言支持.实战_掌握方法分派规则.方法调用问题;

/**
 * @author CoachHe
 * @date 2022/12/29 15:26
 **/
public class Son extends Father {
    void thinking() {
        // 请填入适当的代码，实现调用祖父类的thinking()代码，打印"i am grandfather"
    }
}

class GrandFather {
    void thinking(){
        System.out.println("I am grandfather");
    }
}

class Father extends GrandFather {
    void thinking(){
        System.out.println("I am father");
    }
}
