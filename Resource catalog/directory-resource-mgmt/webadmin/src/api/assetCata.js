import request from '@/utils/request'

export function getAllTree(query) {
  return request({
    url: '/dictAssetCate/getTree',
    method: 'get',
    params: query
  })
}
// 根据权限获取树
export function getPowerTree(query) {
  return request({
    url: '/dictAssetCate/getTreeByPower',
    method: 'get',
    params: query
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
// 删除
export function deleteCata(query) {
  return request({
    url: '/dictAssetCate/delete',
    method: 'get',
    params: query
  })
}
// 生成编码
export function generateCode(query) {
  return request({
    url: '/dictAssetCate/generateCode',
    method: 'get',
    params: query
  })
}
// 详情
export function getCataDetail(query) {
  return request({
    url: '/dictAssetCate/get',
    method: 'get',
    params: query
  })
}
export function ableCata(query) {
  return request({
    url: '/dictAssetCate/isDisabled',
    method: 'get',
    params: query
  })
}
export function getlist(query) {
  return request({
    url: '/dictAssetCate/list',
    method: 'get',
    params: query
  })
}
export function addCata(data) {
  return request({
    url: '/dictAssetCate/add',
    method: 'post',
    data
  })
}
export function updataCata(data) {
  return request({
    url: '/dictAssetCate/update',
    method: 'post',
    data
  })
}

// 数据编码管理
export function sysDictDictAdd(data) {
  return request({
    url: '/sysDict/add',
    method: 'post',
    data
  })
} // 数据编码管理
export function sysDictUpdate(data) {
  return request({
    url: '/sysDict/update',
    method: 'post',
    data
  })
}
// 数据编码删除
export function dsysDictDelete(query) {
  return request({
    url: '/sysDict/delete',
    method: 'get',
    params: query
  })
}
// 数据编码详情
export function dsysDictGet(query) {
  return request({
    url: '/sysDict/get',
    method: 'get',
    params: query
  })
}

// 数据通过类型获取列
export function dsysDictGetByTypet(query) {
  return request({
    url: '/sysDict/getByType',
    method: 'get',
    params: query
  })
}

// 数据编码树

export function dsysDictGetTree(query) {
  return request({
    url: '/sysDict/getTree',
    method: 'get',
    params: query
  })
}
// 数据编码l

export function dsysDictList(query) {
  return request({
    url: '/sysDict/list',
    method: 'get',
    params: query
  })
}
// 组织机构详情

export function orgDeptTree(query) {
  return request({
    url: '/org/deptTree',
    method: 'get',
    params: query
  })
}
// 组织机构详情

export function orgDetail(query) {
  return request({
    url: '/org/detail',
    method: 'get',
    params: query
  })
}
// 组织机构生成机构代码
export function orgGenerateCd(query) {
  return request({
    url: '/org/generateCd',
    method: 'get',
    params: query
  })
}

// 组织机构查询定位
export function orgGetPosition(query) {
  return request({
    url: '/org/getPosition',
    method: 'get',
    params: query
  })
}
// 组织机构分页
export function orgList(query) {
  return request({
    url: '/org/list',
    method: 'get',
    params: query
  })
}
// 组织机构删除
export function orgDelete(query) {
  return request({
    url: '/org/delete',
    method: 'get',
    params: query
  })
}
// 组织机构新增
export function orgSave(data) {
  return request({
    url: '/org/save',
    method: 'post',
    data
  })
}
// 组织机构更新
export function orgUpdateOrg(data) {
  return request({
    url: '/org/updateOrg',
    method: 'post',
    data
  })
}

// 信息箱删除

export function archDelete(data) {
  return request({
    url: '/archBusiUviewStrConfig/delete',
    method: 'post',
    data
  })
}
// 信息箱删除

export function archSave(data) {
  return request({
    url: '/archBusiUviewStrConfig/save',
    method: 'post',
    data
  })
}
// 信息箱删除

export function archUpdate(data) {
  return request({
    url: '/archBusiUviewStrConfig/update',
    method: 'post',
    data
  })
}
export function archList(query) {
  return request({
    url: '/archBusiUviewStrConfig/list',
    method: 'get',
    params: query
  })
}
export function archRead(query) {
  return request({
    url: '/archBusiUviewStrConfig/read',
    method: 'get',
    params: query
  })
}
/* 获取序号*/
export function getDisplaySn(query) {
  return request({
    url: '/archBusiUviewStrConfig/getOrderNum',
    method: 'get',
    params: query
  })
}
/* 获取目录序号*/
export function getCataOrderNum(query) {
  return request({
    url: '/dictAssetCate/getOrderNum',
    method: 'get',
    params: query
  })
}

// 沉余分析列表
export function getNotUseList(query) {
  return request({
    url: '/sysDict/getNotUseList',
    method: 'get',
    params: query
  })
}
// 关联关系图
export function getAnalysisList(query) {
  return request({
    url: '/sysDict/getAnalysisList',
    method: 'get',
    params: query
  })
}
