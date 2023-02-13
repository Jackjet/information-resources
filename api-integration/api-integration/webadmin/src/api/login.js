import request from '@/utils/request'

export function getLoginUser(params) {
    return request({
        url: '/webadmin/system/webAdminUser/getLoginUser',
        method: 'get',
        params
    })
}
export function userLogin(params) {
    return request({
        url: '/webadmin/system/webAdminUser/login',
        method: 'get',
        params
    })
}
export function clearSsoCookie(params) {
    return request({
        url: '/webadmin/system/webAdminUser/clearSsoCookie',
        method: 'get',
        params
    })
}