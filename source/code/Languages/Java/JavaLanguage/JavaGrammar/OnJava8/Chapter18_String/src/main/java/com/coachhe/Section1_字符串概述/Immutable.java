package com.coachhe.Section1_字符串概述;

import org.junit.Test;

/**
 * @author CoachHe
 * @date 2022/12/15 01:00
 **/
public class Immutable {

    @Test
    public void ImmutableTest() {
        String s1 = "abc";
        String s2 = "abc";
        System.out.println(s1 == s2);
        s1 = "hello";
        System.out.println(s1 == s2);

        System.out.println(s1);
        System.out.println(s2);
    }

    @Test
    public void ImmutableTest2() {
        String s1 = "abc";
        String s2 = "abc";
        s2 += "def";
        System.out.println(s2); // abcdef
        System.out.println(s1); // abc
    }


    @Test
    public void ImmutableTest3(){
        String s1 = "abc";
        String s2 = s1.replace("a", "m");
        System.out.println(s1); // abc
        System.out.println(s2); // mbc
    }

    // 这里是为了展示Java字符串的不可变性，传入的其实是q对象引用的副本
    public static String upcase(String s) {
        return s.toUpperCase();
    }

    public static void main(String[] args) {
        String q = "howdy";
        System.out.println(q); //howdy
        String qq = upcase(q);
        System.out.println(qq); // HOWDY
        System.out.println(q); // howdy
    }

}
