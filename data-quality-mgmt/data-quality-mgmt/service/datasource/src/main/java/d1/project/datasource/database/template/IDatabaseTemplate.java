package d1.project.datasource.database.template;

public interface IDatabaseTemplate {
    /**
     * Returns the minimal SQL to launch in order to determine the layout of the resultset for a given com.com.wugui.datax.admin.tool.database table
     *
     * @param tableName The name of the table to determine the layout for
     * @return The SQL to launch.
     */
    String getSQLQueryFields(String database, String tableName);

    /**
     * 获取主键字段
     *
     * @return
     */
    String getSQLQueryPrimaryKey();

    String getSQLQueryTableNameComment();

    String getSQLQueryTablesNameComments();

    /**
     * 获取所有表名的sql
     *
     * @return
     */
    String getSQLQueryTables(String... tableSchema);

    /**
     * 获取 Table schema
     *
     * @return
     */
    String getSQLQueryTableSchema(String... args);

    /**
     * 获取所有的字段的sql
     *
     * @return
     */
    String getSQLQueryColumns(String... args);

    /**
     * 获取表和字段注释的sql语句
     *
     * @return The SQL to launch.
     */
    String getSQLQueryComment(String schemaName, String tableName, String columnName);

    /**
     * 获取‘查询表的数据并且分页’的sql语句
     */
    String getSQLQueryDataWithPaging(String tableName, int pageIndex, int pageSize);

    String getSchemaType();
}
