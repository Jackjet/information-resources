package d1.project.datasource.database.connector.impl;

import d1.project.datasource.database.connector.DatabaseConnector;

public class MysqlConnector extends DatabaseConnector {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
