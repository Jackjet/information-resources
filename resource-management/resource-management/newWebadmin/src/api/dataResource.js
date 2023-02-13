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
export function findAllDataSource(params) {
  return request({
    url: '/webadmin/dataSourceInfo/findAll',
    method: 'get',
    params:params
  })
}

// 详情查询
export function findById(params) {
  return request({
    url: '/webadmin/dataSourceInfo/findById',
    method: 'get',
    params:params
  })
}

// 新增
export function add(params) {
  return request({
    url: '/webadmin/dataSourceInfo/insert',
    method: 'post',
    data:params
  })
}

// 修改查询
export function update(params) {
  return request({
    url: '/webadmin/dataSourceInfo/update',
    method: 'put',
    data:params
  })
}

// 删除
export function deleteById(params) {
  return request({
    url: '/webadmin/dataSourceInfo/deleteById',
    method: 'delete',
    params:params
  })
}

// 测试连接
export function testConnection(params) {
  return request({
    url: '/webadmin/dataSourceInfo/testConnection',
    method: 'get',
    params:params
  })
}

// 测试连接
export function testConnectionButton(params) {
  return request({
    url: '/webadmin/dataSourceInfo/testConnectionButton',
    method: 'post',
    data:params
  })
}

