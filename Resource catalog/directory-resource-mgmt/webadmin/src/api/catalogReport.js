/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/19
 */
import request from '@/utils/request'
/* 列表查询*/

export function getresReportList(query) {
  return request({
    url: '/cataInfoReport/list',
    method: 'get',
    params: query
  })
}
export function getRequireReportList(query) {
  return request({
    url: '/cataRequireReport/list',
    method: 'get',
    params: query
  })
}

/* 删除*/
export function delResItem(query) {
  return request({
    url: '/cataInfoReport/delete',
    method: 'get',
    params: query
  })
}
export function delRequireItem(query) {
  return request({
    url: '/cataRequireReport/delete',
    method: 'get',
    params: query
  })
}
/* 异常信息查询*/
export function getErrorList(query) {
  return request({
    url: '/cataImport/getErrorList',
    method: 'get',
    params: query
  })
}
/* 导入*/
export function upload(data) {
  return request({
    url: '/cataImport/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/* 导出操作exportFile*/
export function exportResFile(query) {
  return request({
    url: '/cataInfoReport/generateReport',
    method: 'get',
    params: query
  })
}
export function exportRequireFile(query) {
  return request({
    url: '/cataRequireReport/generateReport',
    method: 'get',
    params: query
  })
}
