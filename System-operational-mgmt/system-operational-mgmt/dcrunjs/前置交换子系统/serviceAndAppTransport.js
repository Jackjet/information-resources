/**
前置交换子系统
该示例主要功能
支持将现有服务和应用接入到交换传输子系统
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
获取现有服务应用数据
*/
function queryResultList(){
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  var sql = "select *  from demoTest";
  try {
    //获取连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    //执行查询操作
    var queryResult = mysql.executeQuery(connect, sql);
    dcPrint("执行queryResultList方法,连接mysql数据库，获取交换数据信息","queryResultList"); 
    //关闭连接
    mysql.close(connect);
    //返回结果集
    return queryResult;
  }catch(error){
    dcPrint(error,"queryResultList");
  }
}

/**
接入到交换传输子系统
*/
function serviceAndAppTransport() {
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest1?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  try{
    // 获取连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("serviceAndAppTransport,连接mysql数据库，并且开始接入数据","serviceAndAppTransport"); 
    // 获取接入结果集
    var queryResult = queryResultList();
    // 获取接入结果集遍历
    for (var i = 0; i < queryResult.length; i++) {
      var id = queryResult[i]["id"];
      var state = queryResult[i]["state"];
      //执行查询操作，判断子系统中是否存在
      var executeSql = "select * from demoTest where id ='" + id + "'";
      var result = mysql.executeQuery(connect, executeSql);
      if (result.length > 0) {
        //存在进行修改操作
        var updateStr = "update demoTest set  username=?, age=?,createTime=?,updateTime=?,state=? where id=?";
        var item = [queryResult[i]["username"], 
                    queryResult[i]["age"], 
                    queryResult[i]["createTime"], 
                    queryResult[i]["updateTime"],
                    queryResult[i]["state"], 
                    queryResult[i]["id"]];
        var flag = mysql.executeParam(connect, updateStr, item);
        dcPrint("接入数据结果 " + flag,"serviceAndAppTransport");

      } else {
        //不存在进行新增操作
        var insertStr = "insert into demoTest (id, username, age,createTime,updateTime,state) values (?,?,?,?,?,?);";
        var item = [queryResult[i]["id"], 
                    queryResult[i]["username"],
                    queryResult[i]["age"], 
                    queryResult[i]["createTime"], 
                    queryResult[i]["updateTime"], 
                    queryResult[i]["state"]];
        var flag = mysql.executeParam(connect, insertStr, item);
        dcPrint("接入数据结果 " + flag,"serviceAndAppTransport");
      }
    }
    dcPrint("接入数据结束","serviceAndAppTransport");
    //关闭连接
    mysql.close(connect);
  }catch(error){
    dcPrint(error,"serviceAndAppTransport");
  }  
}
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "serviceAndAppTransport.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "serviceAndAppTransport.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.serviceAndAppTransport = serviceAndAppTransport;