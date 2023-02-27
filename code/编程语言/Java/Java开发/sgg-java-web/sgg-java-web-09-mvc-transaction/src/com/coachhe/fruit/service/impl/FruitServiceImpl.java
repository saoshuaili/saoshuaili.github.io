package com.coachhe.fruit.service.impl;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.base.ConnUtil;
import com.coachhe.fruit.pojo.Fruit;
import com.coachhe.fruit.service.FruitService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author CoachHe
 * @date 2023/2/12 23:32
 **/
public class FruitServiceImpl implements FruitService {

    private FruitDAO fruitDao = null;

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNumber) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        // 打印Connection，判断是否处于同一事务
        System.out.println("getFruitList -> ConnUtil.getConn() = " + ConnUtil.getConn());
        return fruitDao.getFruitList(pageNumber);
    }

    @Override
    public void addFruit(Fruit fruit) throws SQLException {
        fruitDao.addFruit(fruit);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        return fruitDao.getFruitByFid(fid);
    }

    @Override
    public void delFruitByFid(Integer fid) throws SQLException {
        fruitDao.delFruitByFid(fid);
    }

    @Override
    public void updateFruit(Fruit fruit) throws SQLException {
        fruitDao.updateFruit(fruit);
    }

}
