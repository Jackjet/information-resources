import request from '@/utils/request'

export function findOne(params) {
  return request({
    url: '/webadmin/baseApi/findOne',
    method: 'get',
    params
  })
}