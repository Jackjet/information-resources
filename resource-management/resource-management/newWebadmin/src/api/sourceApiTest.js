/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 历史记录查询
export function findAllRecordGroup(params) {
  return request({
    url: '/webadmin/sourceApiTest/findAllRecordGroup',
    method: 'get',
    params:params
  })
}

// 测试用例查询
export function findAllTestCaseGroup(params) {
  return request({
    url: '/webadmin/sourceApiTest/findAllTestCaseGroup',
    method: 'get',
    params:params
  })
}

// 删除测试用例
export function deleteTestCase(params) {
  return request({
    url: '/webadmin/sourceApiTest/deleteTestCase',
    method: 'delete',
    params:params
  })
}

// 下载测试用例
export function downloadTestCase(params) {
  return request({
    url: '/webadmin/sourceApiTest/downloadTestCase',
    method: 'get',
    params:params
  })
}

// 添加测试用例
export function addTestCase(params) {
  return request({
    url: '/webadmin/sourceApiTest/addTestCase',
    method: 'post',
    data:params
  })
}

// 测试
export function doTest(params) {
  return request({
    url: '/webadmin/sourceApiTest/doTest',
    method: 'post',
    data:params
  })
}

// 删除测试记录
export function sourceApiTestRecordDelete(params) {
  return request({
    url: '/webadmin/sourceApiTest/sourceApiTestRecordDelete',
    method: 'delete',
    params:params
  })
}

// 新增
export function insertTestResult(data) {
  return request({
    url: '/webadmin/sourceApiTest/insertTestResult',
    method: 'post',
    data
  })
}


