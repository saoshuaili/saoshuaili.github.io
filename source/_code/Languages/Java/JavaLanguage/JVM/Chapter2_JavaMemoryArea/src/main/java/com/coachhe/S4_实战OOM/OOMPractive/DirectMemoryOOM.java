package com.coachhe.S4_实战OOM.OOMPractive;

//import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Author: CoachHe
 * @Date: 2022/11/16 19:01
 * VM Args: -Xmx20M -XX:MaxDirectMemorySize=10M
 */
public class DirectMemoryOOM {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws Exception{
//        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
//        Class a = unsafeField.getClass();
//        Class b = Field.class;
//        Class c = Unsafe.class;
//        System.out.println(a == b);
//        System.out.println(a == c);
//        unsafeField.setAccessible(true);
//        Unsafe unsafe = (Unsafe) unsafeField.get(null);
//        while (true) {
//            unsafe.allocateMemory(_1MB);
//        }
    }
}
