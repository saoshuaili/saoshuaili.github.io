package Section1_接口.C9_对象克隆;

/**
 * @author CoachHe
 * @date 2022/12/8 01:43
 **/
public class TestClone {
    public static void main(String[] args) throws CloneNotSupportedException {
        Employee original = new Employee();
        Employee copy = original.clone();
    }
}

class Employee implements Cloneable {
    public Employee clone() throws CloneNotSupportedException {
        // call Object.clone()
        Employee cloned = (Employee) super.clone();

        // clone mutable fields

        return cloned;
    }
}
