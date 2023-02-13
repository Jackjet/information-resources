package d1.project.d1project.business.workers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.dao.TaskDao;
import d1.project.d1project.business.dao.TaskLogDao;
import d1.project.d1project.business.dao.TaskRuleLogDao;
import d1.project.d1project.business.entity.DataSource;
import d1.project.d1project.business.entity.TaskLog;
import d1.project.d1project.business.entity.TaskRuleLog;
import d1.project.d1project.business.utils.DatabaseType;
import d1.project.d1project.business.utils.Utils;
import d1.project.d1project.business.workers.bean.RunRule;
import d1.project.d1project.business.workers.bean.RunTask;
import d1.project.d1project.common.utils.BaseUtils;
import d1.project.datasource.database.sqlTool.SqlTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Negative;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author Buter
 * @date 2021/4/14 9:42
 */
public class WorkerThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private final RunTaskChannel channel;
    private final TaskDao taskDao;
    private final TaskLogDao taskLogDao;
    private final TaskRuleLogDao taskRuleLogDao;

    public WorkerThread(RunTaskChannel channel, TaskDao taskDao, TaskLogDao taskLogDao, TaskRuleLogDao taskRuleLogDao) {
        this.channel = channel;
        this.taskDao = taskDao;
        this.taskLogDao = taskLogDao;
        this.taskRuleLogDao = taskRuleLogDao;
    }

    @Override
    public void run() {
        while (true) {
            RunTask runTask = this.channel.getTask();
            if (runTask != null) {
                TaskLog taskLog = new TaskLog();
                Calendar lastTime = Calendar.getInstance();
                try {
                    logger.info(Thread.currentThread().getId() + "获取到任务:" + runTask.getId());

                    taskLog.setId(BaseUtils.generate32Id());
                    taskLog.setTaskId(runTask.getId());

                    long totalDataSize = 0;
                    long totalErrorDataSize = 0;
                    long totalTime = 0;

                    for (RunRule runRule : runTask.getRunRules()) {
                        TaskRuleLog taskRuleLog = runTask(taskLog.getId(), runRule);
                        totalDataSize += taskRuleLog.getDataSize();
                        totalErrorDataSize += taskRuleLog.getErrorDataSize();
                        totalTime += taskRuleLog.getTime();
                    }

                    logger.info(Thread.currentThread().getId() + "任务结束:" + runTask.getId());

                    //任务执行结束，插入任务执行日志
                    taskLog.setExecutionResult("1");
                    taskLog.setDataSize(totalDataSize);
                    taskLog.setErrorDataSize(totalErrorDataSize);
                    taskLog.setTime(totalTime);
                    taskLog.setYearMonth(Utils.dataToString(lastTime.getTime(), "yyyy-MM"));

                } catch (Exception e) {
                    taskLog.setExecutionResult("0");
                    logger.error("任务执行异常：" + runTask.getId(), e);
                } finally {
                    this.channel.removeTask(runTask.getId());
                    taskLog.setExecutionTime(lastTime);
                    taskLogDao.save(taskLog);

                    //修改最后执行时间
                    taskDao.updateLastTime(lastTime, runTask.getId());
                }
            }
        }
    }


    ////

    private TaskRuleLog runTask(String taskLogId, RunRule runRule) throws Exception {

            TaskRuleLog taskRuleLog = new TaskRuleLog();
        try {
            long startTime = System.currentTimeMillis();

            DataSource dataSource = runRule.getDataSource();
            if (dataSource == null) {
                throw new DoValidException("数据源不能为空");
            }

            taskRuleLog.setId(BaseUtils.generate32Id());
            taskRuleLog.setCreateTime(Calendar.getInstance());
            taskRuleLog.setDatasourceId(dataSource.getId());
            taskRuleLog.setRuleId(runRule.getId());
            taskRuleLog.setTaskLogId(taskLogId);
            taskRuleLog.setTemplateId(runRule.getTemplateId());
            taskRuleLog.setCreateDay(Utils.dataToString(taskRuleLog.getCreateTime().getTime(), "yyyy-MM-dd"));

            SqlTool sqlTool = new SqlTool(runRule.getDbType(), dataSource.getConnectUrl(), dataSource.getUsername(), dataSource.getPassword());

            //查询当前表的总数居条数
            long count = 0;
            String tableName = runRule.getTableName();
            if (DatabaseType.Oracle.getName().equals(dataSource.getType())) {
                tableName = dataSource.getDataBaseName() + ".\"" + tableName + "\"";
            }
            count = sqlTool.getCount(tableName);

            //查询当前sql语句执行数据的条数
            long validateCount = getValidateCount(sqlTool, runRule.getSql());

            //查询问题数据
            JSONObject errorData = sqlTool.getTableDataWithPaging(getTopFive(sqlTool,runRule.getSql(),dataSource.getDataBaseName()), 1, 5);

            taskRuleLog.setDataSize(count);
            taskRuleLog.setErrorDataSize(validateCount);
            taskRuleLog.setErrorData(JSON.toJSONString(errorData.getJSONArray("content"), SerializerFeature.WriteMapNullValue));
            taskRuleLog.setTime(System.currentTimeMillis() - startTime);

            //插入规则执行日志
            taskRuleLogDao.save(taskRuleLog);
        } catch (Exception exception) {
            throw exception;
        }
        return taskRuleLog;
    }

    private long getValidateCount(SqlTool sqlTool, String sql) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            throw new DoValidException("sql不能为空!");
        }

        if (!sql.toLowerCase().startsWith("select")) {
            throw new DoValidException("sql语句错误,不能执行select以外的语句!");
        }

        String countSql;
        if (sql.contains("\\*")) {
            countSql = sql.replaceFirst("\\*", "count(1)");
        } else {
            int endIndex = sql.indexOf("FROM");
            if (endIndex == -1) {
                endIndex = sql.indexOf("from");
            }

            if (endIndex == -1) {
                throw new DoValidException("sql语句格式不对");
            }

            int startIndex = "SELECT".length();
            countSql = new StringBuffer(sql).replace(startIndex, endIndex, " count(1) ").toString();
        }

        logger.info("count sql =>" + countSql);

        JSONArray validateArray = sqlTool.executeQuery(countSql);
        logger.info("count sql exec result =>" + validateArray.toJSONString());

        JSONObject jsonObject = validateArray.getJSONObject(0);
        Iterator<String> iterator = jsonObject.keySet().iterator();
        if (iterator.hasNext()) {
            return jsonObject.getLong(iterator.next());
        }
        return 0L;
    }

    private String getTopFive(SqlTool sqlTool,String sql,String database) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            throw new DoValidException("sql不能为空!");
        }

        if (!sql.toLowerCase().startsWith("select")) {
            throw new DoValidException("sql语句错误,不能执行select以外的语句!");
        }

        String errorDataQuery;
        if (sql.contains("\\*")) {
            errorDataQuery = sql.replaceFirst("\\*", setColumnsOfSql(sqlTool,database,getTablesFromSql(sql)));
        } else {
            int endIndex = sql.indexOf("FROM");
            if (endIndex == -1) {
                endIndex = sql.indexOf("from");
            }

            if (endIndex == -1) {
                throw new DoValidException("sql语句格式不对");
            }

            int startIndex = "SELECT".length();
            errorDataQuery = new StringBuffer(sql).replace(startIndex, endIndex, setColumnsOfSql(sqlTool,database,getTablesFromSql(sql))).toString();
        }

        StringBuilder sb = new StringBuilder("(");
        return sb.append(errorDataQuery.replace(";", "")).append(") ").toString();
    }

    private JSONArray getTablesFromSql(String sql){
        String tmp = sql.toLowerCase();

        //截取表名
        String tablePart = "";
        if(tmp.contains("where")) {
            tablePart = sql.substring(tmp.indexOf("from") + 4, tmp.indexOf("where"));
        } else if(tmp.contains("group")){
            tablePart = sql.substring(tmp.indexOf("from") + 4, tmp.indexOf("group"));
        } else {
            tablePart = sql.substring(tmp.indexOf("from") + 4, tmp.length());
        }

        //去空格，防止表名存在空格
        tablePart = tablePart.trim();

        String[] tablePartItems = tablePart.split(",");

        JSONArray tableNames = new JSONArray();

        if(tablePartItems.length > 1) {
            for (int i = 0; i < tablePartItems.length; i++) {
                String[] items = tablePartItems[i].split(" ");
                JSONObject obj = new JSONObject();
                obj.put("tableName", items[0]);
                obj.put("alias", items[1]);
                tableNames.add(obj);
            }
        } else {
            JSONObject obj = new JSONObject();
            obj.put("tableName", tablePart);
            obj.put("alias", "");
            tableNames.add(obj);
        }

        return tableNames;
    }

    private String setColumnsOfSql(SqlTool sqlTool, String database,JSONArray tableNames) throws Exception {
        StringBuilder sb = new StringBuilder();
        for(Object tableObj : tableNames) {
            JSONObject tableJsonObj = (JSONObject)tableObj;
            String tableName = tableJsonObj.getString("tableName");
            String alias = tableJsonObj.getString("alias");

            JSONArray columns = sqlTool.getColumns(database,tableName);

            for(Object obj : columns) {
                String columName = ((JSONObject)obj).getString("columnName");
                sb.append(" ");

                if(!StringUtils.isEmpty(alias)) {
                    sb.append(alias).append(".");
                }
                
                sb.append(columName)
                    .append(" ")
                    .append(tableName)
                    .append("_")
                    .append(columName)
                    .append(",");
            }
        }

        String result = sb.toString();

        if(result.lastIndexOf(",") == result.length() -1) {
            result = result.substring(0,result.length() -1);
        }

        return result + " ";
    }
}
