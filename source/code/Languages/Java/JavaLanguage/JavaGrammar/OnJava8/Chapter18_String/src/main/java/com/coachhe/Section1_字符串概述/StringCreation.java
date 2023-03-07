package com.coachhe.Section1_字符串概述;

import org.junit.Test;

/**
 * @author CoachHe
 * @date 2022/12/17 14:58
 **/
public class StringCreation {
    /**
     * 主要展示两种创建方法的区别
     *  0 ldc #2 <abc>
     *  2 astore_1
     *  3 new #3 <java/lang/String>
     *  6 dup
     *  7 ldc #4 <def>
     *  9 invokespecial #5 <java/lang/String.<init> : (Ljava/lang/String;)V>
     * 12 astore_2
     * 13 return
     */
    @Test
    public void StringCreationTest1(){
        String str = "abc";
        String str_new = new String("def");
    }

    /**
     * 展示创建相同字符串时是否和上面的有区别
     *  0 ldc #2 <abc>
     *  2 astore_1
     *  3 new #3 <java/lang/String>
     *  6 dup
     *  7 ldc #2 <abc>
     *  9 invokespecial #5 <java/lang/String.<init> : (Ljava/lang/String;)V>
     * 12 astore_2
     * 13 return
     * 事实证明，没有区别
     * */
    @Test
    public void StringCreationTest2() {
//        String str = "abc";
        String str_new = new String("abc");
    }

    @Test
    public void StringCreationTest3() {
        String str = new String("a") + new String("b");
        str.intern();
    }


}
