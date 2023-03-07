package Section6_对象构造.显式域初始化;

public class Demo {
    public static void main(String[] args) {
        Employee e = new Employee(19);
        Employee e2 = new Employee(true);
        System.out.println(e.getName());
        System.out.println(e.getAge());
        System.out.println(e.getB());
        System.out.println(e2.getName());
        System.out.println(e2.getAge());
        System.out.println(e2.getB());

    }
}

class Employee {
    //初值是常量
    private String name = "Harry";//将Harry这个初值赋予name，这样所有构造方法所创造的对象都拥有相同的name初值，不用重复赋值
    private int age;
    private boolean b;
    public Employee(int age){
        this.age = age;
    }
    public Employee(boolean b){
        this.b = b;
    }
    public int getAge() {
        return age;
    }
    public boolean getB() {
        return b;
    }
    public String getName() {
        return name;
    }

    //用方法初始化
    private static int nextId;
    private int id = assignId();//这样用方法初始化可以让每个雇员拥有自己所独有的id域，非常好

    private static int assignId(){
        int r = nextId;
        nextId++;
        return r;
    }

}
