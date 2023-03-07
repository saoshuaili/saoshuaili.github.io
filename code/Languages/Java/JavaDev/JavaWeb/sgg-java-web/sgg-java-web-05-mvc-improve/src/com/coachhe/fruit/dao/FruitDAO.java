package com.coachhe.fruit.dao;


import com.coachhe.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //查询库存列表
    List<Fruit> getFruitList();

    //根据页码查询对应页库存列表
    List<Fruit> getFruitList(Integer pageNum);

    //根据fid查询库存
    Fruit getFruitByFid(int fid);

    //新增库存
    boolean addFruit(Fruit fruit);

    //修改库存
    boolean updateFruit(Fruit fruit);

    //根据名称查询特定库存
    Fruit getFruitByFname(String fname);

    //删除特定库存记录
    boolean delFruit(String fname);

    //根据fid删除揭露
    void delFruitByFid(int fid);
}
