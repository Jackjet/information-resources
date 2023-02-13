/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-12 15:25:44
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 查询
export function webAdminUserFindall(params) {
  return request({
    url: '/webadmin/system/webAdminUser/findAll',
    method: 'get',
    params
  })
}

// 详情
export function webAdminUserFind(params) {
  return request({
    url: '/webadmin/system/webAdminUser/find',
    method: 'get',
    params
  })
}

// 新增
export function webAdminUserInsert(data) {
  return request({
    url: '/webadmin/system/webAdminUser/insert', 
    method: 'post',
    data
  })
}

// 更新
export function webAdminUserUpdate(data) {
  return request({
    url: '/webadmin/system/webAdminUser/update', 
    method: 'put',
    data
  })
}

// 用户导入
export function webAdminUserImport(data) {
  return request({
    url: '/webadmin/system/webAdminUser/import', 
    method: 'post',
    data
  })
}

// 用户导出
export function webAdminUserExport(params) {
  return request({
    url: '/webadmin/system/webAdminUser/export', 
    method: 'get',
    params
  })
}

// 模板导出
export function webAdminUserExportModel(data) {
  return request({
    url: '/webadmin/system/webAdminUser/exportModel', 
    method: 'post',
    data
  })
}

// 批量删除
export function webAdminUserDeleteAll(ids) {
  return request({
    url: '/webadmin/system/webAdminUser/deleteAll', 
    method: 'delete',
    params: { ids }
  })
}

// 批量重置
export function webAdminUserUpdatePasswordReset(ids) {
  return request({
    url: '/webadmin/system/webAdminUser/updatePasswordReset', 
    method: 'put',
    data: { ids }
  })
}

// 启用列表
export function webAdminUserUpdateAllEnable(data) {
  return request({
    url: '/webadmin/system/webAdminUser/updateAllEnable', 
    method: 'put',
    data
  })
}

// 启用
export function webAdminUserUpdateEnable(data) {
  return request({
    url: '/webadmin/system/webAdminUser/updateEnable', 
    method: 'put',
    data
  })
}

