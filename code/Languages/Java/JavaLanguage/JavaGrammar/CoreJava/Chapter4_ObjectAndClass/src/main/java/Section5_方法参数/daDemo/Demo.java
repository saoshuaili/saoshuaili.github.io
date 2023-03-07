package Section5_方法参数.daDemo;

/**
 * This program demonstrates parameter passing in Java
 * @version 1.00 2019-8-7
 * @author CoachHe
 */
public class Demo {
    public static void main(String[] args) {
        /*
        Test1:Methods can't modify numeric parameters
         */
        System.out.println("Testing tripleValue:");
        double percent = 10;
        System.out.println("Before: percent = " + percent);
        tripleValue(percent);
        System.out.println("After: percent = " + percent);

        /*
        Test2:Methods can change the state of object parameters
         */
        System.out.println("\nTesting tripleSalary");
        Employee harry = new Employee("Harry", 50000);
        System.out.println("Before: salary = " + harry.getSalary());
        tripleSalary(harry);
        System.out.println("After: salary = " + harry.getSalary());

        /*
        Test3:Methods can't attach new objects to object parameters
         */
        System.out.println("\nTesting swap");
        Employee a = new Employee("Alice", 70000);
        Employee b = new Employee("Bob", 60000);
        System.out.println("Before: a = " + a.getName());
        System.out.println("Before: b = " + b.getName());
        swap(a,b);
        System.out.println("After: a = " + a.getName());
        System.out.println("After: b = " + b.getName());
    }

    public static void tripleValue(double p){
        p = p * 3;
    }

    public static void tripleSalary(Employee a){
        a.tripleSalary();
    }

    public static void swap(Employee a, Employee b){
        Employee y;
        y = a;
        a = b;
        b = a;
    }

}

class Employee{
    private double salary;
    private String name;
    public Employee(String str, double s) {
        name = str;
        salary = s;
    }
    public double getSalary(){
        return salary;
    }
    public String getName() {
        return name;
    }
    public void tripleSalary(){
        salary = salary * 3;
    }

}
