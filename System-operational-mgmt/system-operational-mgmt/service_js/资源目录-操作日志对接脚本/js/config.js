var root = 'http://172.16.0.16:8071'
var config = {
  database: {
    dbUrl: 'jdbc:postgresql://8.140.114.226:5432/tianjin_resource_catalog',
    dbUser: 'postgres',
    dbPwd: 'Qwer@1234'
  },
  schedule: {
    cron: '0 * * * * ?',
    threadCount: 1,
    startTime: '2021-12-21 09:40:00'
  },
  interface: {
    operationLog: {
        insertLog: root + '/webadmin/log/operationLog/insertLog'
    },
    auditLog: {
        insertLog: root + '/webadmin/log/auditLog/insertLog'
    },
    systemMonitor: {
      insert: root + '/webadmin/systemMonitor/insert'
    }
  },
  tables: {
    operationLog: 'sys_log',
    auditLog: 'sys_log'
  },
  other: {
    operationLog : {
      source: '资源目录管理系统'
    },
    auditLog : {
      source: '资源目录管理系统'
    },
    systemMonitor: {
      ip: '172.16.0.15',
      nodeName: '172.16.0.15',
      nodeId: '7ca52647bf2c4b79b85ab21af40e1270',
      system: '资源目录管理系统',
      systemId: '19e6c39ef9a44b43a81cee3f0613cb3c',
      directory: '/data/websites/resource_catalog',
      processName: 'resourcecatalog-admin-api'
    }
  }
}

module.exports = {
  instance: function  () {
    return config;
  }
}
