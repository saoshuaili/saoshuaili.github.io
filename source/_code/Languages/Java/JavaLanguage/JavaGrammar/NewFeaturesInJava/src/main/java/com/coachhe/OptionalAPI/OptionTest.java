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
        Optional<Girl> optionalGirl = Optional.of(girl);
    }


}

@Data
@ToString
class Girl{
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
class Boy {
    private Girl girl;
}