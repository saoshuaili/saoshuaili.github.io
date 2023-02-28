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

    @Test
    public void CalendarCreation() {
        // 创建一个代表系统当前日期的Calendar对象
        Calendar c = Calendar.getInstance();

        // 创建一个指定日期的Calendar对象
        Calendar c1 = Calendar.getInstance();
        c1.set(2023, 2, 28);
    }


}
