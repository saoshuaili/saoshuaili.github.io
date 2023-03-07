package com.coachhe.section4_CompilerOptimization.S3_EscapeAnalysis.标量替换;

/**
 * @author CoachHe
 * @date 2023/1/2 03:14
 * 标量替换
 **/
public class ScalarReplacement {

    public static void main(String[] args) {
        alloc();
    }

    private static void alloc() {
        Point point = new Point(1, 2);
        System.out.println("point.x=" + point.x + "; point.y=" + point.y);
    }


    static class Point{
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
