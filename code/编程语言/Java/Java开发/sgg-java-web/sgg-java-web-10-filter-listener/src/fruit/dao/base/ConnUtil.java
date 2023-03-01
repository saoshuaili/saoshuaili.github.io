package fruit.dao.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author CoachHe
 * @date 2023/2/21 00:41
 **/
public class ConnUtil {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public final static String DRIVER = "com.mysql.jdbc.Driver" ;
    public final static String URL = "jdbc:mysql://9.135.222.245:3306/fruitdb?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    public final static String USER = "tdsql_admin";
    public final static String PWD = "gUFeA*4368ef" ;

    private static Connection createConnection() {
        try {
            //1.加载驱动
            Class.forName(DRIVER);
            //2.通过驱动管理器获取连接对象
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConn() {
        Connection conn = threadLocal.get();
        if (conn == null) {
            conn = createConnection();
            threadLocal.set(conn);
        }
        return threadLocal.get();
    }

    public static void closeConn() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn == null) {
            return;
        }
        if (!conn.isClosed()) {
            conn.close();
            threadLocal.set(null);
        }


    }
}
