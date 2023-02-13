package datasource.database.template;

import datasource.constants.DatabaseConstants;
import datasource.constants.MessageConstants;
import datasource.database.template.impl.MysqlTemplate;
import datasource.database.template.impl.OracleTemplate;
import datasource.database.template.impl.PostgresqlTemplate;
import datasource.database.template.impl.SqlServerTemplate;

public class DatabaseTemplateFactory {

    public static IDatabaseTemplate build(String type) throws Exception {
        switch (type) {
            case DatabaseConstants.MYSQL:
                return new MysqlTemplate();

            case DatabaseConstants.ORACLE:
                return new OracleTemplate();

            case DatabaseConstants.POSTGRESQL:
                return new PostgresqlTemplate();

            case DatabaseConstants.SQL_SERVER:
                return new SqlServerTemplate();
                
            default:
                throw new Exception(MessageConstants.ILLEGAL_DATABASE_TYPE);
        }
    }
}
