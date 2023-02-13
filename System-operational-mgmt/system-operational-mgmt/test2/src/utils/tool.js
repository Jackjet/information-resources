/*
 * @Author: lyy
 * @Date: 2020-06-09 23:06:26
 * @LastEditTime: 2020-06-09 23:31:30
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 */
/* eslint-disable prefer-const */
// eslint-disable-next-line no-unused-vars
export const isString = val => typeof val === 'string'

// eslint-disable-next-line no-unused-vars
export const isBoolean = val => typeof val === 'boolean'

// eslint-disable-next-line no-unused-vars
export const isFunction = val => val && typeof val === 'function'

// eslint-disable-next-line no-unused-vars
export const isArray = val => !!val && Array.isArray(val)

// eslint-disable-next-line no-unused-vars
export const isNumber = val => typeof val === 'number'

/**
 *  金钱格式化，三位加逗号
 *  @param { number } num
 */
export const formatMoney = num => num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')

/**
 *  截取字符串并加身略号
 */
export function subText(str, length) {
  if (str.length === 0) {
    return ''
  }
  if (str.length > length) {
    return str.substr(0, length) + '...'
  } else {
    return str
  }
}

/**
  是否为空
 * @param {*} val
 * @returns
 */
export function isEmpty(val) {
  // null or undefined
  if (val == null) return true

  if (typeof val === 'boolean') return false

  if (typeof val === 'number') return !val

  if (val instanceof Error) return val.message === ''

  switch (Object.prototype.toString.call(val)) {
    // String or Array
    case '[object String]':
    case '[object Array]':
      return !val.length

      // Map or Set or File
    case '[object File]':
    case '[object Map]':
    case '[object Set]': {
      return !val.size
    }
    // Plain Object
    case '[object Object]': {
      return !Object.keys(val).length
    }
  }

  return false
}
/**
 * 判断类型
 * @param {*} target 值
 */
export function type(target) {
  let ret = typeof (target)
  let template = {
    '[object Array]': 'array',
    '[object Object]': 'object',
    '[object Number]': 'number - object',
    '[object Boolean]': 'boolean - object',
    '[object String]': 'string-object'
  }

  if (target === null) {
    return 'null'
  } else if (ret === 'object') {
    let str = Object.prototype.toString.call(target)
    return template[str]
  } else {
    return ret
  }
}
