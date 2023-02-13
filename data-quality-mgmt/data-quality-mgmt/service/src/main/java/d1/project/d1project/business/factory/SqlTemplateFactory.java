package d1.project.d1project.business.factory;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.service.template.MysqlSqlTemplateService;
import d1.project.d1project.business.service.template.OracleSqlTemplateService;
import d1.project.d1project.business.service.template.PostgresqlSqlTemplateService;
import d1.project.d1project.business.service.template.SqlServerSqlTemplateService;
import d1.project.d1project.business.service.template.base.SqlTemplateService;
import d1.project.d1project.business.utils.DatabaseType;

public class SqlTemplateFactory {
    private final static SqlTemplateFactory instance = new SqlTemplateFactory();

    private final SqlTemplateService postgresqlSqlTemplateService;
    private final SqlTemplateService mysqlSqlTemplateService;
    private final SqlTemplateService sqlserverSqlTemplateService;
    private final SqlTemplateService oracleSqlTemplateService;

    private SqlTemplateFactory() {
        postgresqlSqlTemplateService = new PostgresqlSqlTemplateService();
        mysqlSqlTemplateService = new MysqlSqlTemplateService();
        sqlserverSqlTemplateService = new SqlServerSqlTemplateService();
        oracleSqlTemplateService = new OracleSqlTemplateService();
    }

    public static SqlTemplateFactory getInstance() {
        return instance;
    }

    public SqlTemplateService getSqlTemplateService(String type) throws DoValidException {
        if (DatabaseType.POSTGRESQL.getName().equals(type)) {
            return postgresqlSqlTemplateService;

        } else if (DatabaseType.MYSQL.getName().equals(type)) {
            return mysqlSqlTemplateService;

        } else if (DatabaseType.SQLSERVER.getName().equals(type)) {
            return sqlserverSqlTemplateService;

        } else if (DatabaseType.Oracle.getName().equals(type)) {
            return oracleSqlTemplateService;

        } else {
            throw new DoValidException("不支持的数据库类型:" + type + ",目前支持的数据库类型‘" + String.join(",", DatabaseType.getNames()) + "’");
        }
    }
}
