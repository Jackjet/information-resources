/**
交换桥接子系统
该示例主要功能
提供单个数据源到多个目标、多个源到单个目标之间的定制功能
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
 提供单个数据源到多个目标
*/
function tableSplit() {
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  try {
    // 获取数据连接
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
    dcPrint("执行tableSplit方法,连接mysql数据库，并且开始同步数据", "tableSplit");
    // 执行查询操作，获取同步数据
    var sql = "select *  from demoTest ";
    var queryResult = mysql.executeQuery(connect, sql);
    for (var i = 0; i < queryResult.length; i++) {
      // 新增操作
      var insertStrOne = "insert into demoTestOne (username, age,peopleId) values (?,?,?)";
      var item = [
        queryResult[i]["username"],
        queryResult[i]["age"],
        queryResult[i]["id"]
      ];
      // 新增操作
      var flagOne = mysql.executeParam(connect, insertStrOne, item);
      dcPrint("同步结果 " + flagOne, "tableSplit");
      var insertStrTwo = "insert into demoTestTwo (state, createTime,updateTime,peopleId) values (?,?,?,?)";
      var item = [
        queryResult[i]["state"],
        queryResult[i]["createTime"],
        queryResult[i]["updateTime"],
        queryResult[i]["id"]
      ];
      var flagTwo = mysql.executeParam(connect, insertStrTwo, item);
      dcPrint("同步结果 " + flagTwo, "tableSplit");
    }
    dcPrint("同步结果结束", "tableSplit");
    mysql.close(connect)
  }catch(error){
    dcError(data,"tableSplit");
  }
}

/**
 多个源到单个目标
*/
function tableMerge() {
  var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
  var dbUser = "root";
  var dbPwd = "Qwer@1234";
  
  try {
    var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
  dcPrint("执行tableMerge方法,连接mysql数据库，并且开始同步数据", "tableMerge");
  var sql = "select A.username,A.age,A.peopleId,B.state,B.createTime,B.updateTime  from  demoTestOne A inner join demoTestTwo B on A.peopleId = B.peopleId";
  var queryResult = mysql.executeQuery(connect, sql);
  for (var i = 0; i < queryResult.length; i++) {
	  // 执行查询操作，获取同步数据
    var executeSql = "select * from demoTest where id ='" + queryResult[i]["peopleId"] + "'";
    var result = mysql.executeQuery(connect, executeSql);
    if (result.length > 0) {
      //修改操作
      var updateStr = "update demoTest set  username=?, age=?,createTime=?,updateTime=?,state=? where id=?";
      var item = [queryResult[i]["username"],
                  queryResult[i]["age"],
                  queryResult[i]["createTime"],
                  queryResult[i]["updateTime"],
                  queryResult[i]["state"],
                  queryResult[i]["peopleId"]
                 ];
      var flag = mysql.executeParam(connect, updateStr, item);
      dcPrint("接入数据结果" + flag, "tableMerge");
    } else {
      // 新增操作
      var insertStr = "insert into demoTest (id, username, age,createTime,updateTime,state) values (?,?,?,?,?,?)";
      var item = [queryResult[i]["peopleId"],
              queryResult[i]["username"],
              queryResult[i]["age"],
              queryResult[i]["createTime"],
              queryResult[i]["updateTime"],
              queryResult[i]["state"]
              ];
      var flag = mysql.executeParam(connect, insertStr, item);
      dcPrint("同步结果 " + flag, "tableMerge");
    }
  }
  dcPrint("同步结束", "tableSplit");
  // 关闭连接
  mysql.close(connect)
  }catch(error){
    dcError(data,"tableMerge");
  } 
}

function dcPrint(data, actionName) {
  logs["data"] = data;
  logs["filePath"] = "tableSplitAndMerge.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "tableSplitAndMerge.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}

module.exports.tableSplit = tableSplit;
module.exports.tableMerge = tableMerge;