package Section4_内部类.D_局部内部类;

import java.util.Arrays;
import java.util.Date;

public class Demo2 {
    public static void main(String[] args) {
        Date[] dates = new Date[100];
        int[] counter = new int[1];
//        int counter = 0;
        for (int i = 0; i < dates.length; i++) {
            dates[i] = new Date()
            {
                public int compareTo(Date other) {
                    counter[0]++;//Error
                    return super.compareTo(other);
                }
            };
        }
        Arrays.sort(dates);
        System.out.println(counter[0] + " comparisons.");
    }
}
