package Section4_内部类.G_静态内部类;

public class StaticInnerClassTest {
    public static void main(String[] args) {
        double[] d = new double[20];
        for (int i = 0; i < d.length; i++) {
            d[i] = 100 * Math.random();
        }
        ArrayAlg.Pair p = ArrayAlg.minmax(d);
        System.out.println("min = " + p.getFirst());
        System.out.println("max = " + p.getSecond());
    }
}

class ArrayAlg{
    /**
     * A pair of floating-point numbers
     */
    public static class Pair{
        private double first;
        private double second;

        /**
         * Constructs a pair from two floating-point numbers
         * @param F the first number
         * @param s the second number
         */
        public Pair(double F, double s) {
            first = F;
            second = s;
        }

        /**
         * Return the first number of the pair
         * @return the first number
         */
        public double getFirst(){
            return first;
        }

        /**
         * Return the second number of the pair
         * @return the second number
         */
        public double getSecond(){
            return second;
        }
    }

    public static Pair minmax(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (double v : values) {
            if (min > v) min = v;
            if (min < v) max = v;
        }
        return new Pair(min, max);
    }
}
