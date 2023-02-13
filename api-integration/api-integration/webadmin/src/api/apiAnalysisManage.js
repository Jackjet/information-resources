/*
 * @Author: your name
 * @Date: 2020-09-14 16:36:54
 * @LastEditTime: 2020-09-16 16:50:08
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\message.js
 */
import request from '@/utils/request'
// API列表
export function apiList(params) {
  return request({
    url: '/webadmin/apiAnalysis/apiList',
    method: 'get',
    params
  })
}

// APP列表
export function appList(params) {
  return request({
    url: '/webadmin/apiAnalysis/appList',
    method: 'get',
    params
  })
}

// 访问量统计
export function statistic(params) {
  return request({
    url: '/webadmin/apiAnalysis/statistic',
    method: 'get',
    params
  })
}

// APP下API列表
export function authApiList(params) {
  return request({
    url: '/webadmin/apiAnalysis/authApiList',
    method: 'get',
    params
  })
}

// API下APP列表
export function authAppList(params) {
  return request({
    url: '/webadmin/apiAnalysis/authAppList',
    method: 'get',
    params
  })
}

// API访问次数
export function apiVisitNum(params) {
  return request({
    url: '/webadmin/apiAnalysis/apiVisitNum',
    method: 'get',
    params
  })
}

// API访问次数 根据IP分组统计
export function ipVisitNum(params) {
  return request({
    url: '/webadmin/apiAnalysis/ipVisitNum',
    method: 'get',
    params
  })
}

// API访问次数 根据CODE分组统计
export function codeVisitNum(params) {
  return request({
    url: '/webadmin/apiAnalysis/codeVisitNum',
    method: 'get',
    params
  })
}

// API访问次数 根据响应时长分组统计
export function durationNum(params) {
  return request({
    url: '/webadmin/apiAnalysis/durationNum',
    method: 'get',
    params
  })
}

// 日志详情记录
export function logRecord(params) {
  return request({
    url: '/webadmin/apiAnalysis/logRecord',
    method: 'get',
    params
  })
}

export function getContent(params) {
  return request({
    url: '/webadmin/apiAnalysis/getContent',
    method: 'get',
    params
  })
}
