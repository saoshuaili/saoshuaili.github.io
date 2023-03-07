package Section2_lambda表达式.how2j.How2J_lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Section2_lambda表达式.how2j.How2J_charactor.Hero;
import Section2_lambda表达式.how2j.How2J_charactor.HeroChecker;

public class TestLambda_nimingneibulei {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero> heros = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化之后的集合");
        System.out.println(heros);
        System.out.println("使用匿名内部类的方式，筛选出hp>100 && damage<50的英雄");
        HeroChecker checker = new HeroChecker() {
            @Override
            public boolean test(Hero h) {
                return (h.hp>100 && h.damage<50);
            }
        };
        filter(heros, checker);
    }

    private static void filter(List<Hero> heros, HeroChecker checker) {
        for (Hero hero : heros) {
            if (checker.test(hero)) {
                System.out.println(hero);
            }
        }
    }
}
