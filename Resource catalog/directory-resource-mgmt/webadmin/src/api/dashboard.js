import request from '@/utils/request'

export function indexSystem(query) {
  return request({
    url: '/index/system',
    method: 'get',
    params: query
  })
}

export function indexDept(query) {
  return request({
    url: '/index/dept',
    method: 'get',
    params: query
  })
}
