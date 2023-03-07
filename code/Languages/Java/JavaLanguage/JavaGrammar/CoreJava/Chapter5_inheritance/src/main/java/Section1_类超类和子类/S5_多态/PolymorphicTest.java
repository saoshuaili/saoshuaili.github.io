package Section1_类超类和子类.S5_多态;

import Section1_类超类和子类.S0_定义子类.Employee;
import Section1_类超类和子类.S0_定义子类.Manager;

/**
 * @author CoachHe
 * @date 2022/12/20 20:16
 **/
public class PolymorphicTest {
    public static void main(String[] args) {
        Employee e;
//        e = new Manager("manager", 1, 1, 1, 1); //ok
        e = new Employee("employee", 1, 1, 1, 1); //ok

        Manager[] managers = new Manager[10];
        Employee[] staff = managers; // 此时是合理的，因为将经理转换为员工是合理的
        staff[0] = e; // 此时好像也是合理的，因为这就是一个员工类，但实际上这应该是不合理的，因为这实际是一个经理类
        System.out.println(staff[0]); // 直接报错ArrayStoreException
    }
}
