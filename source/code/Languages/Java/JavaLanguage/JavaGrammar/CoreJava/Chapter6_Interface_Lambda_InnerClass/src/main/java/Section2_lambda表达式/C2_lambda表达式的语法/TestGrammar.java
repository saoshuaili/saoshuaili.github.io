package Section2_lambda表达式.C2_lambda表达式的语法;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2022/12/8
 * Time: 20:03
 * Description:
 */
public class TestGrammar {
    public static void main(String[] args) {
        ClassB cb = new ClassB();
        cb.printClassB("classB", new InterA() {
            @Override
            public String printHi(String str) {
                return str;
            }
        });
    }
}

class ClassB {
    public void printClassB(String s, InterA interA) {
        System.out.println(interA.printHi(s));
    }
}

class ClassA implements InterA {

    @Override
    public String printHi(String str) {
        return str;
    }
}

interface InterA {
    String printHi(String str);
}