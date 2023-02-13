package d1.project.datasource.database.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnector {
    private Connection conn;

    public Connection getConn(String url, String username, String password) {
        try {
            conn = DriverManager.getConnection(url, username, password);//连接数据库
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
