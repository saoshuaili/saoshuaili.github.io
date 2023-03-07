package Chapter4_预定义类.对象和对象变量;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.Date;

public class Demo {
    public static void main(String[] args) {
        System.out.println();
        //创造一个对象，需要在构造器前面加上new操作符，例如：
        //Date类
        System.out.println(new Date().toString());
        String s = new String("12fsdfs");
        System.out.println(s);
        Date birthday = new Date();
        //Date deadline;
        //deadline = birthday;
        Date deadline = new Date();
        if (deadline == birthday) {
            System.out.println("deadline = birthday");
        }

        //LocalDate类
        LocalDate localDate = LocalDate.now();//创建了当前时间所对应的年月日
        System.out.println(localDate.toString());
        LocalDate aThousandDaysLater = localDate.plusDays(1000);//创建了一个一千天以后的对象
        System.out.println(aThousandDaysLater.toString());

        //制作一个日历
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();  //返回值是这个月的几月，也就是将当前是几月存放在month中
        int today = date.getDayOfMonth();  //返回值是这个月的第几天，也就是将当前是几号存放在today中
        date = date.minusDays(today - 1);  //将date设置为这个月的第一天
        DayOfWeek weekday = date.getDayOfWeek();//返回值是当前星期几的大写英文表示，例如：THURSDAY
        int value = weekday.getValue();    //1=Monday,...,7=Sunday
        System.out.println(weekday);
        System.out.println("Mon Tue Wed Thu Fri Sat Sun");
        for (int i = 1; i < value; i++) {
            System.out.print("  ");         //作用是将日历的第一天（数字1）移动到对应的星期下面
        }
        while (date.getMonthValue() == month) {//当当前月数不是month（也就是这个月结束了）的时候结束
            System.out.printf("%3d", date.getDayOfMonth());
            if (date.getDayOfMonth() == today) {//作用是将当天的天数标记出来
                System.out.print("*");
            } else {
                System.out.print(" ");
            }
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 1) {//每到周一的时候换行
                System.out.println();
            }
        }
        if (date.getDayOfWeek().getValue() != 1) {
            System.out.println();

        }


    }
}
