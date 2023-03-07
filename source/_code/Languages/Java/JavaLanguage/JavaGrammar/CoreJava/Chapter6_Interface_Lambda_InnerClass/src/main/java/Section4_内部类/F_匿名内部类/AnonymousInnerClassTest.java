package Section4_内部类.F_匿名内部类;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Timer;

public class AnonymousInnerClassTest {
    public static void main(String[] args) {

    }

    public void start(int interval, boolean beep){
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("At the tone, the time is " + new Date());
                if (beep) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        };
        Timer t = new Timer(interval, listener);

    }

}
