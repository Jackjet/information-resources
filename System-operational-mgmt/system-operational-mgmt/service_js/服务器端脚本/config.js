var root = 'http://139.9.228.68/tangshan'
var config = {
  database: {
    dbUrl: 'jdbc:mysql://139.9.228.68:3306/test_service',
    dbUser: 'root',
    dbPwd: 'Qwer@1234'
  },
  schedule: {
    cron: '0 * * * * ?',
    threadCount: 1,
    startTime: '2020-07-06 09:40:00'
  },
  interface: {
    operationLog: {
    	insertLog: root + '/webadmin/log/operationLog/insertLog'
    },
    systemLog: {
      insertLog: root + '/webadmin/log/systemLog/insertLog'
    },
    auditLog: {
      insertLog: root + '/webadmin/log/auditLog/insertLog'
    },
    baseEnvironmentMonitor: {
      insert: root + '/webadmin/environment/insert'
    },
    systemMonitor: {
      insert: root + '/webadmin/systemMonitor/insert'
    },
    resourceMonitor: {
      insert: root + '/webadmin/resourcesMonitor/insert'
    },
    command: {
      updateLog: root + '/webadmin/log/operationLog/updateLog'
    }
  },
  tables: {
    operationLog: 'logs',
    systemLog: 'service_logs',
    auditLog: 'audit_logs'
  },
  other: {
    baseEnvironmentMonitor : {
      nodeId: '4bfc29955c1a40019ef8978c140c0e71',
      nodeName: '节点1'
    },
    systemMonitor: {
      ip: '139.9.228.68',
      nodeName: '节点1',
      nodeId: '4bfc29955c1a40019ef8978c140c0e71',
      system: '的',
      systemId: '0cbf50af42c64cdf86618bc052c3e838',
      directory: '/root/websites/tangshan',
      processName: 'nginx'
    },
    resourceMonitor: {
      nodeName: '节点1',
      nodeId: '4bfc29955c1a40019ef8978c140c0e71'
    }
  }
}

module.exports = {
  instance: function  () {
    return config;
  }
}