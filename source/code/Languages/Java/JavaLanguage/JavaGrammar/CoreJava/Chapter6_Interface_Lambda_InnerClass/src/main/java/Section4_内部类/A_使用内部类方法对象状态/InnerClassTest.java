package Section4_内部类.A_使用内部类方法对象状态;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
/**
 * This program demonstrates the use of inner classes
 * @version 1.11 2019-10-21
 * @author CoachHe
 */
public class InnerClassTest {
    public static void main(String[] args) {
        TalkingClock clock = new TalkingClock(1000, true);
        clock.start();

        //keep program running until user selects "OK"
        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }
}
/**
 * A clock that prints the time in regular intervals
 */
class TalkingClock
{
    private int interval;
    private boolean beep;
    /**
     * Constructs a talking clock
      * @param interval the interval between messages ( in milliseconds )
     * @param beep true if the clock should beep
     */
    public TalkingClock(int interval, boolean beep) {
        this.interval = interval;
        this.beep = beep;
    }
    /**
     * Starts the clock
     */
    public void start(){
        ActionListener listener = new TimePrinter();
        Timer t = new Timer(interval, listener);
        t.start();
    }

    public class TimePrinter implements ActionListener
        // an inner class
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("At the tone, the time is " + new Date());
            if (TalkingClock.this.beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
