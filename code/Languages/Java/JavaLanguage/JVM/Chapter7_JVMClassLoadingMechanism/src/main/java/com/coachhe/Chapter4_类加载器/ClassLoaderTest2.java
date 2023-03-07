package com.coachhe.Chapter4_类加载器;

import java.net.URL;

public class ClassLoaderTest2 {
    public static void main(String[] args) {
        System.out.println("==启动类加载器==");
        URL[] urls = null;

        System.out.println("==拓展类加载器==");
        String extDirs = System.getProperty("java.ext.dirs");
//        for (String path : extDirs.split(";")) {
//            System.out.println(path);
//        }


    }
}
