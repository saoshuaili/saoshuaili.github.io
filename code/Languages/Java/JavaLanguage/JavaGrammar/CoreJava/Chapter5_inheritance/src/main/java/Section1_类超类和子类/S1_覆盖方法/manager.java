package Section1_类超类和子类.S1_覆盖方法;

public class manager extends Employee{
    private double bonus;
    public void setBonus(double bonus){
        this.bonus = bonus;
    }

    public double getSalary(){
        double baseSalary = super.getSalary();
        return baseSalary + bonus;
//        double a = this.getSalary();
//        return a;
    }

    public static void main(String[] args) {
        manager m = new manager("Harry", 1000, 2019,8,13);
//        m.setName("MANAGER");
        System.out.println(m.getName());
    }

    public manager(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        bonus = 0;

    }



}
