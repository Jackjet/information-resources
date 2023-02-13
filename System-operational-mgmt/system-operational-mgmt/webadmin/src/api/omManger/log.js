/*
 * @Author: your name
 * @Date: 2020-06-24 09:39:35
 * @LastEditTime: 2020-07-02 11:08:03
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\omManger\log.js
 */ 
import request from '@/utils/request'

// 获取操作日志列表
export function getOperationLogList(params) {
  return request({
    url: '/webadmin/log/operationLog/findAll',
    method: 'get',
    params
  })
}

// 系统日志
export function getSystemLogList(params) {
  return request({
    url: '/webadmin/log/systemLog/findAll',
    method: 'get',
    params
  })
}

// 获取审计日志
export function getAuditLogList(params) {
  return request({
    url: '/webadmin/log/auditLog/findAll',
    method: 'get',
    params
  })
}

// 获取审计日志
export function findById(params) {
  return request({
    url: '/webadmin/log/operationLog/findById',
    method: 'get',
    params
  })
}