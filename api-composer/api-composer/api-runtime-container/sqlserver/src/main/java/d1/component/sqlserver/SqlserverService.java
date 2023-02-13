package d1.component.sqlserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.base.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlserverService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(SqlserverService.class);

    private String source;
    private String sql;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);

        // 加载数据库驱动程序
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        //获取属性
        source = JSON.toJSONString(properties.get("source"));
        sql = properties.get("sql").toString();
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        Connection conn = null;
        super.run(context, input);

        if (conn == null) {
            conn = getConnection(source);
        }

        if (Utils.isEmpty(sql)) {
            throw new Exception("sql属性值不能为空");
        }

        // select * from test where age> ${request.body.age}
        String newSql = processExpressions(context, input.getInput(), sql);
        List<Map<String, Object>> result = query(conn, newSql);

        Input nextInput = new Input();
        nextInput.setInput(JSON.toJSON(result));

        if (conn != null) {
            conn.close();
            conn = null;
        }
        return getNextNode(context).run(context, nextInput);
    }


    @Override
    public void destroy() throws Exception {
        super.destroy();
    }


    ////////////////
    private Connection getConnection(String source) throws Exception {
        if (Utils.isEmpty(source)) {
            throw new Exception("source属性值不能为空");
        }

        JSONObject jsonObject = JSONObject.parseObject(source);

        String host = Utils.getJsonValue(jsonObject, "host", "localhost");
        String port = Utils.getJsonValue(jsonObject, "port", "1433");
        String database = Utils.getJsonValue(jsonObject, "database", "");
        String url = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + database;

        String user = Utils.getJsonValue(jsonObject, "user", "root");
        String password = Utils.getJsonValue(jsonObject, "password", "");
        return DriverManager.getConnection(url, user, password);
    }

    private List<Map<String, Object>> query(Connection conn, String sql) throws Exception {
        List<Map<String, Object>> listData = new ArrayList<>();
        ResultSet rs = null;
        Statement statement = null;
        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            ResultSetMetaData rd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < rd.getColumnCount(); i++) {
                    map.put(rd.getColumnName(i + 1), rs.getObject(i + 1));
                }
                listData.add(map);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        return listData;
    }
}
