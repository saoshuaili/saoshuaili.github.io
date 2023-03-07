package com.coachhe.S3_HotSpot虚拟机对象探秘;

public class Demo {

    public static void main(String[] args) {
        String name;
//        System.out.println(name);
        name = "Hello Java";
        System.out.println(name.hashCode());
        System.out.println(name);
        StringBuffer s1 = new StringBuffer("Hello");
        StringBuffer s = s1;
        s.append(" hi");
        System.out.println(s);
        System.out.println(s1);
    }
}
