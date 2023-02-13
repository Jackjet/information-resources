import Vue from 'vue'
import VueRouter from 'vue-router'
import bus from '@/utils/bus'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        component: () => import('@/views/login/index'),
        hidden: true
    },
    {
        path: '/404',
        component: () => import('@/views/pages/404'),
        hidden: true
    },
    {
        path: '/',
        component: () => import('@/views/layout/index'),
        redirect: '/homePage',
        children: [{
            path: '/homePage',
            name: '首页',
            label: '默认主页',
            component: () => import('@/views/home/index')
        },
        // 日志管理相关路由
        {
            path: '/operationLogs',
            name: '列表',
            label: '操作日志列表页',
            component: () => import('@/views/pages/logManage/operationLog/index')
        }, {
            path: '/operationLogDetail',
            name: '详情',
            label: '操作日志详情页',
            component: () => import('@/views/pages/logManage/operationLog/detail')
        },
        {
            path: '/userLogs',
            name: '列表',
            label: '用户日志列表页',
            component: () => import('@/views/pages/logManage/userLog/index')
        }, {
            path: '/userLogDetail',
            name: '详情',
            label: '用户日志详情页',
            component: () => import('@/views/pages/logManage/userLog/detail')
        },
        // 消息管理相关路由
        {
            path: '/messages',
            name: '列表',
            label: '消息列表页',
            component: () => import('@/views/pages/messageManage/messages/index')
        }, {
            path: '/messageDetail',
            name: '详情',
            label: '消息详情页',
            component: () => import('@/views/pages/messageManage/messages/detail')
        },
        // 用户管理相关路由
        {
            path: '/user',
            name: '列表',
            label: '用户列表页',
            component: () => import('@/views/pages/systemManage/user/index')
        }, {
            path: '/userAdd',
            name: '新增',
            label: '用户新增页',
            component: () => import('@/views/pages/systemManage/user/add')
        }, {
            path: '/userEdit',
            name: '编辑',
            label: '用户编辑页',
            component: () => import('@/views/pages/systemManage/user/add')
        }, {
            path: '/userDetail',
            name: '详情',
            label: '用户详情页',
            component: () => import('@/views/pages/systemManage/user/add')
        },
        // 组织机构相关路由
        {
            path: '/organization',
            name: '列表',
            label: '组织机构列表页',
            component: () => import('@/views/pages/systemManage/organization/index')
        }, {
            path: '/organizationAdd',
            name: '新增',
            label: '组织机构新增页',
            component: () => import('@/views/pages/systemManage/organization/add')
        }, {
            path: '/organizationEdit',
            name: '编辑',
            label: '组织机构编辑页',
            component: () => import('@/views/pages/systemManage/organization/add')
        },
        // 菜单管理相关路由
        {
            path: '/menus',
            name: '列表',
            label: '菜单列表页',
            component: () => import('@/views/pages/systemManage/menu/index')
        }, {
            path: '/menuAdd',
            name: '新增',
            label: '菜单新增功能页',
            component: () => import('@/views/pages/systemManage/menu/addOrEdit')
        }, {
            path: '/menuEdit',
            name: '编辑',
            label: '菜单编辑功能页',
            component: () => import('@/views/pages/systemManage/menu/addOrEdit')
        },
        // 角色管理相关路由
        {
            path: '/role',
            name: '列表',
            label: '角色管理列表页',
            component: () => import('@/views/pages/systemManage/role/index')
        }, {
            path: '/roleAdd',
            name: '新增',
            label: '角色新增功能页',
            component: () => import('@/views/pages/systemManage/role/addOrEdit')
        }, {
            path: '/roleEdit',
            name: '编辑',
            label: '角色编辑功能页',
            component: () => import('@/views/pages/systemManage/role/addOrEdit')
        }, {
            path: '/roleUser',
            name: '用户列表',
            label: '角色绑定用户列表页',
            component: () => import('@/views/pages/systemManage/role/users')
        }, {
            path: '/rolePower',
            name: '权限分配',
            label: '角色权限分配页',
            component: () => import('@/views/pages/systemManage/role/assignPremission')
        },
        // 系统权限分配
        {
            path: '/sysRole',
            name: '列表',
            label: '系统权限列表页',
            component: () => import('@/views/pages/systemManage/sysRole/index')
        }, {
            path: '/sysRolePower',
            name: '权限分配',
            label: '系统权限分配页',
            component: () => import('@/views/pages/systemManage/sysRole/assignPremission')
        },
        // 系统设置相关路由
        {
            path: '/dataBackup',
            name: '设置',
            label: '数据备份设置页',
            component: () => import('@/views/pages/systemSet/dataBackup')
        }, {
            path: '/service',
            name: '详情',
            label: '服务器信息详情页',
            component: () => import('@/views/pages/systemSet/service')
        },
        //需求管理
        {
            path: '/demandedInfo',
            name: '列表',
            label: '需求查询列表页',
            component: () => import('@/views/pages/resourceManage/demandedInfo/index')
        },
        {
            path: '/demandedInfoDetail',
            name: '详情',
            label: '需求详情页',
            component: () => import('@/views/pages/resourceManage/demandedInfo/detail')
        },
        {
            path: '/demandedInfoAudit',
            name: '列表',
            label: '需求受理列表页',
            component: () => import('@/views/pages/resourceManage/demandedInfoAudit/index')
        },
        {
            path: '/demandedInfoUpdate',
            name: '受理',
            label: '需求受理审批页',
            component: () => import('@/views/pages/resourceManage/demandedInfoAudit/edit')
        },
        {
            path: '/demandedSummary',
            name: '汇总',
            label: '需求汇总页',
            component: () => import('@/views/pages/resourceManage/demandedList/summary')
        },
        {
            path: '/demandedStatistics',
            name: '统计',
            label: '需求统计页',
            component: () => import('@/views/pages/resourceManage/demandedList/statistics')
        },
        {
            path: '/demandedStatisticsDetail',
            name: '详情',
            label: '需求统计详情页',
            component: () => import('@/views/pages/resourceManage/demandedList/statisticsDetail')
        },
        //资源使用管理
        {
            path: '/resourceUseInfo',
            name: '列表',
            label: '使用管理查询列表页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/index')
        },
        {
            path: '/resourceUseInfoJournal',
            name: '日志',
            label: '使用管理日志列表页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/journal')
        },
        {
            path: '/resourceUseInfoPrimary',
            name: '列表',
            label: '使用初审查询列表页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/primary')
        },
        {
            path: '/resourceUseInfoUltimate',
            name: '列表',
            label: '使用终审查询列表页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/ultimate')
        },
        {
            path: '/resourceUseInfoDetail',
            name: '详情',
            label: '资源使用详情页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/detail')
        },
        {
            path: '/resourceUseInfoUpdate',
            name: '审核',
            label: '资源使用审核页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/edit')
        },
        {
            path: '/assetApiExDetail',
            name: 'API详情',
            label: '资源使用API详情页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/apiDetail')
        },
        {
            path: '/resourceUseInfoSummary',
            name: '列表',
            label: '资源申请汇总页',
            component: () => import('@/views/pages/resourceManage/resourceUseInfo/summary')
        },
        // 资源管理
        {
            path: '/resourceManagement',
            name: '列表',
            label: '资源管理列表页',
            component: () => import('@/views/pages/resourceManage/resourceManagement/index')
        },
        {
            path: '/archBusiUviewDetail',
            name: '详情',
            label: '资源管理详情页',
            component: () => import('@/views/pages/resourceManage/resourceManagement/detail')
        },
        {
            path: '/archApiExIndex',
            name: '云接口',
            label: '资源管理云接口页',
            component: () => import('@/views/pages/resourceManage/assetApiEx/index')
        },
        {
            path: '/archApiExAdd',
            name: '云数据新增',
            label: '云数据页面中新增',
            component: () => import('@/views/pages/resourceManage/assetApiEx/add')
        },
        {
            path: '/archApiExDetail',
            name: 'api详情',
            label: '云数据详情页',
            component: () => import('@/views/pages/resourceManage/assetApiEx/detail')
        },
        {
            path: '/archApiExTest',
            name: 'api测试',
            label: '云数据测试页',
            component: () => import('@/views/pages/resourceManage/assetApiEx/testPage')
        },
        {
            path: '/archApiExApiDetail',
            name: 'api详情',
            label: '云数据继续添加详情页',
            component: () => import('@/views/pages/resourceManage/assetApiEx/apiDetail')
        },
        {
            path: '/archApiExApiTest',
            name: 'api测试',
            label: '云数据继续添加测试页',
            component: () => import('@/views/pages/resourceManage/assetApiEx/apiTestPage')
        },
        {
            path: '/archBusiUviewAdd',
            name: '挂接数据',
            label: '资源管理挂接云数据页',
            component: () => import('@/views/pages/resourceManage/resourceManagement/add')
        },
        {
            path: '/fileList',
            name: '云文件',
            label: '云文件列表',
            component: () => import('@/views/pages/resourceManage/resourceManagement/file')
        },
        {
            path: '/dataManagement',
            name: '云数据',
            label: '云数据',
            component: () => import('@/views/pages/resourceManage/resourceManagement/data')
        },
        {
            path: '/archBusiUviewApiDetail',
            name: 'Api详情页',
            label: 'Api详情页',
            component: () => import('@/views/pages/resourceManage/resourceManagement/apiDetail')
        },
        {
            path: '/archBusiUviewApiTest',
            name: '挂接云数据测试',
            label: '挂接云数据测试',
            component: () => import('@/views/pages/resourceManage/resourceManagement/testPage.vue')
        },
        //文件管理
        {
            path: '/downloadInfo',
            name: '列表',
            label: '文件管理查询列表页',
            component: () => import('@/views/pages/resourceManage/downloadInfo/index')
        },
        {
            path: '/downloadInfoAdd',
            name: '添加',
            label: '文件管理添加页',
            component: () => import('@/views/pages/resourceManage/downloadInfo/add')
        },
        {
            path: '/dataStatistics',
            name: '列表',
            label: '申请方统计页',
            component: () => import('@/views/pages/resourceManage/dataStatistics/index')
        },
        {
            path: '/dataStatisticsDetail',
            name: '列表',
            label: '申请方统计详情页',
            component: () => import('@/views/pages/resourceManage/dataStatistics/detail')
        },
        {
            path: '/editError',
            name: '列表',
            label: '纠错管理列表页',
            component: () => import('@/views/pages/resourceManage/editError/index')
        },
        {
            path: '/editErrorDetail',
            name: '详情',
            label: '纠错管理详情页',
            component: () => import('@/views/pages/resourceManage/editError/detail')
        },
        {
            path: '/editErrorUpdate',
            name: '审核',
            label: '纠错管理审核页',
            component: () => import('@/views/pages/resourceManage/editError/edit')
        },
        {
            path: '/weEditError',
            name: '列表',
            label: '我的纠错列表页',
            component: () => import('@/views/pages/resourceManage/weEditError/index')
        },
        {
            path: '/weEditErrorDetail',
            name: '详情',
            label: '我的纠错详情页',
            component: () => import('@/views/pages/resourceManage/weEditError/detail')
        },
        // 挂接申请
        {
            path: '/submitAdd',
            name: '新建申请',
            label: '新建申请页',
            component: () => import('@/views/pages/resourceManage/hookUp/submit/add')
        },
        {
            path: '/submitEdit',
            name: '编辑',
            label: '编辑页',
            component: () => import('@/views/pages/resourceManage/hookUp/submit/add')
        },
        {
            path: '/submitList',
            name: '列表',
            label: '申请提交页',
            component: () => import('@/views/pages/resourceManage/hookUp/submit/index')
        },
        {
            path: '/submitRecord',
            name: '申请记录',
            label: '申请记录页',
            component: () => import('@/views/pages/resourceManage/hookUp/submit/list')
        },
        {
            path: '/toExamineDetail',
            name: '详情',
            label: '申请终审详情页',
            component: () => import('@/views/pages/resourceManage/hookUp/toExamine/detail')
        },
        {
            path: '/toExamineList',
            name: '列表',
            label: '申请终审列表页',
            component: () => import('@/views/pages/resourceManage/hookUp/toExamine/list')
        },
        {
            path: '/toExamineRecord',
            name: '日志',
            label: '申请终审日志页',
            component: () => import('@/views/pages/resourceManage/hookUp/toExamine/record')
        },
        // 角色管理
        {
            path: '/agencyDistributionList',
            name: '代办分配',
            label: '代办分配列表页',
            component: () => import('@/views/pages/resourceManage/roleManagement/agencyDistribution/index')
        },
        {
            path: '/serviceGuidelinesList',
            name: '列表',
            label: '服务指引列表页',
            component: () => import('@/views/pages/resourceManage/roleManagement/serviceGuidelines/index')
        },
        {
            path: '/serviceGuidelinesAdd',
            name: '添加',
            label: '服务指引添加页',
            component: () => import('@/views/pages/resourceManage/roleManagement/serviceGuidelines/add')
        },
        {
            path: '/serviceGuidelinesDistribution',
            name: '服务指引分配',
            label: '服务指引分配列表页',
            component: () => import('@/views/pages/resourceManage/roleManagement/guidelinesDistribution/index')
        },
        {
            path: '/replaceRuleList',
            name: '列表',
            label: '脱敏规则列表页',
            component: () => import('@/views/pages/resourceManage/replaceRule/index')
        },
        {
            path: '/replaceRuleAdd',
            name: '添加',
            label: '脱敏规则添加页',
            component: () => import('@/views/pages/resourceManage/replaceRule/add')
        },
        ]
    }
]

const router = new VueRouter({
    routes
})

router.onError((error) => {
    const jsPattern = /Loading chunk (\S)+ failed/g
    const cssPattern = /Loading CSS chunk (\S)+ failed/g
    const isChunkLoadFailed = error.message.match(jsPattern || cssPattern)
    const targetPath = router.history.pending.fullPath
    if (isChunkLoadFailed) {
        window.location.reload()
    }
})

export default router
