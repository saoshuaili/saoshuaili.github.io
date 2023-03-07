package com.coachhe.section3_方法调用.S2_分派.单分派和多分派;

/**
 * @author CoachHe
 * @date 2022/12/28 12:32
 * 单分派，多分派演示
 **/
public class Dispatch {
    static class QQ{}
    static class _360{}

    public static class Father{
        public void hardChoice(QQ arg) {
            System.out.println("father choose QQ");
        }
        public void hardChoice(_360 arg) {
            System.out.println("father choose 360");
        }
    }

    public static class Son extends Father{
        public void hardChoice(QQ arg) {
            System.out.println("son choose QQ");
        }
        public void hardChoice(_360 arg) {
            System.out.println("son choose 360");
        }
    }

    public static void main(String[] args) {
        Father father = new Father();
        Father son = new Son();
        father.hardChoice(new _360());
        son.hardChoice(new QQ());
    }
}
