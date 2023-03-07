package Chapter4_预定义类.用户自定义类;

import java.util.Date;

/**
 * This program tests the Demo class
 * @version 23.01 2019-08-15
 * @author CoachHe
 */
public class Demo {

    public static void main(String[] args) {
        //fill the staff array with three Employee objects
        Employee[] staff = new Employee [3];

        staff [0] = new Employee("E1", 75000, 1987, 12, 15);
        staff [1] = new Employee("E2", 50000, 1989, 10, 1);
        staff [2] = new Employee("E3", 40000, 1990, 3, 15);

        //给所有人加工资
        for(Employee e : staff){
            e.raiseSalary(5);
        }

        //将所有人信息重新打印
        for (Employee e : staff){
            System.out.println(e.toString());
        }

        //错误例子：
        Employee harry = new Employee("Harry", 100, 1999,1888,1777);
        Date d = harry.getHireDay();
        double tenYeasInMilliSeconds = 10 * 365.25 * 24 * 60 * 60 * 1000;
        //下面这两行代码都会报错
//        d.setTime(d.getTime() - (long) tenYeasInMilliSeconds);
//        d.setTime(1);
        //let's give Harry tens yeas of added seniority
        //这是有错的，因为d和Harry.hireDay引用了同一对象，对d调用更改器方法就可以自动改变这个雇员对象的私有状态
    }


}
