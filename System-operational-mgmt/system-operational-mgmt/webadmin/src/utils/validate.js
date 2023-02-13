/*
 * @Author: lyy
 * @Date: 2020-06-09 23:06:26
 * @LastEditTime: 2020-06-29 18:31:02
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
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
  // const valid_map = ['admin', 'editor']
  // valid_map.indexOf(str.trim()) >= 0
  return str.trim() !== "" && str.trim() !== null && str.trim() !== undefined 
}

/**
 *  验证md5格式(32位)
 *  @param { string } value
 */
export const isMD5 = value => /^([a-f\d]{32}|[A-F\d]{32})$/g.test(value)

/**
 *  验证中文姓名
 * @param { string } value
 */
export const isChineseName = value => /^(?:[\u4e00-\u9fa5·]{2,16})$/g.test(value)

/**
 *  验证手机号中国(严谨), 根据工信部2019年最新公布的手机号段
 * @param { string } value
 */
export const isMPStrict = value => /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g.test(value)

/**
 *  验证email(邮箱)
 * @param { string } value
 */
export const isEmail = value => /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/g.test(value)

/**
 *  验证座机电话(国内),如: 0341-86091234
 * @param { string } value
 */
export const isLandlineTelephone = value => /\d{3}-\d{8}|\d{4}-\d{7}/g.test(value)

/**
 *  身份证号, 支持1/2代(15位/18位数字)
 * @param { string } value
 */
export const isIDCard = value => /(^\d{8}(0\d|10|11|12)([0-2]\d|30|31)\d{3}$)|(^\d{6}(18|19|20)\d{2}(0\d|10|11|12)([0-2]\d|30|31)\d{3}(\d|X|x)$)/g.test(value)

/**
 *  验证护照（包含香港、澳门）
 * @param { string } value
 */
export const isPassport = value => /(^[EeKkGgDdSsPpHh]\d{8}$)|(^(([Ee][a-fA-F])|([DdSsPp][Ee])|([Kk][Jj])|([Mm][Aa])|(1[45]))\d{7}$)/g.test(value)
