package com.coachhe.biz.impl;

import com.coachhe.biz.FruitService;
import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.impl.FruitDAOImpl;
import com.coachhe.fruit.pojo.Fruit;

import java.util.List;

/**
 * @author CoachHe
 * @date 2023/2/12 23:32
 **/
public class FruitServiceImpl implements FruitService {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNumber) {
        return fruitDAO.getFruitList(pageNumber);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return fruitDAO.getFruitByFid(fid);
    }

    @Override
    public void delFruitByFid(Integer fid) {
        fruitDAO.delFruitByFid(fid);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruit(fruit);
    }

}
