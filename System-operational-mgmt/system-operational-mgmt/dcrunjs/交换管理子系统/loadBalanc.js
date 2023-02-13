/**
交换管理子系统
该示例主要功能
根据新增负载均衡配置页 传入参数后修改nginx配置文件 重启nginx，
主要实现用postman调用一个路由可随机访问ip地址
*/
var dc_dcrun = require("dc_dcrun");
var dc_shell = require("dc_shell");
var dc_fileutil = require("dc_file.util");

var dcrun = new dc_dcrun.DCRunService();
var fileUtil = new dc_fileutil.FileUtilService();
var shell = new dc_shell.ShellService();


var logs = {};
logs["taskName"] = "loadBalanc";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "对外暴露的负载均衡的接口";//系统服务名称
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者


function loadBalanc(data){

  dcrun.log(data);  
  dcrun.log("进入负载均衡方法");

  //========================================进入负载均衡方法=========================

  var param = data;
//param["name"] = "xplorasia-server";
//param["server"] = "[\"127.0.0.1:8888\",\"127.0.0.1:8888\"]";
//param["oldname"] = "xplorasia-server";
//param["oldserver"] = "[\"127.0.0.1:8082\",\"127.0.0.1:8018\"]";
//param["name"] = "xpll";
//param["server"] = "[\"127.0.0.1:8082\",\"127.0.0.1:8884\"]";
//param["oldname"] = "";
//param["oldserver"] = "";

//要更改的字符串前的固定字符串
 var upstream_string_old = "include /etc/nginx/conf.d/*.conf;";
 var server_config_string_old= "include /etc/nginx/default.d/*.conf;";
 var upstream_string_new= "include /etc/nginx/conf.d/*.conf;";
 var server_config_string_new= "include /etc/nginx/default.d/*.conf;";
//新的字符串
  var server_json_old = JSON.parse(param.server);
  dcrun.log(server_json_old[0])
  var server_String = "";
    for (var i = 0; i < server_json_old.length; i++) {
      server_String += "server " + server_json_old[i]+";";
    }
  dcrun.log(server_String);
  //http块下的负载均衡字符串待写入
  var upstream = "    upstream "+ param.name+" {"+server_String +"}" ;
  dcrun.log(upstream);
  //server块下的负载均衡字符串待写入 
  var server_config = "    location ^~ /"+ param.name+" { rewrite  ^/"+param.name+"/(.*)$ /$1 break;proxy_pass http://"+ param.name+"/;client_max_body_size    100m;}";
  dcrun.log(server_config);
  upstream_string_new = upstream_string_old + upstream;
  server_config_string_new = server_config_string_old+ server_config;
  dcrun.log( "这是新的upstream"+ upstream_string_new);
  dcrun.log("这是新的server"+ server_config_string_new);
//旧的字符串
if(param.oldname!=""){
  var server_json_old = JSON.parse(param.oldserver);
  dcrun.log(server_json_old[0])
  var server_String = "";
    for (var i = 0; i < server_json_old.length; i++) {
      server_String += "server " + server_json_old[i]+";";
    }
  dcrun.log(server_String);
  //http块下的负载均衡字符串待写入
  var upstream = "    upstream "+ param.oldname+" {"+server_String +"}" ;
  dcrun.log(upstream);
  //server块下的负载均衡字符串待写入 
  var server_config = "    location ^~ /"+ param.oldname+" { rewrite  ^/"+param.oldname+"/(.*)$ /$1 break;proxy_pass http://"+ param.name+"/;client_max_body_size    100m;}";
  dcrun.log(server_config);
  upstream_string_old = upstream_string_old + upstream;
  server_config_string_old = server_config_string_old+ server_config;
  dcrun.log( "这是旧的upstream"+ upstream_string_old);
  dcrun.log("这是旧的server"+ server_config_string_old);
}


  //找到文件/etc/nginx/nginx.conf
  var path = "/etc/nginx/nginx.conf";
  var path_bool = fileUtil.fileExists(path);
  dcrun.log(path_bool);
  // 文件内容变成字符串
  var path_string = fileUtil.readFileToString(path,"");
  dcrun.log(path_string);
 //查找固定的字符串

  path_string=path_string.replace(upstream_string_old,upstream_string_new)
  dcrun.log(11);
  dcrun.log(path_string);
  //更改访问配置

  path_string=path_string.replace(server_config_string_old,server_config_string_new)
  dcrun.log(22);
  dcrun.log(path_string);
  //写回文件
  fileUtil.writeStringToFile(path, path_string)

  //重启nginx
  var cmdStr = "nginx -s reload"
  shell.run(cmdStr, function (log) {
    dcrun.log("js接收到：" + log);
  });
   //========================================结束负载均衡方法=========================
  dcPrint("发送的内容是"+path_string,"loadBalanc");
  return "issuccess";
}
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "loadBalanc.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}
dcrun.registerWebApi("POST", loadBalanc, "loadBalanc");

module.exports.loadBalanc = loadBalanc;