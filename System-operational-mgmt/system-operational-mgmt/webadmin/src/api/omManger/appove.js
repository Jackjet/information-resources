import request from '@/utils/request'

// 审批列表
export function getAppoveList(params) {
  return request({
    url: '/webadmin/operations/approval/findAll',
    method: 'get',
    params
  })
}

// 驳回审批
export function updateAppove(data) {
  return request({
    url: '/webadmin/operations/approval/updateStatus',
    method: 'put',
    data
  })
}