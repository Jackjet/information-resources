var dc_dcrun = require("dc_dcrun");
var dc_http = require("dc_http");
var dc_schedule = require("dc_schedule");
var dc_mysql = require("dc_mysql");

var config = require("../config");

var dcrun = new dc_dcrun.DCRunService();
//============================================================
function excute () {
  var logs = getLogs();
  dcrun.log(logs);
  for (i = 0; i < logs.length;i ++) {
    insert(logs[i]);
  }
}

var startTime = config.instance().schedule.startTime;
function getLogs (){
  var dataBase = new dc_mysql.MySqlService();
  var conn = dataBase.getConnection(config.instance().database.dbUrl,config.instance().database.dbUser,config.instance().database.dbPwd);
  var sql = 'select * from '+ config.instance().tables.auditLog + ' where ';
  var where = '';
  if(startTime !== '') {
    where = where + "create_time > '" + startTime + "' and ";
  }
  var endTime = new Date().format('yyyy-MM-dd HH:mm:ss');
  where = where + "create_time <= '" + endTime + "';";
  sql = sql + where;  
  startTime = endTime;
  return dataBase.executeQuery(conn,sql);
}

var http = new dc_http.HttpService();
function insert (log) {
  var header = {};
  var parameters = {
    type: log.type,
    source: log.source,
    operand: log.operand,
    result: log.result,
    description: log.description,
    operator: log.operator,
    createById: log.create_by_id,
    loginIp:log.login_ip,
    level:log.level
  };
  if(parameters.type === undefined){
    parameters.type = ''
  }
  if(parameters.source === undefined){
    parameters.source = ''
  }
  if(parameters.operand === undefined){
    parameters.operand = ''
  }
  if(parameters.result === undefined){
    parameters.result = ''
  }
  if(parameters.description === undefined){
    parameters.description = ''
  }
  if(parameters.operator === undefined){
    parameters.operator = ''
  }
  if(parameters.createById === undefined){
    parameters.createById = ''
  }
  if(parameters.loginIp === undefined){
    parameters.loginIp = ''
  }
  if(parameters.level === undefined){
    parameters.level = ''
  }
  
  var contentType = 'application/json';
  var result = http.post(config.instance().interface.auditLog.insertLog,header,parameters,contentType);
  dcrun.log(result);
}
//============================================================
//???????????????
Date.prototype.format = function (format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                            ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}

module.exports = {
  instance: function  () {
    //??????????????????
    var scheduleService = new dc_schedule.ScheduleService(config.instance().schedule.threadCount);
    var parametersSchedule = {};
    scheduleService.schedule(excute,parametersSchedule, config.instance().schedule.cron);
  }
}
