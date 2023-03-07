package com.coachhe.Chapter4_类加载器.类加载的分类;

/**
 * @author CoachHe
 * @date 2023/1/12 23:59
 * 有显式加载和隐式加载
 **/
public class ClassLoadingTest {
    public static void main(String[] args) {
        User user = new User(); // 隐式加载

        try {
            Class.forName("com.coachhe.Chapter4_类加载器.类加载的分类.User");// 显式加载
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

class User {

}
