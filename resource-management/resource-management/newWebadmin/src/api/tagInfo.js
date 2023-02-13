/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

/*获取标签分页*/
export function findAll (params) {
  return request({
    url: '/webadmin/tagInfo/findAll',
    method: 'get',
    params
  })
}

/*获取所有标签*/
export function findList (params) {
  return request({
    url: '/webadmin/tagInfo/findList',
    method: 'get',
    params
  })
}

// 详情查询
export function findById(params) {
  return request({
    url: '/webadmin/tagInfo/findById',
    method: 'get',
    params:params
  })
}

/*添加*/
export function insert(data) {
  return request({
    url: '/webadmin/tagInfo/insert',
    method: 'post',
    data
  })
}

/*更新*/
export function update(data) {
  return request({
    url: '/webadmin/tagInfo/update',
    method: 'put',
    data
  })
}

/*删除*/
export function deleteById(id) {
  return request({
    url: '/webadmin/tagInfo/deleteById',
    method: 'delete',
    params: {id}
  })
}
