/**
传输子系统
该示例主要功能
提供消息确认和消息选择性重发机制以实现前置交换子系统之间安全的、可靠的信息传递功能
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var dc_emq = require("dc_emq");

var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();
var emq = new dc_emq.EmqService();

var logs = {};
logs["taskName"] = "dataBase";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "fengzi";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";

/**
 通过emq传输数据时，把数据加解密
*/
function msTransmission(){
  //emq配置信息
  var dbData =
      {
        "dbUrl": "jdbc:mysql://47.105.140.86:3306/emqData?characterEncoding=utf8&useSSL=false",
        "dbUserName": "root",
        "dbPassword": "Qwer@1234"
      };
  try{
    //获取连接
    emq.getConnection("mysql", dbData);
    var userDatas = [{"userName": "fengzi", "password": "123456"}];

    dcPrint("密码加密成功"+ userDatas,"dataTransmission");
    //数据保存
    emq.updateUser(userDatas);
    //关闭连接
    emq.close();
    dcPrint("执行dataTransmission方法,连接mysql数据库，数据加密传输"); 
  }catch(error){
    // 捕获异常信息
    dcError(error,"dataTransmission"); 
  }
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "messageTransfer.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "messageTransfer.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.msTransmission = msTransmission;
