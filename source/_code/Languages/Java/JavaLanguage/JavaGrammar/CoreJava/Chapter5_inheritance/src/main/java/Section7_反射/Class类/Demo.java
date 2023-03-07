package Section7_反射.Class类;

import java.util.Random;

public class Demo{

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Employee e = new Employee("Harry", 5000, 1989, 10, 15);

        Class c1 = e.getClass();
        System.out.println(e.getClass().getName() + " " + e.getName());

        //如果类在一个包里，包的名字也作为类名的一部分
        Random generator = new Random();
        Class c2 = generator.getClass();
        String name = c2.getName();         //name is set to "java.util.Random
        System.out.println(c2 + " " + name);


        //调用静态方法forName获得类名对应的Class对象
        String className = "java.util.Random";
        Class c3 = Class.forName(className);

        //第三种或者class类对象的方法
        Class cl1 = Random.class;
        Class cl2 = int.class;
        Class cl3 = Double[].class;
        System.out.println(cl1.getName() + " " + cl2.getName() + " " + cl3.getName());


        //利用==运算符实现两个类对象的比较：
        Employee e2 = new Employee("hi", 1, 1, 1, 1);
        if (e.getClass() == Employee.class) {
            System.out.println(1);
        }


        //将forName和newInstance配合起来根据存储在字符串中的类名创建对象
        String s = "java.util.Random";
        Object m = Class.forName(s).newInstance();
        if (m.getClass() == Random.class) {
            System.out.println(2);
        }

    }
}
