var root = 'http://172.16.0.16:8071'
var config = {
  database: {
    dbUrl: 'jdbc:mysql://8.140.114.226:3306/authentication',
    dbUser: 'root',
    dbPwd: 'Qwer@1234'
  },
  schedule: {
    cron: '0 */1 * * * ?',
    threadCount: 1,
    startTime: '2021-12-23 09:40:00'
  },
  interface: {
    userInfo: {
    	insertUserInfo: root + '/webadmin/loginInfo/insert'
    }
  }
}

module.exports = {
  instance: function  () {
    return config;
  }
}