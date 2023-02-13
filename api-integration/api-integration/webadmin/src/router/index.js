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
    }, , {
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
    }



      //API管理
      , {
      path: '/apiManage/apiManage/apiList',
      name: '列表',
      label: '列表',
      component: () => import('@/views/pages/apiManage/apiManage/apiManage/index')
    }, {
      path: '/apiManage/apiManage/add',
      name: '新增',
      label: '新增',
      component: () => import('@/views/pages/apiManage/apiManage/apiManage/add')
    }, {
      path: '/apiManage/apiManage/edit',
      name: '编辑',
      label: '编辑',
      component: () => import('@/views/pages/apiManage/apiManage/apiManage/edit')
    }, {
      path: '/apiManage/apiManage/copy',
      name: '复制',
      label: '复制',
      component: () => import('@/views/pages/apiManage/apiManage/apiManage/edit')
    }, {
      path: '/apiManage/apiTest/doTest',
      name: '测试',
      label: '测试',
      component: () => import('@/views/pages/apiManage/apiTestManage/apiTest/testPage')
    },


      //授权管理
      , {
      path: '/apiManage/apiAuth/apiList',
      name: '授权管理',
      label: '授权管理',
      component: () => import('@/views/pages/apiManage/apiAuthManage/apiAuth/index')
    }, {
      path: '/apiManage/apiAuth/insertAuth',
      name: 'API授权',
      label: 'API授权',
      component: () => import('@/views/pages/apiManage/apiAuthManage/apiAuth/insertAuth')
    }, {
      path: '/apiManage/apiAuth/groupApiList',
      name: 'API授权',
      label: 'API授权',
      component: () => import('@/views/pages/apiManage/apiAuthManage/apiAuth/apiIndex'),
    }, {
      path: '/apiManage/apiAuth/appList',
      name: '已授权应用',
      label: '已授权应用',
      component: () => import('@/views/pages/apiManage/apiAuthManage/apiAuth/appIndex'),
    }, {
      path: '/apiManage/apiAuth/groupInsertAuth',
      name: 'API组内API授权',
      label: 'API组内API授权',
      component: () => import('@/views/pages/apiManage/apiAuthManage/apiAuth/groupInsertAuth')
    },
    {
      path: '/apiManage/apiAuth/groupInsertAuth',
      name: 'API组内API授权',
      label: 'API组内API授权',
      component: () => import('@/views/pages/apiManage/apiAuthManage/apiAuth/groupInsertAuth')
    },
    {
      path: '/apiManage/apiAuth/testPage',
      name: '测试',
      label: '测试',
      component: () => import('@/views/pages/apiManage/apiAuthManage/apiAuth/testPage'),
    }



      //应用管理
      , {
      path: '/application/list',
      name: '应用管理',
      label: '应用管理',
      component: () => import('@/views/pages/application/index')
    }


      //API分析
      , {
      path: '/apiAnalysisManage/apiAnalysis/apiList',
      name: 'API分析',
      label: 'API分析',
      component: () => import('@/views/pages/apiAnalysisManage/apiAnalysis/index')
    }, {
      path: '/apiAnalysisManage/apiAnalysis/visitInfo',
      name: '访问详情',
      label: '访问详情',
      component: () => import('@/views/pages/apiAnalysisManage/apiAnalysis/visitInfo')
    }, {
      path: '/apiAnalysisManage/apiAnalysis/logRecord',
      name: '日志详情',
      label: '日志详情',
      component: () => import('@/views/pages/apiAnalysisManage/apiAnalysis/logRecordIndex')
    }, {
      path: '/apiAnalysisManage/apiAnalysis/analysis',
      name: 'API日志统计',
      label: 'API日志统计',
      component: () => import('@/views/pages/apiAnalysisManage/apiAnalysis/analysis')
    }, {
      path: '/apiAnalysisManage/apiAnalysis/appApiLog',
      name: '日志详情',
      label: '日志详情',
      component: () => import('@/views/pages/apiAnalysisManage/apiAnalysis/apiApplogIndex')
    }]
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
