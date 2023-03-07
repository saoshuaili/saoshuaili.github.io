package Section7_反射.异常处理;

public class xiao {
    public static void main(String[] args) {
        try {
            String name = "Harry"; //get class name
            Class c1 = Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("ClassNotFound");
        }
    }

    }

