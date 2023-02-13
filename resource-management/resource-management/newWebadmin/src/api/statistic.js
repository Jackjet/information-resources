
import request from '@/utils/request'

export function statisticalByEnd(params) {
  return request({
    url: '/webadmin/statistical/statisticalByEnd',
    method: 'get',
    params
  })
}
export function statisticalByEndTime(params) {
  return request({
    url: '/webadmin/statistical/statisticalByEndTime',
    method: 'get',
    params
  })
}
export function statisticalByTimeQuantum(params) {
  return request({
    url: '/webadmin/statistical/statisticalByTimeQuantum',
    method: 'get',
    params
  })
}