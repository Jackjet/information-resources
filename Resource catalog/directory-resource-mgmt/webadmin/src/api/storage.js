import request from '@/utils/request'

export function listStorage(query) {
  return request({
    url: '/storage/list',
    method: 'get',
    params: query
  })
}

export function createStorage(data) {
  return request({
    url: '/storage/create',
    method: 'post',
    data
  })
}

export function readStorage(query) {
  return request({
    url: '/storage/read',
    method: 'get',
    params: query
  })
}

export function updateStorage(data) {
  return request({
    url: '/storage/update',
    method: 'post',
    data
  })
}

export function deleteStorage(data) {
  return request({
    url: '/storage/delete',
    method: 'post',
    data
  })
}
export function getFetchUrl(url, query) {
  return request({
    url: url,
    method: 'get',
    params: query
  })
}
const uploadPath = location.protocol + '//' + window.location.host + '/admin' + '/storage/create'
export { uploadPath }
