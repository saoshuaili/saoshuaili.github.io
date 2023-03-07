package Section2_lambda表达式.how2j.Before;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;


public class Worker implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //do some work
        TimerTest();
    }

    public static void TimerTest(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("timer????");
            }
        },1000,10000);
    }

    public static void main(String[] args) {
        Worker w = new Worker();
        System.out.println(System.getProperty("user.home"));
    }
}


