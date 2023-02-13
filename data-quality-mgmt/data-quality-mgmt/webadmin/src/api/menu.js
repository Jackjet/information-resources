/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 10:58:34
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 查询
export function menuTreeFindall(params) {
  return request({
    url: '/webadmin/system/menuTree/findAll',
    method: 'get',
    params
  })
}
 
// 新增
export function menuTreeInsert(data) {
  return request({
    url: '/webadmin/system/menuTree/insert', 
    method: 'post',
    data
  })
}

// 更新
export function menuTreeUpdate(data) {
  return request({
    url: '/webadmin/system/menuTree/update', 
    method: 'put',
    data
  })
}

// 批量删除
export function menuTreeDeleteAll(ids) {
  return request({
    url: '/webadmin/system/menuTree/deleteAll', 
    method: 'delete',
    params: { ids }
  })
}

// 上下移动
export function menuTreeUpdateSeq(data) {
  return request({
    url: '/webadmin/system/menuTree/updateSeq', 
    method: 'put',
    data
  })
}

// 删除
export function menuTreedelete(id) {
  return request({
    url: '/webadmin/system/menuTree/delete', 
    method: 'delete',
    params: { id }
  })
}

