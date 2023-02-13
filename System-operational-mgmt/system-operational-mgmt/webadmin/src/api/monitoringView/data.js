import request from '@/utils/request'

// 节点状态分析
export function nodeStatusAnalysis(params) {
  return request({
    url: '/webadmin/dataMonitor/nodeStatusAnalysis',
    method: 'get',
    params
  })
}

// 系统状态分析
export function systemStatusAnalysis(params) {
    return request({
      url: '/webadmin/dataMonitor/systemStatusAnalysis',
      method: 'get',
      params
    })
}

// 资源监控列表
export function getResourceMonitoring(params) {
  return request({
    url: '/webadmin/resourcesMonitor/resourceMonitoring',
    method: 'get',
    params
  })
}