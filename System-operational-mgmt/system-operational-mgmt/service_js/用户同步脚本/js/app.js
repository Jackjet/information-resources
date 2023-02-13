var dc_dcrun = require("dc_dcrun");
//读取配置
var config = require("./config");
var userloginMonitor = require("./modules/userloginMonitor");

//全局变量===================================================
var dcrun = new dc_dcrun.DCRunService();
//===================================================
//用户监控
userloginMonitor.instance();