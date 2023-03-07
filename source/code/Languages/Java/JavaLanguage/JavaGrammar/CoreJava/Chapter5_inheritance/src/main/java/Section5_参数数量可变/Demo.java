package Section5_参数数量可变;

public class Demo {

    public static double max(double... values) {
        double largest = Double.NEGATIVE_INFINITY;
        for (double v : values) {
            if (v > largest) {
                largest = v;
            }
        }
        return largest;
    }


    public static void main(String[] args) {
        int n = (int) 1.11;
        //调用两个参数
        System.out.printf("%d", n);
        System.out.println();
        //调用三个参数
        System.out.printf("%d %s", n, "widgets");
        System.out.println();
        //调用两个参数和调用三个参数其实是同一个方法,甚至可以调用更多参数
        //调用四个参数
        System.out.printf("%d %d %s", n, n, "widgets");
        System.out.println();

        //调用自定义可变参数的方法
        double m = max(3.1, 40.4, - 5);
        System.out.println(m);



    }
}
