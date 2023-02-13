import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  // {
  //   path: '/',
  //   component: () => import('@/views/dashboard/index'),
  //   hidden: true
  // },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '首页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: '',
    meta: { title: '系统管理', icon: 'example' },
    children: [{
      path: 'user',
      name: 'user',
      component: () => import('@/views/omMager/user/user'),
      meta: { title: '用户管理', icon: 'user' }
    }, {
      path: 'permissionView',
      name: 'permissionView',
      component: () => import('@/views/omMager/comments/index'),
      meta: { title: '权限管理', icon: 'permissionView' }
    }, {
      path: 'role',
      name: 'role',
      component: () => import('@/views/omMager/comments/index'),
      meta: { title: '角色管理', icon: 'role' }
    }, {
      path: 'roleList',
      name: 'roleList',
      component: () => import('@/views/omMager/comments/roleList'),
      meta: { title: '分配权限', icon: 'roleList' }
    }]
  },
  {
    path: '/index',
    component: Layout,
    redirect: '/index/model',
    name: '',
    meta: { title: '知识库', icon: 'example' },
    children: [
      {
        path: 'dataModel',
        name: 'dataModel',
        component: () => import('@/views/knowledgeBase/dataModel/index'),
        meta: { title: '数学模型建模', icon: 'dataModel' }
      },
      {
        path: 'model',
        name: 'model',
        component: () => import('@/views/knowledgeBase/model/index'),
        meta: { title: '模型管理', icon: 'model' }
      },
      {
        path: 'echart',
        name: 'echart',
        component: () => import('@/views/echart/index'),
        meta: { title: '图表', icon: 'echart' }
      },
    ]
  }, {
    path: '/dataProcessingService',
    component: Layout,
    redirect: '/dataProcessingService/digitalModel',
    name: '',
    meta: { title: '大数据处理服务', icon: 'example' },
    children: [
      {
        path: 'digitalModel',
        name: 'digitalModel',
        component: () => import('@/views/dataProcessingService/digitalModel/digitalModel'),
        meta: { title: '数字模型执行引擎', icon: 'dataModel' }
      },
      {
        path: 'apiManageView',
        name: 'apiManageView',
        component: () => import('@/views/dataProcessingService/apiManageView/apiManageView'),
        meta: { title: '接口管理', icon: 'dataModel' }
      },
      {
        path: 'dataProcessServiceView',
        name: 'dataProcessServiceView',
        component: () => import('@/views/dataProcessingService/dataProcessServiceView/dataProcessServiceView'),
        meta: { title: '数据处理服务', icon: 'dataModel' }
      },
      {
        path: 'dataServiceApiView',
        name: 'dataServiceApiView',
        component: () => import('@/views/dataProcessingService/dataServiceApiView/dataServiceApiView'),
        meta: { title: '数字服务接口管理', icon: 'dataModel' }
      },
      {
        path: 'dataServicePlanView',
        name: 'dataServicePlanView',
        component: () => import('@/views/dataProcessingService/dataServicePlanView/dataServicePlanView'),
        meta: { title: '数据服务接口计划任务', icon: 'dataModel' }
      }
    ]
  },
  {
    path: '/eaMager',
    component: Layout,
    redirect: '/eaMager/approve',
    name: '',
    meta: { title: '运维审批管理', icon: '' },
    children: [
      {
        path: 'approve',
        name: 'approve',
        component: () => import('@/views/omMager/approve/index'),
        meta: { title: '审批管理', icon: 'approve' }
      }
    ]
  },
  {
    path: '/omMager',
    component: Layout,
    redirect: '/omMager/user',
    name: '',
    meta: { title: '运维行为管理', icon: '' },
    children: [
      {
        path: 'user',
        name: 'user',
        component: () => import('@/views/omMager/user/user'),
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'comments',
        name: 'comments',
        component: () => import('@/views/omMager/comments/index'),
        meta: { title: '组件管理', icon: 'comments' }
      },
      {
        path: 'log',
        name: 'log',
        component: () => import('@/views/omMager/log/index'),
        meta: { title: '日志管理', icon: 'log' }
      },
      {
        path: 'roleList',
        name: 'roleList',
        component: () => import('@/views/omMager/comments/roleList'),
        meta: { title: '权限分配', icon: 'log' }
      },
    ]
  },
  {
    path: '/omMager',
    component: Layout,
    redirect: '/omMager/user',
    name: '',
    meta: { title: '运维行为管理', icon: '' },
    children: [
      {
        path: 'user',
        name: 'user',
        component: () => import('@/views/omMager/user/user'),
        meta: { title: '用户管理', icon: 'user' }
      }
    ]
  },
  {
    path: '/monitoringView',
    component: Layout,
    redirect: '/monitoringView/environmentView',
    name: '',
    meta: { title: '基础环境监控', icon: '' },
    children: [
      {
        path: 'environmentView',
        name: 'environmentView',
        component: () => import('@/views/monitoringView/environmentView/environmentView'),
        meta: { title: '基础环境监控', icon: 'user' }
      },
      {
        path: 'systemView',
        name: 'systemView',
        component: () => import('@/views/monitoringView/systemView/systemView'),
        meta: { title: '系统监控', icon: 'user' }
      },
      {
        path: 'sourceView',
        name: 'sourceView',
        component: () => import('@/views/monitoringView/sourceView/sourceView'),
        meta: { title: '资源监控', icon: 'user' }
      },
      {
        path: 'dataView',
        name: 'dataView',
        component: () => import('@/views/monitoringView/dataView/dataView'),
        meta: { title: '数据监控', icon: 'user' }
      }
    ]
  },
  {
    path: '/behavior',
    component: Layout,
    redirect: '/behavior/activity',
    name: '',
    meta: { title: '用户行为监控', icon: '' },
    children: [
      {
        path: 'userLogin',
        name: 'userLogin',
        component: () => import('@/views/behavior/userLogin/userLogin'),
        meta: { title: '用户登录信息监控', icon: 'user' }
      },
      {
        path: 'online',
        name: 'online',
        component: () => import('@/views/behavior/online/online'),
        meta: { title: '在线用户监控', icon: 'user' }
      },
      {
        path: 'activity',
        name: 'activity',
        component: () => import('@/views/behavior/activity/activity'),
        meta: { title: '用户活跃度', icon: 'user' }
      }
    ]
  }, {
    path: '/moduleMonitorView',
    component: Layout,
    redirect: '/moduleMonitorView/moduleStatisticsView',
    name: '',
    meta: { title: '模块监控', icon: 'example' },
    children: [
      {
        path: 'moduleStatisticsView',
        name: 'moduleStatisticsView',
        component: () => import('@/views/moduleMonitorView/moduleStatisticsView/moduleStatisticsView'),
        meta: { title: '模块访问统计', icon: 'dataModel' }
      },
      {
        path: 'performanceView',
        name: 'performanceView',
        component: () => import('@/views/moduleMonitorView/performanceView/performanceView'),
        meta: { title: '平台性能监控', icon: 'dataModel' }
      },
      {
        path: 'dataMonitorView',
        name: 'dataMonitorView',
        component: () => import('@/views/moduleMonitorView/dataMonitorView/dataMonitorView'),
        meta: { title: '数据监控', icon: 'dataModel' }
      },
      {
        path: 'resourceView',
        name: 'resourceView',
        component: () => import('@/views/moduleMonitorView/resourceView/resourceView'),
        meta: { title: '资源监控', icon: 'dataModel' }
      }
    ]
  }, {
    path: '/dataBase',
    component: Layout,
    redirect: '/dataBase/moduleStatisticsView',
    name: '',
    meta: { title: '数据库运维', icon: 'example' },
    children: [
      {
        path: 'copy',
        name: 'copy',
        component: () => import('@/views/dataBase/copy/copy'),
        meta: { title: '备份和恢复', icon: 'dataModel' }
      },
      {
        path: 'dataMonitoring',
        name: 'dataMonitoring',
        component: () => import('@/views/dataBase/dataMonitoring/dataMonitoring'),
        meta: { title: '数据库监控', icon: 'dataModel' }
      },
      {
        path: 'examples',
        name: 'examples',
        component: () => import('@/views/dataBase/examples/examples'),
        meta: { title: '实例管理', icon: 'dataModel' }
      },
      {
        path: 'parameter',
        name: 'parameter',
        component: () => import('@/views/dataBase/parameter/parameter'),
        meta: { title: '参数配置', icon: 'dataModel' }
      },
      {
        path: 'sql',
        name: 'sql',
        component: () => import('@/views/dataBase/sql/sql'),
        meta: { title: '远程SQL', icon: 'dataModel' }
      }
    ]
  }, {
    path: '/longRangeControlView',
    component: Layout,
    redirect: '/longRangeControlView/operationalClassView',
    name: '',
    meta: { title: '远程控制', icon: 'example' },
    children: [
      {
        path: 'operationalClassView',
        name: 'operationalClassView',
        component: () => import('@/views/longRangeControlView/operationalClassView/operationalClassView'),
        meta: { title: '运维指令分类', icon: 'dataModel' }
      },
      {
        path: 'operationalDayView',
        name: 'operationalDayView',
        component: () => import('@/views/longRangeControlView/operationalDayView/operationalDayView'),
        meta: { title: '日常运维指令', icon: 'dataModel' }
      },
      {
        path: 'remoteConsoleView',
        name: 'remoteConsoleView',
        component: () => import('@/views/longRangeControlView/remoteConsoleView/remoteConsoleView'),
        meta: { title: '远程控制台', icon: 'dataModel' }
      }
    ]
  }, {
    path: '/deploymentView',
    component: Layout,
    redirect: '/deploymentView/installationPathView',
    name: '',
    meta: { title: '自动化部署', icon: 'example' },
    children: [
      {
        path: 'installationPathView',
        name: 'installationPathView',
        component: () => import('@/views/deploymentView/installationPathView/installationPathView'),
        meta: { title: '安装路径管理', icon: 'dataModel' }
      },
      {
        path: 'versionView',
        name: 'versionView',
        component: () => import('@/views/deploymentView/versionView/versionView'),
        meta: { title: '版本管理及部署', icon: 'dataModel' }
      },
      {
        path: 'patchView',
        name: 'patchView',
        component: () => import('@/views/deploymentView/patchView/patchView'),
        meta: { title: '补丁管理及部署', icon: 'dataModel' }
      },
      {
        path: 'configView',
        name: 'configView',
        component: () => import('@/views/deploymentView/configView/configView'),
        meta: { title: '配置管理及部署', icon: 'dataModel' }
      },

      // {
      //   path: 'deploymentView',
      //   name: 'deploymentView',
      //   component: () => import('@/views/deploymentView/deploymentView/deploymentView'),
      //   meta: { title: '自动化部署管理', icon: 'dataModel' }
      // },
      {
        path: 'deploymentLogView',
        name: 'deploymentLogView',
        component: () => import('@/views/deploymentView/deploymentLogView/deploymentLogView'),
        meta: { title: '部署日志', icon: 'dataModel' }
      }
    ]
  },
  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  // { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
