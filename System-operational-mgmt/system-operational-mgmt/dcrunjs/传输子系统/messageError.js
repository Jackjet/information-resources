/**
传输子系统
该示例主要功能
提供消息差错处理功能
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");

var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "dataBase";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "fengzi";//系统服务名称
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

//连接数据库并且可以根据用户名到数据库里面查询到密码
function getPassword1(data){
  
   var dbUrl = "jdbc:mysql://47.105.140.86:3306/mytest?characterEncoding=utf8&useSSL=false";
   var dbUser = "root";
	var dbPwd = "Qwer@1234";
	try{
      //数据库连接
       var connect = mysql.getConnection(dbUrl, dbUser, dbPwd);
      dcPrint("数据库连接成功","getPassword");
     //查询语句
      var selectPasswordStr = "select * from demo WHERE username = \'"+data+"\'";
      dcPrint("查询语句是"+selectPasswordStr,"getPassword");
     //查询结果
      var resultList = mysql.executeQuery(connect, selectPasswordStr);
      var password = resultList[0]["password"];

      dcPrint("执行getPassword方法,查询到的密码是"+password,"getPassword");
    }catch(error){
      //异常捕获
		dcError(error,"getPassword");
    }
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "errorLocation.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}

function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "errorLocation.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.getPassword1 = getPassword1;