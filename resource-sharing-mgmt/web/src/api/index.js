import { get, post, DELETE, put } from '../utils/request.js';
// 在组件中可以直接 this.fun(传递参数).then()调用

const install = function (Vue) {
    // 用户信息
    Vue.prototype.webAdminUserFind = function (body) {
        return get('/webadmin/system/webAdminUser/find', body)
    },
        // 退出
        Vue.prototype.webAdminSignOut = function (body) {
            return get('/webadmin/system/webAdminUser/signOut', body)
        },
        // 修改密码
        Vue.prototype.updatePassword = function (body) {
            return put('/webadmin/system/webAdminUser/updatePassword', body)
        },
        // 获取APIkey
        Vue.prototype.findKeyByUserId = function (body) {
            return get('/web/webUser/findKeyByUserId', body)
        },
        // 修改资料
        Vue.prototype.webAdminUserUpdate = function (body) {
            return put('/webadmin/system/webAdminUser/update', body)
        },
        // 数据汇聚
        Vue.prototype.dataAggregationFind = function (body) {
            return get('/web/dataAggregation/find', body)
        },
        // 信息资源目录分页查询
        Vue.prototype.archBusiUviewExFindAll = function (body) {
            return get('/web/archBusiUviewEx/findAll', body)
        },
        Vue.prototype.archBusiUviewExFindVisits = function (body) {
            return get('/web/archBusiUviewEx/findVisits', body)
        },
        // 查询资源目录分类树
        Vue.prototype.dictAssetCateFindAll = function (body) {
            return get('/web/dictAssetCate/findAll', body)
        },
        // 根据父节点ID查询所有分类目录
        Vue.prototype.dictAssetCateFindAllByParTypId = function (body) {
            return get('/web/dictAssetCate/findAllByParTypId', body)
        },
        // 信息资源目录详情
        Vue.prototype.archBusiUviewExFind = function (body) {
            return get('/web/archBusiUviewEx/find', body)
        },
        // 添加收藏关注
        Vue.prototype.myFocusInfoInsert = function (body) {
            return post('/web/myFocusInfo/insert', body)
        },
        // 我关注的目录
        Vue.prototype.myFocusInfoFindAll = function (body) {
            return get('/web/myFocusInfo/findAll', body)
        },
        // 取消关注
        Vue.prototype.myFocusInfoDelete = function (body) {
            return DELETE('/web/myFocusInfo/delete?uviewId=' + body)
        },
        // 资源目录被收藏次数
        Vue.prototype.myFocusInfoCountByUviewId = function (body) {
            return get('/web/myFocusInfo/countByUviewId', body)
        },
        // 关注详情
        Vue.prototype.myFocusInfoFindById = function (body) {
            return get('/web/myFocusInfo/findById', body)
        },
        // 需求申请
        Vue.prototype.demandedInfoInsert = function (body) {
            return post('/web/demandedInfo/insert', body)
        },
        // 需求详情
        Vue.prototype.demandedInfoFindById = function (body) {
            return get('/web/demandedInfo/findById', body)
        },
        // 我的需求
        Vue.prototype.demandedInfoFindAll = function (body) {
            return get('/web/demandedInfo/findAll', body)
        },
        // 需求删除
        Vue.prototype.demandedInfoDelete = function (body) {
            return DELETE('/web/demandedInfo/delete?id=' + body)
        },
        // 信息项分页查询
        Vue.prototype.archBusiUviewStrExFindAll = function (body) {
            return get('/web/archBusiUviewStrEx/findAll', body)
        },
        // 信息项详情
        Vue.prototype.archBusiUviewStrExFind = function (body) {
            return get('/web/archBusiUviewStrEx/find', body)
        },
        // 云接口分页查询
        Vue.prototype.assetApiExFindAll = function (body) {
            return get('/web/assetApiEx/findAll', body)
        },
        // 云文件分页查询
        Vue.prototype.assetFileExFindAll = function (body) {
            return get('/web/assetFileEx/findAll', body)
        },
        //云数据查询
        Vue.prototype.assetDataExFindAll = function (body) {
            return get('/web/assetDataEx/findAll', body)
        },
        // 云接口详情
        Vue.prototype.assetApiExFind = function (body) {
            return get('/web/assetApiEx/find', body)
        },
        // 云数据详情
        Vue.prototype.assetDataExFind = function (body) {
            return get('/web/assetDataEx/find', body)
        },
        // 云文件详情
        Vue.prototype.assetFileExFind = function (body) {
            return get('/web/assetFileEx/find', body)
        },
        // 资源使用申请(云接口,云文件,云数据)
        Vue.prototype.resourceUseInfoInsert = function (body) {
            return post('/web/resourceUseInfo/insert', body)
        },
        // 资源使用详情
        Vue.prototype.resourceUseInfoFindById = function (body) {
            return get('/web/resourceUseInfo/findById', body)
        },
        // 全部组织机构树
        Vue.prototype.organizationFindAll = function (body) {
            return get('/webadmin/system/organization/findAll', body)
        },
        // 下载中心列表
        Vue.prototype.downloadInfoFindAll = function (body) {
            return get('/web/downloadInfo/findAll', body)
        },
        // 下载
        Vue.prototype.downloadInfoDownload = function (body) {
            return get('web/downloadInfo/download', body)
        },
        // 我的云数据
        Vue.prototype.resourceUseInfoFindAll = function (body) {
            return get('/web/resourceUseInfo/findAll', body)
        },
        // 删除我的云数据
        Vue.prototype.resourceUseInfoDelete = function (body) {
            return DELETE('/web/resourceUseInfo/delete?id=' + body)
        },
        // 我的待办-需求申请
        Vue.prototype.demandedInfoFindAnalysis = function (body) {
            return get('/web/demandedInfo/findAnalysis', body)
        },
        // 我的待办-需求使用申请
        Vue.prototype.resourceUseInfoFindAnalysis = function (body) {
            return get('/web/resourceUseInfo/findAnalysis', body)
        },
        // 纠错
        Vue.prototype.correctionInsert = function (body) {
            return post('/web/correction/insert', body)
        },
        // 门户获取当前用户代办
        Vue.prototype.chargeFindAll = function (body) {
            return get('/web/charge/findAll', body)
        },
        // 门户获取当前用户服务指引
        Vue.prototype.guideFindAll = function (body) {
            return get('/web/guide/findAll', body)
        },
        // 当前登录用户
        Vue.prototype.getLoginUser = function (body) {
            return get('/web/system/webUser/getLoginUser', body)
        },
        // 登录
        Vue.prototype.isLoginUser = function (body) {
            return get('/web/system/webUser/login', body)
        }
    // 清空cookies
    Vue.prototype.clearSsoCookie = function (body) {
        return get('/web/system/webUser/clearSsoCookie', body)
    }
}

export default install;