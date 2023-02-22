package com.coachhe.fruit.service.impl;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.base.ConnUtil;
import com.coachhe.fruit.pojo.Fruit;
import com.coachhe.fruit.service.FruitService;

import java.util.List;

/**
 * @author CoachHe
 * @date 2023/2/12 23:32
 **/
public class FruitServiceImpl implements FruitService {

    private FruitDAO fruitDao = null;

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNumber) {
        // 打印Connection，判断是否处于同一事务
        System.out.println("getFruitList -> ConnUtil.getConn() = " + ConnUtil.getConn());
        return fruitDao.getFruitList(pageNumber);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDao.addFruit(fruit);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return fruitDao.getFruitByFid(fid);
    }

    @Override
    public void delFruitByFid(Integer fid) {
        fruitDao.delFruitByFid(fid);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDao.updateFruit(fruit);
    }

}
