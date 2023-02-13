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
    url: '/webadmin/sourceApi/findAll',
    method: 'get',
    params:params
  })
}

// 详情查询
export function findById(params) {
  return request({
    url: '/webadmin/sourceApi/findById',
    method: 'get',
    params:params
  })
}

// 新增
export function add(params) {
  return request({
    url: '/webadmin/sourceApi/add',
    method: 'post',
    data:params
  })
}

// 修改查询
export function update(params) {
  return request({
    url: '/webadmin/sourceApi/update',
    method: 'put',
    data:params
  })
}

// 删除
export function deleteById(params) {
  return request({
    url: '/webadmin/sourceApi/deleteById',
    method: 'delete',
    params:params
  })
}


// 列表查询
export function supplierFindAll(params) {
  return request({
    url: '/webadmin/sourceApi/supplierFindAll',
    method: 'get',
    params:params
  })
}

// 导出
export function exportFile(params) {
  return request({
    url: '/webadmin/sourceApi/export',
    method: 'post',
    data:params
  })
}

