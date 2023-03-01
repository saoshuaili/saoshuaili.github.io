package fruit.service;


import fruit.pojo.Fruit;

import java.sql.SQLException;
import java.util.List;

/**
 * @author CoachHe
 * @date 2023/2/12 23:31
 **/
public interface FruitService {
    // 获取指定页面的库表信息
    List<Fruit> getFruitList(String keyword, Integer pageNumber) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException;

    // 添加库存记录信息
    void addFruit(Fruit fruit) throws SQLException;


    //......
    Fruit getFruitByFid(Integer fid) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException;


    void delFruitByFid(Integer fid) throws SQLException;

    void updateFruit(Fruit fruit) throws SQLException;
}
