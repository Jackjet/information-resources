/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}
/**
 * @param {string} str
 * @returns {Boolean}
 */

export function validMobile(mobile) {
  const mobileReg = /^1[0-9]{10}$/
  return mobileReg.test(mobile)
}
/**
 * 校验座机
 * @param {fixMobile} str
 * @returns {Boolean}
 */
export function validFixMobile(fixMobile) {
  const mobileReg = /^0\d{2,3}-?\d{7,8}$/
  return mobileReg.test(fixMobile)
}
/**
 * @param {string} str
 * @returns {Boolean}
 */
const idCardNoUtil = {
  provinceAndCitys: {
    11: '北京',
    12: '天津',
    13: '河北',
    14: '山西',
    15: '内蒙古',
    21: '辽宁',
    22: '吉林',
    23: '黑龙江',
    31: '上海',
    32: '江苏',
    33: '浙江',
    34: '安徽',
    35: '福建',
    36: '江西',
    37: '山东',
    41: '河南',
    42: '湖北',
    43: '湖南',
    44: '广东',
    45: '广西',
    46: '海南',
    50: '重庆',
    51: '四川',
    52: '贵州',
    53: '云南',
    54: '西藏',
    61: '陕西',
    62: '甘肃',
    63: '青海',
    64: '宁夏',
    65: '新疆',
    71: '台湾',
    81: '香港',
    82: '澳门',
    91: '国外'
  },

  powers: ['7', '9', '10', '5', '8', '4', '2', '1', '6', '3', '7', '9', '10', '5', '8', '4', '2'],

  parityBit: ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'],

  genders: {
    male: '男',
    female: '女'
  },

  checkAddressCode: function(addressCode) {
    var check = /^[1-9]\d{5}$/.test(addressCode)
    if (!check) return false
    if (idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0, 2))]) {
      return true
    } else {
      return false
    }
  },

  checkBirthDayCode: function(birDayCode) {
    var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode)
    if (!check) return false
    var yyyy = parseInt(birDayCode.substring(0, 4), 10)
    var mm = parseInt(birDayCode.substring(4, 6), 10)
    var dd = parseInt(birDayCode.substring(6), 10)
    var xdata = new Date(yyyy, mm - 1, dd)
    if (xdata > new Date()) {
      return false // 生日不能大于当前日期
    } else if ((xdata.getFullYear() == yyyy) && (xdata.getMonth() == mm - 1) && (xdata.getDate() == dd)) {
      return true
    } else {
      return false
    }
  },

  getParityBit: function(idCardNo) {
    var id17 = idCardNo.substring(0, 17)
    var power = 0
    for (var i = 0; i < 17; i++) {
      power += parseInt(id17.charAt(i), 10) * parseInt(idCardNoUtil.powers[i])
    }
    var mod = power % 11
    return idCardNoUtil.parityBit[mod]
  },

  checkParityBit: function(idCardNo) {
    var parityBit = idCardNo.charAt(17).toUpperCase()
    if (idCardNoUtil.getParityBit(idCardNo) == parityBit) {
      return true
    } else {
      return false
    }
  },

  checkIdCardNo: function(idCardNo) {
    // 15位和18位身份证号码的基本校验
    var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo)
    if (!check) return false
    // 判断长度为15位或18位
    if (idCardNo.length == 15) {
      return idCardNoUtil.check15IdCardNo(idCardNo)
    } else if (idCardNo.length == 18) {
      return idCardNoUtil.check18IdCardNo(idCardNo)
    } else {
      return false
    }
  },
  // 校验15位的身份证号码
  check15IdCardNo: function(idCardNo) {
    // 15位身份证号码的基本校验
    var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo)
    if (!check) return false
    // 校验地址码
    var addressCode = idCardNo.substring(0, 6)
    check = idCardNoUtil.checkAddressCode(addressCode)
    if (!check) return false
    var birDayCode = '19' + idCardNo.substring(6, 12)
    // 校验日期码
    return idCardNoUtil.checkBirthDayCode(birDayCode)
  },
  // 校验18位的身份证号码
  check18IdCardNo: function(idCardNo) {
    // 18位身份证号码的基本格式校验
    var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo)
    if (!check) return false
    // 校验地址码
    var addressCode = idCardNo.substring(0, 6)
    check = idCardNoUtil.checkAddressCode(addressCode)
    if (!check) return false
    // 校验日期码
    var birDayCode = idCardNo.substring(6, 14)
    check = idCardNoUtil.checkBirthDayCode(birDayCode)
    if (!check) return false
    // 验证校检码
    return idCardNoUtil.checkParityBit(idCardNo)
  },
  formateDateCN: function(day) {
    var yyyy = day.substring(0, 4)
    var mm = day.substring(4, 6)
    var dd = day.substring(6)
    return yyyy + '-' + mm + '-' + dd
  },
  // 获取信息
  getIdCardInfo: function(idCardNo) {
    var idCardInfo = {
      gender: '', // 性别
      birthday: '' // 出生日期(yyyy-mm-dd)
    }
    if (idCardNo.length === 15) {
      var aday = '19' + idCardNo.substring(6, 12)
      idCardInfo.birthday = idCardNoUtil.formateDateCN(aday)
      if (parseInt(idCardNo.charAt(14)) % 2 === 0) {
        idCardInfo.gender = idCardNoUtil.genders.female
      } else {
        idCardInfo.gender = idCardNoUtil.genders.male
      }
    } else if (idCardNo.length === 18) {
      var aday = idCardNo.substring(6, 14)
      idCardInfo.birthday = idCardNoUtil.formateDateCN(aday)
      if (parseInt(idCardNo.charAt(16)) % 2 === 0) {
        idCardInfo.gender = idCardNoUtil.genders.female
      } else {
        idCardInfo.gender = idCardNoUtil.genders.male
      }
    }
    // 出生时间 毫秒

    var birthDayTime = new Date(idCardInfo.birthday).getTime()

    // 当前时间 毫秒
    var nowTime = new Date().getTime()
    // 一年毫秒数(365 * 86400000 = 31536000000)
    idCardInfo.age = Math.ceil((nowTime - birthDayTime) / 31536000000)
    return idCardInfo
  },

  getId15: function(idCardNo) {
    if (idCardNo.length === 15) {
      return idCardNo
    } else if (idCardNo.length === 18) {
      return idCardNo.substring(0, 6) + idCardNo.substring(8, 17)
    } else {
      return null
    }
  },

  getId18: function(idCardNo) {
    if (idCardNo.length === 15) {
      var id17 = idCardNo.substring(0, 6) + '19' + idCardNo.substring(6)
      var parityBit = idCardNoUtil.getParityBit(id17)
      return id17 + parityBit
    } else if (idCardNo.length === 18) {
      return idCardNo
    } else {
      return null
    }
  }
}
export function validIdcard(idcard) {
  return idCardNoUtil.checkIdCardNo(idcard)
}
export function getIdCardInfo(idcard) {
  return idCardNoUtil.getIdCardInfo(idcard)
}
/*
 * 验证是否是数字(包含小数)
 * */
export function isNumberPositive(val) {
  const mobileReg = /(^[0-9][0-9]*(.[0-9]+)?)$/
  return mobileReg.test(val)
}
/*
 * 验证是否是数字(非负整数)
 * */
export function isNumberOverall0(val) {
  const mobileReg = /^\d+$/
  return mobileReg.test(val)
}
/*
 * 验证是否有汉字
 * */
export function isHasChina(val) {
  const mobileReg = /.*[\u4e00-\u9fa5]+.*/
  return mobileReg.test(val)
}
/*
 * 验证是否有特殊字符
 * */
export function isHasCharacter(val) {
  const mobileReg = /^(?=[0-9a-zA-Z_]+$)/
  return mobileReg.test(val)
}
