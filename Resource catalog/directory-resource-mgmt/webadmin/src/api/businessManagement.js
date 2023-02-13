/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/13
 */
import request from '@/utils/request'
/* 部门的权责清单树*/
export function getBusinessTree(query) {
  return request({
    url: '/archBusi/tree',
    method: 'get',
    params: query
  })
}
/* 权责清单树*/
export function getBusinessTreeByDeptId(query) {
  return request({
    url: '/archBusi/treeByDeptId',
    method: 'get',
    params: query
  })
}
/* 获取权责清单列表*/
export function getBusinessList(query) {
  return request({
    url: '/archBusi/list',
    method: 'get',
    params: query
  })
}
/* 新增编辑权责清单*/
export function saveOrUpdate(data) {
  return request({
    url: '/archBusi/saveOrUpdate',
    method: 'post',
    data
  })
}
/* 获取产生、需要信息资源列表*/
export function getDataResourceList(query) {
  return request({
    url: '/archBusi/cataInfoPage',
    method: 'get',
    params: query
  })
}
/* 获取信息资源目录列表*/
export function getDataResourceListAll(query) {
  return request({
    url: '/cataInfoTemp/list',
    method: 'get',
    params: query
  })
}
/* 删除关联事项*/
export function delBusiness(query) {
  return request({
    url: '/archBusi/delete',
    method: 'get',
    params: query
  })
}
/* 移除信息资源目录*/
export function deleteCataInfoRel(query) {
  return request({
    url: '/archBusi/deleteCataInfoRel',
    method: 'get',
    params: query
  })
}
/* 获取权责清单详情*/
export function getBusinessDetail(query) {
  return request({
    url: '/archBusi/detail',
    method: 'get',
    params: query
  })
}
/* 获取权责清单编码*/
export function getBusinessNo(query) {
  return request({
    url: '/archBusi/getBusiNo',
    method: 'get',
    params: query
  })
}
export function relDataResource(data) {
  return request({
    url: '/archBusi/relCataInfo',
    method: 'post',
    data
  })
}

/* 获取应用系统分页  */
export function getAppPage(query) {
  return request({
    url: '/archBusi/appPage',
    method: 'get',
    params: query
  })
}
/* 新增-0产生| 1需要的信息资源分页 */
export function getAddCataInfo(query) {
  return request({
    url: '/archBusi/getCataInfoPage',
    method: 'get',
    params: query
  })
}
/* 导入*/
export function upload(data) {
  return request({
    url: '/archBusi/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}