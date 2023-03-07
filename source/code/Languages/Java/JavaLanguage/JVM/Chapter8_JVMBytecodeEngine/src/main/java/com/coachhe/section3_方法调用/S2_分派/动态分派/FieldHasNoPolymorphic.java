package com.coachhe.section3_方法调用.S2_分派.动态分派;

/**
 * @author CoachHe
 * @date 2022/12/28 11:23
 * 字段不参与多态
 **/
public class FieldHasNoPolymorphic {
    static class Father {
        public int money = 1;
        public int home = 100;
        public Father() {
            money = 2;
            home--;
            showMeTheMoney();
        }
        public void showMeTheMoney() {
            System.out.println("I am Father, i have $" + money);
        }
        public void showHome(){
            System.out.println("I am Father, my home is " + home);
        }
    }

    static class Son extends Father {
        public int money = 3;
        // 子类对象会拥有父类对象的
        public Son() {
            money = 4;
            showMeTheMoney();
        }
        public void showMeTheMoney() {
            System.out.println("I am Son, i have $" + money);
        }
    }

    public static void main(String[] args) {
        Father guy = new Son();
        System.out.println("This guy has $" + guy.money);

        Son s = new Son();
        for (int i = 0; i < 10; i++) {
            s = new Son();
        }
        s.home = s.home - 1;
        int home = s.home;
        System.out.println(home);
    }
}
