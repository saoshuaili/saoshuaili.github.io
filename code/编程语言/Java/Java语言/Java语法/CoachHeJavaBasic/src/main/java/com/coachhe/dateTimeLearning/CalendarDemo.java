package com.coachhe.dateTimeLearning;

import org.junit.Test;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/28
 * Time: 10:54
 * Description:
 */
public class CalendarDemo {

    // Calendar对象的创建实例
    @Test
    public void CalendarCreation() {
        // 创建一个代表系统当前日期的Calendar对象
        Calendar c = Calendar.getInstance();

        // 创建一个指定日期的Calendar对象
        Calendar c1 = Calendar.getInstance();
        c1.set(2023, 2, 28);
    }

    // Calendar对象的使用
    @Test
    public void CalendarUsage(){
        Calendar c = Calendar.getInstance();
        c.set(2023, 2, 28);
        // 可以只设置某个具体的值，例如把天置为10
        c.set(Calendar.DATE, 10);
        // 也可以把月份设置为3月
        c.set(Calendar.MONTH, 3);

        // 把对象的日期加上10，获取10天后的时间
        c.add(Calendar.DATE, 10);
        // 把对象的月份加上1，获取一个月后的时间
        c.add(Calendar.MONTH, 1);
        // 把对象的日期减去5，获取5天前的时间
        c.add(Calendar.DATE, -5);

        // 获取Calander对象的值信息
        int year = c.get(Calendar.YEAR); // 获取年份
        int month = c.get(Calendar.MONTH); // 
    }


}
