package Section1_接口.C2_接口的属性;

/**
 * @author CoachHe
 * @date 2022/12/6 01:42
 **/
@SuppressWarnings("all")
public class interfaceTest implements Comparable<Integer>{
    @Override
    public int compareTo(Integer o) {
        return 0;
    }
    public static void main(String[] args) {
        classA testclass = new classA();
        System.out.println(testclass instanceof interfaceA);
        classB testclassB = new classB();
        System.out.println(testclassB.intA);
    }
}
interface interfaceA {
    String intA = "string of interface A";
}
interface interfaceB extends  interfaceA{}
class classA implements interfaceA{}
class classB implements interfaceB{}