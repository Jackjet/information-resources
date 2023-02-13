/*
 * @Author: lyy
 * @Date: 2020-06-09 23:06:26
 * @LastEditTime: 2020-06-09 23:32:00
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin-master\webadmin-master\src\utils\array.js
 */

/**
 * 获取最大值
 * @param {*} arr 数组
 */
export const arrayMax = arr => Math.max(...arr)

/**
 * 获取最小值
 * @param {*} arr 数组
 */
export const arrayMin = arr => Math.min(...arr)

/**
 * 统计个数
 * @param {*} arr 数组
 * @param {*} value 值
 */
export const countOccurrences = (arr, value) => arr.reduce((a, v) => v === value ? a + 1 : a + 0, 0)
// countOccurrences([1,1,2,1,2,3], 1) -> 3

/**
 *  判断两个数组是否相等
 * @param {Array} 数组
 * @param {Array} 数组
 * @return {Boolean}
 */
export function arrayEqual(arr1, arr2) {
  if (arr1 === arr2) return true
  if (arr1.length != arr2.length) return false
  for (var i = 0; i < arr1.length; ++i) {
    if (arr1[i] !== arr2[i]) return false
  }
  return true
}
