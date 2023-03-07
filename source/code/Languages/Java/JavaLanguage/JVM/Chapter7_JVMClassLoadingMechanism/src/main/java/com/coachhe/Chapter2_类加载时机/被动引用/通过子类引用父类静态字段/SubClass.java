package com.coachhe.Chapter2_类加载时机.被动引用.通过子类引用父类静态字段;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubClass extends SuperClass {
    static {
        System.out.println("SubClass init!");
    }

}
