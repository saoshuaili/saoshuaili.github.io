package Section4_内部类.G_静态内部类;

public class MyTest {
    public static void main(String[] args) {
        double[] d = new double[20];
        for (int i = 0; i < d.length; i++) {
            d[i] = Math.random() * 100;
        }
        ArrayLog2.Pair p = ArrayLog2.Pair.minmax(d);
        System.out.println(p.getFirst());
        System.out.println(p.getSecond());
    }
}

class ArrayLog2{
    public static class Pair{
        private double first;
        private double second;
        public Pair(double f, double s){
            first = f;
            second = s;
        }
        public double getFirst(){
            return first;
        }
        public double getSecond(){
            return second;
        }
        public static Pair minmax(double[] values) {
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (double v : values) {
                if (min > v) min = v;
                if (max < v) max = v;
            }
            return new Pair(min, max);
        }
    }

}
