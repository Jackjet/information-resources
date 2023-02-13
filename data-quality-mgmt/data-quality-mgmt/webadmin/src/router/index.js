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
    children: [
      {
        path: '/homePage',
        name: '首页',
        label: '默认主页',
        component: () => import('@/views/home/index')
      },
      // 质量检查规则
      {
        path: '/quality',
        name: '列表',
        label: '质量检查规则',
        component: () => import('@/views/pages/qualityManage/quality/index')
      },
      {
        path: '/qualityAdd',
        name: '新增',
        label: '质量检查规则新增页',
        component: () => import('@/views/pages/qualityManage/quality/add')
      }, {
        path: '/qualityEdit',
        name: '编辑',
        label: '质量检查规则编辑页',
        component: () => import('@/views/pages/qualityManage/quality/add')
      },
      {
        path: '/qualityDetail',
        name: '详情',
        label: '质量检查规则数据模板详情',
        component: () => import('@/views/pages/qualityManage/quality/detail')
      },
      // 质量报告
      {
        path: '/qualityReport',
        name: '列表',
        label: '质量报告',
        component: () => import('@/views/pages/qualityReportManage/qualityReport/index')
      },
      {
        path: '/qualityReportAdd',
        name: '新增',
        label: '质量报告新增页',
        component: () => import('@/views/pages/qualityReportManage/qualityReport/add')
      }, {
        path: '/qualityReportDetail',
        name: '详情',
        label: '质量报告详情页',
        component: () => import('@/views/pages/qualityReportManage/qualityReport/add')
      },
      {
        path: '/qualityReportWord',
        name: '完善报告',
        label: '质量报告完善报告',
        component: () => import('@/views/pages/qualityReportManage/qualityReport/report')
      },
      // 接入任务管理
      {
        path: '/taskManage',
        name: '列表',
        label: '接入任务管理',
        component: () => import('@/views/pages/taskManage/task/index')
      },
      {
        path: '/taskManageAdd',
        name: '新增',
        label: '接入任务管理新增页',
        component: () => import('@/views/pages/taskManage/task/add')
      }, {
        path: '/taskManageEdit',
        name: '编辑',
        label: '接入任务管理编辑页',
        component: () => import('@/views/pages/taskManage/task/add')
      },
      {
        path: '/taskManageLog',
        name: '查看日志',
        label: '接入任务管理任务日志',
        component: () => import('@/views/pages/taskManage/task/log')
      },
      {
        path: '/taskManageDetail',
        name: '查看明细',
        label: '接入任务管理查看明细',
        component: () => import('@/views/pages/taskManage/task/detail')
      },
      {
        path: '/taskManageProblem',
        name: '查看问题',
        label: '接入任务管理查看问题',
        component: () => import('@/views/pages/taskManage/task/problem')
      },
      ,
      {
        path: '/taskManageOrder',
        name: '生成工单',
        label: '接入任务管理生成工单',
        component: () => import('@/views/pages/taskManage/task/order')
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
      // // 消息管理相关路由
      // {
      //   path: '/messages',
      //   name: '列表',
      //   label: '消息列表页',
      //   component: () => import('@/views/pages/messageManage/messages/index')
      // }, {
      //   path: '/messageDetail',
      //   name: '详情',
      //   label: '消息详情页',
      //   component: () => import('@/views/pages/messageManage/messages/detail')
      // },
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
      // // 系统设置相关路由
      // {
      //   path: '/dataBackup',
      //   name: '设置',
      //   label: '数据备份设置页',
      //   component: () => import('@/views/pages/systemSet/dataBackup')
      // }, {
      //   path: '/service',
      //   name: '详情',
      //   label: '服务器信息详情页',
      //   component: () => import('@/views/pages/systemSet/service')
      // },
      // 规则模板
      {
        path: '/ruleTemplate',
        name: '列表',
        label: '数据模板页',
        component: () => import('@/views/pages/ruleTemplate/index')
      },
      {
        path: '/ruleTemplateAdd',
        name: '新增',
        label: '数据模板新增页',
        component: () => import('@/views/pages/ruleTemplate/addOrEdit')
      },
      {
        path: '/ruleTemplateEdit',
        name: '编辑',
        label: '数据模板编辑页',
        component: () => import('@/views/pages/ruleTemplate/addOrEdit')
      },
      // 数据质量工单
      {
        path: '/workOrder',
        name: '列表',
        label: '数据质量工单页',
        component: () => import('@/views/pages/workOrder/index')
      },
      {
        path: '/workOrderDetail',
        name: '工单详情',
        label: '数据质量工单详情页',
        component: () => import('@/views/pages/workOrder/detail')
      },
      {
        path: '/workOrderHandle',
        name: '工单处理',
        label: '数据质量工单处理页',
        component: () => import('@/views/pages/workOrder/handle')
      },

      //数据源管理
      {
        path: '/datasource/manage/index',
        name: '数据源管理',
        label: '数据源管理',
        component: () => import('@/views/pages/datasourceManage/index')
      }, {
        path: '/datasource/manage/add',
        name: '添加数据源',
        label: '添加数据源',
        component: () => import('@/views/pages/datasourceManage/edit')
      }, {
        path: '/datasource/manage/edit',
        name: '编辑数据源',
        label: '编辑数据源',
        component: () => import('@/views/pages/datasourceManage/edit')
      }, {
        path: '/datasource/manage/dataUnit',
        name: '查看数据源',
        label: '查看数据源',
        component: () => import('@/views/pages/datasourceManage/dataUnit')
      }, {
        path: '/datasource/manage/dataField',
        name: '查看字段',
        label: '查看字段',
        component: () => import('@/views/pages/datasourceManage/dataField')
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
