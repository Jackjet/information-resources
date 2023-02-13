var root = 'http://172.16.0.16:8071'
var config = {
  database: {
    dbUrl: 'jdbc:postgresql://8.140.114.226:5432/resource_sharing_mgmt',
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
    operationLog: 'd1_operation_log',
    auditLog: 'd1_user_log'
  },
  other: {
    operationLog : {
      source: '资源共享管理系统'
    },
    auditLog : {
      source: '资源共享管理系统'
    },
    systemMonitor: {
      ip: '172.16.0.15',
      nodeName: '172.16.0.15',
      nodeId: '7ca52647bf2c4b79b85ab21af40e1270',
      system: '资源共享管理系统',
      systemId: 'e76086083fd14f63ae374fdf0211d2d9',
      directory: '/data/websites/resourceSharingMgmt',
      processName: 'resourceSharingMgmt'
    }
  }
}

module.exports = {
  instance: function  () {
    return config;
  }
}
