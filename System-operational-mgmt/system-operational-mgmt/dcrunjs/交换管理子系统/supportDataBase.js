/**
交换管理子系统
该示例主要功能
支持各种主流数据库
主要实现了对mysql和postgre这两种数据库的连接和查询等功能
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var dc_postgre = require("dc_postgre");
var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();
var postgre = new dc_postgre.PostgreService();

var logs = {};
logs["taskName"] = "dataBase";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "fengzi";//系统服务名称
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

var connect = null;
/**
	连接mysql数据库
*/
function connect_mysql(){
   var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
	var dbUser = "root";
	var dbPwd = "Qwer@1234";
	connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
   dcPrint("执行connect_mysql方法,连接mysql数据库","connect_mysql");
}
/**
	查询demo表中的数据并且打印出来
*/
function query_mysql(){
  var queryStr = "select * from demo";
  var queryResult = mysql.executeQuery(connect,queryStr);
  dcPrint("查询数据","query_mysql");
}
/**
	连接postgre数据库
*/
function connect_postgre(){
 	var dbUrl = "jdbc:postgresql://47.105.140.86:5432/dcrun_user";
	var dbUser = "postgres";
	var dbPwd = "1234qwer1234asdf";
	connect = postgre.getConnection(dbUrl, dbUser, dbPwd);
   dcPrint("执行connect_postgre方法,连接postgre数据库","connect_postgre");
}

/**
	查询mqtt_user表中的数据并且打印出来
*/
function query_postgre(){
  var queryStr = "select * from mqtt_user";
  var queryResult = postgre.executeQuery(connect,queryStr);
  dcPrint("查询数据","query_postgre");
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "supportDataBase.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}
module.exports.connect_mysql = connect_mysql;
module.exports.connect_postgre = connect_postgre;
module.exports.query_mysql = query_mysql;
module.exports.query_postgre = query_postgre;






