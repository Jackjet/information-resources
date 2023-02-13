/**
  交换管理子系统
  支持流程版本管理能力，支持一个流程多个版本同时运行
*/
var dc_dcrun = require("dc_dcrun");
var dcrun = new dc_dcrun.DCRunService();
//
var schedule = new dc_schedule.ScheduleService(4);
// 日志
var logs = {};
logs["taskName"] = "Demo";
logs["taskType"] = "management-tasks";
logs["nodeName"] = "86服务器";
logs["serviceName"] = "杜德伟";
logs["startTime"] = new Date().getTime();
logs["userName"] = "d1Test1";//使用者

// verson_log
function main(){
  // 计时器
  schedule.schedule(function(data){
      dcPrint("定时器执行版本1","main");
  },null,"*/10 * * * * ?");

  schedule.schedule(function(data){
      dcPrint("定时器执行版本2","main");
  },null,"*/10 * * * * ?");
  
}
// 打印
function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "switchingVersonManage.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
  
}

module.exports.main = main;
