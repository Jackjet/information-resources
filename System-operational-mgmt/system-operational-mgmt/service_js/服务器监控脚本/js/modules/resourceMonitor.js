var dc_dcrun = require('dc_dcrun')
var dc_osinfo = require("dc_osinfo");
var dc_http = require("dc_http");
var dc_schedule = require("dc_schedule");

//============================================================
var dcrun = new dc_dcrun.DCRunService();
var osinfo = new dc_osinfo.OsInfoService();

function excute () {
  var all = getInfo();
  insert(all);
}

function getInfo(){
  var all = osinfo.getOsInfo();
  return all;
}

function insert (all) {
  var http = new dc_http.HttpService();
  var header = {};
  var contentType = 'application/json';
  var parameters = {
    intranetIp:config.instance().other.resourceMonitor.intranetIp,
    ram:Number(all.ramInfo.memRam.replace(' GB','')) * 1024,
    rom:Number(all.hardDiskInfo.used.replace('kb','')) / 1024,
    cpu:Number(all.cpuInfo.cupFreeIdle.replace('%',''))
  };
  dcrun.log(all);
    dcrun.log(parameters.rom);
  var result = http.post(config.instance().interface.resourceMonitor.insert,header,parameters,contentType);
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
