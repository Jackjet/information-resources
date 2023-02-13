/**
交换桥接子系统
该示例主要功能
提供大于2G以上文件传输功能，并支持分包机制进行传输
*/
var dc_dcrun = require("dc_dcrun");
var dc_http = require("dc_http");
var dchttp = new dc_http.HttpService();
var dcrun = new dc_dcrun.DCRunService();
var logs = {};
logs["taskName"] = "dataBase";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "张喆的脚本Demo";//系统服务名称
logs["filePath"] = "httpFile.js";//文件路径 有目录把目录加上
logs["startTime"] = new Date().getTime();//调用时间
logs["userName"] = "d1Test1";//使用者
//大文件上传
function httpFileUpload(){
    try {
        dcPrint("上传开始","httpFileUpload")
        var url = "http://127.0.0.1:7665/receiveFile";
        dchttp.uploadFileWithForm(url, "/root/websites/resourceLib/tangshanFileDemo/tangshanFileTemp.rar")
        dcPrint("上传结束","httpFileUpload")
    } catch (e) {
        dcPrint("js error:" + e,"httpFileUpload");
   }
}



function dcPrint(data,actionName){
  logs["data"] = data;
  var tempLogs= JSON.parse(JSON.stringify(logs));
  tempLogs["filePath"] += ("/"+actionName);
  dcrun.log(JSON.stringify(tempLogs));
}

module.exports.httpFileUpload = httpFileUpload;