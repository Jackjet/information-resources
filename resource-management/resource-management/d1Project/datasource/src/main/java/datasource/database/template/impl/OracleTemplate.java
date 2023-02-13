package datasource.database.template.impl;

import datasource.constants.DatabaseConstants;
import datasource.database.template.DatabaseTemplate;

public class OracleTemplate extends DatabaseTemplate {
    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        return String.format("select B.comments \n" +
                "  from user_tab_columns A, user_col_comments B\n" +
                " where a.COLUMN_NAME = b.column_name\n" +
                "   and A.Table_Name = B.Table_Name\n" +
                "   and A.Table_Name = upper('%s')\n" +
                "   AND A.column_name  = '%s'", tableName, columnName);
    }

    @Override
    public String getSQLQueryPrimaryKey() {
        return "select cu.column_name from user_cons_columns cu, user_constraints au where cu.constraint_name = au.constraint_name and au.owner = ? and au.constraint_type = 'P' and au.table_name = ?";
    }

    @Override
    public String getSQLQueryTablesNameComments() {
        return "select table_name,comments from user_tab_comments";
    }

    @Override
    public String getSQLQueryTableNameComment() {
        return "select table_name,comments from user_tab_comments where table_name = ?";
    }

    @Override
    public String getSQLQueryTables(String... tableSchema) {
        return "select table_name from dba_tables where owner='" + tableSchema[0] + "'";
    }

    @Override
    public String getSQLQueryTableSchema(String... args) {
        return String.format("select table_name from dba_tables where owner='%s'",args[0]);
    }


    /*@Override
    public String getSQLQueryTables(String... args) {
        return "select table_name from user_tab_comments";
    }*/

    @Override
    public String getSQLQueryColumns(String... args) {
        return "select table_name,comments from user_tab_comments where table_name = ?";
    }

    @Override
    public String getSQLQueryFields(String tableName) {
        return String.format("select * from all_tab_columns where table_name = '%s'", tableName);
    }

    @Override
    public String getSchemaType() {
        return DatabaseConstants.ORACLE;
    }

    @Override
    public String getSQLQueryDataWithPaging(String tableName, int pageIndex, int pageSize) {
        return String.format("select * from (select rownum, pt.* from %s pt WHERE rownum <= %d)  WHERE rownum >= %d", tableName,pageSize * (pageIndex + 1), pageSize * pageIndex);
    }
}
