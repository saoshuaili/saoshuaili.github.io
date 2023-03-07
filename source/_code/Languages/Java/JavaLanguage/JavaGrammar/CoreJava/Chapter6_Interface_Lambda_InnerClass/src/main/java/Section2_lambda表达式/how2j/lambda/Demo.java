package Section2_lambda表达式.how2j.lambda;
import java.util.*;

public class Demo {
    public static void main(String[] args) {
        //排序
//        String[] heroes = new String[]{"Earth", "Venus", "Mars", "Saturn"};
//        System.out.println("排序之前的heroes");
//        test2 t = new test2();
//        Arrays.sort(heroes,t);
//        System.out.println("排序之后的heroes");
//        System.out.println(Arrays.toString(heroes));

//        //
//        Timer timer = new Timer(1000,event ->
//        {
//            System.out.println("At the tone, the time is " + new Date());
//            Toolkit.getDefaultToolkit().beep();
//        });
//        BiFunction<String, String , Integer > comp
//                = (first, second) -> first.length() - second.length();



    }
}
class test implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.length()-o2.length();
    }
}
