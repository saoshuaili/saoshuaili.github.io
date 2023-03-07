package com.coachhe.annotations.追踪项目示例;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: CoachHe
 * @Date: 2023/1/class27 13:44
 * on java8 中的示例
 * 可以用来追踪项目中的用例。
 * 程序员可以使用该注解来标注满足特定用例的一个方法或者一组方法
 * 项目经理可以通过统计已经实现的用例来掌控项目的进展
 * 而开发者可以在维护项目时轻松找到该用例用于更新或调试
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    int id();
    String description() default "no description";
}
