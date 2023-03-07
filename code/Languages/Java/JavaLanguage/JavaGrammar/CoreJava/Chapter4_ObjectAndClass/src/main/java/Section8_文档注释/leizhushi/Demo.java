package Section8_文档注释.leizhushi;


//类注释的例子：
/**
 * A {@code Card} object represents a playing card, such as "Queen of Hearts". A card has a suit (Diamond,
 * Heart, Spade or Club) and a value (1 = Ace, 2 ... 10, 11 = Jack, 12 = Queen, 13 = King)
 */
public class Demo {
    /**
     * The "Hearts" card suit
     */
    public static final int Hearts = 1;
    //这种方法一般用在静态常量上
    private double salary;
    /**
     * @deprecated Use <code> setVisible(true) </code> instead
     */

    /**
     * package.class#feature label
     * <a href=>"..."label</a>
     * "text"
     */

    /**
     * @see com.horstmann.corejava.Employee#raiseSalary(double)
     * @see <a href = "www.horstmann.com/corejava.html">The Core Java home page</a>
     * @see "Core Java 2 volume 2"
     * 第一行会建立一个链接到com.horstmann.corejava.Employee类
     * 的raiseSalary(double)方法的超链接。可以省略包名，甚至把包名和类名都省去，此时，链接将定位于当前包或当前类
     * 第二行@see标识后面有一个<字符，就需要指定一个超链接。可以超链接到任何URL
     * 第三行有("),文本会显示在"see also"部分。
     */
    //@see是通用标识，可以用在类，方法.....上





    /**
     * Raise the salary of an employee
     *
     * @param byPercent the percentage by which to raise the salary (e.g. 10 means 10%)
     * @return the amount of the raise
     */
    public double raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        salary += raise;
        return raise;
    }

}
