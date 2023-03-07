package com.coachhe.Chapter2_类加载时机;

/**
 * 被动引用示例2：
 * 在这里不会有输出，这是因为通过数组定义来引用类，不会触发此类的初始化
 */
public class NotInitialization2 {
    public static void main(String[] args) {
        SuperClass[] sca = new SuperClass[10];
    }
}
