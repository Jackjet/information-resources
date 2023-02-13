
import request from '@/utils/request'

// 规则查询
export function qualityReportFindAll(params) {
    return request({
        url: '/webadmin/dataQualityReport/findAll',
        method: 'get',
        params
    })
}

// 新增
export function qualityReportInsert(data) {
    return request({
        url: '/webadmin/dataQualityReport/insert', 
        method: 'POST',
        data
    })
}

// 删除
export function qualityReportDelete(id) {
    return request({
        url: '/webadmin/dataQualityReport/delete', 
        method: 'delete',
         params: { id }
    })
}

// id规则查询
export function qualityReportFind(params) {
    return request({
        url: '/webadmin/dataQualityReport/find',
        method: 'get',
        params
    })
}

// id规则查询
export function statisticsFind(params) {
    return request({
        url: '/webadmin/dataQualityReport/statistics',
        method: 'get',
        params
    })
}


// 更新
export function statisticsUpdate(data) {
    return request({
        url: '/webadmin/dataQualityReport/update',
        method: 'put',
        data
    })
}
