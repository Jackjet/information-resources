/**
前置子系统
该示例主要功能
采用应用适配器访问交换信息库，实现对交换信息库中数据的获取与存储
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
交换信息库中数据的获取
*/
function queryResultList(){
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  var sql = "select *  from demoTest";
  try {
    // 交换信息库的数据库连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    // 执行查询操作
    var queryResult = mysql.executeQuery(connect, sql);
    dcPrint("执行queryResultList方法,连接mysql数据库，获取交换数据信息","queryResultList"); 
    mysql.close(connect);
    return queryResult;
  }catch(error){
    // 捕获异常信息
    dcError(error,"queryResultList"); 
  } 
}

/**
实现对交换信息库中数据存储
*/
function appAdapterDataTransfer(){
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest1?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  try {
    // 获取数据连接
  var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
  dcPrint("appAdapterDataTransfer方法,连接mysql数据库，并且开始接入数据","appAdapterDataTransfer"); 
  // 获取交换数据信息
  var queryResult = queryResultList();
  for (var i = 0; i < queryResult.length; i++) {
    var id = queryResult[i]["id"];
    var state = queryResult[i]["state"];
    var executeSql = "select * from demoTest where id ='" + id + "'";
    var result = mysql.executeQuery(connect, executeSql);
    if (result.length > 0) {
        //修改操作
        var updateStr = "update demoTest set  username=?, age=?,createTime=?,updateTime=?,state=? where id=?";
        var item = [queryResult[i]["username"], 
                queryResult[i]["age"], 
                queryResult[i]["createTime"], 
                queryResult[i]["updateTime"],
                queryResult[i]["state"], 
                queryResult[i]["id"]];
        var flag = mysql.executeParam(connect, updateStr, item);
        dcPrint("接入数据结果" + flag,"appAdapterDataTransfer");
     
    } else {
      //新增操作
      var insertStr = "insert into demoTest (id, username, age,createTime,updateTime,state) values (?,?,?,?,?,?);";
      var item = [queryResult[i]["id"], 
              queryResult[i]["username"],
              queryResult[i]["age"], 
              queryResult[i]["createTime"], 
              queryResult[i]["updateTime"], 
              queryResult[i]["state"]];
      var flag = mysql.executeParam(connect, insertStr, item);
      dcPrint("接入数据结果 " + flag,"appAdapterDataTransfer");
    }
  }
  dcPrint("接入数据结果结束","appAdapterDataTransfer");
  mysql.close(connect);
  }catch(error){
     // 捕获异常信息
    dcError(error,"appAdapterDataTransfer"); 
  } 
}
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "appAdapterDataTransfer.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}

function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "appAdapterDataTransfer.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.appAdapterDataTransfer = appAdapterDataTransfer;