package Section1_接口.C4_静态和私有方法;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2022/12/7
 * Time: 13:26
 * Description:
 */
public class DefaultTest {
    public static void main(String[] args) {
        Bag bag = new Bag();
        bag.sayHi();
    }
}

class Bag implements InterfaceA {

}

interface InterfaceA {
    default void sayHi(){
        System.out.println("Hi, I am interface A");
    }
}
