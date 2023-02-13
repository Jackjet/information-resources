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
    url: '/webadmin/apiDesign/findAll',
    method: 'get',
    params:params
  })
}

// 详情查询
export function findById(params) {
  return request({
    url: '/webadmin/apiDesign/findById',
    method: 'get',
    params:params
  })
}

// 新增
export function add(params) {
  return request({
    url: '/webadmin/apiDesign/insert',
    method: 'post',
    data:params
  })
}

// 修改查询
export function update(params) {
  return request({
    url: '/webadmin/apiDesign/update',
    method: 'put',
    data:params
  })
}

// 删除
export function deleteById(params) {
  return request({
    url: '/webadmin/apiDesign/deleteById',
    method: 'delete',
    params:params
  })
}

// 获取组列表
export function getTree(params) {
  return request({
    url: '/webadmin/api/group/getTree',
    method: 'get',
    params
  })
}

/*添加组*/
export function insertApiGroup(data) {
  return request({
    url: '/webadmin/api/group/insert',
    method: 'post',
    data
  })
}

/*更新组*/
export function updateApiGroup(data) {
  return request({
    url: '/webadmin/api/group/update',
    method: 'put',
    data
  })
}

// 删除组
export function deleteApiGroupById(id) {
  return request({
    url: '/webadmin/api/group/deleteById',
    method: 'delete',
    params: { id }
  })
}

