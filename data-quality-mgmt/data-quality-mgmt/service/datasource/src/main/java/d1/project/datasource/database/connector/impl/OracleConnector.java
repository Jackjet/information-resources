package d1.project.datasource.database.connector.impl;

import d1.project.datasource.database.connector.DatabaseConnector;

public class OracleConnector extends DatabaseConnector {
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
