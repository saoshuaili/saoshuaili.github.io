package com.coachhe.Chapter4_类加载器.命名空间;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author CoachHe
 * @date 2023/1/13 02:46
 **/
public class UserClassLoader extends ClassLoader {
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
//                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            String fileName = "";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                return super.loadClass(name);
            }
            byte[] b = new byte[is.available()];
            is.read(b);
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }

    public static void main(String[] args) {
        String rootDir = "/Users/heyizhi/Notes/coachhe.github.io/code/编程语言/Java/Java语言/JVM/Chapter7_JVMClassLoadingMechanism/src";
        try {
            UserClassLoader classLoader1 = new UserClassLoader(rootDir);
            classLoader1.findClass("com.coachhe.Chapter4_类加载器.命名空间");
            UserClassLoader classLoader2 = new UserClassLoader(rootDir);
            classLoader2.findClass("com.coachhe.Chapter4_类加载器.命名空间");

            Class clazz1 = classLoader1.loadClass("ClassLoaderTest");
            Class clazz2 = classLoader2.loadClass("ClassLoaderTest");
            System.out.println(clazz1 == clazz2);

            System.out.println(clazz1.getClassLoader());
            System.out.println(clazz2.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
