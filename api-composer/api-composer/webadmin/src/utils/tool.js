/*
 * @Author: lyy
 * @Date: 2020-06-09 23:06:26
 * @LastEditTime: 2020-10-12 00:23:30
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 */

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
    if (val === undefined) return true
    if (val == null) return true
    if (val === 'undefined') return true

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


/**
 * 防抖
 *
 */
export function debounce(func, wait, immediately) {
  let timer
  let debounced = function (...args) {
    let result
    // 清除闹钟后，闹钟还是存在的
    if (timer) clearTimeout(timer)
    if (immediately) {
      let called = !timer
      timer = setTimeout(() => {
        timer = null
      }, wait)
      if (called) {
        result = func.apply(this, args)
      }
    } else {
      timer = setTimeout(() => {
        func.apply(this, args)
      }, wait)
    }
    return result
  }
  debounced.cancel = function () {
    clearTimeout(timer)
    timer = null
  }
  return debounced
}

/**
 * 节流
 *
 */
export function throttle(func, wait, options) {
  let time, context, args, result;
  let previous = 0;
  if (!options) options = {};

  let later = function () {
    previous = options.leading === false ? 0 : new Date().getTime();
    time = null;
    func.apply(context, args);
    if (!time) context = args = null;
  };

  let throttled = function () {
    let now = new Date().getTime();
    if (!previous && options.leading === false) previous = now;
    let remaining = wait - (now - previous);
    context = this;
    args = arguments;
    if (remaining <= 0 || remaining > wait) {
      if (time) {
        clearTimeout(time);
        time = null;
      }
      previous = now;
      func.apply(context, args);
      if (!time) context = args = null;
    } else if (!time && options.trailing !== false) {
      time = setTimeout(later, remaining);
    }
  };
  return throttled;
}
