package Section6_对象构造.无参构造器;

public class Demo {
    public static void main(String[] args) {
        Employee e = new Employee();
        e.setAge(18);
        e.setName("Harry");
        System.out.println(e.getAge());
        System.out.println(e.getName());
    }

}

class Employee {
    private int age;
    private String name;

    public void setAge(int age) {
        this.age = age;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public String getName() {
        return name;
    }
}
