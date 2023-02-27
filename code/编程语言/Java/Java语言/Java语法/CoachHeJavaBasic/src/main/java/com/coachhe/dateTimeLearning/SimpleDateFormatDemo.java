package com.coachhe.dateTimeLearning;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/27
 * Time: 17:04
 * Description:
 */
public class SimpleDateFormatDemo {

    public static void main(String[] args) {
        Date now_date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        System.out.println("当前时间为: " + ft.format(now_date));
    }
}
