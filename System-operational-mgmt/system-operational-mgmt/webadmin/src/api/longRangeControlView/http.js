import request from '@/utils/request'

// 运营指令分类
export function commandTypeFindAll(params) {
  return request({
    url: '/webadmin/remoteControl/commandType/findAll',
    method: 'get',
    params
  })
}

export function commandTypeInsert(data) {
  return request({
    url: '/webadmin/remoteControl/commandType/insert',
    method: 'post',
    data
  })
}

export function commandTypeDelete(params) {
  return request({
    url: '/webadmin/remoteControl/commandType/delete',
    method: 'delete',
    params
  })
}

export function commandTypeFindById(params) {
  return request({
    url: '/webadmin/remoteControl/commandType/findById',
    method: 'get',
    params
  })
}

export function commandTypeUpdate(data) {
  return request({
    url: '/webadmin/remoteControl/commandType/update',
    method: 'put',
    data
  })
}

export function commandTypeFindRoles(params) {
  return request({
    url: '/webadmin/remoteControl/commandType/findRoles',
    method: 'get',
    params
  })
}

// 日常运维指令
export function commandsFindAll(params) {
  return request({
    url: '/webadmin/remoteControl/commands/findAll',
    method: 'get',
    params
  })
}

export function commandsInsert(data) {
  return request({
    url: '/webadmin/remoteControl/commands/insert',
    method: 'post',
    data
  })
}

export function commandsDelete(params) {
  return request({
    url: '/webadmin/remoteControl/commands/delete',
    method: 'delete',
    params
  })
}

export function commandsFindById(params) {
  return request({
    url: '/webadmin/remoteControl/commands/findById',
    method: 'get',
    params
  })
}

export function commandsUpdate(data) {
  return request({
    url: '/webadmin/remoteControl/commands/update',
    method: 'put',
    data
  })
}

export function commandsFindCommandTypes(params) {
  return request({
    url: '/webadmin/remoteControl/commands/findCommandTypes',
    method: 'get',
    params
  })
}

// 远程控制台
export function getFindAllNodes(params) {
	return request({
		url: '/webadmin/databaseOperation/remoteExecution/findAllNodes',
		method: 'get',
		params
	})
}

export function getFindCommands(params) {
	return request({
		url: '/webadmin/remoteControl/remoteConsole/findCommands',
		method: 'get',
		params
	})
}

export function getCheckPermissions(params) {
	return request({
		url: '/webadmin/remoteControl/remoteConsole/checkPermissions',
		method: 'get',
		params
	})
}

export function getExecute(data) {
	return request({
		url: '/webadmin/remoteControl/remoteConsole/execute',
		method: 'post',
		data
	})
}
