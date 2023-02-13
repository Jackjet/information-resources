import request from '@/utils/request'

// 按照部门统计挂接资源目录
export function findArchBusiUviewExByProvOrgId(params) {
    return request({
        url: '/webadmin/dataAnalysis/findArchBusiUviewExByProvOrgId',
        method: 'get',
        params
    })
}

// 资源统计
export function findgetArchBusiUviewExDataAnalysis(params) {
    return request({
        url: '/webadmin/dataAnalysis/findgetArchBusiUviewExDataAnalysis',
        method: 'get',
        params
    })
}

// 需求统计
export function findDemandInfo(params) {
    return request({
        url: '/webadmin/dataAnalysis/findDemandInfo',
        method: 'get',
        params
    })
}

// 按照时间统计资源使用
export function findgetResourceUseInfoByTime(params) {
    return request({
        url: '/webadmin/dataAnalysis/findgetResourceUseInfoByTime',
        method: 'get',
        params
    })
}

// 资源使用统计
export function findgetResourceUseInfo(params) {
    return request({
        url: '/webadmin/dataAnalysis/findgetResourceUseInfo',
        method: 'get',
        params
    })
}