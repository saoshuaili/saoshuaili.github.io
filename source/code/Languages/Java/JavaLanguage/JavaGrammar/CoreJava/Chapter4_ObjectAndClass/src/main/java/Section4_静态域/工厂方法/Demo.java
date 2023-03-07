package Section4_静态域.工厂方法;

import java.text.NumberFormat;

public class Demo<x> {
    public static void main(String[] args) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();
        double x = 0.1;
        System.out.println(currencyFormatter.format(x));
        System.out.println(percentFormatter.format(x));
    }
}
