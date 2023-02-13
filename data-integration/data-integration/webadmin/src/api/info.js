/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 操作查询
export function timedTaskFindOsInfo(params) {
  return request({
    url: '/webadmin/system/base/findOsInfo',
    method: 'get',
    params
  })
}

// 新增
export function timedTaskInsert(data) {
  return request({
    url: '/webadmin/system/timedTask/update', 
    method: 'put',
    data
  })
}


// 获取数据库消息
export function findDatabaseInfo(params) {
  return request({
    url: '/webadmin/system/base/findDatabaseInfo', 
    method: 'get',
    params
  })
}

// 获取数据库消息
export function backupPostgreSQL(params) {
  return request({
    url: '/webadmin/system/base/backupPostgreSQL', 
    method: 'get',
    params
  })
}

// 获取数据库消息
export function timedTask(params) {
  return request({
    url: '/webadmin/system/timedTask/find', 
    method: 'get',
    params
  })
}

