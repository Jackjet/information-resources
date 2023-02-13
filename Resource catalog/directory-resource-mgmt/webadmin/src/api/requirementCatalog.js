/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/13
 */
import request from '@/utils/request'
/* 获取信息需求目录列表*/
export function getRequirementCatalog(query) {
  return request({
    url: '/cataRequire/list',
    method: 'get',
    params: query
  })
}
/* 获取信息需求目录删除*/
export function delRequirementCatalog(data) {
  return request({
    url: '/cataRequire/delete',
    method: 'post',
    data
  })
}
/* 信息需求目录新增*/
export function addRequirementCatalog(data) {
  return request({
    url: '/cataRequire/save',
    method: 'post',
    data
  })
}
/* 信息需求目录新增*/
export function updateRequirementCatalog(data) {
  return request({
    url: '/cataRequire/update',
    method: 'post',
    data
  })
}
/* 信息需求目录详情*/
export function getRequirementDetail(query) {
  return request({
    url: '/cataRequire/read',
    method: 'get',
    params: query
  })
}
/* 信息需求目录-关联权责清单列表查询*/
export function requirementrCataRelList(query) {
  return request({
    url: '/cataBusInfoRel/list',
    method: 'get',
    params: query
  })
}
/* 信息需求目录-关联权责清单新增*/
export function addRequirementrCataRel(url, data) {
  return request({
    url: url,
    method: 'post',
    data
  })
}
/* 信息需求目录-关联权责清单删除*/
export function delRequirementrCataRel(data) {
  return request({
    url: '/cataBusInfoRel/delete',
    method: 'post',
    data
  })
}

/* 未关联权责清单查询*/
export function getToAddBusList(query) {
  return request({
    url: 'cataBusInfoRel/busList',
    method: 'get',
    params: query
  })
}
/* 获取信息提供单位的树*/
export function getProvDepTree(query) {
  return request({
    // url: '/cataRequire/deptTree',
    url: '/cataRequire/deptTree',
    method: 'get',
    params: query
  }) 
}
  // 内设部门
  export function getOrgTree(query) {
    return request({
      url: '/archAppSys/orgTree',
      method: 'get',
      params: query
    })
  }
   