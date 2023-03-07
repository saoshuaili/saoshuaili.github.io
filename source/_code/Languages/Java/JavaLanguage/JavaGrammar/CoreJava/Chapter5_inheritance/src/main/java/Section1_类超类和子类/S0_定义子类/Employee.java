package Section1_类超类和子类.S0_定义子类;
import	java.time.LocalDate;

public class Employee {
    private String name;
    private double salary;
    private LocalDate hireDay;

    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        hireDay = LocalDate.of(year, month, day);
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDay() {
        return hireDay;
    }

    public String getName() {
        return name;
    }

    public void raiseSalary(double bypercent) {
        double raise = salary * bypercent/100;
        salary += raise;
    }


}
