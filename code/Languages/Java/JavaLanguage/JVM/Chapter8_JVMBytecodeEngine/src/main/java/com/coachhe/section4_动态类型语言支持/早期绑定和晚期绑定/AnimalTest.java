package com.coachhe.section4_动态类型语言支持.早期绑定和晚期绑定;

/**
 * 说明早期绑定和晚期绑定的例子
 * @author CoachHe
 * @date 2022/12/19 23:50
 **/
public class AnimalTest {
    public void showAnimal(Animal animal) {
        animal.eat();
    }

    public void showHunt(Huntable huntable) {
        huntable.hunt();
    }
}

class Animal {
    public void eat(){
        System.out.println("动物进食");
    };
}

interface Huntable {
    void hunt();
}

class Dog extends Animal implements Huntable {

    @Override
    public void eat() {
        System.out.println("狗吃骨头");
    }

    @Override
    public void hunt() {
        System.out.println("捕食耗子，多管闲事");
    }
}

class Cat extends Animal implements Huntable {

    @Override
    public void eat() {
        System.out.println("猫吃鱼");
    }

    @Override
    public void hunt() {
        System.out.println("猫吃耗子，天经地义");
    }
}

