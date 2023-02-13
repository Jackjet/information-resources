var root = 'http://172.16.0.16:8071'
var config = {
  database: {
    dbUrl: 'jdbc:postgresql://8.140.114.226:5432/CP_EXCHANGE',
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
    }
  },
  tables: {
    operationLog: 'operation_log_info',
    auditLog: 'operation_log_info'
  },
  other: {
    operationLog : {
      source: '资源交换管理系统'
    },
    auditLog : {
      source: '资源交换管理系统'
    }
  }
}

module.exports = {
  instance: function  () {
    return config;
  }
}