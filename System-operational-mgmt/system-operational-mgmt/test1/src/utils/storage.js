
/**
 * localStorage 存贮
 * 目前对象值如果是函数 、RegExp等特殊对象存贮会被忽略
 * @param { String } key  属性
 * @param { string } value 值
 */
export const localStorageSet = (key, value) => {
  if (typeof (value) === 'object') value = JSON.stringify(value)
  localStorage.setItem(key, value)
}

/**
 * localStorage 获取
 * @param {String} key  属性
 */
export const localStorageGet = (key) => {
  return localStorage.getItem(key)
}

/**
 * localStorage 移除
 * @param {String} key  属性
 */
export const localStorageRemove = (key) => {
  localStorage.removeItem(key)
}

/**
 * localStorage 存贮某一段时间失效
 * @param {String} key  属性
 * @param {*} value 存贮值
 * @param { number } expire 过期时间,毫秒数
 */
export const localStorageSetExpire = (key, value, expire) => {
  if (typeof (value) === 'object') value = JSON.stringify(value)
  localStorage.setItem(key, value)
  setTimeout(() => {
    localStorage.removeItem(key)
  }, expire)
}

/**
 * sessionStorage 存贮
 * @param {String} key  属性
 * @param {*} value 值
 */
export const sessionStorageSet = (key, value) => {
  if (typeof (value) === 'object') value = JSON.stringify(value)
  sessionStorage.setItem(key, value)
}

/**
 * sessionStorage 获取
 * @param {String} key  属性
 */
export const sessionStorageGet = (key) => {
  return sessionStorage.getItem(key)
}
/**
 * @param {*} key 属性
 */
export const sessionStorageRemove = (key) => {
  sessionStorage.removeItem(key)
}

/**
 * @param {*} key 属性
 * @param {*} value 值
 * @param {*} expire 时间
 */
export const sessionStorageSetExpire = (key, value, expire) => {
  if (typeof (value) === 'object') value = JSON.stringify(value)
  sessionStorage.setItem(key, value)
  setTimeout(() => {
    sessionStorage.removeItem(key)
  }, expire)
}
