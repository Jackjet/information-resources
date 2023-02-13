/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 查询所有标签
export function findAllMetas(params) {
  return request({
    url: '/webadmin/resources/findAllMetas',
    method: 'get',
    params:params
  })
}

// 查询所有数据源
export function findAllDataSource(params) {
  return request({
    url: '/webadmin/resources/findAllDataSource',
    method: 'get',
    params:params
  })
}

// 查询所有容器
export function findAllContainer(params) {
  return request({
    url: '/webadmin/resources/findAllContainer',
    method: 'get',
    params:params
  })
}