/** 
  前置交换子系统
  支持国内外主流数据库,如达梦数据库.
*/
var dbUrl = "jdbc:dm://localhost:5236";
var dbUser = "SYSDBA";
var dbPwd = "123456789";

var Service = require("dc_dameng");
var damengService = new Service.DMServerService();

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

function main(){
    try {
        // 1.获得达梦数据库连接
        var connect = damengService.getConnection(dbUrl, dbUser, dbPwd);
        dcPrint("获得达梦数据库连接========================================","main");
        dcPrint("获得达梦数据库连接=" + connect,"main");
        dcPrint("获得达梦数据库连接========================================","main");
      
        // 2.获得查询结果列表
        dcPrint("获得查询结果列表===============================================",main);
        var findSql = "select * from PERSON.PERSON";
        var resultList = damengService.executeQuery(connect, findSql);
        dcPrint("获得查询结果列表=" + resultList,"main");
        dcPrint("获得查询列表===============================================","main");
 
        // 3.新增操作
        dcPrint("新增操作===============================================","main");
        var insertSql = "insert into PERSON.PERSON (EMAIL,SEX,NAME,PHONE)values('test@test.com','F','测试','12456247856') ";
        result = damengService.execute(connect, insertSql);
        dcPrint("新增操作结果=" + result,"main");
        dcPrint("新增操作===============================================","main");
      
        // 4.更新操作
        dcPrint("更新操作===============================================","main");
        var updateSql = "update PERSON.PERSON set EMAIL = 'new@new.com' where NAME = '测试'  ";
        result = damengService.execute(connect, updateSql);
        dcPrint("更新操作结果=" + result,"main");
        dcPrint("更新操作===============================================","main");
      
        // 5.删除操作
        dcPrint("删除操作===============================================","main");
        var deleteSql = "delete from  PERSON.PERSON where NAME = '测试'";
        result = damengService.execute(connect, deleteSql);
        dcPrint("删除操作=" + result,"main");
        damengService.close(connect);
        dcPrint("删除操作===============================================","main");
  
    } catch (e) {
        dcError("js 达梦数据库操作异常 error:" + e,"main");
    }
    
}

function dcPrint(data,actionName){
  logs["data"] = data;
  logs["filePath"] = "damengDemo.js/"+actionName;
  dcrun.log(JSON.stringify(logs));
}

function dcError(data,actionName){
   logs["data"] = data+"";
   logs["filePath"] = "errorLocation.js/"+actionName;
   dcrun.error(JSON.stringify(logs));
}

