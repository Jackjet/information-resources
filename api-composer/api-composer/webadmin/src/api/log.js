/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-14 13:56:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 操作查询
export function operationLogFindAll(params) {
  return request({
    url: '/webadmin/log/operationLog/findAll',
    method: 'get',
    params
  })
}

// 详情
export function operationLogFind(params) {
  return request({
    url: '/webadmin/log/operationLog/find', 
    method: 'get',
    params
  })
}

// 导出
export function operationLogExport(params) {
  return request({
    url: '/webadmin/log/operationLog/export', 
    method: 'get',
    params
  })
}

// 用户查询
export function userLogFindAll(params) {
  return request({
    url: '/webadmin/log/userLog/findAll',
    method: 'get',
    params
  })
}

// 详情
export function userLogFind(params) {
  return request({
    url: '/webadmin/log/userLog/find', 
    method: 'get',
    params
  })
}

// 导出
export function userLogExport(params) {
  return request({
    url: '/webadmin/log/userLog/export', 
    method: 'get',
    params
  })
}