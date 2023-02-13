import request from '@/utils/request'

// 获取模型列表
export function getList(params) {
  return request({
    url: '/webadmin/repository/knowledgeModel/findAll',
    method: 'get',
    params
  })
}

// 模型删除
export function deModel(id) {
  return request({
    url: '/webadmin/repository/knowledgeModel/delete',
    method: 'delete',
    params: { id }
  })
}

// 模型新增
export function insert(data) {
  return request({
    url: '/webadmin/repository/knowledgeModel/insert',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/webadmin/repository/knowledgeModel/update',
    method: 'put',
    data
  })
}

export function findById(params) {
  return request({
    url: '/webadmin/repository/knowledgeModel/findById',
    method: 'get',
    params
  })
}




