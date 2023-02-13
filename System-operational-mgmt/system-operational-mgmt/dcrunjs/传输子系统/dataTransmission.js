/**
传输子系统
该示例主要功能
提供对数据进行加解密、压缩及解压缩等功能
*/
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var dc_emq = require("dc_emq");
var dc_compression = require("dc_compression");
var dc_security = require("dc_security");

var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();
var emq = new dc_emq.EmqService();
var security = dc_security.SecurityService();
var compression = new dc_compression.CompressService();

var logs = {};
logs["taskName"] = "dataBase";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "交换桥接子系统";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";

/**
 提供对数据进行加解密
*/
function dataTransmission(){
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
    var userDatas = [{"userName": "shaozl", "password": "123456"}];
    dcPrint("密码加密前"+ userDatas[0]["password"],"dataTransmission");
    //数据加密
    userDatas[0]["password"] = security.md5(userDatas[0]["password"]);
    dcPrint("密码加密后"+ userDatas[0]["password"],"dataTransmission");
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

/**
压缩
outputPath 文件路径
unzipPath 目标路径
*/
function zipFile(filePath, outputPath){
  try{
    compression.zipFile(filePath, outputPath);
    dcPrint("压缩成功","zipFile");
  }catch(error){
    // 捕获异常信息
    dcError(error,"zipFile"); 
  }
}

/**
解压
outputPath 解压文件路径
unzipPath 目标路径
*/
function unzip(outputPath,unzipPath){
  try{
    compression.unzip(outputPath, unzipPath);
    dcPrint("解压成功","unzip");
  }catch(error){
    // 捕获异常信息
    dcError(error,"unzip"); 
  }
}
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "dataTransmission.js/" + actionName;
  dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
  logs["data"] = data+"";
  logs["filePath"] = "dataTransmission.js/"+actionName;
  dcrun.error(JSON.stringify(logs));
}
module.exports.dataTransmission = dataTransmission;
module.exports.zipFile = zipFile;
module.exports.unzip = unzip;