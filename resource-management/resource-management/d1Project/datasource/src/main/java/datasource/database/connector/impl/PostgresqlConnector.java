package datasource.database.connector.impl;

import datasource.database.connector.DatabaseConnector;

public class PostgresqlConnector extends DatabaseConnector {
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
