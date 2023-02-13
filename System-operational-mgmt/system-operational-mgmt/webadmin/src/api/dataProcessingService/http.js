import request from '@/utils/request'

// 数据接口服务管理
export function dataApiList(params) {
  return request({
    url: '/webadmin/dataApi/findAll',
    method: 'get',
    params
  })
}

export function dataApiInsert(data) {
  return request({
    url: '/webadmin/dataApi/insert',
    method: 'post',
    data
  })
}

export function dataApiDelete(data) {
  return request({
    url: '/webadmin/dataApi/delete',
    method: 'delete',
    data
  })
}

export function dataApiFindById(params) {
  return request({
    url: '/webadmin/dataApi/findById',
    method: 'get',
    params
  })
}

export function dataApiUpdate(data) {
  return request({
    url: '/webadmin/dataApi/update',
    method: 'put',
    data
  })
}

export function dataApiTest(data) {
  return request({
    url: '/webadmin/dataApi/testInterface',
    method: 'post',
    data
  })
}

// 数据服务接口计划任务
export function dataApiPlanList(params) {
  return request({
    url: '/webadmin/dataApiPlan/findAll',
    method: 'get',
    params
  })
}

export function dataApiPlanInsert(data) {
  return request({
    url: '/webadmin/dataApiPlan/insert',
    method: 'post',
    data
  })
}

export function dataApiPlanDelete(data) {
  return request({
    url: '/webadmin/dataApiPlan/delete',
    method: 'delete',
    data
  })
}

export function dataApiPlanFindById(params) {
  return request({
    url: '/webadmin/dataApiPlan/findById',
    method: 'get',
    params
  })
}

export function dataApiPlanUpdate(data) {
  return request({
    url: '/webadmin/dataApiPlan/update',
    method: 'PUT',
    data
  })
}

export function dataApiPlanStart(data) {
  return request({
    url: '/webadmin/dataApiPlan/start',
    method: 'put',
    data
  })
}

export function dataApiPlanStop(data) {
  return request({
    url: '/webadmin/dataApiPlan/stop',
    method: 'put',
    data
  })
}