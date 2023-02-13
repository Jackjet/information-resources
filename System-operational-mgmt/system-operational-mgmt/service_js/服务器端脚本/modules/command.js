var dc_dcrun = require("dc_dcrun");
var dc_shell = require("dc_shell");
var dc_http = require("dc_http");

var config = require("../config");

var dcrun = new dc_dcrun.DCRunService()
function excute (data){
  var shell = new dc_shell.ShellService()
  dcrun.log('recieve=' + data);
  shell.run(data.command,function (result) {
    dcrun.log('result=' + result);
    var http = new dc_http.HttpService();
    var header = {};
    var parameters = {
      id:data.id,
      result:result
    };
    var contentType = 'application/json';
    http.post(config.instance().interface.command.updateLog,header,parameters,contentType);
  });
  return {
    code: '200'
  };
}

module.exports = {
  instance: function () {
    dcrun.registerWebApi('GET',excute,'/commandExcute');
  }
}
