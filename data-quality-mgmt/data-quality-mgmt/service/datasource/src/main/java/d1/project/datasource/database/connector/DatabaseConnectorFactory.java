package d1.project.datasource.database.connector;

import d1.project.datasource.constants.DatabaseConstants;
import d1.project.datasource.constants.MessageConstants;
import d1.project.datasource.database.connector.impl.MysqlConnector;
import d1.project.datasource.database.connector.impl.OracleConnector;
import d1.project.datasource.database.connector.impl.PostgresqlConnector;
import d1.project.datasource.database.connector.impl.SqlServerConnector;

public class DatabaseConnectorFactory {

    public static DatabaseConnector build(String type) throws Exception {
        switch (type) {
            case DatabaseConstants.MYSQL:
                return new MysqlConnector();

            case DatabaseConstants.ORACLE:
                return new OracleConnector();

            case DatabaseConstants.POSTGRESQL:
                return new PostgresqlConnector();

            case DatabaseConstants.SQL_SERVER:
                return new SqlServerConnector();

            default:
                throw new Exception(MessageConstants.ILLEGAL_DATABASE_TYPE);
        }
    }
}
