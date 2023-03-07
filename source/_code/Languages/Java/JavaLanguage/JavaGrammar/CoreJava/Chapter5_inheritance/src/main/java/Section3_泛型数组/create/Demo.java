package Section3_泛型数组.create;

import java.util.ArrayList;

public class Demo {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        int i = 0;
        String x;
        while(i < 10){
            x = "我是" + i;
            list.add(x);
        }


        //执行完删除操作后，使用toArray方法将数组元素拷贝到一个数组中
        String[] a = new String[list.size()];
        list.toArray(a);



        //除了在数组列表的尾部追加元素外，还可以在数组列表中间插入元素，使用带索引参数的add方法
//        int n = staff.size()/2;
//        staff.add(n, e);

        //同样可以从数组列表中间删除一个元素。
//        Employee e = staff.remove(n);
    }
}
