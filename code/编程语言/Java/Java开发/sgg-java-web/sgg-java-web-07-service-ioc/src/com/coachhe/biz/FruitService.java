package com.coachhe.biz;

import com.coachhe.fruit.pojo.Fruit;

import java.util.List;

/**
 * @author CoachHe
 * @date 2023/2/12 23:31
 **/
public interface FruitService {
    // 获取指定页面的库表信息
    List<Fruit> getFruitList(String keyword, Integer pageNumber);

    // 添加库存记录信息
    void addFruit(Fruit fruit);

    //......


}
