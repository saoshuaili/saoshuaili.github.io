package Section4_内部类.F_匿名内部类;

import java.util.ArrayList;
import java.util.List;

public class shuangkuohaochushihua {
    public static void main(String[] args) {
        ArrayList<String> friends = new ArrayList<>();
        friends.add("Harry");
        friends.add("tony");
        invite(friends);

        invite(new ArrayList<String>(){{add("harry");add("Tony");}});
        shuangkuohaochushihua s = new shuangkuohaochushihua();
        s.read();
    }

    public static void invite(List<String> list){
        System.out.println(list);
        new Object(){}.getClass().getEnclosingClass();
    }
    public void read(){
        System.err.println("Something awful happened in " + getClass());
    }
}


