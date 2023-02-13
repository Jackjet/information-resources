import request from '@/utils/request'

// 列表
export function demandedInfoFindAll(params) {
  return request({
    url: '/webadmin/demandedInfo/findAll',
    method: 'get',
    params
  })
}

// 详情
export function demandedInfoFind(params) {
  return request({
    url: '/webadmin/demandedInfo/find',
    method: 'get',
    params
  })
}

// 受理列表
export function demandedInfoFindAllByAcceptDeptId(params) {
    return request({
        url: '/webadmin/demandedInfo/findAllByAcceptDeptId',
        method: 'get',
        params
    })
}

// 审核
export function demandedInfoUpdate(data) {
  return request({
    url: '/webadmin/demandedInfo/update',
    method: 'put',
    data
  })
}
