var dc_dcrun = require("dc_dcrun");
//读取配置
var config = require("./config");
var operatelogs = require("./modules/operatelogs");
var systemlogs = require("./modules/systemlogs");
var auditlogs = require("./modules/auditlogs");
var baseEnvironmentMonitor = require("./modules/baseEnvironmentMonitor");
var systemMonitor = require("./modules/systemMonitor");
var resourceMonitor = require("./modules/resourceMonitor");
var command = require("./modules/command");
//全局变量===================================================
var dcrun = new dc_dcrun.DCRunService();
//===================================================
//远程控制台
command.instance();
//操作日志
operatelogs.instance();
//系统日志
systemlogs.instance();
//审计日志
auditlogs.instance();
//基础环境监控
baseEnvironmentMonitor.instance();
//系统监控
systemMonitor.instance();
//资源监控
resourceMonitor.instance();