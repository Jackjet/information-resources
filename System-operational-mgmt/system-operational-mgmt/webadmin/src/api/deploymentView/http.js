import request from '@/utils/request'

// 安装路径管理
export function installPathFindAll(params) {
  return request({
    url: '/webadmin/installPath/findAll',
    method: 'get',
    params
  })
}

export function installPathInsert(data) {
  return request({
    url: '/webadmin/installPath/insert',
    method: 'post',
    data
  })
}

export function installPathDelete(data) {
  return request({
    url: '/webadmin/installPath/delete',
    method: 'delete',
    data
  })
}

export function installPathFindById(params) {
  return request({
    url: '/webadmin/installPath/findById',
    method: 'get',
    params
  })
}

export function installPathUpdate(data) {
  return request({
    url: '/webadmin/installPath/update',
    method: 'put',
    data
  })
}

// 自动化部署管理
export function deploymentFindAll(params) {
  return request({
    url: '/webadmin/deployment/findAll',
    method: 'get',
    params
  })
}

export function deploymentInsert(data) {
  return request({
    url: '/webadmin/deployment/insert',
    method: 'post',
    data
  })
}

export function deploymentDelete(data) {
  return request({
    url: '/webadmin/deployment/delete',
    method: 'delete',
    data
  })
}

export function deploymentFindById(params) {
  return request({
    url: '/webadmin/deployment/findById',
    method: 'get',
    params
  })
}

export function deploymentUpdate(data) {
  return request({
    url: '/webadmin/deployment/update',
    method: 'put',
    data
  })
}

export function execute(data) {
  return request({
    url: '/webadmin/deployment/execute',
    method: 'put',
    data
  })
}

export function dataApi(params) {
  return request({
    url: '/webadmin/dataApi/findAll',
    method: 'get',
    params
  })
}

export function deleteDeploymentFile(params) {
  return request({
    url: '/webadmin/service/file/deleteDeploymentFile/' + params.fileName + '?type=' + params.type + '&versionNumber=' + params.versionNumber + '&systemId=' + params.systemId + '&nodeId=' + params.nodeId,
    method: 'delete'
  })
}

export function deploymentApi(params) {
  return request({
    url: '/webadmin/deployment/deployment',
    method: 'get',
    params
  })
}

export function rollback(params) {
  return request({
    url: '/webadmin/deployment/rollback',
    method: 'get',
    params
  })
}

// 自动化部署日志
export function deploymentLogfindAll(params) {
  return request({
    url: '/webadmin/deploymentLog/findAll',
    method: 'get',
    params
  })
}

export function deploymentLogFindById(params) {
  return request({
    url: '/webadmin/deploymentLog/findById',
    method: 'get',
    params
  })
}

export function findByAutomatedDeploymentEntityId(params) {
  return request({
    url: '/webadmin/deploymentLog/findByAutomatedDeploymentEntityId',
    method: 'get',
    params
  })
}
