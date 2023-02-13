import request from '@/utils/request';

// 申请记录
export function assetExFindAll(params) {
    return request({
        url: '/webadmin/assetEx/findAll',
        method: 'get',
        params
    })
}

// 申请记录审核列表
export function assetExFindAllByExamine(params) {
    return request({
        url: '/webadmin/assetEx/findAllByExamine',
        method: 'get',
        params
    })
}

// 新建申请
export function assetExInsert(data) {
    return request({
        url: '/webadmin/assetEx/insert',
        method: 'post',
        data
    })
}

// 当前委办局分类
export function organizationFindAllById(params) {
    return request({
        url: '/webadmin/system/organization/findAllById',
        method: 'get',
        params
    })
}

// 申请提交
export function assetExSubmit(data) {
    return request({
        url: '/webadmin/assetEx/submit',
        method: 'put',
        data
    })
}

// 终审
export function assetExExamine(data) {
    return request({
        url: '/webadmin/assetEx/examine',
        method: 'put',
        data
    })
}

// 详情
export function assetExFind(params) {
    return request({
        url: '/webadmin/assetEx/find',
        method: 'get',
        params
    })
}

// 日志
export function assetExLogFindAll(params) {
    return request({
        url: '/webadmin/assetExLog/findAll',
        method: 'get',
        params
    })
}

// 云文件列表
export function assetFileExFindAll(params) {
    return request({
        url: '/webadmin/assetFileEx/findAll',
        method: 'get',
        params
    })
}

// 删除文件
export function assetFileExDelete(id) {
    return request({
        url: '/webadmin/assetFileEx/delete',
        method: 'delete',
        params: { id }
    })
}

// 云数据列表
export function assetDataExFindByUviewId(params) {
    return request({
        url: '/webadmin/assetDataEx/findByUviewId',
        method: 'get',
        params
    })
}

// 提交审核
export function assetExInsertAndSubmit(data) {
    return request({
        url: '/webadmin/assetEx/insertAndSubmit',
        method: 'post',
        data
    })
}

// 
export function updateUpdate(data) {
    return request({
        url: '/webadmin/assetEx/update',
        method: 'put',
        data
    })
}

// 编辑提交审核
export function updateUpdateAndSubmit(data) {
    return request({
        url: '/webadmin/assetEx/updateAndSubmit',
        method: 'put',
        data
    })
}
