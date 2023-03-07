package Section7_反射.Class类;

import java.time.LocalDate;

public class Employee {
    private String name;
    private double salary;
    private LocalDate hireDay;

    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = LocalDate.of(year, month, day);
    }

    public LocalDate getHireday(){
        return hireDay;
    }

    public java.lang.String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}
