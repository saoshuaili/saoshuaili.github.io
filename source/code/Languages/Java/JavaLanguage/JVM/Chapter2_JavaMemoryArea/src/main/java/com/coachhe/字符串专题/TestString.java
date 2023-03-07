package com.coachhe.字符串专题;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2022/12/14
 * Time: 19:11
 * Description:
 */
public class TestString {

    @Test
    public void test3(){
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";

        String s4 = s1 + s2;
        System.out.println(s3 == s4); // false
    }

    @Test
    public void test4(){
        final String s1 = "a";
        final String s2 = "b";
        String s3 = "ab";

        String s4 = s1 + s2;
        System.out.println(s3 == s4); // true
    }
}
