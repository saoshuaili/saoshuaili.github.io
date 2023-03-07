package com.coachhe.Chapter3_类加载过程.第4步_类加载器;

/**
 * @Author: CoachHe
 * @Date: 2022/11/23 13:24
 * 双亲委派机制，为了说明java.lang.String类型不能被自定义加载
 */
public class ParentClass {
    public static void main(String[] args) {
        String str = new String();
        // 这里输出为空，说明我们自定义的java.lang.String类没有被初始化，也就没有生效
        System.out.println(str);
    }
}
