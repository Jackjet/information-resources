/**
  传输子系统
  支持多种传输协议，包括Http、Soap、MQ、JMS，可灵活选择其中的一种或多种协议进行通信
*/

// https
var dc_https = require("dc_https");
var https = new dc_https.HttpsService();
// http
var url = "http://45.32.39.221/xxjw.7z";
var dc_http = require("dc_http");
var http = new dc_http.HttpService();
// jms
var dc_jms = require("dc_jms");
var jms = new dc_jms.JmsService();
// mqtt
var dc_mqtt = require("dc_mqtt.client");
var mqtt_client = new dc_mqtt.MqttClientService();
// soap
var dc_webservice = require("dc_webservice.client");
var webservice_client = new dc_webservice.WebserviceClientService(); 
//
var dc_dcrun = require("dc_dcrun");
var dcrun = new dc_dcrun.DCRunService();
// 日志
var logs = {};
logs["taskName"] = "Demo";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "杜德伟";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

// https请求
function https_request(){
    try {
      https.setTimeout(10 * 60, 10 * 60, 10 * 60);
      https.setCert(["/root/ddw/dcrun/test.crt"]);
      var map = {
          "phone" : "chuzhang9",
          "password" : "f9e9c455c87f29309f7e2d32b70c4817"
      };
      var result = https.post("https://smartbusinessrel.yunleader.com/smartbusiness/webadmin/user/signIn",
          null,
          map,
          "application/json");
      dcPrint("https请求结果:"+result,"https_request");
    } catch (e) {
      dcError("js https请求失败:" + e,"https_request");
    }
}

// http请求
function http_request(){
    try {
      // http.download(url, ".", "2.7z", function (isSuccess, msg) {
      //     print(isSuccess + "," + msg);
      // })
      // print(http.get("http://www.baidu.com", null, null));
      // print(http.post("https://cnodejs.org/api/v1/message/mark_one/58ec7d39da8344a81eee0c14", null, {
      //     "accesstoken": "41818e45-7feb-43fe-999f-7523fdf9ea61"
      // }, null));
      var url1 = "http://speedtest.fremont.linode.com/100MB-fremont.bin";
      http.setTimeout(10 * 60, 10 * 60, 10 * 60);
      http.downloadWithMultiThread(url1,"/root/ddw/dcrun/","100MB-fremont.bin",24,100,function (isSuccess, msg) {
           dcPrint("http请求结果:"+isSuccess + "," + msg,"http_request");
      });
  } catch (e) {
      dcError("js http请求失败:" + e,"http_request");
  }
}

// jms协议请求
function jms_request(){
   var conn = jmsService.openConnect("admin", "admin", "tcp://127.0.0.1:61616");
   //队列模式
   jmsService.createConsumer("queue", "test", conn, function (s, s1) {
        dcPrint(s1,"jms_request");
   });
   jmsService.sendMsg("queue", "test", conn, "This is a test msg");
   //订阅模式
   jmsService.createConsumer("subs", "test", conn, function (s, s1) {
        dcPrint(s1,"jms_request");
   });
   // 发送
   jmsService.sendMsg("subs", "test", conn, "This is a test msg");
   jmsService.sendMsg("subs", "test", conn, "This is a test msg2");
   jmsService.sendMsg("subs", "test", conn, "This is a test msg3");
 
}

// mqtt
function mqtt_request(){
    mqtt_client.on("tttt/#", function (topic, data) {
        dcPrint("js接收到："+topic + "," + data,"mqtt_request");
    })
    mqtt_client.on("#/sys/app/#/ping", function (topic, data) {
        dcPrint("js接收到："+topic + "," + data,"mqtt_request");
    })
    mqtt_client.on("xxx/ping", function (topic, data) {
        dcPrint("js接收到："+topic + "," + data,"mqtt_request");
    })
    //
    try {
        mqtt_client.connect2("tcp://47.105.207.68:61613", "admin", "password");
        dcPrint("Mqtt链接成功","mqtt_request");
    } catch (e) {
        dcError("js Mqtt链接失败 error:"+e,"mqtt_request");
    }
}

// soap协议
function webservice_request(){
    var url = "http://www.webxml.com.cn/WebServices/RandomFontsWebService.asmx?wsdl";
    var method = "getChineseFonts";
    var result = webservice_client.request(url, method,[6]);
    //
    dcPrint(result,"webservice_request");
}

// 打印
function dcPrint(data,actionName){
    logs["data"] = data;
    logs["filePath"] = "httpsDemo.js/"+actionName;
    dcrun.log(JSON.stringify(logs));
}
function dcError(data,actionName){
   logs["data"] = data+"";
   logs["filePath"] = "errorLocation.js/"+actionName;
   dcrun.error(JSON.stringify(logs));
}

module.exports.https_request = https_request;
module.exports.http_request = http_request;
module.exports.jms_request = jms_request;
module.exports.mqtt_request = mqtt_request;
module.exports.webservice_request = webservice_request;




