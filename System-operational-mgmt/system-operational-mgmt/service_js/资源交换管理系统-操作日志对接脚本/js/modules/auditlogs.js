var dc_dcrun = require("dc_dcrun");
var dc_http = require("dc_http");
var dc_schedule = require("dc_schedule");
var dc_postgre = require("dc_postgre");

var config = require("../config");
var startTime = config.instance().schedule.startTime;
var http = new dc_http.HttpService();
//============================================================
function excute () {
  dcrun.log('查询审计日志');
  var logs = getLogs();
  dcrun.log('写入审计日志');
  for (i = 0; i < logs.length;i ++) {
    insert(logs[i]);
  }
}

function getLogs (){
  var dataBase = new dc_postgre.PostgreService();
  var conn = dataBase.getConnection(config.instance().database.dbUrl,config.instance().database.dbUser,config.instance().database.dbPwd);
  var endTime = new Date().format('yyyy-MM-dd HH:mm:ss');
  var sql = " SELECT " +
            "     oli.login_name" +
            "     ,regexp_replace(oli.module, '/', '') as module" +
            "     ,oli.create_date" +
            "     ,oli.exception_dm" +
            "     ,oli.ip" +
            " FROM " +
            "     operation_log_info oli" +
            " WHERE " +
            "     oli.log_msg IS NULL" +
            " AND position('执行记录' in oli.module) <= 0" +
            " AND create_date > '" + startTime + "'" +
            " AND create_date <= '" + endTime + "'";
  startTime = endTime;
  return dataBase.executeQuery(conn,sql);
}

function insert (log) {
  var header = {};
  var parameters = {
    type: log.module,
    source: config.instance().other.auditLog.source,
    operand: config.instance().other.auditLog.source + "-" + log.module,
    result: log.result,
    description: log.module,
    operator: log.login_name,
    createById: log.login_name,
    createTime: new Date(log.create_date).format('yyyy-MM-dd HH:mm:ss'),
    loginIp:log.ip,
    level: "1"
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
//格式化时间
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
    //启动定时任务
    var scheduleService = new dc_schedule.ScheduleService(config.instance().schedule.threadCount);
    var parametersSchedule = {};
    scheduleService.schedule(excute,parametersSchedule, config.instance().schedule.cron);
  }
}