import request from '@/utils/request'

// 列表
export function downloadInfoFindAll(params) {
    return request({
        url: '/webadmin/downloadInfo/findAll',
        method: 'get',
        params
    })
}

// 添加
export function downloadInfoInsert(data) {
    return request({
        url: '/webadmin/downloadInfo/insert',
        method: 'post',
        data
    })
}

// 删除
export function downloadInfoDelete(id) {
    return request({
        url: '/webadmin/downloadInfo/delete',
        method: 'delete',
        params: { id }
    })
}