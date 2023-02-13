/*
 * @Author: your name
 * @Date: 2020-06-23 14:10:36
 * @LastEditTime: 2020-06-26 15:42:01
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\omManger\user.js
 */ 
import request from '@/utils/request'

// 获取模型列表
export function getList(params) {
  return request({
    url: '/webadmin/user/findAll',
    method: 'get',
    params
  })
}

// 模型删除
export function deUser(id) {
  return request({
    url: '/webadmin/user/delete',
    method: 'delete',
    params: { id }
  })
}

// 模型新增
export function insert(data) {
  return request({
    url: '/webadmin/user/insert',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/webadmin/user/update',
    method: 'put',
    data
  })
}

export function updateStatus(data) {
  return request({
    url: '/webadmin/user/updateStatus',
    method: 'put',
    data
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/user/findById',
    method: 'get',
    params
  })
}

export function roleList() {
  return request({
    url: '/webadmin/permission/role/findAll',
    method: 'get',
  })
}

export function setPasswd(data) {
  return request({
    url: '/webadmin/user/resetPassword',
    method: 'put',
    data
  })
}

