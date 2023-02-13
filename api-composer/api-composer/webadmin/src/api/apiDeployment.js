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
export function findDesign(params) {
  return request({
    url: '/webadmin/apiDeployment/findDesign',
    method: 'get',
    params:params
  })
}

// 部署
export function deployApi(params) {
  return request({
    url: '/webadmin/apiDeployment/deployApi',
    method: 'post',
    data:params
  })
}

// 删除
export function deleteById(params) {
  return request({
    url: '/webadmin/apiDeployment/deleteById',
    method: 'delete',
    params:params
  })
}

//查询已部署容器
export function findAll(params) {
  return request({
    url: '/webadmin/apiDeployment/findAll',
    method: 'get',
    params:params
  })
}


export function findAllDeployment(params) {
  return request({
    url: '/webadmin/apiDeployment/testInfo',
    method: 'get',
    params
  })
}
