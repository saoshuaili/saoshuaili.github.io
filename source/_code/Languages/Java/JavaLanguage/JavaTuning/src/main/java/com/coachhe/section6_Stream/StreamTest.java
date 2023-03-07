package com.coachhe.section6_Stream;




import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamTest {

    @Test
    public void testMap() {
        Map<String, List<Student>> stuMap = new HashMap<>();
        List<Student> studentsList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Student student = new Student("male", "male" + i, 150 + i);
            studentsList.add(student);
        }
        for (int i = 0; i < 20; i++) {
            Student student = new Student("female", "female" + i, 150 + i);
            studentsList.add(student);
        }

        // 传统迭代方式实现过滤身高在169以上的男女同学
        for (Student stu : studentsList) {
            if (stu.getHeight() > 160) {
                if (stuMap.get(stu.getSex()) == null) {
                    List<Student> list = new ArrayList<>();
                    list.add(stu);
                    stuMap.put(stu.getSex(), list);
                } else {
                    stuMap.get(stu.getSex()).add(stu);
                }
            }
        }
        System.out.println("================传统迭代方式实现============");
        printMap(stuMap);

        // Stream API实现
        // 1. 串行实现
        Map<String, List<Student>> stuStreamMap1 = studentsList.stream().filter((Student s) -> s.getHeight() > 160).
                collect(Collectors.groupingBy(Student::getSex));
        System.out.println("================stream-串行方式实现============");
        printMap(stuStreamMap1);

        // 2、 并行实现
        Map<String, List<Student>> stuStreamMap2 = studentsList.parallelStream().filter((Student s) -> s.getHeight() > 160).
                collect(Collectors.groupingBy(Student::getSex));
        System.out.println("================stream-并行方式实现============");
        printMap(stuStreamMap2);



    }

    public void printMap(Map<String, List<Student>> stuMap) {
        for (Student stu : stuMap.get("male")) {
            System.out.println(stu.getName());
        }
        for (Student stu : stuMap.get("female")) {
            System.out.println(stu.getName());
        }
    }

}

class Student {
    String sex;
    String name;
    int height;
    public String getSex() {
        return sex;
    }
    public String getName() {
        return name;
    }
    public int getHeight() {
        return height;
    }
    public Student(String sex, String name, int height){
        this.sex = sex;
        this.name = name;
        this.height = height;
    }
}