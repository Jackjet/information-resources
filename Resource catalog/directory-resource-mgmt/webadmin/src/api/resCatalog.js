import request from '@/utils/request'

export function getTableList(query) {
  return request({
    url: '/cataInfoTemp/list',
    method: 'get',
    params: query
  })
}
export function getTableList2(query) {
  return request({
    url: '/cataInfoTemp/list2',
    method: 'get',
    params: query
  })
}
// 详情
export function resDetail(query) {
  return request({
    url: '/cataInfoTemp/read',
    method: 'get',
    params: query
  })
}
// 详情
export function resHistoryDetail(query) {
  return request({
    url: '/cataInfoHistory/read',
    method: 'get',
    params: query
  })
}
// 信息项详情
export function infoHistoryDetail(query) {
  return request({
    url: '/cataInfoItemHistoryRel/list',
    method: 'get',
    params: query
  })
}
export function infoResDetail(query) {
  return request({
    url: '/cataInfoItemTempRel/list',
    method: 'get',
    params: query
  })
}
// 审核记录
export function checkRecordDetail(query) {
  return request({
    url: '/cataInfoApprove/approveList',
    method: 'get',
    params: query
  })
}
export function getTreeAll(query) {
  return request({
    url: '/cataInfoTemp/getTreeForCata',
    method: 'get',
    params: query
  })
}
// 信息格式分类/类型/周期
export function getInfoSelect1(query) {
  return request({
    url: '/sysDict/getByType',
    method: 'get',
    params: query
  })
}
// 部门信息资源分类
export function getDepartRes(query) {
  return request({
    url: '/dictAssetCate/getTreeByCodeAndOrgId',
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
export function submitCheckRes(data) {
  return request({
    url: '/cataInfoTemp/check',
    method: 'post',
    data
  })
}
export function allCheckRes(data) {
  return request({
    url: '/cataInfoTemp/allCheck',
    method: 'post',
    data
  })
}
export function recallBack(data) {
  return request({
    url: '/cataInfoTemp/back',
    method: 'post',
    data
  })
}
export function deleteRes(data) {
  return request({
    url: '/cataInfoTemp/delete',
    method: 'post',
    data
  })
}
/* 获取信息资源代码*/
export function getUviewNo(query) {
  return request({
    url: '/cataInfoTemp/getCode',
    method: 'get',
    params: query
  })
}
/* 获取信息资源代码*/
export function getDataTypes(query) {
  return request({
    url: '/sysDict/getByType',
    method: 'get',
    params: query
  })
}
/* 新增数据项*/
export function createInfo(data) {
  return request({
    url: '/cataInfoItemTempRel/save',
    method: 'post',
    data
  })
}

/* 保存接口*/
export function saveOrUpdateRes(data) {
  return request({
    url: '/cataInfoTemp/update',
    method: 'post',
    data
  })
}
/* 编辑*/
export function editInfo(data) {
  return request({
    url: '/cataInfoItemTempRel/update',
    method: 'post',
    data
  })
}
/* 删除*/
export function deleteInfo(url, data) {
  return request({
    url: url,
    method: 'post',
    data
  })
}

/* 版本比较*/
export function historyCompare(query) {
  return request({
    url: '/cataInfoHistory/compare',
    method: 'get',
    params: query
  })
}
/* 动态表单获取*/
export function getFormItem(query) {
  return request({
    url: '/archBusiUviewStrConfig/list',
    method: 'get',
    params: query
  })
}
/* 信息项显示序号*/
export function getOrderNum(query) {
  return request({
    url: '/cataInfoItemTempRel/getOrderNum',
    method: 'get',
    params: query
  })
}

// 脱敏详情
export function getRegInfo(query) {
  return request({
    url: '/desensitizationRuleTemp/info',
    method: 'get',
    params: query
  })
}

// 新增/编辑 脱敏
export function saveAddOrUpdate(data) {
  return request({
    url: '/desensitizationRuleTemp/addOrUpdate',
    method: 'post',
    data
  })
}

// 删除脱敏
export function delRegInfo(query) {
  return request({
    url: '/desensitizationRuleTemp/delete',
    method: 'get',
    params: query
  })
}
// 已读
export function updateLimit(query) {
  return request({
    url: '/cataInfoTemp/updateLimit',
    method: 'get',
    params: query
  })
}
// 全部已读
export function updateLimitAll(query) {
  return request({
    url: '/cataInfoTemp/updateLimitAll',
    method: 'get',
    params: query
  })
}