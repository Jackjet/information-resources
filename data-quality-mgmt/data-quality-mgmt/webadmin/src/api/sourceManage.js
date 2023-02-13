import request from '@/utils/request'

// 组查询
export function getDataSourceGroup(params) {
    return request({
        url: '/webadmin/dataSourceManage/getDataSourceGroup',
        method: 'get',
        params
    })
}

// 添加组
export function groupInsert(data) {
    return request({
        url: '/webadmin/dataSourceManage/groupInsert',
        method: 'post',
        data
    })
}

// 修改组
export function groupUpdate(data) {
    return request({
        url: '/webadmin/dataSourceManage/groupUpdate',
        method: 'put',
        data
    })
}

// 组删除
export function groupDelete(params) {
    return request({
        url: '/webadmin/dataSourceManage/groupDelete',
        method: 'delete',
        params
    })
}

// 数据源查询
export function getDataSource(params) {
    return request({
        url: '/webadmin/dataSourceManage/getDataSource',
        method: 'get',
        params
    })
}


// 数据源查询
export function getDataSourceGroupList(params) {
    return request({
        url: '/webadmin/dataSourceManage/getDataSourceGroupList',
        method: 'get',
        params
    })
}

// 数据源详情查询
export function getDataSourceById(params) {
    return request({
        url: '/webadmin/dataSourceManage/getDataSourceById',
        method: 'get',
        params
    })
}


// 新增数据源
export function dataSourceInsert(data) {
    return request({
        url: '/webadmin/dataSourceManage/dataSourceInsert',
        method: 'post',
        data
    })
}

// 修改数据源
export function dataSourceUpdate(data) {
    return request({
        url: '/webadmin/dataSourceManage/dataSourceUpdate',
        method: 'put',
        data
    })
}

// 删除数据源
export function deleteDataSourceById(params) {
    return request({
        url: '/webadmin/dataSourceManage/deleteDataSourceById',
        method: 'delete',
        params
    })
}

// 启用或禁用数据源
export function dataSourceEnableOrForbidden(data) {
    return request({
        url: '/webadmin/dataSourceManage/dataSourceEnableOrForbidden',
        method: 'put',
        data
    })
}

// 组织查询
export function getOrganizationOptions(params) {
    return request({
        url: '/webadmin/dataSourceManage/getOrganizationOptions',
        method: 'get',
        params
    })
}

// 元数据查询
export function getDataUnit(params) {
    return request({
        url: '/webadmin/dataSourceManage/getDataUnit',
        method: 'get',
        params
    })
}

// 更新元数据
export function updateDataUnit(data) {
    return request({
        url: '/webadmin/dataSourceManage/updateDataUnit',
        method: 'put',
        data
    })
}

// 元数据查询
export function getDataUnitById(params) {
    return request({
        url: '/webadmin/dataSourceManage/getDataUnitById',
        method: 'get',
        params
    })
}

// 更新字段
export function updateDataUnitFields(data) {
    return request({
        url: '/webadmin/dataSourceManage/updateDataUnitFields',
        method: 'put',
        data
    })
}

// 字段查询
export function getDataUnitFields(params) {
    return request({
        url: '/webadmin/dataSourceManage/getDataUnitFields',
        method: 'get',
        params
    })
}

// 数据库连接测试
export function connectTest(params) {
    return request({
        url: '/webadmin/dataSourceManage/connectTest',
        method: 'get',
        params
    })
}
