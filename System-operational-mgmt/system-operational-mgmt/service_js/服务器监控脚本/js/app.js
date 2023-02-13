var dc_dcrun = require("dc_dcrun");
//读取配置
var config = require("./config");
var baseEnvironmentMonitor = require("./modules/baseEnvironmentMonitor");
var resourceMonitor = require("./modules/resourceMonitor");
var command = require("./modules/command");
//全局变量===================================================
var dcrun = new dc_dcrun.DCRunService();
//===================================================
//远程控制台
command.instance();
//基础环境监控
baseEnvironmentMonitor.instance();
//资源监控
resourceMonitor.instance();