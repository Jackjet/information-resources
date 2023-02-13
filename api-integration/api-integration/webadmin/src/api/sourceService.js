/*
 * @Author: your name
 * @Date: 2020-09-14 16:36:54
 * @LastEditTime: 2020-09-16 16:50:08
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\message.js
 */
import request from '@/utils/sourceServiceRequest'
// 标签
export function findAllMeta(params) {
    return request({
        url: '/webadmin/tagInfo/findAll',
        method: 'get',
        params
    })
}


// 查询容器
export function findAllContainer(params) {
    return request({
        url: '/webadmin/containerInfo/findAll',
        method: 'get',
        params
    })
}

// 查询API
export function findAllApi(params) {
    return request({
        url: '/webadmin/sourceApi/findAll',
        method: 'get',
        params
    })
}


// 查询API详情
export function findApiDetail(params) {
    return request({
        url: '/webadmin/sourceApi/findById',
        method: 'get',
        params
    })
}
