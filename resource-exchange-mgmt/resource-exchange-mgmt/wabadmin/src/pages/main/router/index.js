import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '@/pages/main/views/index/index'
import * as util from '@/assets/js/common'
Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push

VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

export default new VueRouter({
  routes: [{
    path: '/',
    redirect: '/login',
    name: 'login',
    component: () => import('@/pages/main/views/login/login')
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录',
      hideInMenu: true,
      slots: ''
    },
    component: () => import('@/pages/main/views/login/login')
  },
  {
    path: '/admin',
    name: 'adminLogin',
    meta: {
      title: '管理员登录',
      hideInMenu: true,
      slots: ''
    },
    component: () => import('@/pages/main/views/login/adminLogin')
  },
  {
    path: '/tenant',
    name: 'tenantLogin',
    meta: {
      title: '租户登录',
      hideInMenu: true,
      slots: ''
    },
    component: () => import('@/pages/main/views/login/tenantLogin')
  },
  {
    path: '/documents',
    name: 'documents',
    meta: {
      title: '安装文档',
      hideInMenu: true,
      slots: ''
    },
    component: () => import('@/pages/main/views/subPage/itemAdminProject/node/documents')
  },
  {
    path: '/index',
    name: 'Home',
    meta: {
      title: '首页',
      hideInMenu: true,
      requireAuth: true,
      slots: ''
    },
    component: Index,
    // redirect: '/index/preface',
    redirect: to => {
      let type = util.default.session('type')
      console.log(type)
      let returnType = ''
      switch (Number(type)) {
        case 1:
          returnType = '/index/otherService'
          break
        case 2:
          returnType = '/index/integratedProject'
          break
        case 3:
          returnType = '/index/runNode'
          break
        default:
          console.log(type)
          break
      }
      return returnType
    // 方法接收 目标路由 作为参数
    // return 重定向的 字符串路径/路径对象
    },
    children: [{
      path: 'uploadService',
      meta: {
        title: '配置服务',
        name: '服务上传',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/adminProject/disposeManage/uploadService/list')
    },
    {
      path: 'otherService',
      meta: {
        title: '配置服务',
        name: '服务配置',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/adminProject/disposeManage/otherService/otherService')
    },
    {
      path: 'lesseeManage',
      meta: {
        title: '租户管理',
        name: '租户列表',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/adminProject/lesseeProject/lesseeManage/lesseeManage')
    },
    {
      path: 'integratedProject',
      meta: {
        title: '用户管理',
        name: '用户列表',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/lesseeProject/integratedManage/integratedProject/integratedManage')
    },
    {
      path: 'record',
      meta: {
        title: '系统服务管理',
        name: '操作记录',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/record')
    },
    {
      path: 'EMQDisp',
      meta: {
        title: '系统服务管理',
        name: '系统服务配置',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/EMQDisp/main')
    },
    {
      path: 'APIDisp',
      meta: {
        title: '系统服务管理',
        name: '系统服务配置',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/APIDisp/main')
    },
    {
      path: 'dataCacheDisp',
      meta: {
        title: '系统服务管理',
        name: '系统服务配置',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/dataCacheDisp/main')
    },
    {
      path: 'sysServiceInstallPack',
      meta: {
        title: '服务适配',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/uploadService/list')
    },
    {
      path: 'appPack',
      meta: {
        title: '节点管理',
        name: '服务打包',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/appPack/appPack')
    },
    {
      path: 'dcRunAPIPack',
      meta: {
        title: '节点管理',
        name: '服务打包',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/appPack/dcRunAPIPack')
    },
    {
      path: 'sysUser',
      meta: {
        title: '身份管理',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/other/systemServiceDeveloper/main')
    },
    {
      path: 'runNode',
      meta: {
        title: '节点管理',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/node/main')
    },
    {
      path: 'services',
      meta: {
        title: '服务管理',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/main')
    },
    {
      path: 'developer',
      meta: {
        title: '微应用开发',
        name: '微应用开发',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/itemAdminProject/services/APIDisp/developer')
    },
    {
      path: 'componentManage',
      meta: {
        title: '组件适配',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/componentManage/list')
    },
    {
      path: 'dataResource',
      meta: {
        title: '数据源',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/dataResource/index')
    },
    {
      path: 'dataResourceAdd',
      meta: {
        title: '数据源添加',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/dataResource/add')
    },
    {
      path: 'dataResourceEdit',
      meta: {
        title: '数据源编辑',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/dataResource/add')
    },
    {
      path: 'container',
      meta: {
        title: '容器管理',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/containerResource/index')
    },
    {
      path: 'containerDetail',
      meta: {
        title: '容器详情',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/containerResource/detail')
    },
    {
      path: 'containerAdd',
      meta: {
        title: '容器添加',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/containerResource/add')
    },
    {
      path: 'analysis',
      meta: {
        title: '任务分析',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/task/analysis')
    },
    {
      path: 'task',
      meta: {
        title: '任务列表',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/task/index')
    },
    {
      path: 'taskAdd',
      meta: {
        title: '任务列表',
        name: '任务新增',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/task/add')
    },
    {
      path: 'taskEdit',
      meta: {
        title: '任务列表',
        name: '任务编辑',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/task/add')
    },
    {
      path: 'taskLogs',
      meta: {
        title: '任务列表',
        name: '任务日志',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/task/logs')
    },
    {
      path: 'taskDesign',
      meta: {
        title: '任务列表',
        name: '任务编排',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/task/design')
    },
    {
      path: 'taskShowKtr',
      meta: {
        title: '任务列表',
        name: '任务编排',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/task/showKtr')
    },
    {
      path: 'backups',
      meta: {
        title: '备份管理',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/backups/list')
    },
    {
      path: 'operationLog',
      meta: {
        title: '系统日志',
        name: '',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/logManage/operationLog/index')
    },
    {
      path: 'operationLogDetail',
      meta: {
        title: '系统日志',
        name: '日志详情',
        slots: '',
        hideInMenu: true
      },
      component: () => import('@/pages/main/views/subPage/logManage/operationLog/detail')
    }]
  }]
})
