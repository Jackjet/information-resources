/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-14 13:56:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

export function workOrderFindAll(params) {
    return request({
        url: '/webadmin/workOrder/findAll',
        method: 'get',
        params
    })
}

export function workOrderFindAllByMy(params) {
    return request({
        url: '/webadmin/workOrder/findAllByMy',
        method: 'get',
        params
    })
}

export function workOrderFind(params) {
    return request({
        url: '/webadmin/workOrder/find',
        method: 'get',
        params
    })
}

export function workOrderCount(params) {
    return request({
        url: '/webadmin/workOrder/count',
        method: 'get',
        params
    })
}

export function workOrderInsert(data) {
    return request({
        url: '/webadmin/workOrder/insert',
        method: 'post',
        data
    })
}

export function workOrderHandle(data) {
    return request({
        url: '/webadmin/workOrder/handle',
        method: 'put',
        data
    })
}


export function statisticsFind(params) {
    return request({
        url: '/webadmin/task/rule/log/statistics',
        method: 'get',
        params
    })
}

