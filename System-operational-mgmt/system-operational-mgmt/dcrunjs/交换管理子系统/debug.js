/**
交换管理子系统
该示例主要功能
提供流程的调试功能
*/
var dc_dcrun = require("dc_dcrun");
var dc_fileUtil = require("dc_file.util");
var dc_http = require("dc_http");

var dcrun = new dc_dcrun.DCRunService();
var fileUtil = new dc_fileUtil.FileUtilService();
var http = new dc_http.HttpService();

var logs = {};
logs["taskName"] = "dataBase";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "fengzi";//系统服务名称
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

/**
	读取文件内容，文件大小以及文件名称，并且打印出来
*/
function doFile(data){
  var filePath = "/root/fengzi.json";

  var content = fileUtil.readFileToString(filePath,"utf-8");
  var fileSize = fileUtil.sizeOf(filePath);
  var fileName = fileUtil.getName(filePath);

  dcPrint("文件名称 "+fileName,"doFile");
  dcPrint("文件内容 "+content,"doFile");
  dcPrint("文件大小 "+fileSize,"doFile");
}
/**
	http get请求
*/
function doHttp(data){
  var url = "http://www.baidu.com";
  var response = http.get(url,{},{});
  dcPrint("请求内容 "+response,"doHttp");
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "debug.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}
module.exports.doFile = doFile;
module.exports.doHttp = doHttp;





