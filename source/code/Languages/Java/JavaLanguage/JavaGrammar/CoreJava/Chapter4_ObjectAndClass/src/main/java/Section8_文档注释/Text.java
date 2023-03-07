/**
 * @author CoachHe
 * @version  Java 8
 */

public class Text {

    public static void main(String[] args) {
        Employee employee = new Employee();
        System.out.println(employee.returnName("Harry"));
    }

}

class Employee {
    /**
     * The "Hearts" card suit
     */
    public static final int HEARTS = 1;

    private String name;

    /**
     * @return String
     * @param name
     */
    public String returnName(String name){
        this.name = name;
        return name;
    }

}
