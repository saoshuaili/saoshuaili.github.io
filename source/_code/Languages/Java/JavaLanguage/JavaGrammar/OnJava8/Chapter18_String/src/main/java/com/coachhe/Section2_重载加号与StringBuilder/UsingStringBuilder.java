package com.coachhe.Section2_重载加号与StringBuilder;

import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author CoachHe
 * @date 2022/12/15 22:49
 **/
public class UsingStringBuilder {

    // 最终的结果是用append()语句对每一部分进行拼接。
    public static String string1() {
        Random random = new Random(47);
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < 25; i++) {
            result.append(random.nextInt(100));
            result.append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("]");
        return result.toString();
    }

    public static String string2(){
        String result = new Random(47)
                .ints(25, 0, 100)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
        return "[" + result + "]";
    }

    public static void main(String[] args) {
        System.out.println(string1());
        System.out.println(string2());
    }
}
