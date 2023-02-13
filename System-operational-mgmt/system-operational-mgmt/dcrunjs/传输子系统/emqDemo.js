/** 
  传输子系统
  提供信息转换功能，支持基于规则配置的格式转换与内容转换功能
*/
var dc_dcrun = require('dc_dcrun')
var dcrun = new dc_dcrun.DCRunService()
var dc_mqttClient = require("dc_mqtt.client")
var mqtt = new dc_mqttClient.MqttClientService()
var dc_http = require('dc_http')
var http = new dc_http.HttpService()
var dc_security = require('dc_security')
var security = new dc_security.SecurityService()
var JSONs = Java.type('com.alibaba.fastjson.JSON')
var hmac = new dc_security.HmacSignService()

var mqttUrl = 'tcp://47.105.140.86:1883'
var appid = '0000001x'
var appkey = 'ead69a29335b4ccfbfb5658dae53b811'
var topic = 'cpu_memory/#'
var key = 'all'
var topicSetInfo = '0000001u/set'

var infos = JSONs.parse('[]')
var md5 = ''

// 日志
var logs = {};
logs["taskName"] = "Demo";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "杜德伟";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

// main
function main(){
  try {
      //订阅topic
      mqtt.on(topic,consumer)
      mqtt.on(topicSetInfo,consumerSetInfo)
      //连接mqtt
      mqtt.connect2(mqttUrl,appid,appkey)
  } catch (e) {
      dcError("js mqtt操作异常 error:"+e,"main");
  }
}

// 订阅回调
function consumer(topic, content) {
  //处理数据
  dataDispose(JSONs.parse(content))
  //发送信息
  send()
}

function consumerSetInfo(topic, content) {
  var tempMd5 = JSONs.parse(content).body.md5
  if(tempMd5 !== md5) {
    send()
  }
}

function dataDispose(data){
  var flag = true
  for (i = 0; i < infos.length; i++) {
    if(infos[i].ip === data.ip) {
      infos[i] = data
      flag = false
    }
  }
  if(flag){
    infos.add(data)
  }
}

// 请求
function send(){
  md5 = security.md5(JSONs.toJSONString(infos))
  var url = 'http://47.105.140.86:18081/setCpuAndMemory?key=' + key + '&md5=' + md5
  var timestamp = (new Date()).valueOf() + ''

  var authorization = hmac.sign(appid,timestamp,appkey)
  var header = {
    appId: appid,
    authorization: 'sign ' + authorization,
    timestamp: timestamp
  }
  var contentType = 'application/json'
  var parameters = {
    data: infos
  }
  var result = http.post(url,header,parameters,contentType)
  dcPrint(JSON.stringify(result),"send");
}


function dcPrint(data,actionName){
    logs["data"] = data;
    logs["filePath"] = "apiDemo.js/"+actionName;
    dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
    logs["data"] = data+"";
    logs["filePath"] = "errorLocation.js/"+actionName;
    dcrun.error(JSON.stringify(logs));
}

module.exports.send = send;
module.exports.main = main;


