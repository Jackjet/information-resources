var dc_dcrun = require("dc_dcrun");
var dc_shell = require("dc_shell");
var dc_http = require("dc_http");
var dc_osinfo = require("dc_osinfo");
var dc_schedule = require("dc_schedule");

var config = require("../config");

var dcrun = new dc_dcrun.DCRunService();
var shell = new dc_shell.ShellService();
//============================================================
function excute() {
  var osinfo = new dc_osinfo.OsInfoService();
  var memRam = osinfo.getOsInfoByType('RAM').getMemRam().replace(' GB','') * 1024;
  
  dcrun.log('memRam = ' + memRam);
  //nginx
  var memorySource = 0;
  var dataSource = 0;
  var command1 = "ps auxw|sort -rn -k4| grep " + config.instance().other.systemMonitor.processName + " | grep -v grep| awk '{print $4}'"
  var status = 'normal';
  shell.run(command1,function (result) {
    var arr = result.substring(0,result.length-1).split('\n');
	if(arr.length === 0){
      status = 'abnormal';
    }
    for(i = 0; i< arr.length; i++){
      memorySource += Number(arr[i])
    }
    memorySource = (memorySource * memRam / 100).toFixed(2);
    dcrun.log('memorySource = ' + memorySource);
  });
  var command2 = 'cd '+ config.instance().other.systemMonitor.directory + '\n du -sh\n';
  shell.run(command2,function (result) {
    dcrun.log('result = ' + result);
    dataSource = result.split('M')[0];
  });
  var data = {
    nodeName:config.instance().other.systemMonitor.nodeName,
    nodeId:config.instance().other.systemMonitor.nodeId,
    system:config.instance().other.systemMonitor.system,
    systemId:config.instance().other.systemMonitor.systemId,
    dataSource:dataSource,
    memorySource:memorySource,
    status:status
  }
  insert(data)
}

function insert (data) {
  var http = new dc_http.HttpService();
  var header = {};
  var contentType = 'application/json';
  var result = http.post(config.instance().interface.systemMonitor.insert,header,data,contentType);
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