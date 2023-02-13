import request from '@/utils/withoutIpRequest.js'
import requestUser from '@/utils/requestUser.js'

export function withoutIpRequest(baseURL,routUrl,method) {
    return request({
        baseURL: baseURL,
        url: routUrl,
        method: method
    })
}

export function getHttpsMethod(baseURL,routUrl,method,headers,params,body) {
    return request({
        baseURL: baseURL,
        url: routUrl,
        params:params,
        data:body,
        method: method,
        headers: headers
    })
}

export function downLoadFile(baseURL,routUrl,method,body) {
    return request({
        baseURL: baseURL,
        url: routUrl,
        method: method,
        data:body
    })
}


export function apiDesignList(baseURL,routUrl,method,params) {
    return request({
        baseURL: baseURL,
        url: routUrl,
        method: method,
        params:params
    })
}

export function updatePassword(data) {
    return requestUser({
        url: '/webadmin/system/webAdminUser/updatePassword',
        method: 'put',
        data
    })
}