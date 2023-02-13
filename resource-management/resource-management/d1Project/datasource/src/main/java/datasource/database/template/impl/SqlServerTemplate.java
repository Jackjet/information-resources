package datasource.database.template.impl;

import datasource.constants.DatabaseConstants;
import datasource.database.template.DatabaseTemplate;

public class SqlServerTemplate extends DatabaseTemplate {
    @Override
    public String getSQLQueryTables(String... tableSchema) {
        return "select schema_name(schema_id)+'.'+object_name(object_id) from sys.objects \n" +
                "where type ='U' \n" +
                "and schema_name(schema_id) ='" + tableSchema[0] + "'";

    }

    @Override
    public String getSQLQueryTableSchema(String... args) {
        return "select name from sysobjects where xtype='U'";
    }

    @Override
    public String getSQLQueryFields(String tableName) {
        return String.format("SELECT SO.name as table_name,SC.name as column_name,SC.isnullable as isnullable,SC.colid as colid,ST.name as type,IK.column_name as primary_key FROM sysobjects SO,syscolumns SC,systypes ST,INFORMATION_SCHEMA.KEY_COLUMN_USAGE IK  WHERE SO.id = SC.id AND SO.xtype = 'U' AND SO.status >= 0 AND SC.xtype = ST.xusertype AND SO.name = IK.TABLE_NAME AND SO.name = '%s' ORDER BY SO.name, SC.colorder", tableName);
    }

    @Override
    public String getSchemaType() {
        return DatabaseConstants.SQL_SERVER;
    }

    @Override
    public String getSQLQueryDataWithPaging(String tableName, int pageIndex, int pageSize) {
        if (pageIndex == 0) {
            return String.format("select TOP %d pt.* from %s pt", pageSize * (pageIndex + 1), tableName);
        } else {
            return String.format("select TOP %d pt.* from %s pt except select TOP %d * from %s", pageSize * (pageIndex + 1), tableName, pageSize * pageIndex, tableName);
        }
    }
}
