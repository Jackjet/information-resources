export default {
  session: (key, value) => {
    if (value === void (0)) {
      let lsVal = localStorage.getItem(key)
      if (lsVal && lsVal.indexOf('autostringify-') === 0) {
        return JSON.parse(lsVal.split('autostringify-')[1])
      } else {
        return lsVal
      }
    } else {
      if (typeof (value) === 'object' || Array.isArray(value)) {
        value = 'autostringify-' + JSON.stringify(value)
      }
    }
    return localStorage.setItem(key, value)
  },
  isInteger: (rule, value, callback) => {
    const reg = /^[0-9]+$/
    if (!value) {
      callback(new Error(rule.msg))
    } if (!reg.test(value)) {
      callback(new Error('请输入正整数'))
    } else {
      callback()
    }
  },
  isUrl: (rule, value, callback) => {
    let str = value.substring(0, 1)
    const reg0 = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    const reg1 = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\*\+,;=.]+$/
    if (!value) {
      callback(new Error(rule.msg))
    } if (str !== 'h' && !reg0.test(value)) {
      callback(new Error('IP地址有误'))
    } else if (str === 'h' && !reg1.test(value)) {
      callback(new Error('URL格式有误'))
    } else {
      callback()
    }
  }
}
