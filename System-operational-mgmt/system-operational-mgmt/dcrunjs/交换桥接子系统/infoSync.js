/**
交换桥接子系统
该示例主要功能
支持部门业务信息库与交换库之间的双向信息同步
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "infoSync";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "fengzi";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

var queryResult;
/**
	连接业务部门信息库，并且读取出source表中内容
*/
function getData_InfoDatabase(){
   var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
	var dbUser = "root";
	var dbPwd = "Qwer@1234";
	var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
   dcPrint("执行connect_InfoDatabase方法,连接mysql数据库","getData_InfoDatabase");
  
   var queryStr = "select * from source";
   queryResult = mysql.executeQuery(connect,queryStr);
   dcPrint("执行getData_InfoDatabase方法,获取到source表中数据","getData_InfoDatabase");
   mysql.close(connect);
   dcPrint("关闭数据库连接","getData_InfoDatabase");
}
/**
	根据从业务部门信息库读取出来的内容，同步到交换库里面去
*/
function sync_InfoDatabase(){
	var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest1?characterEncoding=utf8&useSSL=false";
	var dbUser = "root";
	var dbPwd = "Qwer@1234";
	var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
   dcPrint("执行sync_InfoDatabase方法,连接mysql数据库，并且开始同步数据","sync_InfoDatabase");
  for(var i = 0;i< queryResult.length;i++){
    var insertStr = "insert into user (name, pwd) values (?,?);";
    var item = [queryResult[i]["name"],queryResult[i]["pwd"]];
    var flag = mysql.executeParam(connect, insertStr, item);
    dcPrint("同步结果 "+flag,"sync_InfoDatabase");
  } 
  dcPrint("同步结束","sync_InfoDatabase");
  mysql.close(connect);
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "infoSync.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}

module.exports.getData_InfoDatabase = getData_InfoDatabase;
module.exports.sync_InfoDatabase = sync_InfoDatabase;






