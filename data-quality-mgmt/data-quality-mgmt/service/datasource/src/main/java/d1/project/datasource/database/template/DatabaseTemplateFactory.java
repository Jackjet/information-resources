package d1.project.datasource.database.template;


import d1.project.datasource.constants.DatabaseConstants;
import d1.project.datasource.constants.MessageConstants;
import d1.project.datasource.database.template.impl.MysqlTemplate;
import d1.project.datasource.database.template.impl.OracleTemplate;
import d1.project.datasource.database.template.impl.PostgresqlTemplate;
import d1.project.datasource.database.template.impl.SqlServerTemplate;

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
