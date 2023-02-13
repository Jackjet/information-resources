import request from '@/utils/request'

// 基础环境监控列表
export function getList(params) {
  return request({
    url: '/webadmin/environment/findAll',
    method: 'get',
    params
  })
}
// 系统监控
export function systemList(params) {
  return request({
    url: '/webadmin/systemMonitor/findAll',
    method: 'get',
    params
  })
}

// 预警设置
export function update(data) {
  return request({
    url: '/webadmin/environmentAlarm/update',
    method: 'PUT',
    data
  })
}

// 节点名称
export function getFindAllNodes(params) {
	return request({
		url: '/webadmin/databaseOperation/remoteExecution/findAllNodes',
		method: 'get',
		params
	})
}

// 系统名称
export function findAllServices(params) {
  return request({
    url: '/webadmin/operations/services/findAllServices',
    method: 'get',
    params
  })
}

// 查询手机号和邮箱
export function findByNodeId(params) {
  return request({
    url: '/webadmin/environmentAlarm/findByNodeId',
    method: 'get',
    params
  })
}
