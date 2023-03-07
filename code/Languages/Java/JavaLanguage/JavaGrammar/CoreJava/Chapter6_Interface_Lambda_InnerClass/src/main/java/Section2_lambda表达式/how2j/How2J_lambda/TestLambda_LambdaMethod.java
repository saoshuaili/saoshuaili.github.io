package Section2_lambda表达式.how2j.How2J_lambda;

import Section2_lambda表达式.how2j.How2J_charactor.Hero;
import Section2_lambda表达式.how2j.How2J_charactor.HeroChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestLambda_LambdaMethod {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero> heroes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合");
        System.out.println(heroes);
        System.out.println("使用Lambda的方式，筛选出hp>100 && damage<50的英雄");
        filter(heroes,h -> h.hp > 100 && h.damage < 50);
    }

    private static void filter(List<Hero> heroes, HeroChecker checker) {
        for (Hero hero : heroes) {
            if (checker.test(hero)) {
                System.out.println(hero);
            }
        }
    }
}
