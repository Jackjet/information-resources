/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/13
 */
import request from '@/utils/request'
/* 部门的权责清单树*/
export function getDepartmentTree(query) {
  return request({
    url: '/org/deptTree',
    method: 'get',
    params: query
  })
}
/* 部门and机构的权责清单树*/
export function getDeptOrgTree(query) {
  return request({
    url: '/org/deptTree2',
    method: 'get',
    params: query
  })
}
/* 部门详情*/
export function getOrgDetail(query) {
  return request({
    url: '/org/detail',
    method: 'get',
    params: query
  })
}
/* 获取权责清单列表*/
export function getBusinessList(query) {
  return request({
    url: '/org/busiList',
    method: 'get',
    params: query
  })
}
/* 删除权责清单列表*/
export function delBusiness(url, data) {
  return request({
    url: url,
    method: 'post',
    data
  })
}
/* 新增编辑权责清单*/
export function saveDepartment(data) {
  return request({
    url: '/org/updateDept',
    method: 'post',
    data
  })
}
/* 关联权责清单新增-分页*/
export function addRelBusList(query) {
  return request({
    url: '/org/addBusiPage',
    method: 'get',
    params: query
  })
}
/* 根据type查询数据字典*/
export function getDictByType(query) {
  return request({
    url: '/sysDict/getByType',
    method: 'get',
    params: query
  })
}
/* 关联权责清单新增*/

export function saveBusiRef(data) {
  return request({
    url: '/org/saveBusiRef',
    method: 'post',
    data
  })
}
/* 是否可以点击权责清单*/
export function isCilckBusi(query) {
  return request({
    url: '/org/isCilckBusi',
    method: 'get',
    params: query
  })
}
// 脱敏详情
export function getRegInfo(query) {
  return request({
    url: '/desensitizationRule/info',
    method: 'get',
    params: query
  })
}
