package Section2_lambda表达式.how2j.Before;
import java.util.Arrays;
import java.util.Comparator;
public class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
    public static void main(String[] args) {
        LengthComparator l = new LengthComparator();
        int i = l.compare("1", "12");
        System.out.println(i);
        String strings[] = new String[]{"1234", "12", "123"};
        System.out.println(Arrays.toString(strings));
        Arrays.sort(strings, new LengthComparator());//��˼�ǽ�strings���ݳ�����������
        System.out.println(Arrays.toString(strings));
    }
}
