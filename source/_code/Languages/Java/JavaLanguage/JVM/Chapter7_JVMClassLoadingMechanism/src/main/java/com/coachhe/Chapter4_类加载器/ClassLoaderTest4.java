package com.coachhe.Chapter4_类加载器;

import java.io.IOException;
import java.io.InputStream;

/**
 * 这里演示了不同类加载器对instanceof关键字运算的结果的影响
 */
public class ClassLoaderTest4 {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassLoader myLoader = new ClassLoader() {
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
        };

        Object obj = myLoader.loadClass("com.coachhe.Chapter4_ClassLoader.ClassLoaderTest4").newInstance();
        // 得到一个由我们的自定义类加载器实例化的类实例

        System.out.println(obj.getClass());
        // 在这里打印的是class com.coachhe.Chapter4_ClassLoader.ClassLoaderTest4，说明的确是从我们所需要的类中实例化出来的
        System.out.println(obj instanceof com.coachhe.Chapter4_类加载器.ClassLoaderTest4);
        //在这里会输出false，这是因为用我们自定义类加载器myLoader加载得到的类和虚拟机的应用程序类加载器加载的类即使来自同一个Class文件，也是不一样的

    }
}
