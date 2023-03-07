package com.coachhe.fruit.dao.impl;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.base.BaseDAO;
import com.coachhe.fruit.pojo.Fruit;

import java.sql.SQLException;
import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {
    public FruitDAOImpl() throws ClassNotFoundException {
    }

    @Override
    public List<Fruit> getFruitList() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        return super.executeQuery("select * from t_fruit");
    }

    @Override
    public List<Fruit> getFruitList(Integer pageNum) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        return super.executeQuery("select * from t_fruit limit ?, 5", (pageNum - 1) * 5);
    }

    @Override
    public Fruit getFruitByFid(int fid) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        return super.load("select * from t_fruit where fid = ?", fid);
    }

    @Override
    public boolean addFruit(Fruit fruit) throws SQLException {
        String sql = "insert into t_fruit values(0,?,?,?,?)";
        int count = super.executeUpdate(sql,fruit.getFname(),fruit.getPrice(),fruit.getFcount(),fruit.getRemark()) ;
        //insert语句返回的是自增列的值，而不是影响行数
        //System.out.println(count);
        return count>0;
    }

    @Override
    public boolean updateFruit(Fruit fruit) throws SQLException {
//        String sql = String.format("update t_fruit set fcount = %s and price = %s and fname = '%s' and remark = '%s' where fid = %s",
//                fruit.getFcount(), fruit.getPrice(), fruit.getFname(), fruit.getRemark(), fruit.getFid());
        String sql = "update t_fruit set fcount = ?, price = ?, fname = ?, remark = ? where fid = ?";
        return super.executeUpdate(sql, fruit.getFcount(), fruit.getPrice(), fruit.getFname(), fruit.getRemark(), fruit.getFid()) > 0;
    }

    @Override
    public Fruit getFruitByFname(String fname) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        return super.load("select * from t_fruit where fname like ? ",fname);
    }

    @Override
    public boolean delFruit(String fname) throws SQLException {
        String sql = "delete from t_fruit where fname like ? " ;
        return super.executeUpdate(sql,fname)>0;
    }

    @Override
    public void delFruitByFid(int fid) throws SQLException {
        super.executeUpdate("delete from t_fruit where fid = ?", fid);
    }


}