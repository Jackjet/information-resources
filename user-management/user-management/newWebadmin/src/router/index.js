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
    path: '/nav',
    component: () => import('@/views/layout/nav'),
    redirect: '/navIndex',
    children: [{
      path: '/navIndex',
      name: '首页',
      label: '导航页',
      component: () => import('@/views/nav/index')
    }]
  },
  {
    path: '/',
    component: () => import('@/views/layout/index'),
    redirect: '/nav',
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
    },
    //API管理系统
    {
      path: '/operationLogs',
      name: '列表',
      label: '操作日志列表页'
    }, {
      path: '/operationLogDetail',
      name: '详情',
      label: '操作日志详情页'
    },
    {
      path: '/userLogs',
      name: '列表',
      label: '用户日志列表页'
    }, {
      path: '/userLogDetail',
      name: '详情',
      label: '用户日志详情页'
    },
    //API管理
    {
      path: '/apiManage/apiManage/apiList',
      name: 'Api管理',
      label: 'Api管理'
    }, {
      path: '/apiManage/apiManage/add',
      name: '新增',
      label: '新增'
    }, {
      path: '/apiManage/apiManage/edit',
      name: '编辑',
      label: '编辑'
    }, {
      path: '/apiManage/apiManage/copy',
      name: '复制',
      label: '复制'
    }

      //授权管理
      , {
      path: '/apiManage/apiAuth/apiList',
      name: '授权管理',
      label: '授权管理'
    }
      //应用管理
      , {
      path: '/application/list',
      name: '应用管理',
      label: '应用管理'
    },
    //数据源
    {
      path: '/datasource',
      name: '云数据',
      label: '云数据'
    }, {
      path: '/datasourceAdd',
      name: '添加云数据',
      label: '添加云数据'
    }, {
      path: '/datasourceEdit',
      name: '编辑云数据',
      label: '编辑云数据'
    }, {
      path: '/datasourceDataUnit',
      name: '查看数据源',
      label: '查看数据源'
    }, {
      path: '/datasourceDataField',
      name: '查看字段',
      label: '查看字段'
    },
    //容器
    {
      path: '/container',
      name: '列表',
      label: '容器列表',
    },
    {
      path: '/containerDetail',
      name: '详情',
      label: '容器详情',
    },
    {
      path: '/containerAdd',
      name: '新增',
      label: '容器新增',
    },
    //标签
    {
      path: '/tag',
      name: '列表',
      label: '标签列表',
    },

    //API管理
    {
      path: '/apiInfo',
      name: '列表',
      label: 'API列表',
    },
    {
      path: '/apiAdd',
      name: '新增',
      label: 'API新增',
    },
    {
      path: '/apiUpdate',
      name: '编辑',
      label: 'API编辑',
    },
    {
      path: '/apiTest',
      name: '测试',
      label: 'API测试',
    },
    {
      path: '/apiDesign',
      name: '列表',
      label: 'API设计列表页'
    }, {
      path: '/apiDesignAdd',
      name: '新增',
      label: 'API设计新增'
    }, {
      path: '/apiDevelopment',
      name: '列表',
      label: 'API编排列表页'
    }, {
      path: '/apiDesignEdit',
      name: '编排',
      label: 'API编排页'
    }, {
      path: '/apiDesignTest',
      name: 'API测试',
      label: 'API编排测试页'
    },

    //API管理
    {
      path: '/apiManage/apiManage/apiList',
      name: 'API管理',
      label: 'API管理'
    }, {
      path: '/apiManage/apiManage/add',
      name: '新增',
      label: '新增'
    }, {
      path: '/apiManage/apiManage/edit',
      name: '编辑',
      label: '编辑'
    }, {
      path: '/apiManage/apiManage/copy',
      name: '复制',
      label: '复制'
    }, {
      path: '/apiManage/apiTest/doTest',
      name: '测试',
      label: '测试'
    },
    //授权管理
    {
      path: '/apiManage/apiAuth/apiList',
      name: '授权管理',
      label: '授权管理'
    }, {
      path: '/apiManage/apiAuth/insertAuth',
      name: 'API授权',
      label: 'API授权'
    }, {
      path: '/apiManage/apiAuth/groupApiList',
      name: 'API授权',
      label: 'API授权'
    }, {
      path: '/apiManage/apiAuth/appList',
      name: '已授权应用',
      label: '已授权应用'
    }, {
      path: '/apiManage/apiAuth/groupInsertAuth',
      name: 'API组内API授权',
      label: 'API组内API授权'
    },

    //应用管理
    {
      path: '/application/list',
      name: '应用管理',
      label: '应用管理'
    }

      //API分析
      , {
      path: '/apiAnalysisManage/apiAnalysis/apiList',
      name: 'API分析',
      label: 'API分析'
    }, {
      path: '/apiAnalysisManage/apiAnalysis/visitInfo',
      name: '访问详情',
      label: '访问详情'
    }, {
      path: '/apiAnalysisManage/apiAnalysis/logRecord',
      name: '日志详情',
      label: '日志详情'
    }, {
      path: '/apiAnalysisManage/apiAnalysis/analysis',
      name: 'API日志统计',
      label: 'API日志统计'
    }, {
      path: '/apiAnalysisManage/apiAnalysis/appApiLog',
      name: '日志详情',
      label: '日志详情'
    }
      //数据集成
      , {
      path: '/task',
      name: '列表',
      label: '任务列表页',
    },
    {
      path: '/taskAdd',
      name: '添加',
      label: '任务添加页',
    },
    {
      path: '/taskLogs',
      name: '运行日志',
      label: '运行日志列表页',
    },
    {
      path: '/taskDesign',
      name: '编排',
      label: '运行日志列表页',
    }
      ,
    {
      path: '/taskShowKtr',
      name: '编排',
      label: '运行日志列表页',
    },
    {
      path: '/taskAnalysis',
      name: '任务分析',
      label: '任务分析列表页',
    }
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
