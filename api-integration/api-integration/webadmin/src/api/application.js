/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 列表查询
export function findAll(params) {
  return request({
    url: '/webadmin/application/findAll',
    method: 'get',
    params:params
  })
}

// 添加
export function add(params) {
  return request({
    url: '/webadmin/application/add',
    method: 'post',
    data:params
  })
}

// 修改
export function update(params) {
  return request({
    url: '/webadmin/application/update',
    method: 'put',
    data:params
  })
}

// 详情
export function findById(params) {
  return request({
    url: '/webadmin/application/findById',
    method: 'get',
    params:params
  })
}

// 删除
export function deleteById(params) {
  return request({
    url: '/webadmin/application/deleteById',
    method: 'delete',
    params:params
  })
}


// 导出
export function exportApplication(params) {
  return request({
    url: '/webadmin/application/export',
    method: 'get',
    params:params
  })
}

// 更新密钥
export function updateKey(params) {
  return request({
    url: '/webadmin/application/updateKey',
    method: 'put',
    data:params
  })
}
