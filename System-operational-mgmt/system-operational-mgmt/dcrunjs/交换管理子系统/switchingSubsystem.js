/**
 交换管理子系统
 该示例主要功能
 实现点对点、点对多点、多点对点、多点对多点的业务协同的传输模式
 */
var dc_dcrun = require("dc_dcrun");
var dc_mySql = require("dc_mysql");
var mysql = new dc_mySql.MySqlService();
var dcrun = new dc_dcrun.DCRunService();

var logs = {};
logs["taskName"] = "dataBase";//任务名称
logs["taskType"] = "management-tasks";//任务类型
logs["nodeName"] = "86服务器";//节点名称
logs["serviceName"] = "张喆的脚本Demo";//系统服务名称
logs["filePath"] = "switchingSubsystem.js";//文件路径 有目录把目录加上
logs["startTime"] = new Date().getTime();//调用时间
logs["userName"] = "d1Test1";//使用者

/**
 点对点:从一个数据库取出数据写入到另一个数据库中
 */
function oneToOne() {
    //从数据库中取出数据

    var dbUrlOut = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemOne?characterEncoding=utf8&useSSL=false";
    var dbUserOut = "root";
    var dbPwdOut = "Qwer@1234";
    var connectOut = mysql.getConnection(dbUrlOut, dbUserOut, dbPwdOut);
    logs["data"] = "执行oneToOne方法,连接mysql数据库";
    dcPrint(JSON.stringify(logs),"oneToOne");
    dcPrint("读取数据库switchingSubsystemOne","oneToOne");
    var queryStr = "select * from one_to_one";
    var queryResult = mysql.executeQuery(connectOut, queryStr);
    dcPrint(queryResult,"oneToOne");
    logs["data"] = "执行oneToOne方法,获取到one_to_one表中数据";
    dcPrint(JSON.stringify(logs),"oneToOne");
    mysql.close(connectOut);


    var dbUrlIn = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemTwo?characterEncoding=utf8&useSSL=false";
    var dbUserIn = "root";
    var dbPwdIn = "Qwer@1234";
    var connectIn = mysql.getConnection(dbUrlIn, dbUserIn, dbPwdIn);
    dcPrint("写入数据库switchingSubsystemOne","oneToOne");
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr = "replace into one_to_one (name, id,sex) values (?,?,?);";
        var item = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag = mysql.executeParam(connectIn, insertStr, item);
        dcPrint("同步结果 " + item,"oneToOne");
    }
    mysql.close(connectIn);

}

/**
 点对多:从一个数据库取出数据写入到多个数据库中
 */
function oneToMany() {
    //从数据库中取出数据
    var dbUrlOut = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemOne?characterEncoding=utf8&useSSL=false";
    var dbUserOut = "root";
    var dbPwdOut = "Qwer@1234";
    var connectOut = mysql.getConnection(dbUrlOut, dbUserOut, dbPwdOut);
    logs["data"] = "执行oneToMany方法,连接mysql数据库";
    dcPrint(JSON.stringify(logs),"oneToMany");
    dcPrint("读取数据库switchingSubsystemOne","oneToMany");
    var queryStr = "select * from one_to_many";
    var queryResult = mysql.executeQuery(connectOut, queryStr);
    dcPrint(queryResult);
    logs["data"] = "执行oneToMany方法,获取到one_to_many表中数据";
    dcPrint(JSON.stringify(logs),"oneToMany");
    mysql.close(connectOut);


    var dbUrlIn = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemTwo?characterEncoding=utf8&useSSL=false";
    var dbUserIn = "root";
    var dbPwdIn = "Qwer@1234";
    var connectIn = mysql.getConnection(dbUrlIn, dbUserIn, dbPwdIn);
    dcPrint("写入数据库switchingSubsystemTwo","oneToMany");
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr = "replace into one_to_many (name, id,sex) values (?,?,?);";
        var item = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag = mysql.executeParam(connectIn, insertStr, item);
        dcPrint("同步结果 " + item,"oneToMany");
    }
    mysql.close(connectIn);

    dcPrint("写入数据库switchingSubsystemThree","oneToMany");
    var dbUrlIn1 = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemThree?characterEncoding=utf8&useSSL=false";
    var dbUserIn1 = "root";
    var dbPwdIn1 = "Qwer@1234";
    var connectIn1 = mysql.getConnection(dbUrlIn1, dbUserIn1, dbPwdIn1);
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr1 = "replace into one_to_many (name, id,sex) values (?,?,?);";
        var item1 = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag1 = mysql.executeParam(connectIn1, insertStr1, item1);
        dcPrint("同步结果 " + item1,"oneToMany");
    }
    mysql.close(connectIn1);
}

/**
 多对点:从多个数据库取出数据写入到一个数据库中
 */
