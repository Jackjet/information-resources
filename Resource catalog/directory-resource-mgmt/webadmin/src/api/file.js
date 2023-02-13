/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/22
 */
import request from '@/utils/request'
/* 上报列表*/
export function getUploadList(query) {
  return request({
    url: '/fileUploadRel/list1',
    method: 'get',
    params: query
  })
}
/* 上报xiangqing*/
export function getUploadDetail(query) {
  return request({
    url: '/fileUploadRel/detail',
    method: 'get',
    params: query
  })
}
/* 上报编辑*/
export function uploadEdit(data) {
  return request({
    url: '/fileUploadRel/update',
    method: 'post',
    data
  })
}
/* 确认上报文件*/
export function saveUploadForm(data) {
  return request({
    url: '/fileUploadRel/save',
    method: 'post',
    data
  })
}

//删除
export function deleteUpload(query) {
  return request({
    url: '/fileUploadRel/delete',
    method: 'get',
    params: query
  })
}

