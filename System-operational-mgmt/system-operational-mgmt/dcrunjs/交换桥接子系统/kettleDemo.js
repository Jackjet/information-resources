/**
  交换桥接子系统
  利用kettle对于大量数据的快速抽取.
*/
var dbUrl = "jdbc:mysql://47.105.140.86:3306/DCRunKettleTarget";
var dbUser = "root";
var dbPwd = "Qwer@1234";
var dcMysql = require("dc_mysql");
var mysql = new dcMysql.MySqlService();

var dc_dcrun = require("dc_dcrun");
var dcrun = new dc_dcrun.DCRunService();

var kettleClass = require("dc_kettle");
// 日志
var logs = {};
logs["taskName"] = "Demo";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "杜德伟";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

// main
function main(){
   // 执行kettle
   try {
       var kettleInstance = new kettleClass.KettleService();
       var result = kettleInstance.run("/root/ddw/dcrun/Untitled.kjb");
       dcPrint(result,"main");
    } catch (e) {
       dcError("js kettle运行错误 error:" + e,"main");
    }
  
    // 1.获得mysql数据库连接
    dcPrint("获得mysql数据库连接========================================","main");
    var connect = null;
    try {
        connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
        dcPrint("获得mysql数据库连接=" + connect,"main");
    } catch (e) {
        dcError("js mysql数据库链接失败 error:" + e,"main");
    }
    // 2.获得查询结果列表
    dcrun.log("获得查询结果列表===============================================","main");
    try {
        var findSql = "select * from user";
        var resultList = mysql.executeQuery(connect, findSql);
        dcPrint("获得查询结果列表=" + resultList,"main");
    } catch (e) {
        dcError("js mysql查询失败 error:" + e,"main");
    }
    dcPrint("关闭数据库===============================================","main");
    mysql.close(connect);
}

// 打印
function dcPrint(data,actionName){
    logs["data"] = data;
    logs["filePath"] = "kettleDemo.js/"+actionName;
    dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
   logs["data"] = data+"";
   logs["filePath"] = "errorLocation.js/"+actionName;
   dcrun.error(JSON.stringify(logs));
}

module.exports.main = main;

