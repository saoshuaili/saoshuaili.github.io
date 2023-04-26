package com.coachhe.OptionalAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import java.util.Optional;

public class OptionTest {

    /**
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

    public String getGirlName(Boy boy) {
        return boy.getGirl().getName();
    }


}

@Data
@ToString
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