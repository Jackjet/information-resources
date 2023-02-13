
import request from '@/utils/request'

// 规则查询
export function qualityFindAll(params) {
    return request({
        url: '/webadmin/verifyRule/findAll',
        method: 'get',
        params
    })
}

// 元数据实体库查询
export function datasourceFindAll(params) {
    return request({
        url: '/webadmin/common/datasource/findAll',
        method: 'get',
        params
    })
}

// 元数据实体表查询
export function dataSourceById(params) {
    return request({
        url: '/webadmin/common/dataUnit/findAllByDataSourceId',
        method: 'get',
        params
    })
}

// 元数据检查字段查询
export function dataSourceByUnitId(params) {
    return request({
        url: '/webadmin/common/dataUnitField/findAllByDataUnitId',
        method: 'get',
        params
    })
}

// 新增
export function verifyRuleInsert(data) {
    return request({
        url: '/webadmin/verifyRule/insert',
        method: 'POST',
        data
    })
}

// 修改规则状态

export function updateStatus(data) {
    return request({
        url: '/webadmin/verifyRule/updateStatus',
        method: 'PUT',
        data
    })
}


// 删除规则
export function verifyRuleDelete(id) {
    return request({
        url: '/webadmin/verifyRule/delete',
        method: 'delete',
        params: { id }
    })
}

// 修改规则状态
export function updateVerifyRule(data) {
    return request({
        url: '/webadmin/verifyRule/update',
        method: 'PUT',
        data
    })
}

// 变更历史数量
export function verifyRuleLogCountByRuleId(params) {
    return request({
        url: '/webadmin/verifyRuleLog/countByRuleId',
        method: 'get',
        params
    })
}

// 变更历史列表
export function verifyRuleLogFindByRuleId(params) {
    return request({
        url: '/webadmin/verifyRuleLog/findByRuleId',
        method: 'get',
        params
    })
}
// 模板信息接口
export function ruleTemplateFindOne(params) {
    return request({
        url: '/webadmin/ruleTemplate/findOne',
        method: 'get',
        params
    })
}










// 详情
export function operationLogFind(params) {
    return request({
        url: '/webadmin/log/operationLog/find',
        method: 'get',
        params
    })
}

// 导出
export function operationLogExport(params) {
    return request({
        url: '/webadmin/log/operationLog/export',
        method: 'get',
        params
    })
}

// 用户查询
export function userLogFindAll(params) {
    return request({
        url: '/webadmin/log/userLog/findAll',
        method: 'get',
        params
    })
}

// 详情
export function userLogFind(params) {
    return request({
        url: '/webadmin/log/userLog/find',
        method: 'get',
        params
    })
}

// 导出
export function userLogExport(params) {
    return request({
        url: '/webadmin/log/userLog/export',
        method: 'get',
        params
    })
}