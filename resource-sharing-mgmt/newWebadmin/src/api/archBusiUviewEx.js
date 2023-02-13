import request from '@/utils/request'

// 列表
export function archBusiUviewExFindAll(params) {
    return request({
        url: '/webadmin/archBusiUviewEx/findAll',
        method: 'get',
        params
    })
}

// 详情
export function archBusiUviewExFind(params) {
    return request({
        url: '/webadmin/archBusiUviewEx/find',
        method: 'get',
        params
    })
}

//下架
export function archBusiUviewExUpdate(data) {
    return request({
        url: '/webadmin/archBusiUviewEx/update',
        method: 'put',
        data
    })
}

//树结构导航
export function dictAssetCateFindAll(params) {
    return request({
        url: '/webadmin/dictAssetCate/findAll',
        method: 'get',
        params
    })
}

//同步资源目录信息
export function archBusiUviewExSyncData(data) {
    return request({
        url: '/webadmin/archBusiUviewEx/syncData',
        method: 'get',
        data
    })
}

// 列表
export function findResourceUseInfoByProvOrgId(params) {
    return request({
        url: '/webadmin/dataAnalysis/findResourceUseInfoByProvOrgId',
        method: 'get',
        params
    })
}

// 列表
export function resourceUseInfoFindAll(params) {
    return request({
        url: '/webadmin/resourceUseInfo/findAll',
        method: 'get',
        params
    })
}

// 字典
export function assetDictFindAll(params) {
    return request({
        url: '/webadmin/assetDict/findAll',
        method: 'get',
        params
    })
}


