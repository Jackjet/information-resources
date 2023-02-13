package d1.project.datasource.database.template.impl;


import d1.project.datasource.constants.DatabaseConstants;
import d1.project.datasource.database.template.DatabaseTemplate;

public class MysqlTemplate extends DatabaseTemplate {
    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        return String.format("SELECT COLUMN_COMMENT FROM information_schema.COLUMNS where TABLE_SCHEMA = '%s' and TABLE_NAME = '%s' and COLUMN_NAME = '%s'", schemaName, tableName, columnName);
    }

    @Override
    public String getSQLQueryPrimaryKey() {
        return "select column_name from information_schema.columns where table_schema=? and table_name=? and column_key = 'PRI'";
    }

    @Override
    public String getSQLQueryTables(String... args) {
        return "select table_name from information_schema.tables where table_schema=?";
    }

    @Override
    public String getSQLQueryTableSchema(String... args) {
        return "show tables";
    }

    @Override
    public String getSQLQueryFields(String database,String tableName) {
        return String.format("select * from information_schema.columns where table_name='%s'", tableName);
    }

    @Override
    public String getSQLQueryColumns(String... args) {
        return "select column_name from information_schema.columns where table_schema=? and table_name=?";
    }

    @Override
    public String getSQLQueryDataWithPaging(String tableName, int pageIndex, int pageSize) {
        return "select pt.* from " + tableName + " pt limit " + pageIndex * pageSize + ", " + pageSize;
    }

    @Override
    public String getSchemaType() {
        return DatabaseConstants.MYSQL;
    }
}
