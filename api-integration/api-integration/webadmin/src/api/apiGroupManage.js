/*
 * @Author: your name
 * @Date: 2020-09-14 16:36:54
 * @LastEditTime: 2020-09-16 16:50:08
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\message.js
 */
import request from '@/utils/request'

// 组
export function tree(params) {
  return request({
    url: '/webadmin/apiGroupManage/tree',
    method: 'get',
    params
  })
}


// 列表
export function findAll(params) {
  return request({
    url: '/webadmin/apiGroupManage/list',
    method: 'get',
    params
  })
}

// 详情
export function findById(params) {
  return request({
    url: '/webadmin/apiGroupManage/findById',
    method: 'get',
    params
  })
}

// 删除
export function deleteGroup(id) {
  return request({
    url: '/webadmin/apiGroupManage/delete',
    method: 'delete',
    params: { id }
  })
}

// 新增
export function add(data) {
  return request({
    url: '/webadmin/apiGroupManage/add',
    method: 'POST',
    data
  })
}

// 编辑
export function update(data) {
  return request({
    url: '/webadmin/apiGroupManage/update',
    method: 'put',
    data
  })
}

// 组内新增API
export function insertApi(data) {
  return request({
    url: '/webadmin/apiGroupManage/insertApi',
    method: 'POST',
    data
  })
}

// 组内API列表
export function groupApiList(params) {
  return request({
    url: '/webadmin/apiGroupManage/groupApiList',
    method: 'get',
    params
  })
}

// 删除
export function groupApiDelete(ids) {
  return request({
    url: '/webadmin/apiGroupManage/groupApiDelete',
    method: 'delete',
    params: { ids }
  })
}

// API列表
export function apiList(params) {
  return request({
    url: '/webadmin/apiGroupManage/apiList',
    method: 'get',
    params
  })
}
