package d1.project.d1project.apidesign.utils;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.apidesign.model.RequestMetaDataItemVm;

import java.util.List;

public class SqlUtils {

    public static String generateSql(String databaseType, String tableName, List<String> fields, List<List<RequestMetaDataItemVm>> wheres) throws DoValidException {
        switch (databaseType.toLowerCase()) {
            case "mysql":
            case "postgresql":
            case "sqlserver":
                return getSql(tableName, fields, wheres);
            case "oracle":
                return getOracleSql(tableName, fields, wheres);
        }
        return null;
    }


    private static String getSql(String tableName, List<String> fields, List<List<RequestMetaDataItemVm>> wheres) throws DoValidException {
        //String sql = "select * from user where age < ${request.params.age} and sex = ${request.params.sex}";
        StringBuilder sql = new StringBuilder("select ");

        //拼接查询字段
        if (fields == null || fields.size() == 0) {
            sql.append("*").append(" ");
        } else {
            sql.append(String.join(",", fields)).append(" ");
        }

        sql.append("from").append(" ");
        sql.append(tableName).append(" ");

        //拼接查询条件
        for (int i = 0; i < wheres.size(); i++) {
            List<RequestMetaDataItemVm> where = wheres.get(i);
            if (where == null || where.size() != 2) {
                throw new DoValidException("where 条件传入错误！");
            }

            if (i == 0) {
                sql.append("where").append(" ");
            }

            RequestMetaDataItemVm source = where.get(0);
            sql.append(source.getName()).append(" = ");
            sql.append(getPathExpressions(where.get(1))).append(" ");

            if (i != wheres.size() - 1) {
                sql.append("and ");
            }
        }
        return sql.toString();
    }

    private static String getOracleSql(String tableName, List<String> fields, List<List<RequestMetaDataItemVm>> wheres) throws DoValidException {
        //String sql = "select * from user where "age" < ${request.params.age} and "sex" = ${request.params.sex}";
        StringBuilder sql = new StringBuilder("select ");

        //拼接查询字段
        if (fields == null || fields.size() == 0) {
            sql.append("*").append(" ");
        } else {
            sql.append(joinFields("\",\"", fields)).append(" ");
        }

        sql.append("from").append(" ");
        sql.append("\"" + tableName + "\"").append(" ");

        //拼接查询条件
        for (int i = 0; i < wheres.size(); i++) {
            List<RequestMetaDataItemVm> where = wheres.get(i);
            if (where == null || where.size() != 2) {
                throw new DoValidException("where 条件传入错误！");
            }

            if (i == 0) {
                sql.append("where").append(" ");
            }

            RequestMetaDataItemVm source = where.get(0);
            sql.append("\"" + source.getName() + "\"").append(" = ");
            sql.append(getPathExpressions(where.get(1))).append(" ");

            if (i != wheres.size() - 1) {
                sql.append("and ");
            }
        }
        return sql.toString();
    }


    private static String getPathExpressions(RequestMetaDataItemVm requestMetaDataItemVm) {
        String from = null;
        switch (requestMetaDataItemVm.getFrom()) {
            case "Header":
                from = "headers";
                break;
            case "Parameter":
                from = "params";
                break;
            case "Body":
                from = "body";
                break;
            default:
                break;
        }
        return "${" + "request" + "." + from + "." + requestMetaDataItemVm.getName() + "}";
    }

    private static String joinFields(String delimiter, List<String> tableFields) {
        StringBuffer fields = new StringBuffer();
        for (int i = 0; i < tableFields.size(); i++) {
            fields.append(tableFields.get(i));
            if (i != tableFields.size() - 1) {
                fields.append(delimiter);
            }
        }
        return fields.toString();
    }
}
