package com.coachhe.fruit.service.impl;

import com.coachhe.fruit.dao.FruitDAO;
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
