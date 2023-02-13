package d1.project.datasource.database.connector.impl;

import d1.project.datasource.database.connector.DatabaseConnector;

public class SqlServerConnector extends DatabaseConnector {
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
