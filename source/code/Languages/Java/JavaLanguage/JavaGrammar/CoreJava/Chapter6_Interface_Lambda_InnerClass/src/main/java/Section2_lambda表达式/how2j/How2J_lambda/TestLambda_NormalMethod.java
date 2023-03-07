package Section2_lambda表达式.how2j.How2J_lambda;

import Section2_lambda表达式.how2j.How2J_charactor.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestLambda_NormalMethod {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero> heros = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            heros.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heros);
        System.out.println("筛选出hp>100 && damage<50的英雄");
        filter(heros);
    }

    public static void filter(List<Hero> heros){
        for (Hero hero : heros) {
            if (hero.hp > 100 && hero.damage < 50) {
                System.out.println(hero);
            }
        }

    }
}
