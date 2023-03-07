package com.coachhe.S3_HotSpot虚拟机对象探秘;

public class ClassDemo {
    public void print(String s) {
        System.out.println(s);
    }

    public static void print2(String s) {
        System.out.println(s);
    }

    public ClassDemo(int id){
        this.id = id;
    }

    int id = 1;

    static int j = 1;

    public static void main(String[] args) {
        ClassDemo c1 = new ClassDemo(1);
        ClassDemo c2 = new ClassDemo(1);
        System.out.println(c1.getClass());
        System.out.println(c2.getClass());
        System.out.println(c1.getClass() == c2.getClass());
        System.out.println(c1 == c2);
        System.out.println(c1.hashCode() == c2.hashCode());
        Object object;
    }

}
