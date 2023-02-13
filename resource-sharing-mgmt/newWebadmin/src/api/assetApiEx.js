import request from '@/utils/request'

// 列表
export function assetApiExFindAll(params) {
    return request({
        url: '/webadmin/assetApiEx/findAll',
        method: 'get',
        params
    })
}

// 列表
export function assetApiExFindAllApi(params) {
    return request({
        url: '/webadmin/assetApiEx/findAllApi',
        method: 'get',
        params
    })
}

// 详情
export function assetApiExFind(params) {
    return request({
        url: '/webadmin/assetApiEx/find',
        method: 'get',
        params
    })
}

// 挂接云数据api详情
export function assetApiExFindByApiId(params) {
    return request({
        url: '/webadmin/assetApiEx/findByApiId',
        method: 'get',
        params
    })
}

// 删除
export function assetApiExDelete(ids) {
    return request({
        url: '/webadmin/assetApiEx/deleteAll',
        method: 'delete',
        params: { ids }
    })
}

// 新增
export function assetApiExAdd(data) {
    return request({
        url: '/webadmin/assetApiEx/insertAll',
        method: 'post',
        data
    })
}

// 新增
export function findGroupTree(params) {
    return request({
        url: 'webadmin/assetApiEx/findGroupTree',
        method: 'get',
        params
    })
}

