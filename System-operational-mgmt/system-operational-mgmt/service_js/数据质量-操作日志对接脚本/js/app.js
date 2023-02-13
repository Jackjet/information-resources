var dc_dcrun = require("dc_dcrun");
//读取配置
var config = require("./config");
var operatelogs = require("./modules/operatelogs");
var auditlogs = require("./modules/auditlogs");
var systemMonitor = require("./modules/systemMonitor");
//全局变量===================================================
var dcrun = new dc_dcrun.DCRunService();
//===================================================
//操作日志
operatelogs.instance();
//审计日志
auditlogs.instance();
//系统监控
systemMonitor.instance();
