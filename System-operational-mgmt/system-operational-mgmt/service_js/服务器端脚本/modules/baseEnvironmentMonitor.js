var dc_dcrun = require("dc_dcrun");
var dc_shell = require("dc_shell");
var dc_http = require("dc_http");
var dc_schedule = require("dc_schedule");

var config = require("../config");

var dcrun = new dc_dcrun.DCRunService();
var shell = new dc_shell.ShellService();

//============================================================
function excute() {
  var nginxStatus = 'normal';
  var dbStatus = 'normal';
  //nginx状态
  var command = "ps aux | grep nginx | grep -v grep | grep -v /bin/sh | awk '{print $2}' | sort -n | head -n 1";
  shell.run(command,function (result) {
    dcrun.log('result=' + result);
    if(Number(result) === 0){
      nginxStatus = 'abnormal';
    }
  });

  //数据库状态
  var command1 = "ps aux | grep mysql | grep -v grep | grep -v /bin/sh | awk '{print $2}' | sort -n | head -n 1";
  shell.run(command1,function (result) {
    dcrun.log('result1=' + result);
    if(Number(result) === 0){
      dbStatus = 'abnormal';
    }
  });
  
  var data = {
    nodeId: config.instance().other.baseEnvironmentMonitor.nodeId,
    nodeName: config.instance().other.baseEnvironmentMonitor.nodeName,
    db: dbStatus,
    systemSoftware:nginxStatus
  };
  insert (data);
}

function insert (data) {
  var http = new dc_http.HttpService();
  var header = {};
  var contentType = 'application/json';
  var result = http.post(config.instance().interface.baseEnvironmentMonitor.insert,header,data,contentType);
  dcrun.log(result);
}

module.exports = {
  instance: function  () {
    //启动定时任务
    var scheduleService = new dc_schedule.ScheduleService(config.instance().schedule.threadCount);
    var parametersSchedule = {};
    scheduleService.schedule(excute,parametersSchedule, config.instance().schedule.cron);
  }
}



