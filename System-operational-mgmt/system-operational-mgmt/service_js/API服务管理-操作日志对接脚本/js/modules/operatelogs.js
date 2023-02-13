var dc_postgre = require("dc_postgre");
var dc_schedule = require("dc_schedule");
var dc_http = require("dc_http");

var config = require("../config");
var http = new dc_http.HttpService();

var startTime = config.instance().schedule.startTime;
function excute () {
  dcrun.log('查询操作日志');
  var dataBase = new dc_postgre.PostgreService();
  var conn = dataBase.getConnection(config.instance().database.dbUrl,config.instance().database.dbUser,config.instance().database.dbPwd);
  var sql = 'select * from '+ config.instance().tables.operationLog + ' where ';
  var where = '';
  if(startTime !== '') {
    where = where + "create_time > '" + startTime + "' and ";
  }
  var endTime = new Date().format('yyyy-MM-dd HH:mm:ss');
  where = where + "create_time <= '" + endTime + "';";
  sql = sql + where;  
  startTime = endTime;
  var logs = dataBase.executeQuery(conn,sql);
  dcrun.log('写入操作日志');
  for (i = 0; i < logs.length;i ++){
    insertlogs(logs[i])
  }
}

function insertlogs (log) {
  var header = {};
  var parameters = {
    type: log.module + "-" + log.api,
    source: config.instance().other.operationLog.source,
    operand: config.instance().other.operationLog.source + "-" + log.module,
    result: log.result,
    description: log.content_msg,
    operator: log.create_by_id,
    operatorId: log.create_by_id,
    createTime: new Date(log.create_time).format('yyyy-MM-dd HH:mm:ss')
  };
  
  var contentType = 'application/json';
  var result = http.post(config.instance().interface.operationLog.insertLog,header,parameters,contentType);
  dcrun.log(result);
}
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