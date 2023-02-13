import request from '@/utils/request'

// 获取节点列表
export function getNodeList(params) {
  return request({
    url: '/webadmin/operations/node/findAll',
    method: 'get',
    params
  })
}

// 节点删除
export function deNodeModel(id) {
  return request({
    url: '/webadmin/operations/node/delete',
    method: 'delete',
    params: { id }
  })
}

// 节点新增
export function insertNode(data) {
  return request({
    url: '/webadmin/operations/node/insert',
    method: 'post',
    data
  })
}
// 节点更新
export function updateNode(data) {
  return request({
    url: '/webadmin/operations/node/update',
    method: 'put',
    data
  })
}

export function findIdNode(params) {
  return request({
      url: '/webadmin/operations/node/findById',
      method: 'get',
      params
  })
}


// 获取服务列表
export function getServiceList(params) {
  return request({
    url: '/webadmin/operations/services/findAll',
    method: 'get',
    params
  })
}

// 模型删除
export function deServiceModel(id) {
  return request({
    url: '/webadmin/operations/services/delete',
    method: 'delete',
    params: { id }
  })
}

// 模型新增
export function insertService(data) {
  return request({
    url: '/webadmin/operations/services/insert',
    method: 'post',
    data
  })
}

export function updateService(data) {
  return request({
    url: '/webadmin/operations/services/update',
    method: 'put',
    data
  })
}

export function findIdService(params) {
  return request({
      url: '/webadmin/operations/services/findById',
      method: 'get',
      params
  })
}

export function findAllNodes() {
  return request({
      url: '/webadmin/operations/services/findAllNodes',
      method: 'get'
  })
}

// 获取数据库列表
export function getDatabaseList(params) {
  return request({
    url: '/webadmin/operations/database/findAll',
    method: 'get',
    params
  })
}

// 模型删除
export function deDatabase(id) {
  return request({
    url: '/webadmin/operations/database/delete',
    method: 'delete',
    params: { id }
  })
}

// 模型新增
export function insertDatabase(data) {
  return request({
    url: '/webadmin/operations/database/insert',
    method: 'post',
    data
  })
}

export function updateDatabase(data) {
  return request({
    url: '/webadmin/operations/database/update',
    method: 'put',
    data
  })
}

export function findIdDatabase(params) {
  return request({
      url: '/webadmin/operations/database/findById',
      method: 'get',
      params
  })
}

export function findAllDatabaseNodes() {
  return request({
      url: '/webadmin/operations/database/findAllNodes',
      method: 'get'
  })
}

export function getRoleList(params) {
  return request({
    url: '/webadmin/permission/role/findAll',
    method: 'get',
    params
  })
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: '/webadmin/permission/role/deleteRole',
    method: 'delete',
    params: { id }
  })
}

// 查询
export function getRoleId(params) {
  return request({
    url: '/webadmin/permission/role/findById',
    method: 'get',
    params
  })
}


// 插入
export function insertRole(data) {
  return request({
    url: '/webadmin/permission/role/insert',
    method: 'post',
    data
  })
}

// 更新
export function updateRole(data) {
  return request({
    url: '/webadmin/permission/role/update',
    method: 'put',
    data
  })
}

export function getRolePermissionList(params) {
  return request({
    url: '/webadmin/permission/rolePermission/findAll',
    method: 'get',
    params
  })
}

// 更新
export function insertRolePermission(data) {
  return request({
    url: '/webadmin/permission/rolePermission/insert',
    method: 'post',
    data
  })
}