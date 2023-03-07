package com.coachhe.section4_动态类型语言支持.虚方法和非虚方法;

/**
 * @author CoachHe
 * @date 2022/12/20 00:50
 **/
public class Son extends Father{
    public Son() {
        super();
    }

    public Son(int age) {
        this();
    }

    // 不是重写父类方法，因为静态方法不能被重写
    public static void showStatic(String str) {
        System.out.println("son " + str);
    }

    private void showPrivate(String str) {
        System.out.println("son private" + str);
    }

    public void show(){
        showStatic("coachhe.github.io"); // 自身的showStatic
        super.showStatic("good"); // 父类的showStatic
        showPrivate("hello"); // 自身的private方法
        super.showCommon(); // 父类的Common方法
        showFinal();

        showCommon();
        info();

        MethodInterface in = null;
        in.methodA();
    }

    public void info(){}
}

class Father {
    public Father() {
        System.out.println("father的构造器");
    }

    public static void showStatic(String str) {
        System.out.println("father " + str);
    }

    public final void showFinal() {
        System.out.println("father show final");
    }

    public void showCommon() {
        System.out.println("father 普通方法");
    }
}

interface MethodInterface{
    void methodA();
}
