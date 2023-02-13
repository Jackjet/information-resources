/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-14 13:56:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 操作查询
export function ruleTemplateFindAll(params) {
    return request({
        url: '/webadmin/ruleTemplate/findAll',
        method: 'get',
        params
    })
}

export function ruleTemplateInsert(data) {
    return request({
        url: '/webadmin/ruleTemplate/insert',
        method: 'post',
        data
    })
}

export function ruleTemplateUpdate(data) {
    return request({
        url: '/webadmin/ruleTemplate/update',
        method: 'put',
        data
    })
}

export function ruleTemplateUpdateStatus(data) {
    return request({
        url: '/webadmin/ruleTemplate/updateStatus',
        method: 'put',
        data
    })
}


export function ruleTemplateDelete(id) {
    return request({
        url: '/webadmin/ruleTemplate/delete',
        method: 'delete',
        params: {id}
    })
}

