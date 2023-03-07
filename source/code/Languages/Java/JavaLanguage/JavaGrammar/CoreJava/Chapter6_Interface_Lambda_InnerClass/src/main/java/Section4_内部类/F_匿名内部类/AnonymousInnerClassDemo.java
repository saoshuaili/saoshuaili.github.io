package Section4_内部类.F_匿名内部类;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;

/**
 * This program demonstrates anonymous inner class
 * @version 1.11 2019-10-26
 * @author CoachHe
 */
public class AnonymousInnerClassDemo {
    public static void main(String[] args) {
        TalkingClock clock = new TalkingClock();
        //使用匿名内部类
//        clock.start1(1000,true);
        //使用lambda表达式
        clock.start2(1000,true);
        //keep program running until user selects "OK"
        JOptionPane.showMessageDialog(null, "Quit Program?");
        System.exit(0);
    }
}

/**
 * A clock that prints the time in regular intervals
 */
class TalkingClock{
    /**
     * Starts the clock
     *
     * @param interval the interval between messages ( in milliseconds)
     * @param beep     true if the clock should beep
     */
    public void start1(int interval, boolean beep) {
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
        t.start();
    }

    public void start2(int interval, boolean beep) {
        Timer t = new Timer(interval, event ->
        {
            System.out.println("At the tone, the time is " + new Date());
            if (beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
                );
        t.start();
    }

}
