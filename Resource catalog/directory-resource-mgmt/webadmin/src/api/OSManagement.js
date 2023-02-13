/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/14
 */
import request from '@/utils/request'
export function getDepartmentTree(query) {
  return request({
    url: '/archAppSys/deptTree',
    method: 'get',
    params: query
  })
}
export function getOrgTree(query) {
  return request({
    url: '/archAppSys/orgTree',
    method: 'get',
    params: query
  })
}
export function getOSList(query) {
  return request({
    url: '/archAppSys/list',
    method: 'get',
    params: query
  })
}
export function getOSDetail(query) {
  return request({
    url: '/archAppSys/detail',
    method: 'get',
    params: query
  })
}
/* 删除*/
export function delOS(url, data) {
  return request({
    url: url,
    method: 'post',
    data
  })
}
/* 新增或保存*/
export function saveOS(data) {
  return request({
    url: '/archAppSys/saveOrUpdate',
    method: 'post',
    data
  })
}
/* 所制成的权责清单分页*/
export function getBusiPage(query) {
  return request({
    url: '/archAppSys/busiPage',
    method: 'get',
    params: query
  })
}

export function getDiagram(query) {
  return request({
    url: '/archAppSys/getDiagram',
    method: 'get',
    params: query
  })
}
export function getCode() {
  return request({
    url: '/archAppSys/getCode',
    method: 'get',
    params: {}
  })
}