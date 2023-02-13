import request from '@/utils/request'

// 模块访问统计
export function count(params) {
  return request({
    url: '/webadmin/moduleAccess/count',
    method: 'get',
    params
  })
}

// 模块监控
export function findAll(params) {
  return request({
    url: '/webadmin/moduleMonitor/findAll',
    method: 'get',
    params
  })
}

export function insert(data) {
  return request({
    url: '/webadmin/moduleMonitor/insert',
    method: 'post',
    data
  })
}

export function deleteData(data) {
  return request({
    url: '/webadmin/moduleMonitor/delete',
    method: 'delete',
    data
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/moduleMonitor/findById',
    method: 'get',
    params
  })
}

export function update(data) {
  return request({
    url: '/webadmin/moduleMonitor/update',
    method: 'put',
    data
  })
}

export function findAllNodes(params) {
  return request({
    url: '/webadmin/operations/node/findAllNodes',
    method: 'get',
    params
  })
}

export function findAllServices(params) {
  return request({
    url: '/webadmin/operations/services/findAllServices',
    method: 'get',
    params
  })
}
