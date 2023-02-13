/**
交换桥接子系统
该示例主要功能
采用适配器组件访问桥接对像，实现数据的获取与存储
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "dataBase";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "fengzi";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

/**
 同步数据方法
*/
function executeDataSync() {
    var queryResult = queryData();
    var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest1?characterEncoding=utf8&useSSL=false";
    var dbUser = "root";
    var dbPwd = "Qwer@1234";
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("连接mysql数据库成功，并且开始同步数据","executeDataSync");
    for (var i = 0; i < queryResult.length; i++) {
        var id = queryResult[i]["id"];
        dcrun.log("id=" + id);
        var executeSql = "select * from demoTest where id ='" + id + "'";
        var result = mysql.executeQuery(connect, executeSql);
        dcPrint("查询结果 " + result,"executeDataSync");
        if (result.length > 0) {
            //新增
            var updateStr = "update demoTest set  username=?, age=?,createTime=?,updateTime=?,state=? where id=? ";
            var item = [queryResult[i]["username"], queryResult[i]["age"], queryResult[i]["createTime"], queryResult[i]["updateTime"], queryResult[i]["state"], queryResult[i]["id"]];
            var flag = mysql.executeParam(connect, updateStr, item);
            dcPrint("同步结果 " + flag,"executeDataSync");
        } else {
            //修改
            var insertStr = "insert into demoTest (id, username, age,createTime,updateTime,state) values (?,?,?,?,?,?);";
            var item = [queryResult[i]["id"], queryResult[i]["username"], queryResult[i]["age"], queryResult[i]["createTime"], queryResult[i]["updateTime"], queryResult[i]["state"]];
            var flag = mysql.executeParam(connect, insertStr, item);
            dcPrint("同步结果 " + flag,"executeDataSync");
        }
    }
    dcPrint("同步结束","executeDataSync");
    mysql.close(connect);
}

/**
 获取同步数据方法
*/
function queryData(data) {
  	  
    var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
    var dbUser = "root";
    var dbPwd = "Qwer@1234";
    var sql = "select *  from demoTest1"
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("连接数据库成功","queryData");
    var queryResult = mysql.executeQuery(connect, sql);
    mysql.close(connect);
    dcPrint("关闭数据库","queryData");
    return queryResult;
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "getDataAndSaveData.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}
module.exports.executeDataSync = executeDataSync;