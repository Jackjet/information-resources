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
    url: '/webadmin/authManage/apiList',
    method: 'get',
    params
  })
}

// 分组列表
export function apiGroupList(params) {
  return request({
    url: '/webadmin/authManage/groupList',
    method: 'get',
    params
  })
}


// 组内API列表
export function groupApiList(params) {
  return request({
    url: '/webadmin/authManage/groupApiList',
    method: 'get',
    params
  })
}

// 应用列表
export function appList(params) {
  return request({
    url: '/webadmin/authManage/appList',
    method: 'get',
    params
  })
}

// API下已授权的APP列表
export function apiAuthAppList(params) {
  return request({
    url: '/webadmin/authManage/authList',
    method: 'get',
    params
  })
}

// 新增
export function insertAuth(data) {
  return request({
    url: '/webadmin/authManage/add',
    method: 'POST',
    data
  })
}

// 删除授权
export function authDelete(ids) {
  return request({
    url: '/webadmin/authManage/authDelete',
    method: 'delete',
    params: { ids }
  })
}

export function getAuthNum(params) {
  return request({
    url: '/webadmin/authManage/getAuthNum',
    method: 'get',
    params
  })
}

