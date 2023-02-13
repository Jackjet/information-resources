/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/4/3
 */
import { validMobile, validIdcard, validFixMobile } from '@/utils/validate'

export function checkMobile(rule, value, callback) {
  if (value) {
    var vals = []
    if (value.indexOf('，') > 0) {
      value = value.replace(/，/g, ',')
    }
    if (value.indexOf(',') > 0) {
      vals = value.split(',')
      var flag = true
      vals.forEach((item, index) => {
        if (!validMobile(item) && !validFixMobile(item)) {
          flag = false
          return
        }
      })
      if (flag) {
        callback()
      } else {
        return callback(new Error(rule.message || '手机号格式不正确'))
      }
    } else {
      if (!validMobile(value) && !validFixMobile(value)) {
        return callback(new Error(rule.message || '手机号格式不正确'))
      } else {
        callback()
      }
    }
  } else {
    callback()
  }
}
export function checkIdcard(rule, value, callback) {
  if (value) {
    if (!validIdcard(value)) {
      return callback(new Error('身份证格式不正确'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}
export function checkMobileRepeat(rule, value, callback) {
  if (value) {
    var vals = []
    var tempSet = new Set()
    if (value.indexOf('，') > 0) {
      value = value.replace(/，/g, ',')
    }
    if (value.indexOf(',') > 0) {
      vals = value.split(',')
      var flag = true
      for (let i = 0; i < vals.length; i++) {
        tempSet.add(vals[i])
      }
      if (vals.length != tempSet.size) {
        return callback(new Error(rule.message || '手机号号码重复'))
      } else {
        callback()
      }
    } else {
      callback()
    }
  } else {
    callback()
  }
}
