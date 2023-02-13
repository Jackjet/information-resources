/**
交换桥接子系统
该示例主要功能
提供增量数据自动识别的功能，在不修改业务信息库结构的情况下，系统应能够自动的识别出需要交换的信息，包括新增、被修改或被删除的信息
提供部分数据、指标更新时的识别、同步功能
支持数据的实时同步、定时、增量同步
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var dc_schedule = require("dc_schedule") ;
var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "dataBase";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "交换桥接子系统";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";

/**
 查询同步数据方法
*/
function queryResultList() {
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  var sql = "select *  from demoTest ";
  try{
    //获取数据库连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    //执行查询操作
    var queryResult = mysql.executeQuery(connect, sql);
    dcPrint("执行queryResultList方法,连接mysql数据库，获取待同步数据","queryResultList");
    //关闭数据库连接
    mysql.close(connect);
    //返回查询结果
    return queryResult;
  }catch(error){
    //捕获异常信息
    dcError(error,"queryResultList");
  } 
}

/**
 查询待删除数据方法
*/
function queryDelResultList() {
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  try{
    var sql = "select *  from del_table where tableName='demoTest'";
    dcPrint("执行queryDelResultList,连接mysql数据库，获取待删除数据","queryDelResultList");
    //获取连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    //执行查询操作
    var queryResult = mysql.executeQuery(connect, sql);
    //关闭连接
    mysql.close(connect);
    //返回结果集
    return queryResult;
  }catch(error){
    //捕获异常信息
    dcError(error,"queryDelResultList");
  }
}

/**
 删除数据方法
*/
function deleteDate(id) {
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  var sql = "delete  from del_table where tableId='" + id + "' and tableName='demoTest'";
  try{
    //获取连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    //删除数据操作
    var result = mysql.execute(connect, sql);
    dcPrint("执行deleteDate,连接mysql数据库，删除del_table中数据","deleteDate");
    dcPrint("删除del_table中数据 " + result,"deleteDate");
  }catch(error){
    //捕获异常信息
    dcError(error,"deleteDate");
  }
}

/**
 提供部分数据、指标更新时的识别、同步功能
 支持数据的实时同步、定时、增量同步
*/
function executeDateSync() {
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest1?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  try{
    //获取数据库连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("执行executeDateSync方法,连接mysql数据库，并且开始同步数据","executeDateSync");
    //执行查询操作获取同步数据
    var queryResult = queryResultList();
    for (var i = 0; i < queryResult.length; i++) {
      var id = queryResult[i]["id"];
      var state = queryResult[i]["state"];
      //判断同步数据是否存在
      var executeSql = "select * from demoTest where id ='" + id + "'";
      var result = mysql.executeQuery(connect, executeSql);
      if (result.length > 0) {
        //存在进行修改操作
        var updateStr = "update demoTest set  username=?, age=?,createTime=?,updateTime=?,state=? where id=? ";
        var item = [queryResult[i]["username"],
                    queryResult[i]["age"], 
                    queryResult[i]["createTime"], 
                    queryResult[i]["updateTime"], 
                    queryResult[i]["state"], 
                    queryResult[i]["id"]];
        var flag = mysql.executeParam(connect, updateStr, item);
        dcPrint("同步结果 " + flag,"executeDateSync");

      } else {
        //不存在进行新增操作
        var insertStr = "insert into demoTest (id, username, age,createTime,updateTime,state) values (?,?,?,?,?,?)";
        var item = [queryResult[i]["id"], 
                    queryResult[i]["username"], 
                    queryResult[i]["age"], 
                    queryResult[i]["createTime"], 
                    queryResult[i]["updateTime"], 
                    queryResult[i]["state"]];
        var flag = mysql.executeParam(connect, insertStr, item);
        dcPrint("同步结果 " + flag,"executeDateSync");
      }
    }
    // 删除操作
    var delResult = queryDelResultList();
    for (var j = 0; j < delResult.length; j++) {
      var delId = delResult[j]["tableId"];
      var delsql = "delete  from demoTest where id='" + delId + "'";
      // 被删除的信息
      var delFlag = mysql.execute(connect, delsql);
      deleteDate(delId);
      dcPrint("同步结果 " + delFlag,"executeDateSync");
    }
    dcPrint ("同步结束","executeDateSync");
    //关闭连接
    mysql.close(connect);
  }catch(error){
    //捕获异常信息
    dcError(error,"executeDateSync");
  }
}

/**
定时、实时同步
*/
function setSchedule(cron, threadCount){
  dcPrint("执行setSchedule方法,启动定时器","setSchedule");
  //启动定时任务
  var scheduleService = new dc_schedule.ScheduleService(threadCount);
  scheduleService.schedule(executeDateSync, {}, cron);
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "dateSync.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}

function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "dateSync.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.setSchedule = setSchedule;