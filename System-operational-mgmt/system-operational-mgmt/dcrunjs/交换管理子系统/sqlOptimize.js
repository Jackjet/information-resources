/**
交换管理子系统
该示例主要功能
支持服务查询指标的优化，能为不同的查询条件订制化的服务
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "dataBase";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "张喆的脚本Demo";//系统服务名称
logs["filePath"] = "sqlOptimize.js";//文件路径 有目录把目录加上
logs["startTime"] = new Date().getTime();//调用时间
logs["userName"] = "d1Test1";//使用者

/**
	sql语句优化
*/
function sqlOptimize(){
  //从数据库中取出数据
   var dbUrlOut = "jdbc:mysql://47.105.140.86:3306/zzDemo?characterEncoding=utf8&useSSL=false";
	var dbUserOut = "root";
	var dbPwdOut = "Qwer@1234";
	var connectOut = mysql.getConnection(dbUrlOut, dbUserOut,dbPwdOut);
   logs["data"] = "执行oneToOne方法,连接mysql数据库";
   dcPrint(JSON.stringify(logs),"sqlOptimize");
   dcPrint("读取数据库switchingSubsystemOne","sqlOptimize");
   var queryStr = "select * from sql_optimize where num/2=100";
   var before = new Date();
   mysql.executeQuery(connectOut,queryStr);
   var after = new Date();
   dcPrint("优化前:"+(after-before)/1000+"秒","sqlOptimize");
   var queryStr1 = "select * from sql_optimize where  num=100*2";
   var before1 = new Date();
   mysql.executeQuery(connectOut,queryStr1);
   var after1 = new Date();
   dcPrint("优化后:"+(after1-before1)/1000+"秒","sqlOptimize");
   logs["data"] = "执行oneToOne方法,获取到one_to_one表中数据";
   dcPrint(JSON.stringify(logs),"sqlOptimize");
   mysql.close(connectOut);
}

function dcPrint(data,actionName){
  logs["data"] = data;
  var tempLogs= JSON.parse(JSON.stringify(logs));
  tempLogs["filePath"] += ("/"+actionName);
  dcrun.log(JSON.stringify(tempLogs));
}
module.exports.sqlOptimize = sqlOptimize;