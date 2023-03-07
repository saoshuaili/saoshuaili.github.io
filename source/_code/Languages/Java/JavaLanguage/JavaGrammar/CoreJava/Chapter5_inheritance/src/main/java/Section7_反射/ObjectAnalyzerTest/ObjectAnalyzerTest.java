package Section7_反射.ObjectAnalyzerTest;

import java.util.ArrayList;

/**
 * This program uses reflection to spy on objects
 * @version 1.12 2019-8-17
 * @author CoachHe
 */

public class ObjectAnalyzerTest {
    public static void main(String[] args) {
        ArrayList<Integer> squares = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            squares.add(i * i);
        }
        System.out.println(new ObjectAnalyzer().toString(squares));
    }
}

