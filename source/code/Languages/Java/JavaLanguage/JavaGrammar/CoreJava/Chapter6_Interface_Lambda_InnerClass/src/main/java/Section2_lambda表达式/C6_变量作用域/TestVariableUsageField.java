package Section2_lambda表达式.C6_变量作用域;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author CoachHe
 * @date 2022/12/10 18:00
 **/
public class TestVariableUsageField {

    static String text2 = "Hello2";


    public static void main(String[] args) throws InterruptedException {
        repeatMessage("Hello", 1000);
        Thread.sleep(500000);
    }

    // 在lambda表达式中访问外围方法或类中的变量
    public static void repeatMessage(String text, int delay) {
        ActionListener listener = event -> {
            System.out.println(text);
            System.out.println(text2);
            Toolkit.getDefaultToolkit().beep();
        };
        new Timer(delay, listener).start();
    }

    // 不合法的做法，引用值被改变了
    public static void countDown(int start, int delay) {
        ActionListener listener = event -> {
//            start--;
            System.out.println(start);
        };
        new Timer(delay, listener).start();
    }

    // 不合法的做法2，lambda表达式中引用的对象在外部可能会改变。
    public static void repeat(String text, int count) {
        for (int i = 0; i < count; i++) {
            ActionListener listener = event -> {
//                System.out.println(i + ": " + text);

            };
            new Timer(1000, listener).start();
        }
    }
}
