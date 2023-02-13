package datasource.database.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnector {
    private Connection conn;

    public Connection getConn(String url, String username, String password) {
        try {
            //连接数据库
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
