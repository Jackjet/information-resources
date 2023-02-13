import request from '@/utils/request'

// 列表
export function resourceUseInfoFindAll(params) {
    return request({
        url: '/webadmin/resourceUseInfo/findAll',
        method: 'get',
        params
    })
}

// 详情
export function resourceUseInfoFind(params) {
    return request({
        url: '/webadmin/resourceUseInfo/findById',
        method: 'get',
        params
    })
}

// 终审审核
export function resourceUseInfoUpdate(data) {
    return request({
        url: '/webadmin/resourceUseInfo/update',
        method: 'put',
        data
    })
}

// API详情
export function resourceUseInfoApiFind(params) {
    return request({
        url: '/webadmin/assetApiEx/find',
        method: 'get',
        params
    })
}

// 纠错列表
export function correctionFindAll(params) {
    return request({
        url: '/webadmin/correction/findAll',
        method: 'get',
        params
    })
}

// 详情
export function correctionFind(params) {
    return request({
        url: '/webadmin/correction/find',
        method: 'get',
        params
    })
}

// 纠错审核
export function correctionUpdate(data) {
    return request({
        url: '/webadmin/correction/update',
        method: 'put',
        data
    })
}

// 列表
export function findAllByProvOrgId(params) {
    return request({
        url: '/webadmin/resourceUseInfo/findAllByProvOrgId',
        method: 'get',
        params
    })
}

// 初审
export function resourceUseInfoAudit(data) {
    return request({
        url: '/webadmin/resourceUseInfo/audit',
        method: 'put',
        data
    })
}