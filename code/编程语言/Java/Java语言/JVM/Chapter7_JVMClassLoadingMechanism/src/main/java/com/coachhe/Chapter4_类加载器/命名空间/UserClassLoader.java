package com.coachhe.Chapter4_类加载器.命名空间;

import java.io.*;

/**
 * @author CoachHe
 * @date 2023/1/13 02:46
 **/
public class UserClassLoader extends ClassLoader {
    private String rootDir;

    public UserClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    // 编写findClass方法的逻辑
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 获取类的class文件字节数组
        byte[] classData = getClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            // 直接生成class对象
            return defineClass(name, classData, 0, classData.length);
        }
    }

    // 编写获取class文件并转换为字节码流的逻辑
    private byte[] getClassData(String className) {
        // 读取类文件的字节
        String path = classNameToPath(className);
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            // 读取类文件的字节码
            while ((len = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 类文件的完全路径
    private String classNameToPath(String className) {
        return rootDir + "/" + className.replace(".", "/") + ".class";
    }

    public static void main(String[] args) {
        String rootDir = "/Users/heyizhi/Notes/coachhe.github.io/code/编程语言/Java/Java语言/JVM/Chapter7_JVMClassLoadingMechanism/src/main/java";
        try {
            UserClassLoader classLoader1 = new UserClassLoader(rootDir);
            classLoader1.findClass("com.coachhe.Chapter4_类加载器.命名空间.ClassLoaderTest");
            UserClassLoader classLoader2 = new UserClassLoader(rootDir);
            classLoader2.findClass("com.coachhe.Chapter4_类加载器.命名空间.ClassLoaderTest");

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
