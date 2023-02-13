package d1.project.datasource.database.sqlTool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.datasource.constants.DatabaseConstants;
import d1.project.datasource.constants.MessageConstants;
import d1.project.datasource.database.connector.DatabaseConnector;
import d1.project.datasource.database.connector.DatabaseConnectorFactory;
import d1.project.datasource.database.template.DatabaseTemplateFactory;
import d1.project.datasource.database.template.IDatabaseTemplate;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlTool implements Closeable {
    private Connection conn;
    private IDatabaseTemplate databaseTemplate;

    public SqlTool(String type, String url, String username, String password) throws Exception {
        DatabaseConnector connector = DatabaseConnectorFactory.build(type);
        conn = connector.getConn(url, username, password);

        databaseTemplate = DatabaseTemplateFactory.build(type);
    }

    /**
     * 测试数据库连接
     *
     * @return
     */
    public Boolean connectTest() {
        DatabaseMetaData metaData = null;
        try {
            metaData = conn.getMetaData();
            if (metaData.getDatabaseProductName().length() > 0) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }

        return false;
    }

    /**
     * 获取所有表名
     *
     * @return
     */
    public List<String> getTableOfSchema(String database) {
        List<String> schemas = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            //获取sql
            String sql = databaseTemplate.getSQLQueryTableSchema(database);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                schemas.add(tableName);
            }
        } catch (SQLException e) {
            return schemas;
        } finally {
            statementClose(stmt);
            resultSetClose(rs);
        }
        return schemas;
    }

    /**
     * 获取字段信息
     *
     * @param tableName 表名
     * @return
     * @throws Exception
     */
    public JSONArray getColumns(String database,String tableName) throws Exception {
        JSONArray array = new JSONArray();
        //获取指定表的所有字段
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = databaseTemplate.getSQLQueryFields(database,tableName);

            //获取所有字段
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);

            while (resultSet.next()) {
                JSONObject item = new JSONObject();
                switch (databaseTemplate.getSchemaType()) {
                    case DatabaseConstants.MYSQL:
                        item = getColumnsMysql(resultSet);
                        break;
                    case DatabaseConstants.POSTGRESQL:
                        item = getColumnsPostgresql(resultSet);
                        break;
                    case DatabaseConstants.SQL_SERVER:
                        item = getColumnsSqlServer(resultSet);
                        break;
                    case DatabaseConstants.ORACLE:
                        item = getColumnsOracle(resultSet);
                        break;
                    default:
                        throw new Exception(MessageConstants.ILLEGAL_DATABASE_TYPE);
                }

                array.add(item);
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * 分页查询
     *
     * @param tableName 表名
     * @param pageIndex 页码
     * @param pageSize  页的大小
     * @return
     */
    public JSONObject getTableDataWithPaging(String tableName, Integer pageIndex, Integer pageSize) {
        JSONObject result = new JSONObject();

        try {
            pageIndex = (pageIndex == null || pageIndex < 1) ? 0 : pageIndex - 1;
            if (pageSize == null || pageSize < 1) {
                pageSize = 10;
            }

            //获取sql
            String sql = databaseTemplate.getSQLQueryDataWithPaging(tableName, pageIndex, pageSize);

            JSONArray data = executeQuery(sql);

            long totalCount = getCount(tableName);
            int totalPages = (int) Math.ceil((double) totalCount / (double) pageSize);

            result.put("content", data);
            result.put("totalElements", totalCount);
            result.put("totalPages", totalPages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public long getCount(String tableName) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            //获取sql
            String sql = "select count(1) from " + tableName + " ct";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statementClose(stmt);
            resultSetClose(rs);
        }
        return 0;
    }

    public JSONArray executeQuery(String sql) throws Exception {
        sql = sql.replace(";", "");
        verifySqlEmpty(sql);

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //查询结果的字段信息
            ResultSetMetaData metaData = rs.getMetaData();

            JSONArray data = new JSONArray();
            while (rs.next()) {
                JSONObject item = new JSONObject();

                //遍历字段
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    item.put(columnName, rs.getObject(columnName));
                }
                data.add(item);
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            statementClose(stmt);
            resultSetClose(rs);
        }
    }

    @Override
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //===============================================================
    private void statementClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void resultSetClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void verifySqlEmpty(String sql) throws Exception {
        if (sql == null || "".equals(sql.trim())) {
            throw new Exception("sql语句不能为空");
        }
    }

    private JSONObject getColumnsMysql(ResultSet resultSet) throws SQLException {
        JSONObject data = new JSONObject();

        data.put("columnName", resultSet.getString("column_name"));
        data.put("isNullable", resultSet.getString("is_nullable"));
        data.put("columnType", resultSet.getString("column_type"));
        data.put("columnKey", resultSet.getString("column_key").equals("PRI") ? true : false);
        data.put("columnComment", resultSet.getString("column_comment"));

        return data;
    }

    private JSONObject getColumnsOracle(ResultSet resultSet) throws SQLException {
        JSONObject data = new JSONObject();

        data.put("columnName", resultSet.getString("column_name"));
        data.put("isNullable", resultSet.getString("nullable").equals("Y") ? "YES" :"NO");
        data.put("columnType", resultSet.getString("data_type"));
        data.put("columnKey", resultSet.getString("identity_column").equals("YES") ? true : false);

        return data;
    }

    private JSONObject getColumnsPostgresql(ResultSet resultSet) throws SQLException {
        JSONObject data = new JSONObject();

        data.put("columnName", resultSet.getString("column_name"));
        data.put("isNullable", resultSet.getString("is_nullable"));
        data.put("columnType", resultSet.getString("udt_name"));
        data.put("columnKey", resultSet.getString("is_identity").equals("YES") ? true : false);

        return data;
    }

    private JSONObject getColumnsSqlServer(ResultSet resultSet) throws SQLException {
        JSONObject data = new JSONObject();

        String columnName = resultSet.getString("column_name");
        String primaryKey = resultSet.getString("primary_key");

        data.put("columnName", columnName);
        data.put("isNullable", resultSet.getString("isnullable").equals("1") ? "YES":"NO");
        data.put("columnType", resultSet.getString("type"));
        data.put("columnKey", columnName.equals(primaryKey) ? true : false);

        return data;
    }
}
