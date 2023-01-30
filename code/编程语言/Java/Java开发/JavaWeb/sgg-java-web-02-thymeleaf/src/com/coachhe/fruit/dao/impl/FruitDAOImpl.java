package com.coachhe.fruit.dao.impl;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.base.BaseDAO;
import com.coachhe.fruit.pojo.Fruit;

import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {
    @Override
    public List<Fruit> getFruitList() {
        return super.executeQuery("select * from t_fruit");
    }

    @Override
    public Fruit getFruitByFid(int fid) {
        return super.load("select * from t_fruit where fid = ?", fid);
    }

    @Override
    public boolean addFruit(Fruit fruit) {
        String sql = "insert into t_fruit values(0,?,?,?,?)";
        int count = super.executeUpdate(sql,fruit.getFname(),fruit.getPrice(),fruit.getFcount(),fruit.getRemark()) ;
        //insert语句返回的是自增列的值，而不是影响行数
        //System.out.println(count);
        return count>0;
    }

    @Override
    public boolean updateFruit(Fruit fruit) {
//        String sql = String.format("update t_fruit set fcount = %s and price = %s and fname = '%s' and remark = '%s' where fid = %s",
//                fruit.getFcount(), fruit.getPrice(), fruit.getFname(), fruit.getRemark(), fruit.getFid());
        String sql = "update t_fruit set fcount = ?, price = ?, fname = ?, remark = ? where fid = ?";
        return super.executeUpdate(sql, fruit.getFcount(), fruit.getPrice(), fruit.getFname(), fruit.getRemark(), fruit.getFid()) > 0;
    }

    @Override
    public Fruit getFruitByFname(String fname) {
        return super.load("select * from t_fruit where fname like ? ",fname);
    }

    @Override
    public boolean delFruit(String fname) {
        String sql = "delete from t_fruit where fname like ? " ;
        return super.executeUpdate(sql,fname)>0;
    }

    @Override
    public void delFruitByFid(int fid) {
        super.executeUpdate("delete from t_fruit where fid = ?", fid);
    }


}