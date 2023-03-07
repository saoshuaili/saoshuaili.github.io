package com.coachhe.Section2_重载加号与StringBuilder;

import org.junit.Test;

/**
 * @author CoachHe
 * @date 2022/12/15 02:37
 **/
public class Concatenation {

    // 证明常量拼接之后会通过编译器间优化放入常量池中
    @Test
    public void ConcatenationTest1(){
        String a = "abc";
        String b = "a" + "b" + "c";
        /**
         * 最终，java编译成.class，再执行.class
         * String a = "abc";
         * String b = "abc";
         */
        System.out.println(a == b); //true
        System.out.println(a.equals(b)); //true
    }

    // 证明拼接时只要有一个变量，结果就会放入堆中
    @Test
    public void ConcatenationTest2(){
        String s1 = "javaEE";
        String s2 = "hadoop";

        String s3 = "javaEEhadoop";
        String s4 = "javaEE" + "hadoop";
        String s5 = s1 + "hadoop";
        String s6 = "javaEE" + s2;
        String s7 = s1 + s2;

        System.out.println(s3 == s4); // true
        System.out.println(s3 == s5); // false
        System.out.println(s3 == s6); // false
        System.out.println(s3 == s7); // false
        System.out.println(s5 == s6); // false
        System.out.println(s5 == s7); // false
        System.out.println(s6 == s7); // false

        String s8 = s6.intern();
        System.out.println(s3 == s8); // true
    }

    // 为了说明用final声明的变量重载时不会自动使用new创建
    @Test
    public void ConcatenationTest3(){
        final String s1 = "a";
        final String s2 = "b";
        String s3 = "ab";
        String s4 = s1 + s2;
        System.out.println(s3 == s4);//true
    }

    //  append测试，代表直接用StringBuilder效率更高
    @Test
    public void ConcatenationTest4(){
        long start = System.currentTimeMillis();

        int highLevel = 100000;

//        method1(highLevel); //花费的时间为：4936
        method2(highLevel); //花费的时间为：4

        long end = System.currentTimeMillis();

        System.out.println("花费的时间为：" + (end - start));
    }

    // 直接使用字符串拼接
    public void method1(int highLevel) {
        String src = "";
        for (int i = 0; i < highLevel; i++) {
            src = src + "a"; // 每次循环都会创建一个StringBuilder
        }
    }

    // 使用StringBuilder的append操作
    public void method2(int highLevel) {
        // 只需要创建一个StringBuilder
        StringBuilder src = new StringBuilder();
        for (int i = 0; i < highLevel; i++) {
            src.append("a");
        }
    }

    public static void main(String[] args) {
        String mango = "mango";
        String s = "abc" + mango + "def" + 47;
        System.out.println(s);
    }

    public static void main_like(String[] args) {
        String mango = "mango";
        StringBuilder s = new StringBuilder();
        s.append("abc");
        s.append(mango);
        s.append("def");
        s.append(47);
        System.out.println(s.toString());
    }
}
