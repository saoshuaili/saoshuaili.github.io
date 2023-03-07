package com.coachhe.Section1_为什么需要反射;

import java.util.stream.Stream;

/**
 * @author CoachHe
 * @date 2022/12/29 01:40
 **/
public class Shapes {
    public static void main(String[] args) {
        Stream.of(
                new Circle(), new Square(), new Triangle()
        ).forEach(Shape::draw);
    }
}

abstract class Shape{
    void draw(){
        System.out.println(this + ".draw()");
    }
    @Override
    public abstract String toString();
}

class Circle extends Shape {
    @Override
    public String toString() {
        return "Circle";
    }
}

class Square extends Shape {
    @Override
    public String toString() {
        return "Square";
    }
}

class Triangle extends Shape {
    @Override
    public String toString() {
        return "Triangle";
    }
}

