package fruit.dao;


import com.coachhe.fruit.pojo.Fruit;

import java.sql.SQLException;
import java.util.List;

public interface FruitDAO {
    //查询库存列表
    List<Fruit> getFruitList() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException;

    //根据页码查询对应页库存列表
    List<Fruit> getFruitList(Integer pageNum) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException;

    //根据fid查询库存
    Fruit getFruitByFid(int fid) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException;

    //新增库存
    boolean addFruit(Fruit fruit) throws SQLException;

    //修改库存
    boolean updateFruit(Fruit fruit) throws SQLException;

    //根据名称查询特定库存
    Fruit getFruitByFname(String fname) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException;

    //删除特定库存记录
    boolean delFruit(String fname) throws SQLException;

    //根据fid删除揭露
    void delFruitByFid(int fid) throws SQLException;
}
