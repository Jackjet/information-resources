
import request from '@/utils/request'

// 查询接入任务
export function taskFindAll(params) {
    return request({
        url: '/webadmin/task/findAll',
        method: 'get',
        params
    })
}

// 修改状态

export function updateStatus(data) {
    return request({
        url: '/webadmin/task/taskEnableOrForbidden', 
        method: 'PUT',
        data
    })
}

// 查询任务组
export function taskGroupFindAll(params) {
    return request({
        url: '/webadmin/task/getTaskGroup',
        method: 'get',
        params
    })
}
// 重命名任务组
export function taskGroupUpdate(data) {
    return request({
        url: '/webadmin/task/groupUpdate',
        method: 'PUT',
        data
    })
}

// 添加任务组
export function taskGroupInsert(data) {
    return request({
        url: '/webadmin/task/groupInsert',
        method: 'post',
        data
    })
}

// 删除任务组
export function taskGroupDelete(id) {
    return request({
        url: '/webadmin/task/groupDelete', 
        method: 'delete',
         params: { id }
    })
}

// 删除任务
export function taskDeleteById(id) {
    return request({
        url: '/webadmin/task/deleteById', 
        method: 'delete',
         params: { id }
    })
}

// 规则查询
export function qualityFindAll(params) {
    return request({
        url: '/webadmin/verifyRule/findAll',
        method: 'get',
        params
    })
}

// 添加任务
export function taskInsert(data) {
    return request({
        url: '/webadmin/task/insert',
        method: 'post',
        data
    })
}

// 查询日志
export function taskLogFindAll(params) {
    return request({
        url: '/webadmin/task/log/findAll',
        method: 'get',
        params
    })
}

// 查询明细
export function taskRuleLogFindAll(params) {
    return request({
        url: '/webadmin/task/rule/log/findAll',
        method: 'get',
        params
    })
}

// 查询看问题
export function findTaskRuleLogById(params) {
    return request({
        url: '/webadmin/task/rule/log/findById',
        method: 'get',
        params
    })
}

// 查询看问题
export function findworkOrderById(params) {
    return request({
        url: '/webadmin/workOrder/find',
        method: 'get',
        params
    })
}

// 查询处理人
export function findUser(params) {
    return request({
        url: '/webadmin/common/system/user/findAll',
        method: 'get',
        params
    })
}


// 查询处理人
export function insertOrder(data) {
    return request({
        url: '/webadmin/workOrder/insert',
        method: 'post',
        data
    })
}

// 执行一次
export function excuteOnce(params) {
    return request({
        url: '/webadmin/task/excuteOnce',
        method: 'get',
        params
    })
}

// 查询详情
export function taskFindById(params) {
    return request({
        url: '/webadmin/task/findById',
        method: 'get',
        params
    })
}

// 查询详情
export function taskUpdate(data) {
    return request({
        url: '/webadmin/task/update',
        method: 'put',
        data
    })
}








