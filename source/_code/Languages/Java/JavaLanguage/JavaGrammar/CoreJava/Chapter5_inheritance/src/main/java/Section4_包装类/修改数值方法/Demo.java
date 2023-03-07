package Section4_包装类.修改数值方法;

import org.omg.CORBA.IntHolder;

public class Demo {

    //三种更改方法值的方式
    //1.int
    public static void triple(int x){
        x = 3 * x;
    }

    //2.Integer
    public static void triple(Integer x) {
        x = 3 * 3;
    }

    public static void triple(IntHolder x){
        x.value = 3 * x.value;
    }
    public static void main(String[] args) {
        int x = 3;
        Integer y = 3;
        IntHolder z = new IntHolder(3);

        triple(x);
        triple(y);
        triple(z);

        System.out.println("int方法的输出值： " + x);
        System.out.println("Integer方法的输出值： " + y);
        System.out.println("IntHolder方法的输出值： " + z.value);


    }
}
