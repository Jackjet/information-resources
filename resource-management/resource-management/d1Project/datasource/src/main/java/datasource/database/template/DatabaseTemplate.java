package datasource.database.template;

public abstract class DatabaseTemplate implements IDatabaseTemplate {
    @Override
    public String getSQLQueryTablesNameComments() {
        return "select table_name,table_comment from information_schema.tables where table_schema=?";
    }

    @Override
    public String getSQLQueryTableNameComment() {
        return "select table_name,table_comment from information_schema.tables where table_schema=? and table_name = ?";
    }

    @Override
    public String getSQLQueryPrimaryKey() {
        return null;
    }

    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        return null;
    }

    @Override
    public String getSQLQueryColumns(String... args) {
        return null;
    }

    @Override
    public String getSQLQueryTableSchema(String... args) {
        return null;
    }

    @Override
    public String getSQLQueryFields(String tableName) {
        return null;
    }

    @Override
    public String getSQLQueryDataWithPaging(String tableName, int pageIndex, int pageSize) {
        return null;
    }
}
