import request from '@/utils/request'

export function findOne(params) {
  return request({
    url: '/webadmin/baseApi/findOne',
    method: 'get',
    params
  })
}
export function findTwo(params) {
  return request({
    url: '/webadmin/baseApi/findTwo',
    method: 'get',
    params
  })
}
export function findThree(params) {
  return request({
    url: '/webadmin/baseApi/findThree',
    method: 'get',
    params
  })
}