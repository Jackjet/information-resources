import request from '@/utils/request'
import requestUser from '@/utils/requestUser'

// 组查询
export function getDataSourceGroup(params) {
    return request({
        url: '/webadmin/dataSourceInfo/getDataSourceGroup',
        method: 'get',
        params
    })
}

// 添加组
export function groupInsert(data) {
    return request({
        url: '/webadmin/dataSourceInfo/groupInsert',
        method: 'post',
        data
    })
}

// 修改组
export function groupUpdate(data) {
    return request({
        url: '/webadmin/dataSourceInfo/groupUpdate',
        method: 'put',
        data
    })
}

// 组删除
export function groupDelete(params) {
    return request({
        url: '/webadmin/dataSourceInfo/groupDelete',
        method: 'delete',
        params
    })
}

// 数据源查询
export function getDataSource(params) {
    return request({
        url: '/webadmin/dataSourceInfo/findAll',
        method: 'get',
        params
    })
}


// 数据源查询
export function getDataSourceGroupList(params) {
    return request({
        url: '/webadmin/dataSourceInfo/getDataSourceGroupList',
        method: 'get',
        params
    })
}

// 数据源详情查询
export function getDataSourceById(params) {
    return request({
        url: '/webadmin/dataSourceInfo/getDataSourceById',
        method: 'get',
        params
    })
}


// 新增数据源
export function dataSourceInsert(data) {
    return request({
        url: '/webadmin/dataSourceInfo/dataSourceInsert',
        method: 'post',
        data
    })
}

// 修改数据源
export function dataSourceUpdate(data) {
    return request({
        url: '/webadmin/dataSourceInfo/dataSourceUpdate',
        method: 'put',
        data
    })
}

// 删除数据源
export function deleteDataSourceById(params) {
    return request({
        url: '/webadmin/dataSourceInfo/deleteDataSourceById',
        method: 'delete',
        params
    })
}

// 启用或禁用数据源
export function dataSourceEnableOrForbidden(data) {
    return request({
        url: '/webadmin/dataSourceInfo/dataSourceEnableOrForbidden',
        method: 'put',
        data
    })
}

// 组织查询
export function getOrganizationOptions(params) {
    return request({
        url: '/webadmin/dataSourceInfo/getOrganizationOptions',
        method: 'get',
        params
    })
}

// 组织查询
// export function getOrganizationOptions(params) {
//     return requestUser({
//         url: '/webadmin/system/organization/getOrganizationOptions',
//         method: 'get',
//         params
//     })
// }

// 元数据查询
export function getDataUnit(params) {
    return request({
        url: '/webadmin/dataSourceInfo/getDataUnit',
        method: 'get',
        params
    })
}

// 更新元数据
export function updateDataUnit(data) {
    return request({
        url: '/webadmin/dataSourceInfo/updateDataUnit',
        method: 'put',
        data
    })
}

// 元数据查询
export function getDataUnitById(params) {
    return request({
        url: '/webadmin/dataSourceInfo/getDataUnitById',
        method: 'get',
        params
    })
}

// 更新字段
export function updateDataUnitFields(data) {
    return request({
        url: '/webadmin/dataSourceInfo/updateDataUnitFields',
        method: 'put',
        data
    })
}

// 字段查询
export function getDataUnitFields(params) {
    return request({
        url: '/webadmin/dataSourceInfo/getDataUnitFields',
        method: 'get',
        params
    })
}

// 更新字段脱敏
export function updateDataUnitFieldReplace(data) {
    return request({
        url: '/webadmin/dataSourceInfo/updateFieldReplace',
        method: 'put',
        data
    })
}

// 数据库连接测试
export function connectTest(params) {
    return request({
        url: '/webadmin/dataSourceInfo/connectTest',
        method: 'get',
        params
    })
}

// 获取脱敏规则列表
export function getReplaceRuleList(params) {
    return request({
        url: '/common/replaceRule/findAllList',
        method: 'get',
        params
    })
}
