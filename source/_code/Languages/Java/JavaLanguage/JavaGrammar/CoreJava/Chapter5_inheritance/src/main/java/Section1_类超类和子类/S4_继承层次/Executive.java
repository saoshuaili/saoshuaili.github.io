package Section1_类超类和子类.S4_继承层次;

import Section1_类超类和子类.S0_定义子类.Manager;

/**
 * @author CoachHe
 * @date 2022/12/20 19:48
 **/
public class Executive extends Manager {

    /**
     * @param name   the employee's name
     * @param salary the salary
     * @param year   the hire year
     * @param month  the hire month
     * @param day    the hire day
     */
    public Executive(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
    }

    public static void main(String[] args) {
        Executive e = new Executive("coachhe", 100, 26, 4, 3);
//        e.
    }
}
