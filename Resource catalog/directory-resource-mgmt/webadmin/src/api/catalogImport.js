/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/19
 */
import request from '@/utils/request'
/* 列表查询*/

export function getImportList(query) {
  return request({
    url: '/cataImport/list',
    method: 'get',
    params: query
  })
}

/* 删除*/
export function delImportItem(query) {
  return request({
    url: '/cataImport/delete',
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
/* 导入*/
export function uploadCity(data) {
  return request({
    url: '/cataImport/uploadCity',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
/* 导出操作exportFile*/
export function exportFile(query) {
  return request({
    url: '/cataExport/export',
    method: 'get',
    params: query
  })
}
/* 导出分页查询*/
export function getExportList(query) {
  return request({
    url: '/cataExport/list',
    method: 'get',
    params: query
  })
}
/* 导出删除*/
export function exportDel(query) {
  return request({
    url: '/cataExport/delete',
    method: 'get',
    params: query
  })
}
