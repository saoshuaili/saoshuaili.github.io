package Section4_内部类.A_使用内部类方法对象状态;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyTest {
    public static int times = 0;
    public static Timer timer;

    public static void main(String[] args) {
        timer = new Timer();
        timer.schedule(new MyTask(),1000,6000);
    }
}

class MyTask extends TimerTask {

    @Override
    public void run() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间为："+sdf.format(d));
        MyTest.times++;
        if (MyTest.times == 3) {
            System.out.println("当前人数：" + MyTest.times + " \r\n");
            MyTest.timer.cancel();
        }
    }
}
