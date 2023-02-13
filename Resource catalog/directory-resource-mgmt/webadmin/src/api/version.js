/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/14
 */
import request from '@/utils/request'
/* 获取版本管理列表*/
export function getVersionList(query) {
  return request({
    url: '/cataInfoHistory/list',
    method: 'get',
    params: query
  })
}
/* 删除版本*/
export function delVersion(data) {
  return request({
    url: '/cataInfoHistory/delete',
    method: 'post',
    data
  })
}
/* 版本比较*/
export function compareVersion(query) {
  return request({
    url: '/cataInfoHistory/delete',
    method: 'get',
    params: query
  })
}
/* 版本详情*/
export function getVersionDetail(query) {
  return request({
    url: '/cataInfoHistory/read',
    method: 'get',
    params: query
  })
}
/* 版本恢复*/
export function versionRevert(query) {
  return request({
    url: '/cataInfoHistory/rollback',
    method: 'get',
    params: query
  })
}
