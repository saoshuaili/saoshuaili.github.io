package Section5_方法参数.texing;

public class Demo {
    public static void tripleValue(double x){        //doesn't work，进去的值是x的拷贝值
        x = x * 3;
    }
    public static void tripleSalary(Employee e){
        e.raiseSalary(500);
    }
    public static void main(String[] args) {
        double percent = 10;
        tripleValue(percent);                       //在这里，percent的值不会有丝毫改变，这是因为java采用按值调用，不能改变方法参数的值
        System.out.println(percent);
        Employee e = new Employee();
        tripleSalary(e);
        System.out.println(e.getSalary());          //在这里，salary的值会被改变，这是因为java调用的拷贝值改变了对象
    }

}
class Employee{
    private double salary;
    public void raiseSalary(double s){
        salary = s;
        salary = salary * 1.5;
    }
    public double getSalary(){
        return salary;
    }
}