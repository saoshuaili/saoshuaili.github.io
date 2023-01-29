package com.coachhe.util;

/**
 * @author CoachHe
 * @date 2023/1/30 01:01
 **/
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
