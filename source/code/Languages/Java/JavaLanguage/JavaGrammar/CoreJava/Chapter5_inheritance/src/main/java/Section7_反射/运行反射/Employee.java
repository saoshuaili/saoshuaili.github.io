package Section7_反射.运行反射;

public class Employee {
    private String name;
    private double salary;

    public String getName() {
        return name;
    }

    public double getSalary(){
        return salary;
    }

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
}

