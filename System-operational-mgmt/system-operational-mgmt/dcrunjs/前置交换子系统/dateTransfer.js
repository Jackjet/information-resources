/**
前置交换子系统
该示例主要功能
支持不同交换信息库之间的双向信息交换。
前置交换子系统能够从交换信息库中提取数据交给交换传输子系统传递，
也能够从交换传输子系统中获取数据存储到指定的信息库或其他数据库中
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
获取交换数据信息
*/
function queryResultList(){
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  var sql = "select *  from demoTest";
  try{
    //获取数据库连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    //执行查询操作
    var queryResult = mysql.executeQuery(connect, sql);
    dcPrint("执行queryResultList方法,连接mysql数据库，获取交换数据信息","queryResultList"); 
    //关闭连接
    mysql.close(connect);
    //返回查询结果
    return queryResult;
  }catch(error){
    dcError(error,"queryResultList");
  }
}

/**
存储到指定的信息库或其他数据库中
*/
function dateTransfer(){
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest1?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  try{
    // 获取连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("执行dateTransfer方法,连接mysql数据库，并且开始传递数据","dateTransfer"); 
    // 执行查询操作
    var queryResult = queryResultList();
    // 同步数据
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
        dcPrint("数据传输结果 " + flag,"dateTransfer");

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
        dcPrint("数据传输结果" + flag,"dateTransfer");
      }
    }
    dcPrint("数据传输结束","dateTransfer");
    // 关闭数据库连接
    mysql.close(connect);
  }catch(error){
    dcError(error,"dateTransfer");
  }
}
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "dateTransfer.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "dateTransfer.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.dateTransfer = dateTransfer;