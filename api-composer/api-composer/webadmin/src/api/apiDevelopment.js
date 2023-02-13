/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 详情查询
export function findById(params) {
  return request({
    url: '/webadmin/apiDevelopment/findByApiId',
    method: 'get',
    params:params
  })
}

// 新增
export function add(params) {
  return request({
    url: '/webadmin/apiDevelopment/insert',
    method: 'post',
    data:params
  })
}

