package Section7_反射.daDemo;

import java.lang.reflect.Method;

public class testDemo {
    public static void main(String[] args) {
        try {
            Class c1 = Class.forName("java.util.Date");
            haveMethods(c1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void haveMethods(Class c1) {
        Method[] m1 = c1.getMethods();
        for (Method m : m1) {
            System.out.print(m.getModifiers() + " ");
            System.out.println(m.getName());

        }
    }
}
