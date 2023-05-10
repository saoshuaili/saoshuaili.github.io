package com.coachhe.future.mallDemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/10 23:06
 */
public class CompletableFutureMallDemo {
    public static void main(String[] args) {
        Student student = new Student();
        student.setId(12).setStudentName("li4").setMajor("english");
    }
}

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
class Student {
    private Integer id;
    private String studentName;
    private String major;
}
