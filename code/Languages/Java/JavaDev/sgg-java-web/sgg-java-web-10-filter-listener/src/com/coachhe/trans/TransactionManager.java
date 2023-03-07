package com.coachhe.trans;


import com.coachhe.fruit.dao.base.ConnUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author CoachHe
 * @date 2023/2/21 00:36
 **/
public class TransactionManager {


    // 开启事务
    public static void beginTrans() throws SQLException {
        ConnUtil.getConn().setAutoCommit(false);
    }

    // 提交事务
    public static void commit() throws SQLException {
        Connection conn = ConnUtil.getConn();
        conn.commit();
        ConnUtil.closeConn();
    }

    // 回滚事务
    public static void rollback() throws SQLException {
        Connection conn = ConnUtil.getConn();
        conn.rollback();
        ConnUtil.closeConn();
    }
}
