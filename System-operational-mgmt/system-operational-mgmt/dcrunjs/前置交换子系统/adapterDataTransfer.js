/**
前置交换子系统
该示例主要功能
采用传输适配器接入传输子系统
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
查询同步结果集
*/
function queryResultList(){
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  var sql = "select *  from demoTest";
  try {
    // 获取数据库连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    // 执行查询操作
    var queryResult = mysql.executeQuery(connect, sql);
    dcPrint("执行queryResultList方法,连接mysql数据库，采用应用适配器获取交换数据信息","queryResultList"); 
    mysql.close(connect);
    return queryResult;
  }catch (error) {
   // 捕获异常信息
   dcError(error,"queryResultList");
  }
}

/**
采用传输适配器接入传输子系统
*/
function adapterDataTransfer(){
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest1?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  try{
    // 获取数据库连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("执行adapterDataTransfer方法,连接mysql数据库，并且开始接入数据","adapterDataTransfer"); 
    // 执行查询操作，获取结果集
    var queryResult = queryResultList();
    for (var i = 0; i < queryResult.length; i++) {
      var id = queryResult[i]["id"];
      var state = queryResult[i]["state"];
      // 判断子系统表中是否存在当前数据
      var executeSql = "select * from demoTest where id ='" + id + "'";
      var result = mysql.executeQuery(connect, executeSql);
      if (result.length > 0) {
        //存在进行更新操作
        var updateStr = "update demoTest set  username=?, age=?,createTime=?,updateTime=?,state=? where id=? ";
        var item = [queryResult[i]["username"], 
                    queryResult[i]["age"], 
                    queryResult[i]["createTime"], 
                    queryResult[i]["updateTime"],
                    queryResult[i]["state"], 
                    queryResult[i]["id"]];
        var flag = mysql.executeParam(connect, updateStr, item);
        dcPrint("数据传输结果 " + flag,"adapterDataTransfer");

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
        dcPrint("接入数据结果 " + flag,"adapterDataTransfer");
      }
    }
    dcPrint("接入数据结束","adapterDataTransfer");
    mysql.close(connect);
  }catch(error){
    //捕获异常
    dcError(error,"adapterDataTransfer");
  }
}
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "adapterDataTransfer.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}

function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "adapterDataTransfer.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.adapterDataTransfer = adapterDataTransfer;