function manyToOne() {
    //从数据库中取出数据
    var dbUrlOut = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemOne?characterEncoding=utf8&useSSL=false";
    var dbUserOut = "root";
    var dbPwdOut = "Qwer@1234";
    var connectOut = mysql.getConnection(dbUrlOut, dbUserOut, dbPwdOut);
    logs["data"] = "执行manyToOne方法,连接mysql数据库";
    dcPrint(JSON.stringify(logs),"manyToOne");
    dcPrint("读取数据库switchingSubsystemOne","manyToOne");
    var queryStr = "select * from many_to_one";
    var queryResult = mysql.executeQuery(connectOut, queryStr);
    dcPrint(queryResult,"manyToOne");
    logs["data"] = "执行manyToOne方法,获取到many_to_one表中数据";
    dcPrint(JSON.stringify(logs),"manyToOne");
    mysql.close(connectOut);

    var dbUrlOut1 = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemTwo?characterEncoding=utf8&useSSL=false";
    var dbUserOut1 = "root";
    var dbPwdOut1 = "Qwer@1234";
    var connectOut1 = mysql.getConnection(dbUrlOut1, dbUserOut1, dbPwdOut1);
    logs["data"] = "执行manyToOne方法,连接mysql数据库";
    dcPrint(JSON.stringify(logs),"manyToOne");
    dcPrint("读取数据库switchingSubsystemTwo","manyToOne");
    var queryStr1 = "select * from many_to_one";
    var queryResult1 = mysql.executeQuery(connectOut1, queryStr1);
    dcPrint(queryResult1,"manyToOne");
    logs["data"] = "执行manyToOne方法,获取到many_to_one表中数据";
    dcPrint(JSON.stringify(logs),"manyToOne");
    mysql.close(connectOut1);

    dcPrint("写入数据库1","manyToOne");
    var dbUrlIn = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemThree?characterEncoding=utf8&useSSL=false";
    var dbUserIn = "root";
    var dbPwdIn = "Qwer@1234";

    var connectIn = mysql.getConnection(dbUrlIn, dbUserIn, dbPwdIn);
    dcPrint("写入数据库switchingSubsystemTwo.many_to_one","manyToOne");
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr = "replace into many_to_one (name, id,sex) values (?,?,?);";
        var item = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag = mysql.executeParam(connectIn, insertStr, item);
        dcPrint("同步结果 " + item,"manyToOne");
    }
    dcPrint("写入数据库switchingSubsystemTwo.many_to_one_1","manyToOne");
    for (var i = 0; i < queryResult1.length; i++) {
        var insertStr = "replace into many_to_one_1 (name, id,sex) values (?,?,?);";
        var item = [queryResult1[i]["name"], queryResult1[i]["id"], queryResult1[i]["sex"]];
        var flag = mysql.executeParam(connectIn, insertStr, item);
        dcPrint("同步结果 " + item,"manyToOne");
    }

    mysql.close(connectIn);

}

/**
 多对多:从多个数据库取出数据写入到多个数据库中
 */
