import request from '@/utils/request'

// 服务指引查询列表
export function guideFindAll(params) {
    return request({
        url: '/webadmin/guide/findAll',
        method: 'get',
        params
    })
}

// 服务指引新增
export function guideInsert(data) {
    return request({
        url: '/webadmin/guide/insert',
        method: 'post',
        data
    })
}

// 服务指引删除
export function guideDelete(id) {
    return request({
        url: '/webadmin/guide/delete',
        method: 'delete',
        params: { id }
    })
}

// 服务指引上移
export function guideUp(params) {
    return request({
        url: '/webadmin/guide/up',
        method: 'get',
        params
    })
}

// 服务指引下移
export function guideDown(params) {
    return request({
        url: '/webadmin/guide/down',
        method: 'get',
        params
    })
}

// 服务指引编辑
export function guideUpdate(data) {
    return request({
        url: '/webadmin/guide/update',
        method: 'put',
        data
    })
}

// 保存服务指引列表--关联角色保存
export function guideRoleInsert(data) {
    return request({
        url: '/webadmin/guide/guideRoleInsert',
        method: 'post',
        data
    })
}

// 获取服务指引列表--通过角色Id标记已选列表
export function guideRoleGet(params) {
    return request({
        url: '/webadmin/guide/guideRoleGet',
        method: 'get',
        params
    })
}

// 获取代办列表--通过角色Id标记已选列表
export function wayChargeRoleGet(params) {
    return request({
        url: '/webadmin/charge/chargeRoleGet',
        method: 'get',
        params
    })
}

// 保存代办列表--关联角色保存
export function wayChargeRoleInsert(data) {
    return request({
        url: '/webadmin/charge/chargeRoleInsert',
        method: 'post',
        data
    })
}