import request from '@/utils/request'

export function listAdmin(query) {
  return request({
    url: '/user/list',
    method: 'get',
    params: query
  })
}
export function deleteAdmin(data) {
  return request({
    url: '/user/delete',
    method: 'post',
    data
  })
}
export function createAdmin(data) {
  return request({
    url: '/user/create',
    method: 'post',
    data
  })
}
// 启用禁用
export function ableAdmin(data) {
  return request({
    url: '/user/isDisabled',
    method: 'post',
    data
  })
}
// 重置密码
export function resetAdmin(data) {
  return request({
    url: '/user/resetpwd',
    method: 'post',
    data
  })
}
// 设置权限
export function setCataRole(data) {
  return request({
    url: '/user/setCataRole',
    method: 'post',
    data
  })
}
export function readminAdmin(query) {
  return request({
    url: '/user/read',
    method: 'get',
    params: query
  })
}

export function updateAdmin(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data
  })
}
// 获取组织树

export function getDeptTree(data) {
  return request({
    url: '/org/deptTree',
    method: 'get',
    data
  })
}
// 获取角色树
export function getRoleOptions(data) {
  return request({
    url: '/role/options',
    method: 'get',
    data
  })
}
// 获取目录树
export function getCataTree(query) {
  return request({
    url: '/dictAssetCate/getTree',
    method: 'get',
    params: query
  })
}
