import request from '@/utils/request'


// 详情
export function archBusiUviewStrExFind(params) {
    return request({
        url: '/webadmin/archBusiUviewStrEx/findAll',
        method: 'get',
        params
    })
}