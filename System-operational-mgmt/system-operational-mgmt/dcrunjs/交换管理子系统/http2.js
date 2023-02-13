/**
交换管理子系统
该示例主要功能
根据新增负载均衡配置页 传入参数后修改nginx配置文件 重启nginx后，
主要实现搭两个服务，访问一个路由地址会随机访问一个服务器，能把访问结果返回
*/
var http_class = require("dc_http");
var http = new http_class.HttpService();

var dc_dcrun = require("dc_dcrun");
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "http2";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "对外暴露的负载均衡的接口";//系统服务名称
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

function http2(url1,parameters1){  
  try {
    var url = url1;
    var parameters = parameters1;
    http.setTimeout(10 * 60, 10 * 60, 10 * 60); 
    var res = http.get(url,null,parameters);
    dcrun.log("返回结果:" + res);
    dcPrint("发送的内容是"+res,"http1");
} catch (e) {
    dcrun.log("js error:" + e);
    dcPrint("发送的内容是error","http1"); 
}
}
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "http2.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}
module.exports.http2 = http2;