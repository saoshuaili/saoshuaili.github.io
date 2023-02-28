package com.coachhe.dateTimeLearning;

import org.junit.Test;

import java.text.ParseException;
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

    // 解析字符串为时间
    @Test
    public void strToDate() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        String input = "1818-11-11";

        System.out.println(input + " Parses as ");

        Date t;

        try {
            t = ft.parse(input);
            System.out.println(t);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // printf格式化打印时间
    @Test
    public void printfDate() {
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
