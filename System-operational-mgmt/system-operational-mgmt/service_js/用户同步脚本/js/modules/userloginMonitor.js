var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var dc_schedule = require("dc_schedule");
var dc_http = require("dc_http");

var dcrun = new dc_dcrun.DCRunService();
var http = new dc_http.HttpService();

var config = require("../config");
var startTime = config.instance().schedule.startTime;

function excute () {
  var results = queryResultList();
  dcrun.log(results);
  for (i = 0; i < results.length;i ++) {
    insert(results[i]);
  }
}

/**
获取用户数据信息
*/
function queryResultList(){
  var endTime = new Date().format('yyyy-MM-dd HH:mm:ss');
  var sql = " SELECT " +
            "     DISTINCT" +
            "     ue.ID" +
            "     ,ue.FIRST_NAME" +
            "     ,ee.EVENT_TIME" +
            "     ,ci.`NAME`" +
            "     ,ee.IP_ADDRESS" +
            "     ,(" +
            "       SELECT " +
            "           MIN(FROM_UNIXTIME(ees.EVENT_TIME/1000,'%Y-%m-%d %H:%i:%S')) AS EVENT_TIME" +
            "       FROM" +
            "           EVENT_ENTITY ees" +
            "       WHERE" +
            "           ees.REALM_ID = 'tianjin'" +
            "           AND ees.TYPE = 'LOGIN'" +
            "           AND ees.USER_ID = ue.ID" +
            "           AND ees.CLIENT_ID = ci.CLIENT_ID" +
            "       GROUP BY ees.CLIENT_ID, ees.USER_ID" +
            "      ) AS FIRST_LOGIN_TIME" +
            " FROM " +
            "     USER_ENTITY ue" +
            "     ,(SELECT" +
            "           CLIENT_ID" +
            "           ,IP_ADDRESS" +
            "           ,FROM_UNIXTIME(EVENT_TIME/1000,'%Y-%m-%d %H:%i:%S') AS EVENT_TIME" +
            "           ,USER_ID" +
            "       FROM" +
            "           EVENT_ENTITY" +
            "       WHERE" +
            "           REALM_ID = 'tianjin'" +
            "       AND FROM_UNIXTIME(EVENT_TIME/1000,'%Y-%m-%d %H:%i:%s') > '" + startTime + "'" +
            "       AND FROM_UNIXTIME(EVENT_TIME/1000,'%Y-%m-%d %H:%i:%s') <= '" + endTime + "'" +
            "       AND TYPE = 'LOGIN') ee" +
            "      ,CLIENT ci" +
            " WHERE " +
            "     ue.ENABLED = 1" +
            " AND ue.FIRST_NAME IS NOT NULL" +
            " AND ue.ID = ee.USER_ID" +
            " AND ci.CLIENT_ID = ee.CLIENT_ID";
  startTime = endTime;
  //获取数据库连接
  var dataBase = new dc_mySql.MySqlService();
  var connect = dataBase.getConnection(config.instance().database.dbUrl,config.instance().database.dbUser,config.instance().database.dbPwd);
  //执行查询操作
  var queryResult = dataBase.executeQuery(connect, sql);
  //返回查询结果
  return queryResult;
}

/**
存储到指定的信息库或其他数据库中
*/
function insert (result){
  var header = {};
  var parameters = {
    loginUserId: result.ID,
    name: result.FIRST_NAME,
    loginTime: result.EVENT_TIME,
    type: result.NAME,
    firstLoginTime: result.FIRST_LOGIN_TIME,
    ip: result.IP_ADDRESS
  };
  
  var contentType = 'application/json';
  var resultHttp = http.post(config.instance().interface.userInfo.insertUserInfo,header,parameters,contentType);
  dcrun.log(resultHttp);
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