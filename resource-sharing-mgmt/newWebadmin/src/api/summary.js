
import request from '@/utils/request'

// 需求汇总
export function getDemandCountAnalysis(params) {
    return request({
        url: '/webadmin/demandedAnalysis/getDemandCountAnalysis',
        method: 'get',
        params
    })
}
export function getDemandByReason(params) {
    return request({
        url: '/webadmin/demandedAnalysis/getDemandByReason',
        method: 'get',
        params
    })
}
export function getDemandByRequestType(params) {
    return request({
        url: '/webadmin/demandedAnalysis/getDemandByRequestType',
        method: 'get',
        params
    })
}
export function getDemandByPassRate(params) {
    return request({
        url: '/webadmin/demandedAnalysis/getDemandByPassRate',
        method: 'get',
        params
    })
}




// 需求统计
export function findDemandedInfoCountByProvOrgId(params) {
    return request({
        url: '/webadmin/demandedInfo/findDemandedInfoCountByProvOrgId',
        method: 'get',
        params
    })
}

export function exportDemandedInfoCountByProvOrgId(params) {
    return request({
        url: '/webadmin/demandedInfo/exportDemandedInfoCountByProvOrgId',
        method: 'get',
        params
    })
}




// 资源使用汇总
export function getResourceUseCountAnalysis(params) {
    return request({
        url: '/webadmin/resourceUseAnalysis/getResourceUseCountAnalysis',
        method: 'get',
        params
    })
}
export function getResourceUseCountByType(params) {
    return request({
        url: '/webadmin/resourceUseAnalysis/getResourceUseCountByType',
        method: 'get',
        params
    })
}
export function getResourceUseByMonth(params) {
    return request({
        url: '/webadmin/resourceUseAnalysis/getResourceUseByMonth',
        method: 'get',
        params
    })
}
export function getResourceUseByPassRate(params) {
    return request({
        url: '/webadmin/resourceUseAnalysis/getResourceUseByPassRate',
        method: 'get',
        params
    })
}




// 资源使用日志
export function resourceUseLogFindAll(params) {
    return request({
        url: '/webadmin/resourceUseLog/findAll',
        method: 'get',
        params
    })
}
export function exportByProvOrgId(params) {
    return request({
        url: '/webadmin/resourceUseLog/exportByProvOrgId',
        method: 'get',
        params
    })
}