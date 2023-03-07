package Section6_对象构造.调用另一个构造器;

public class Demo {
    public static void main(String[] args) {
        Employee[] a = new Employee [2];
        a[0] = new Employee(100);
        a[1] = new Employee(200);
        System.out.println(a[0].getId());
        System.out.println(a[0].getName());
        System.out.println(a[1].getId());
        System.out.println(a[1].getName());
    }

}

class Employee {
    private static int nextId;
    private String name;
    private int id;
    public Employee(double s){
        //calls Employee(String,double)
        this("Employee # "+ nextId, s );
        nextId++;
    }
    public Employee(String str, double s) {
        this.name = str;
        this.id = nextId;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

}