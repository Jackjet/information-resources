var root = 'http://8.140.127.114:8071'
var config = {
  database: {
    dbUrl: 'jdbc:postgresql://8.140.127.114:3306/operational_mgmt',
    dbUser: 'postgres',
    dbPwd: 'Qwer@1234'
  },
  schedule: {
    cron: '0 0/5 * * * ?',
    threadCount: 1,
    startTime: '2021-12-14 09:40:00'
  },
  interface: {
    baseEnvironmentMonitor: {
      insert: root + '/webadmin/environment/insert'
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
    auditLog: 'd1_audit_logs'
  },
  other: {
    baseEnvironmentMonitor : {
      intranetIp: '172.16.0.16'
    },
    resourceMonitor: {
      intranetIp: '172.16.0.16'
    }
  }
}

module.exports = {
  instance: function  () {
    return config;
  }
}