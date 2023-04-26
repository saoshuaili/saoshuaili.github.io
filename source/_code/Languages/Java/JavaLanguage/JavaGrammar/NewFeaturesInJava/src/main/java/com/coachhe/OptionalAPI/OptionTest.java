package com.coachhe.OptionalAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import java.util.Optional;

public class OptionTest {

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