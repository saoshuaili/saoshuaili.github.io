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

        //c的使用
        System.out.printf("全部日期和时间信息：%tc%n",now_date);
        //f的使用
        System.out.printf("年-月-日格式：%tF%n",now_date);
        //d的使用
        System.out.printf("月/日/年格式：%tD%n",now_date);
        //r的使用
        System.out.printf("HH:MM:SS PM格式（12时制）：%tr%n",now_date);
        //t的使用
        System.out.printf("HH:MM:SS格式（24时制）：%tT%n",now_date);
        //R的使用
        System.out.printf("HH:MM格式（24时制）：%tR",now_date);

        // 使用toString()显示日期和时间
        System.out.printf("%1$s %2$tB %2$td, %2$tY",
                "Due date:", now_date);
    }
}
