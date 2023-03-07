package com.coachhe.Chapter3_类加载过程.第5步_初始化;

public class IllegalForwardReference {
    /**
     * 非法向前引用变量
     * 在这里第一次打印i失败了，第二次打印i就成功了，这是因为在初始化过程中执行clinit方法，这个方法是顺序执行的，
     * 第一次打印i时，还没有对i进行定义，因此打印失败了。
     * 第二次打印i时，已经成功定义了，所以打印成功了
     */
    static {
        i = 0; //给变量赋值是可以正常编译通过的
//        System.out.println(i); //这句编译器会提示"非法向前引用",illegal forward reference
    }
    static int i = 1;
    static {
        System.out.println(i); // 在这里就可以正常使用i这个值了
    }


    /**
     * clint方法执行顺序示例
     */
    static class Parent {
        public static int A = 1;
        static {
            A = 2;
        }
    }
    static class Sub extends Parent {
        public static int B = A;
    }
    public static void main(String[] args) {
        // 在这里打印的是2，因为父类中的静态语句要优于子类的变量赋值操作
        System.out.println(Sub.B);
    }

}
