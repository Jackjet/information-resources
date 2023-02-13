export default {
  isInteger: (rule, value, callback) => {
    const reg = /^\+?[1-9]\d*$/
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
    const reg0 = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/
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
  },
  english: (rule, value, callback) => {
    let str = value.substring(0, 1)
    const reg0 = /^[a-zA_Z]+$/
    if (!value) {
      callback(new Error(rule.msg))
    } if (str !== 'h' && !reg0.test(value)) {
      callback(new Error('只允许输入英文'))
    } else {
      callback()
    }
  },
  isVersion: (rule, value, callback) => {
    const reg = /[^\d^\.]+/g
    if (!value) {
      callback(new Error(rule.msg))
    // } if (reg.test(value)) {
    //   callback(new Error('只允许输入小数及小数点'))
    } else {
      callback()
    }
  }
}
