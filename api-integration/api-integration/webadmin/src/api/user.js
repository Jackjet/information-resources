/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-18 00:43:59
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/requestHost'

export function login(params) {
  return request({
    url: 'webadmin/system/webAdminUser/login',
    method: 'get',
    params
  })
}

export function resetPassword(data) {
  return request({
    url: '/webadmin/system/webAdminUser/updatePassword',
    method: 'put',
    data
  })
}

export function logout() {
  return request({
    url: '/webadmin/system/webAdminUser/signOut',
    method: 'get'
  })
}

export function userFindAll(params) {
  return request({
    url: '/webadmin/system/user/findAll',
    method: 'get',
    params
  })
}

export function userFindById(params) {
  return request({
    url: '/webadmin/system/user/findById',
    method: 'get',
    params
  })
}

export function userInsert(data) {
  return request({
    url: '/webadmin/system/user/insert',
    method: 'post',
    data
  })
}

export function userUpdate(data) {
  return request({
    url: '/webadmin/system/user/update',
    method: 'put',
    data
  })
}

// 重置密码
export function resetUser(data) {
  return request({
    url: '/webadmin/system/user/resetPassword',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: '/webadmin/system/user/delete',
    method: 'delete',
    params: { id }
  })
}

export function getSystem(params) {
  return request({
    url: '/webadmin/system/dept/findAll',
    method: 'get',
    params
  })
}

export function getUserType(params) {
  return request({
    url: 'webadmin/system/userType/findAll',
    method: 'get',
    params
  })
}

export function getUserfindAllList(params) {
  return request({
    url: '/webadmin/system/userType/findAllList',
    method: 'get',
    params
  })
}
export function roleList(params) {
  return request({
    url: '/webadmin/permission/role/findAll',
    method: 'get',
    params
  })
}

export function SmsSend(data) {
  return request({
    url: '/webadmin/sendSMS/SmsSend',
    method: 'post',
    data
  })
}

export function forgetPassword(data) {
  return request({
    url: '/webadmin/system/user/forgetPassword',
    method: 'put',
    data
  })
}

export function PermissionFindAll(params) {
  return request({
    url: '/webadmin/permission/my/menuTree',
    method: 'get',
    params
  })
}

export function findAllList(params) {
  return request({
    url: '/webadmin/system/dept/findAllListForUser',
    method: 'get',
    params
  })
}

export function permissionFindAllList(params) {
  return request({
    url: '/webadmin/permission/role/findAllList',
    method: 'get',
    params
  })
}

export function findById(id) {
  return request({
    url: '/webadmin/system/dept/findById',
    method: 'get',
    params: { id }
  })
}

export function getVerifyState() {
  return request({
    url: '/webadmin/system/base/getImageVerifyCodeState',
    method: 'get'
  })
}

export function getVerifyCode(data) {
  return request({
    url: '/webadmin/system/base/getImageVerifyCode',
    method: 'post',
    data
  })
}
