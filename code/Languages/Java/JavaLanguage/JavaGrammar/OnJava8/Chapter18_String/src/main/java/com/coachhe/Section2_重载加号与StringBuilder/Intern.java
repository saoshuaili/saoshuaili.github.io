package com.coachhe.Section2_重载加号与StringBuilder;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2022/12/15
 * Time: 20:09
 * Description:
 */
public class Intern {

    // 测试随着jdk版本变化，字符串常量池由只会存放实际字符串对象变成会存放堆的引用
    @Test
    public void internTest1(){
        String s = new String("1");
        s.intern(); // 调用此方法之前字符串常量池中已经存在1
        String s2 = "1";
        System.out.println(s == s2); // false

        String s3 = new String("1") + new String("8"); // 执行完之后字符串常量池中不存在"18"，和jdk版本无关
        s3.intern(); // jdk6： 在字符串常量池中生成新对象"18"， jdk7：在字符串常量池中生成堆中对象的引用
        String s4 = "18"; // s4变量记录的地址：使用的上一行代码执行时，在常量池中生成的"18"的地址
        System.out.println(s3 == s4); // jdk6及以前:false, jdk7及以后:true
    }

    // 何时执行intern方法的影响
    @Test
    public void internTest2(){
        String s3 = new String("1") + new String("8");
        String s4 = "18";
        s3.intern();
        System.out.println(s3 == s4); // false
    }

    @Test
    public void internTest3(){
        String s = new String("a") + new String("b");

        String s2 = s.intern(); // jdk6中：在常量池中创建一个字符串"ab"
                                // jdk7及以后：在常量池中没有创建字符串"ab",而是创建一个引用，指向new String("ab")，将此引用返回

        System.out.println(s2 == "ab"); // true
        System.out.println(s == "ab"); // jdk1.6及以前:false， jdk1.7及以后:true
    }

    // 两种创建对象的方法对结果的印象
    @Test
    public void internTest4(){
//        String s1 = new String("a") + new String("b"); // 不会在字符串常量池中创建"ab"常量
        String s1 = new String("ab"); // 会在字符串常量池中创建"ab"常量
        s1.intern();
        String s2 = "ab";
        System.out.println(s1 == s2);
    }

    // 空间效率测试
    @Test
    public void internTest5(){
        int MAX_COUNT = 1000 * 10000;
        String[] arr = new String[MAX_COUNT];
        Integer[] data = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        long start = System.currentTimeMillis();
        for (int i = 0; i < MAX_COUNT; i++) {
            arr[i] = new String(String.valueOf(data[i % data.length])).intern();
//            arr[i] = new String(String.valueOf(data[i % data.length]));
        }
        long end = System.currentTimeMillis();
        System.out.println("花费的时间为: " + (end - start));

        try {
            Thread.sleep(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
    }
}
