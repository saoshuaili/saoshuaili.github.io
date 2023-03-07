package Section2_lambda表达式.C4_方法引用;

import javax.swing.*;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2022/12/8
 * Time: 13:19
 * Description:
 */
public class TestMethodReference {
    public static void main(String[] args) {

        Timer timer_ori = new Timer(1000, event -> System.out.println(event));
        Timer timer = new Timer(1000, System.out::println);

        String[] strings = new String[]{"Abc", "bcd"};
        Arrays.sort(strings, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(strings));
    }
}
