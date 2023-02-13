/**
前置交换子系统
该示例主要功能
提供详细的错误日志，具备异常的快速定位功能
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
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
查询操作记录日志功能
*/
function queryResultList(){
   dcPrint("执行queryResultList方法开始","queryResultList","info");
  try {
    //数据库连接字符串
    var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
    dcPrint("数据库连接字符串:"+ dbUrl,"queryResultList","info");
    //数据库连接用户名
    var dbUser = "root";
    dcPrint("数据库连接用户名:"+ dbUser,"queryResultList","info");
    //数据库连接密码
    var dbPwd = "Qwer@1234";
    dcPrint("数据库连接密码:"+ dbPwd,"queryResultList","info");
    //数据查询语句
    var sql = "select *  from demoTest1";
    dcPrint("数据查询语句:"+sql,"queryResultList","info");
    //数据库连接成功
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("数据库连接成功","queryResultList","info");
    //执行查询语句返回结果
    var queryResult = mysql.executeQuery(connect, sql);
    //执行查询语句返回结果
    dcPrint("执行查询语句返回结果:" + queryResult,"queryResultList","info");
    //关闭数据库连接
    mysql.close(connect);
    dcPrint("关闭数据库连接","queryResultList","info");
  } catch(err) {
    //捕获异常信息
    dcPrint("数据库异常" + err,"queryResultList","error"); 
    dcPrint("执行queryResultList方法结束","queryResultList","info");
  }
}

function dcPrint(data,actionName,level){
  logs["data"] = data;
  logs["filePath"] = "errorLogLocation.js/" + actionName;
  var logInfo = JSON.stringify(logs);
  if ("error" == level) {
    dcrun.error(logInfo);
  } else {
    dcrun.log(logInfo);
  } 
}
module.exports.queryResultList = queryResultList;