package Section4_内部类.F_匿名内部类;

public class MyTest {
    public static void main(String[] args) {
        Person queen = new Person() {
            @Override
            public void eat() {
                System.out.println("eat something 1");
            }
        };
        //a Person object
        Person count = new Child();
        queen.eat();
        count.eat();


    }
}

abstract class Person{
    public abstract void eat();
}

class Child extends Person {

    @Override
    public void eat() {
        System.out.println("eat something");
    }
}