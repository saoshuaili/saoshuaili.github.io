package com.coachhe.Chapter2_类加载时机;

public class DD {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("第2节_类加载的时机.Demo");
        Demo d = new Demo();
    }
}
