/* eslint-disable prefer-const */
/*
 * @Author: lyy
 * @Date: 2020-06-09 23:06:26
 * @LastEditTime: 2020-06-09 23:51:54
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 */
/**
 * 获取url参数（第一种）
 * @param {*} name
 * @param {*} origin
 */

export function getUrlParam(name, origin = null) {
  let reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
  let r = null
  if (origin == null) {
    r = window.location.search.substr(1).match(reg)
  } else {
    r = origin.substr(1).match(reg)
  }
  if (r != null) return decodeURIComponent(r[2])
  return null
}

/**
 * 获取窗口可视范围的高度
 */
export function getClientHeight() {
  let clientHeight = 0
  if (document.body.clientHeight && document.documentElement.clientHeight) {
    clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight
  } else {
    clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight
  }
  return clientHeight
}

/**
 * 获取窗口可视范围宽度
 */
export function getPageViewWidth() {
  let d = document
  let a = d.compatMode === 'BackCompat' ? d.body : d.documentElement
  return a.clientWidth
}

/**
 * 获取窗口宽度
 */
export function getPageWidth() {
  let g = document
  let a = g.body
  let f = g.documentElement
  let d = g.compatMode === 'BackCompat' ? a : g.documentElement
  return Math.max(f.scrollWidth, a.scrollWidth, d.clientWidth)
}

/**
 *  获取滚动条距顶部高度
 */
export function getPageScrollTop() {
  let a = document
  return a.documentElement.scrollTop || a.body.scrollTop
}

/**
 * 返回当前滚动条位置
 */
export const getScrollPosition = (el = window) => ({
  x: el.pageXOffset !== undefined ? el.pageXOffset : el.scrollLeft,
  y: el.pageYOffset !== undefined ? el.pageYOffset : el.scrollTop
})

/**
 * 获取元素顶部与文档顶部的距离
 * @param {element} el
 * @return {Number}
 */
export const getElementTopFromDocument = el => {
  var top = el.offsetTop
  var activeNode = el.offsetParent
  while (activeNode != null) {
    top += activeNode.offsetTop
    activeNode = activeNode.offsetParent
  }
  return top
}
