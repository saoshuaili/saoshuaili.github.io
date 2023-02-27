package com.coachhe.dateTimeLearning;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/27
 * Time: 11:40
 * Description:
 */
public class DateDemo {
    public static void main(String[] args) {
        // 打印时间
        Date now_date = new Date();
        System.out.println(now_date);

        // 比较时间方法1
        Date date1 = new Date(1); // 获取1970年1月1号之后1毫秒之后的时间
        Long now_time = now_date.getTime();
        Long time1 = date1.getTime();
        System.out.println(String.format("%s > %s", (now_time > time1 ? now_date.toString() : date1.toString()), (now_time > time1 ? date1.toString() : now_date.toString())));

        // 比较时间方法2
        boolean before = now_date.before(date1);
        boolean after = now_date.after(date1);
        boolean equals = now_date.equals(date1);
        System.out.println(before);
        System.out.println(after);
        System.out.println(equals);

    }
}
