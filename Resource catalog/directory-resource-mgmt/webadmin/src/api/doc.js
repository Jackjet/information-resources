import request from '@/utils/request'

export function docDownList(query) {
  return request({
    url: '/fileCenterRel/list2',
    method: 'get',
    params: query
  })
}
export function docPublishList(query) {
  return request({
    url: '/fileCenterRel/list1',
    method: 'get',
    params: query
  })
}

/* 详情*/
export function getUploadDetail(query) {
  return request({
    url: '/fileCenterRel/detail',
    method: 'get',
    params: query
  })
}
/* 获取次数*/
export function getCount(query) {
  return request({
    url: '/fileCenterRel/addCount',
    method: 'get',
    params: query
  })
}
/* 删除*/
export function delFile(data) {
  return request({
    url: '/fileCenterRel/removeBatch',
    method: 'post',
    data
  })
}
/* 保存*/
export function saveFile(data) {
  return request({
    url: '/fileCenterRel/save',
    method: 'post',
    data
  })
}
/* 编辑*/
export function updateFile(data) {
  return request({
    url: '/fileCenterRel/update',
    method: 'post',
    data
  })
}
