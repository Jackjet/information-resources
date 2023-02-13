import request from '@/utils/request'

// 脱敏规则查询列表
export function replaceRuleFindAll(params) {
    return request({
        url: '/webadmin/replaceRule/findAll',
        method: 'get',
        params
    })
}

// 脱敏规则详情
export function replaceRuleFind(params) {
    return request({
        url: '/webadmin/replaceRule/find',
        method: 'get',
        params
    })
}

// 脱敏规则新增
export function replaceRuleInsert(data) {
    return request({
        url: '/webadmin/replaceRule/insert',
        method: 'post',
        data
    })
}

// 脱敏规则删除
export function replaceRuleDelete(id) {
    return request({
        url: '/webadmin/replaceRule/delete',
        method: 'delete',
        params: { id }
    })
}

// 脱敏规则编辑
export function replaceRuleUpdate(data) {
    return request({
        url: '/webadmin/replaceRule/update',
        method: 'put',
        data
    })
}
