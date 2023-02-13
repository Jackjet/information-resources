import request from '@/utils/request'

// 用户登录信息
export function getLoginInfoList(params) {
  return request({
    url: '/webadmin/loginInfo/findAll',
    method: 'get',
    params
  })
}

// 用户活跃度
export function getActivity(params) {
  return request({
    url: '/webadmin/loginInfo/activity',
    method: 'get',
    params
  })
}

// 用户访问趋势图
export function accessTrend(params) {
    return request({
      url: '/webadmin/loginInfo/accessTrend',
      method: 'get',
      params
    })
}

// 在线用户监控
export function online(params) {
    return request({
      url: '/webadmin/loginInfo/online',
      method: 'get',
      params
    })
}

