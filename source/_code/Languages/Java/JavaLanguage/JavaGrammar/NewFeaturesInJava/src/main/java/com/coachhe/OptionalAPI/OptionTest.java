package com.coachhe.OptionalAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import java.util.Optional;

public class OptionTest {

    /**
     * Optional实例创建
     * Optional.of(T t) 创建一个Optional实例，t必须非空
     * Optional.empty() 创建一个空的Optional实例
     * Optional.ofNullable(T t) t可以为null
     */
    @Test
    public void test1(){
        Girl girl = new Girl();
        girl = null;
        Optional<Girl> optionalGirl = Optional.of(girl); // 会报错

        // 创建一个空的Optional实例
        Optional<Object> empty = Optional.empty();

        // 创建一个元素为空的Optional实例
        Optional<Girl> girl1 = Optional.ofNullable(girl);

    }

    // 优化之前的方法
    public String getGirlName(Boy boy) {
        return boy.getGirl().getName();
    }

    // 优化之后的方法
    public String getGirlNameNew(Boy boy) {
        // 创建一个Optional实例，将boy包装进去
        Optional<Boy> boyOptional = Optional.ofNullable(boy);
        Boy boy1 = boyOptional.orElse(new Boy(new Girl("赵丽颖")));
        System.out.println(boy1);
        return boy1.getGirl().getName();
    }

    // Optional实例使用以及如何避免空指针异常
    @Test
    public void test2(){
        Boy boy = new Boy();
//        String girlName = getGirlName(boy); // 这里一定有空指针异常
        boy = null;
        String girlName = getGirlNameNew(boy);
        System.out.println(girlName);
    }


}

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Girl{
    private String name;
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
class Boy {
    private Girl girl;
}