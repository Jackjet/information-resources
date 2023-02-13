
import request from '@/utils/request'

// 实例管理
export function findAllNodes(params) {
  return request({
    url: '/webadmin/databaseOperation/instances/findAllNodes',
    method: 'get',
    params
  })
}

export function findAllDatabases(params) {
	return request({
		url: '/webadmin/databaseOperation/instances/findAllDatabases',
		method: 'get',
		params
	})
}


export function findAll(params) {
	return request({
		url: '/webadmin/databaseOperation/instances/findAll',
		method: 'get',
		params
	})
}

export function operationinsert(data) {
  return request({
    url: '/webadmin/databaseOperation/instances/insert',
    method: 'post',
    data
  })
}


export function findById(params) {
    return request({
      url: 'webadmin/databaseOperation/instances/findById',
      method: 'get',
      params
    })
}


export function deleteIns(params) {
    return request({
      url: '/webadmin/databaseOperation/instances/delete',
      method: 'delete',
      params
    })
}


export function update(data) {
	return request({
		url: '/webadmin/databaseOperation/instances/update',
		method: 'put',
		data
	})
}

// 参数管理

export function parameterConfigList(params) {
  return request({
    url: '/webadmin/databaseOperation/parameterConfig/findAll',
    method: 'get',
    params
  })
}


export function operationinsertInsert(data) {
  return request({
    url: '/webadmin/databaseOperation/parameterConfig/insert',
    method: 'post',
    data
  })
}


export function parameterConfigFindById(params) {
    return request({
      url: 'webadmin/databaseOperation/parameterConfig/findById',
      method: 'get',
      params
    })
}


export function parameterConfigDelete(params) {
    return request({
      url: '/webadmin/databaseOperation/parameterConfig/delete',
      method: 'delete',
      params
    })
}


export function parameterConfigUpdate(data) {
	return request({
		url: '/webadmin/databaseOperation/parameterConfig/update',
		method: 'put',
		data
	})
}



// 数据监控
export function databaseMonitoringList(params) {
  return request({
    url: '/webadmin/databaseOperation/databaseMonitoring/findAll',
    method: 'get',
    params
  })
}


export function databaseMonitoringInsert(data) {
  return request({
    url: '/webadmin/databaseOperation/databaseMonitoring/insert',
    method: 'post',
    data
  })
}


export function databaseMonitoringFindById(params) {
    return request({
      url: 'webadmin/databaseOperation/databaseMonitoring/findById',
      method: 'get',
      params
    })
}


export function databaseMonitoringDelete(params) {
    return request({
      url: '/webadmin/databaseOperation/databaseMonitoring/delete',
      method: 'delete',
      params
    })
}


export function databaseMonitoringUpdate(data) {
	return request({
		url: '/webadmin/databaseOperation/databaseMonitoring/update',
		method: 'put',
		data
	})
}

export function databaseMonitoringFindAllDatabases(params) {
	return request({
		url: '/webadmin/databaseOperation/databaseMonitoring/findAllDatabases',
		method: 'get',
		params
	})
}

export function databaseMonitoringFindAllNodes(params) {
	return request({
		url: '/webadmin/databaseOperation/databaseMonitoring/findAllNodes',
		method: 'get',
		params
	})
}

// 备份与恢复
export function databaseBackupList(params) {
  return request({
    url: '/webadmin/databaseOperation/databaseBackup/findAll',
    method: 'get',
    params
  })
}


export function databaseBackupInsert(data) {
  return request({
    url: '/webadmin/databaseOperation/databaseBackup/insert',
    method: 'post',
    data
  })
}


export function databaseBackupFindById(params) {
    return request({
      url: 'webadmin/databaseOperation/databaseBackup/findById',
      method: 'get',
      params
    })
}


export function databaseBackupDelete(params) {
    return request({
      url: '/webadmin/databaseOperation/databaseBackup/delete',
      method: 'delete',
      params
    })
}


export function databaseBackupUpdate(data) {
	return request({
		url: '/webadmin/databaseOperation/databaseBackup/rollback',
		method: 'post',
		data
	})
}

export function databaseBackupFindAllDatabases(params) {
	return request({
		url: '/webadmin/databaseOperation/databaseBackup/findAllDatabases',
		method: 'get',
		params
	})
}

export function databaseBackupFindAllNodes(params) {
	return request({
		url: '/webadmin/databaseOperation/databaseBackup/findAllNodes',
		method: 'get',
		params
	})
}

export function databaseBackupFindAllInstances(params) {
	return request({
		url: '/webadmin/databaseOperation/databaseBackup/findAllInstances',
		method: 'get',
		params
	})
}

export function getFindAllNodes(params) {
	return request({
		url: '/webadmin/databaseOperation/remoteExecution/findAllNodes',
		method: 'get',
		params
	})
}

export function getFindAllDatabases(params) {
	return request({
		url: '/webadmin/databaseOperation/remoteExecution/findAllDatabases',
		method: 'get',
		params
	})
}

export function sqlPost(data) {
	return request({
		url: '/webadmin/databaseOperation/remoteExecution/sql',
		method: 'post',
		data
	})
}


