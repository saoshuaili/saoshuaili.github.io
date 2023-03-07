package com.coachhe.S4_实战OOM.OOMPractive;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: CoachHe
 * @Date: 2022/11/16 18:21
 * VM Args: -XX:PermSize=6M -XX:MaxPermSize=6M
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        // 使用Set保持常量池应用，避免Full GC回收常量池行为
        Set<String> set = new HashSet<>();
        // 在short范围内足以让6MB的PermSize产生OOM了
        short i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }

}
