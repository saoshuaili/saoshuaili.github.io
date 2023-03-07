package Section7_包.接收的包;

// the Employee class is defined in that package

import Section7_包.发射的包.Employee;

import static java.lang.System.*;


/**
 * This program demonstrates the use of package
 * @version 1.11 2019-8-8
 * @author CoachHe
 */

public class Demo {
    public static void main(String[] args) {
        //because of the import statement, we don't have to use fashedeba2.Employee2 here
        Employee harry = new Employee("Harry Hacker", 5000, 1989, 10, 1);

        harry.raiseSalary(5);

        //because of the static import statement , we don't have to use System.out here
        out.println("name = " + harry.getName() + " , salary = " + harry.getSalary());
        Demo d = new Demo();
//        out.println(d.i);

    }
}
