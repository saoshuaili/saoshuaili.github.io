package Section1_类超类和子类.S1_覆盖方法;

public class Employee {
    private String name = "";
    private double salary;
    private int hireDay;

    public int getHireDay() {
        return hireDay;
    }

    public void setHireDay(int hireDay) {
        this.hireDay = hireDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = year;
    }



}
