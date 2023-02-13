/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'
import request3 from '@/utils/request3'

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

// 查询所有数据源
export function findAllContainer(params) {
  return request({
    url: '/webadmin/resources/findAllContainer',
    method: 'get',
    params:params
  })
}

// 同步
export function apiDesignInsert(apiId) {
  return request({
    url: '/webadmin/resources/apiDesignInsert',
    method: 'post',
    params: { apiId }
  })
}

export function datasourceFindAll(params) {
  return request3({
    url: '/common/dataSourceManage/findAll',
    method: 'get',
    params:params
  })
}

export function dataSourceById(params) {
  return request3({
    url: '/common/dataSourceManage/findAllByDataSourceId',
    method: 'get',
    params:params
  })
}

export function dataSourceByUnitId(params) {
  return request3({
    url: '/common/dataSourceManage/findAllByDataUnitId',
    method: 'get',
    params:params
  })
}

export function findRequestMetaData(params) {
  return request({
    url: '/webadmin/apiDevelopment/findRequestMetaData',
    method: 'get',
    params:params
  })
}

export function generateSql(data) {
  return request({
    url: '/webadmin/apiDevelopment/generateSql',
    method: 'post',
    data: data
  })
}
