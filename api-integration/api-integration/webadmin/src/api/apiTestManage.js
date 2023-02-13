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
    url: '/webadmin/apiTestManage/apiList',
    method: 'get',
    params
  })
}

export function historyList(params) {
  return request({
    url: '/webadmin/apiTestManage/historyList',
    method: 'get',
    params
  })
}


export function apiTestParams(params) {
  return request({
    url: '/webadmin/apiTestManage/apiTestParams',
    method: 'get',
    params
  })
}


export function saveApiTestCase(data) {
  return request({
    url: '/webadmin/apiTestManage/saveApiTestCase',
    method: 'post',
    data
  })
}

export function getSign(params) {
  return request({
    url: '/webadmin/apiTestManage/getSign',
    method: 'get',
    params
  })
}


export function testCaseList(params) {
  return request({
    url: '/webadmin/apiTestManage/testCaseList',
    method: 'get',
    params
  })
}

export function getContent(params) {
  return request({
    url: '/webadmin/apiTestManage/getContent',
    method: 'get',
    params
  })
}

// 删除
export function deleteApiTestLog(id) {
  return request({
    url: '/webadmin/apiTestManage/deleteApiTestLog',
    method: 'delete',
    params: { id }
  })
}

// 删除
export function deleteApiTestCase(id) {
  return request({
    url: '/webadmin/apiTestManage/deleteApiTestCase',
    method: 'delete',
    params: { id }
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/apiTestManage/findById',
    method: 'get',
    params
  })
}

export function findApiTestCaseById(params) {
  return request({
    url: '/webadmin/apiTestManage/findApiTestCaseById',
    method: 'get',
    params
  })
}

export function apiTestLogList(params) {
  return request({
    url: '/webadmin/apiTestManage/apiTestLogList',
    method: 'get',
    params
  })
}

export function exportCase(params) {
  return request({
    url: '/webadmin/apiTestManage/exportCase',
    method: 'get',
    params
  })
}

// 新增
export function getApiTestHeader(data) {
  return request({
    url: '/webadmin/apiTestManage/getApiTestHeader',
    method: 'post',
    data
  })
}

// 新增
export function insertTestResult(data) {
  return request({
    url: '/webadmin/apiTestManage/insertTestResult',
    method: 'post',
    data
  })
}

export function hasAuth(params) {
  return request({
    url: '/webadmin/apiTestManage/hasAuth',
    method: 'get',
    params
  })
}
