package Section1_类超类和子类.S0_定义子类;


/**
 * This program demonstrates inheritance
 * @version 1.21 2019-08-13
 * @author CoachHe
 */


public class ManagerTest {

    public static void main(String[]args) {

        //construct a Manager object
        Manager boss = new Manager("Carl Cracker", 80000, 1987, 12, 15);
        boss.setBonus(5000);

        Employee[] staff = new Employee[3];

        //fill the staff array with Manager and Employee objects
//        boss = (Manager) staff[0];    //强制转换
        staff[0] = boss;
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tommy Tester", 40000, 1990, 3, 15);

        // print out information about all Employee objects
        for (Employee e : staff) {
            System.out.println("name = " + e.getName() + ", salary = " + e.getSalary());
        }


        //多态
        Manager boss2 = new Manager("Harry Hacker", 50000, 1989, 10, 1);
        Employee staff2 = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff2 = boss;
        boss.setBonus(5000);//correct
//        staff2.setBonus(5000);//Error

        //警告
        Manager[] managers = new Manager[10];//合法
        Employee[] staff3 = managers;          //同样合法
//        staff3[0] = new Employee("Hi", 10, 1,1,1);
//        managers[0].setBonus(5000);             //这种调用方法是错误的，这是因为编辑器是认为managers是个Manager对象，但是实际上和
        //staff3引用了同一个数组，因此就导致实际上没有setBonus这个方法


    }

}
