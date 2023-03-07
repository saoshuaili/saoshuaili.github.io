package Section2_lambda表达式.how2j.How2J_lambda;

import Section2_lambda表达式.how2j.How2J_charactor.Hero;
import Section2_lambda表达式.how2j.How2J_charactor.HeroChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class niming_to_lambda {

    public static void main(String[] args) {
        Random r = new Random();
        List<Hero> heroes = new ArrayList<Hero>();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heroes);
        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");

        //匿名内部类正常写法
        HeroChecker c1 = new HeroChecker() {
            @Override
            public boolean test(Hero h) {
                return (h.hp > 100 && h.damage < 50);
            }
        };

        //把外面的壳子去掉,只保留方法参数和方法体，参数和方法体之间加上符号 ->
        HeroChecker c2 = (Hero h) ->{
            return h.hp > 100 && h.damage < 50;
        };

        //把return和{}去掉
        HeroChecker c3 = (Hero h) -> h.hp > 100 && h.damage < 50;

        //把参数类型和圆括号去掉（只有一个参数的时候，才可以去掉圆括号）
        HeroChecker c4 = h -> h.hp > 100 && h.damage < 50;

        //把c4作为参数传递进去
        filter(heroes,c4);

        //直接把表达式传递进去
        filter(heroes, h -> h.hp > 100 && h.damage < 50);
    }



    private static void filter(List<Hero> heroes, HeroChecker checker) {
        for (Hero hero : heroes) {
            if (checker.test(hero)) {
                System.out.println(hero);
            }
        }
    }

}
