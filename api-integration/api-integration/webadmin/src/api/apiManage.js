/*
 * @Author: your name
 * @Date: 2020-09-14 16:36:54
 * @LastEditTime: 2020-09-16 16:50:08
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\message.js
 */
import request from '@/utils/request'
// 列表
export function findAll(params) {
  return request({
    url: '/webadmin/apiManage/list',
    method: 'get',
    params
  })
}

// 详情
export function findById(params) {
  return request({
    url: '/webadmin/apiManage/findById',
    method: 'get',
    params
  })
}

// 详情
export function findSourceApiById(params) {
  return request({
    url: '/webadmin/apiManage/findSourceApiById',
    method: 'get',
    params
  })
}

// 删除
export function deleteApi(id) {
  return request({
    url: '/webadmin/apiManage/delete',
    method: 'delete',
    params: { id }
  })
}

// 新增
export function add(data) {
  return request({
    url: '/webadmin/apiManage/add',
    method: 'POST',
    data
  })
}

// 编辑
export function update(data) {
  return request({
    url: '/webadmin/apiManage/update',
    method: 'put',
    data
  })
}

// 复制
export function copy(params) {
  return request({
    url: '/webadmin/apiManage/copy',
    method: 'get',
    params
  })
}

// 数据导出导出
export function exportApiDoc(params) {
  return request({
    url: '/webadmin/apiManage/exportApiDoc',
    method: 'get',
    responseType:'blob',
    params
  })
}

// 列表
export function sourceApiList(params) {
  return request({
    url: '/webadmin/apiManage/sourceApiList',
    method: 'get',
    params
  })
}

// 发布
export function publish(data) {
  return request({
    url: '/webadmin/apiManage/publish',
    method: 'PUT',
    data
  })
}

// 撤回发布
export function revocation(data) {
  return request({
    url: '/webadmin/apiManage/revocation',
    method: 'PUT',
    data
  })
}


