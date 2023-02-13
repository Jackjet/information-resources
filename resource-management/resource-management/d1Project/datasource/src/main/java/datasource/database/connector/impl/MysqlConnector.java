package datasource.database.connector.impl;

import datasource.database.connector.DatabaseConnector;

public class MysqlConnector extends DatabaseConnector {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
