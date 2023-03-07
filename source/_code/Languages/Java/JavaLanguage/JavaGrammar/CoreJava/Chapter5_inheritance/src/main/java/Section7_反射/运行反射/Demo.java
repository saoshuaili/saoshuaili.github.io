package Section7_反射.运行反射;

import java.lang.reflect.Field;

public class Demo {
    public static void main(String[] args) {
        Employee harry = new Employee("Harry", 1);
        Class c1 = harry.getClass();
            // the class object representing Employee
        try {
            Field f = c1.getDeclaredField("name");
            f.setAccessible(true);
            Object v = f.get(harry);
            System.out.println(f.getName());
                //the value of the name field of the harry object, i.e., the String object "Harry"
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
