package Section6_对象构造.默认预初始化;

public class Demo {
    public static void main(String[] args) {
        Employee e = new Employee("Harry",50);
        System.out.println(e.getAge());
        System.out.println(e.getName());
    }
}

class Employee{
    private String name;
    private int age;

    public Employee(String s, int a) {
        this.name = s;
        this.age = a;
    }

    public int getAge() {
        return age;
    }
    public String getName() {
        return name;
    }
}
