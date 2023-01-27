package com.coachhe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: CoachHe
 * @Date: 2023/1/27 13:02
 * 定义一个注解
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyDefineAnnotation {
}

//@MyDefineAnnotation
class TestClass {
    @MyDefineAnnotation
    String testField;
    @MyDefineAnnotation
    void testMethod(){}
}
