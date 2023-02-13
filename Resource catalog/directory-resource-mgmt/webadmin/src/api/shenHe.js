/**
 * @Description:
 * @author: xcn
 * @date: 2020/5/14
 */
import request from '@/utils/request'
/* 初审列表分页*/
export function cataShenHeList(query) {
  return request({
    url: '/cataInfoApprove/approveList',
    method: 'get',
    params: query
  })
}
/* 终审列表分页*/
export function cataShenHeList2(query) {
  return request({
    url: '/cataInfoApprove/approveList2',
    method: 'get',
    params: query
  })
}
/* 初审列表分页*/
export function cataShenHeApprove(data, query) {
  return request({
    url: '/cataInfoApprove/approve',
    method: 'post',
    params: query,
    data
  })
}
/* 终审列表分页*/
export function cataShenHeApprove2(data, query) {
  return request({
    url: '/cataInfoApprove/approve2',
    method: 'post',
    params: query,
    data
  })
}
/* 终审列表分页*/
export function cataShenHeApproveDelete(data, query) {
  return request({
    url: '/cataInfoApprove/approveDelete',
    method: 'post',
    params: query,
    data
  })
}
export function fileShenHeInfoList(query) {
  return request({
    url: '/fileUploadRel/list2',
    method: 'get',
    params: query
  })
}

export function fileShenHeDetail(query) {
  return request({
    url: '/fileUploadRel/detail',
    method: 'get',
    params: query
  })
}
/* 审核列表分页*/
export function fileShenHeApprove(data, query) {
  return request({
    url: '/fileUploadRel/reviewed',
    method: 'post',
    params: query,
    data
  })
}
export function cataShenHeInfoList(query) {
  return request({
    url: '/cataInfoApprove/cataInfoList',
    method: 'get',
    params: query
  })
}
export function cataShenHeInfoDeleteList(query) {
  return request({
    url: '/cataInfoApprove/cataInfoDeleteList',
    method: 'get',
    params: query
  })
}