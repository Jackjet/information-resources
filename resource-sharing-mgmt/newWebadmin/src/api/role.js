/*
 * @Author: lyy
 * @Date: 2020-09-08 18:26:43
 * @LastEditTime: 2020-09-20 21:53:44
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \web\src\api\organization.js
 */

import request from '@/utils/request'

// 查询
export function roleFindAll(params) {
  return request({
    url: '/webadmin/system/role/findAll',
    method: 'get',
    params
  })
}

// 插入
export function roleInsert(data) {
  return request({
    url: '/webadmin/system/role/insert',
    method: 'post',
    data
  })
}

// 更新
export function roleUpdate(data) {
  return request({
    url: '/webadmin/system/role/update',
    method: 'put',
    data
  })
}

// 更新
export function organizationUpdateSeq(data) {
  return request({
    url: '/webadmin/system/organization/updateSeq',
    method: 'post',
    data
  })
}

//删除
export function roleDelete(id) {
  return request({
    url: '/webadmin/system/role/delete',
    method: 'delete',
    params: { id }
  })
}

//获取
export function findRoleId(params) {
  return request({
    url: '/webadmin/system/roleUser/findRoleId',
    method: 'get',
    params
  })
}

//获取角色权限
export function findAllByRoleId(params) {
  return request({
    url: '/webadmin/system/roleMenuTree/findAllByRoleId',
    method: 'get',
    params
  })
}

//权限分配
export function roleMenuTreeInsert(data) {
  return request({
    url: '/webadmin/system/roleMenuTree/insert',
    method: 'post',
    data
  })
}

// 获取按钮权限
export function findCurrentUserButton(params) {
  return request({
    url: '/webadmin/system/roleUser/findCurrentUserButton',
    method: 'get',
    params
  })
}

// 获取树
export function findCurrentUser(params) {
  return request({
    url: '/webadmin/system/roleUser/findCurrentUser',
    method: 'get',
    params
  })
}

// 获取权限
export function findAllRoleIdChoose(params) {
  return request({
    url: '/webadmin/system/roleMenuTree/findAllRoleIdChoose',
    method: 'get',
    params
  })
}

// 获取menu树
export function findCurrentUserSidebar(params) {
  return request({
    url: '/webadmin/system/roleUser/findCurrentUserSidebar',
    method: 'get',
    params
  })
}