function manyToMany() {
    //从数据库中取出数据
    //读取switchingSubsystemOne
    dcPrint("读取数据库switchingSubsystemOne","manyToMany");
    var dbUrlOut = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemOne?characterEncoding=utf8&useSSL=false";
    var dbUserOut = "root";
    var dbPwdOut = "Qwer@1234";
    var connectOut = mysql.getConnection(dbUrlOut, dbUserOut, dbPwdOut);
    logs["data"] = "执行oneToOneDemo方法,连接mysql数据库";
    dcPrint(JSON.stringify(logs),"manyToMany");
    var queryStr = "select * from many_to_many_one";
    var queryResult = mysql.executeQuery(connectOut, queryStr);
    dcPrint(queryResult,"manyToMany");
    logs["data"] = "执行manyToMany方法,获取到many_to_many表中数据";
    dcPrint(JSON.stringify(logs),"manyToMany");
    mysql.close(connectOut);
    //写入switchingSubsystemTwo
    dcPrint("写入数据库switchingSubsystemTwo","manyToMany");
    var dbUrlIn = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemTwo?characterEncoding=utf8&useSSL=false";
    var dbUserIn = "root";
    var dbPwdIn = "Qwer@1234";
    var connectIn = mysql.getConnection(dbUrlIn, dbUserIn, dbPwdIn);
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr = "replace into many_to_many_one (name, id,sex) values (?,?,?);";
        var item = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag = mysql.executeParam(connectIn, insertStr, item);
        dcPrint("同步结果 " + item,"manyToMany");
    }
    mysql.close(connectIn);

    dcPrint("写入数据库switchingSubsystemThree","manyToMany");
    var dbUrlIn1 = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemThree?characterEncoding=utf8&useSSL=false";
    var dbUserIn1 = "root";
    var dbPwdIn1 = "Qwer@1234";
    var connectIn1 = mysql.getConnection(dbUrlIn1, dbUserIn1, dbPwdIn1);
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr1 = "replace into many_to_many_one (name, id,sex) values (?,?,?);";
        var item1 = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag1 = mysql.executeParam(connectIn1, insertStr1, item1);
        dcPrint("同步结果 " + item1,"manyToMany");
    }
    mysql.close(connectIn1);

    dcPrint("读取数据库switchingSubsystemTwo","manyToMany");
    var dbUrlOut = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemTwo?characterEncoding=utf8&useSSL=false";
    var dbUserOut = "root";
    var dbPwdOut = "Qwer@1234";
    var connectOut = mysql.getConnection(dbUrlOut, dbUserOut, dbPwdOut);
    logs["data"] = "执行oneToOneDemo方法,连接mysql数据库";
    dcPrint(JSON.stringify(logs),"manyToMany");
    var queryStr = "select * from many_to_many_two";
    var queryResult = mysql.executeQuery(connectOut, queryStr);
    dcPrint(queryResult,"manyToMany");
    logs["data"] = "执行manyToMany方法,获取到many_to_many表中数据";
    dcPrint(JSON.stringify(logs),"manyToMany");
    mysql.close(connectOut);
    //写入switchingSubsystemTwo
    dcPrint("写入数据库switchingSubsystemOne","manyToMany");
    var dbUrlIn = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemOne?characterEncoding=utf8&useSSL=false";
    var dbUserIn = "root";
    var dbPwdIn = "Qwer@1234";
    var connectIn = mysql.getConnection(dbUrlIn, dbUserIn, dbPwdIn);
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr = "replace into many_to_many_two (name, id,sex) values (?,?,?);";
        var item = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag = mysql.executeParam(connectIn, insertStr, item);
        dcPrint("同步结果 " + item,"manyToMany");
    }
    mysql.close(connectIn);

    dcPrint("写入数据库switchingSubsystemThree","manyToMany");
    var dbUrlIn1 = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemThree?characterEncoding=utf8&useSSL=false";
    var dbUserIn1 = "root";
    var dbPwdIn1 = "Qwer@1234";
    var connectIn1 = mysql.getConnection(dbUrlIn1, dbUserIn1, dbPwdIn1);
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr1 = "replace into many_to_many_two (name, id,sex) values (?,?,?);";
        var item1 = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag1 = mysql.executeParam(connectIn1, insertStr1, item1);
        dcPrint("同步结果 " + item1,"manyToMany");
    }
    mysql.close(connectIn1);

    dcPrint("读取数据库switchingSubsystemThree","manyToMany");
    var dbUrlOut = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemThree?characterEncoding=utf8&useSSL=false";
    var dbUserOut = "root";
    var dbPwdOut = "Qwer@1234";
    var connectOut = mysql.getConnection(dbUrlOut, dbUserOut, dbPwdOut);
    logs["data"] = "执行oneToOneDemo方法,连接mysql数据库";
    dcPrint(JSON.stringify(logs),"manyToMany");
    var queryStr = "select * from many_to_many_three";
    var queryResult = mysql.executeQuery(connectOut, queryStr);
    dcPrint(queryResult,"manyToMany");
    logs["data"] = "执行manyToMany方法,获取到many_to_many_three表中数据";
    dcPrint(JSON.stringify(logs),"manyToMany");
    mysql.close(connectOut);
    //写入switchingSubsystemTwo
    dcPrint("写入数据库switchingSubsystemOne","manyToMany");
    var dbUrlIn = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemOne?characterEncoding=utf8&useSSL=false";
    var dbUserIn = "root";
    var dbPwdIn = "Qwer@1234";
    var connectIn = mysql.getConnection(dbUrlIn, dbUserIn, dbPwdIn);
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr = "replace into many_to_many_three (name, id,sex) values (?,?,?);";
        var item = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag = mysql.executeParam(connectIn, insertStr, item);
        dcPrint("同步结果 " + item,"manyToMany");
    }
    mysql.close(connectIn);

    dcPrint("写入数据库switchingSubsystemTwo","manyToMany");
    var dbUrlIn1 = "jdbc:mysql://47.105.140.86:3306/switchingSubsystemTwo?characterEncoding=utf8&useSSL=false";
    var dbUserIn1 = "root";
    var dbPwdIn1 = "Qwer@1234";
    var connectIn1 = mysql.getConnection(dbUrlIn1, dbUserIn1, dbPwdIn1);
    for (var i = 0; i < queryResult.length; i++) {
        var insertStr1 = "replace into many_to_many_three (name, id,sex) values (?,?,?);";
        var item1 = [queryResult[i]["name"], queryResult[i]["id"], queryResult[i]["sex"]];
        var flag1 = mysql.executeParam(connectIn1, insertStr1, item1);
        dcPrint("同步结果 " + item1,"manyToMany");
    }
    mysql.close(connectIn1);
}

function dcPrint(data,actionName){
  logs["data"] = data;
  var tempLogs= JSON.parse(JSON.stringify(logs));
  tempLogs["filePath"] += ("/"+actionName);
  dcrun.log(JSON.stringify(tempLogs));
}

//module.exports.oneToOne = oneToOne;
//module.exports.oneToMany = oneToMany;
//module.exports.manyToOne = manyToOne;
//module.exports.manyToMany = manyToMany;



