/**
前置交换管理子系统
该示例主要功能
传输适配器支持RFC2616
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
logs["filePath"] = "frontHttp.js";//文件路径 有目录把目录加上
logs["startTime"] = new Date().getTime();//调用时间
logs["userName"] = "d1Test1";//使用者
//get请求
function get(){
  dcPrint("get请求开始","get")
  var url = "http://127.0.0.1:7665/get?name=get";
  var result =dchttp.get(url,null,null);
  dcPrint(result,"get")
  dcPrint("get请求结束","get")
}

//post请求
function post(){
  dcPrint("post请求开始","post")
  var url = "http://127.0.0.1:7665/post?name=post";
  var result =dchttp.post(url,null,null);
  dcPrint(result,"post")
  dcPrint("post请求结束","post")
}

//post请求
function delete1(){
  dcPrint("delete请求开始","delete1")
  var url = "http://127.0.0.1:7665/delete?name=delete1";
  var result =dchttp.delete(url,null,null,null);
  dcPrint(result,"delete1")
  dcPrint("delete请求结束","delete1")
}

//post请求
function put(){
  dcPrint("put请求开始","put")
  var url = "http://127.0.0.1:7665/put?name=put";
  var result =dchttp.put(url,null,null,null);
  dcPrint(result,"put")
  dcPrint("put请求结束","put")
}

function dcPrint(data,actionName){
  logs["data"] = data;
  var tempLogs= JSON.parse(JSON.stringify(logs));
  tempLogs["filePath"] += ("/"+actionName);
  dcrun.log(JSON.stringify(tempLogs));
}

module.exports.get = get;
module.exports.post = post;
module.exports.put = put;
module.exports.delete1 = delete1;