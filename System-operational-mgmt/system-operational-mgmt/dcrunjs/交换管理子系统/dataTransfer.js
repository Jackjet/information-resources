/**
交换管理子系统
该示例主要功能
支持跨平台的数据和消息传输
*/
var dc_dcrun = require("dc_dcrun");
var dc_mqttclient = require("dc_mqtt.client");
var mqttclient = new dc_mqttclient.MqttClientService();
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "dataBase";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "fengzi";//系统服务名称
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

/**
	根据用户名密码连接mqtt
*/
function connectMqtt(data){
	var uri = "tcp://47.105.207.68:61613";
	var user = "admin";
	var password = "password";
	mqttclient.connect2(uri, user, password);
   dcPrint("连接mqtt成功","connectMqtt");
}
/**
	把数据通过mqtt进行发送出去
*/
function sendData(data){
  var userInfo = {};
  userInfo["name"] = "fengzi";
  userInfo["sex"] = "man";
  userInfo["age"] = 28;
  mqttclient.fire("user",JSON.stringify(userInfo));
  dcPrint("发送的内容是"+JSON.stringify(userInfo),"sendData");
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "dataTransfer.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}

module.exports.connectMqtt = connectMqtt;
module.exports.sendData = sendData;





