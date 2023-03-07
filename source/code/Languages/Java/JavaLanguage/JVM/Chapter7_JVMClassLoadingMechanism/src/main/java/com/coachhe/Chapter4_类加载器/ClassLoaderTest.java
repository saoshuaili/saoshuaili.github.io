package com.coachhe.Chapter4_类加载器;

/**
 * 这里查看了基本类的类加载器
 */
public class ClassLoaderTest {

    public static void main(String[] args) {

        //获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println("系统类加载器为 " + systemClassLoader);

        //获取其上层:拓展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println("系统类加载器的上层拓展类加载器为 " + extClassLoader);

        //获取其上层，获取不到引导类加载器
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();
        System.out.println("拓展类加载器的上层类加载器为 " + bootstrapClassLoader);

        //对于用户自定义类来说：默认使用系统类加载器
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println("用户自定义类的默认类加载器为 " + classLoader);

        // String使用引导类加载器，系统核心类库都是使用引导类加载器
        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println("String的类加载器为 " + classLoader1);
    }
}
