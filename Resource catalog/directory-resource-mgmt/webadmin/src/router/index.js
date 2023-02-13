import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    perms: ['GET /aaa','POST /bbb']     will control the page perms (you can set multiple perms)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
 **/
export const constantRouterMap = [{
  path: '/redirect',
  component: Layout,
  hidden: true,
  children: [{
    path: '/redirect/:path*',
    component: () => import('@/views/redirect/index')
  }]
},
{
  path: '/login',
  component: () => import('@/views/login/index'),
  hidden: true
},
{
  path: '/auth-redirect',
  component: () => import('@/views/login/authredirect'),
  hidden: true
},
{
  path: '/404',
  component: () => import('@/views/errorPage/404'),
  hidden: true
},
{
  path: '/401',
  component: () => import('@/views/errorPage/401'),
  hidden: true
},
{
  path: '/',
  component: Layout,
  redirect: 'dashboard',
  children: [{
    path: 'dashboard',
    component: () => import('@/views/dashboard/index'),
    name: 'Dashboard',
    meta: {
      title: '首页',
      icon: 'shouye',
      affix: true
    }
  }]
}
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRouterMap
})
export const asyncRouterMap = [{
  path: '/departmentRes',
  component: Layout,
  children: [{
    path: 'index',
    component: () => import('@/views/departmentRes/index'),
    name: 'departmentRes',
    meta: {
      perms: ['GET /admin/org/busiList', 'GET /admin/org/deptTree', 'POST /admin/org/updateDept', 'GET /admin/org/isCilckBusi', 'POST /admin/org/removeBusiList/{deptId}', 'POST /admin/org/saveBusiRef', 'GET /admin/org/addBusiPage'],
      title: '部门职能职责',
      icon: 'bumen',
      noCache: true
    }
  }]
},
{
  path: '/businessManagement',
  component: Layout,
  children: [{
    path: 'index',
    component: () => import('@/views/businessManagement/index'),
    name: 'businessManagement',
    meta: {
      perms: ['GET /admin/archBusi/list'],
      title: '权责清单管理',
      icon: 'yewu',
      noCache: true
    }
  }]
},
{
  path: '/OSManagement',
  component: Layout,
  children: [{
    path: 'index',
    component: () => import('@/views/OSManagement/index'),
    name: 'OSManagement',
    meta: {
      perms: ['GET /admin/archAppSys/list'],
      title: '应用系统管理',
      icon: 'yingyong',
      noCache: true
    }
  },
  {
    path: 'OSManagementChart/:id',
    component: () => import('@/views/OSManagement/chart'),
    name: 'OSManagementChart',
    meta: {
      perms: ['GET /admin/archAppSys/list'],
      title: '应用系统支撑权责清单与信息资源关系图',
      icon: 'yingyong',
      noCache: true
    },
    hidden: true
  }
  ]
},
{
  path: '/Cataloging',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  name: 'Cataloging',
  meta: {
    perms: ['GET /admin/cataInfoTemp/list', 'GET /admin/cataRequire/list', 'POST /admin/cataImport/upload'],
    title: '目录编制',
    icon: 'mulu'
  },
  children: [{
    path: 'infoCatalogList',
    component: () => import('@/views/Cataloging/infoCatalog/list'),
    name: 'infoCatalogList',
    meta: {
      perms: ['GET /admin/cataInfoTemp/list'],
      title: '信息资源目录',
      noCache: true
    }
  },
  {
    path: 'infoCatalogList2',
    component: () => import('@/views/Cataloging/infoCatalog/list2'),
    name: 'infoCatalogList2',
    meta: {
      perms: ['GET /admin/cataInfoTemp/list2'],
      title: '更新预警',
      noCache: true
    }
  },
  {
    path: 'infoCatalogMaintain/:id',
    component: () => import('@/views/Cataloging/infoCatalog/infoCatalogMaintain'),
    name: 'infoCatalogMaintain',
    meta: {
      perms: ['POST /admin/cataInfoTemp/update'],
      title: '信息资源目录维护',
      noLink: true,
      noCache: true
    },
    props: true,
    hidden: true
  },
  {
    path: 'infoCatalogDetail/:id',
    component: () => import('@/views/Cataloging/infoCatalog/infoCatalogDetail'),
    name: 'infoCatalogDetail',
    meta: {
      perms: ['GET /admin/cataInfoTemp/read'],
      title: '信息资源目录详情',
      noLink: true,
      noCache: true
    },
    props: true,
    hidden: true
  },

  {
    path: 'versionManagement/:id',
    component: () => import('@/views/Cataloging/infoCatalog/versionManagement'),
    name: 'versionManagement',
    meta: {
      perms: ['GET /admin/cataInfoHistory/list'],
      title: '版本管理',
      noLink: true,
      noCache: true
    },
    props: true,
    hidden: true
  },
  {
    path: 'versionComparison/:id',
    component: () => import('@/views/Cataloging/infoCatalog/versionComparison'),
    name: 'versionComparison',
    meta: {
      perms: ['GET /admin/cataInfoHistory/compare'],
      title: '版本比较',
      noLink: true,
      noCache: true
    },
    props: true,
    hidden: true
  },
  /**
  {
    path: 'requirementCatalog',
    component: () => import('@/views/Cataloging/requirementCatalog/list'),
    name: 'requirementCatalogList',
    meta: {
      perms: ['GET /admin/cataRequire/list'],
      title: '信息需求目录',
      noCache: true
    }
  },
  {
    path: 'requirementCatalogMaintain/:id',
    component: () => import('@/views/Cataloging/requirementCatalog/requirementCatalogMaintain'),
    name: 'requirementCatalogMaintain',
    meta: {
      perms: ['POST /admin/cataRequire/save'],
      noLink: true,
      title: '信息需求目录维护',
      noCache: true
    },
    props: true,
    hidden: true
  },
  {
    path: 'requirementCatalogDetail/:id',
    component: () => import('@/views/Cataloging/requirementCatalog/requirementCatalogDetail'),
    name: 'requirementCatalogDetail',
    meta: {
      perms: ['GET /admin/cataRequire/read'],
      title: '信息需求目录详情',
      noLink: true,
      noCache: true
    },
    props: true,
    hidden: true
  },
  {
    path: 'relatedBusinessMatters/:id',
    component: () => import('@/views/Cataloging/requirementCatalog/relatedBusinessMatters'),
    name: 'relatedBusinessMatters',
    meta: {
      perms: ['GET /admin/cataBusInfoRel/list'],
      title: '关联权责清单',
      noLink: true,
      noCache: true
    },
    props: true,
    hidden: true
  },
  */
  {
    path: 'infoCatalogTopo',
    component: () => import('@/views/Cataloging/infoCatalog/topo'),
    name: 'infoCatalogTopo',
    meta: {
      // perms: ['GET /admin/cataInfoTemp/list'],
      title: '信息资源图谱',
      noCache: true
    },
    hidden: true
  },
  {
    path: 'importExport',
    component: () => import('@/views/router/index'),
    redirect: 'noredirect',
    name: 'CatalogingImportExport',
    meta: {
      perms: ['GET /admin/cataExport/export', 'GET /admin/cataImport/getErrorList', 'POST /admin/cataImport/upload', 'GET /admin/cataImport/delete', 'GET /admin/cataImport/list'],
      title: '导入导出',
      noCache: true
    },
    children: [{
      path: 'import',
      component: () => import('@/views/Cataloging/importExport/import'),
      name: 'CatalogingImport',
      meta: {
        perms: ['GET /admin/cataImport/getErrorList', 'POST /admin/cataImport/upload', 'GET /admin/cataImport/delete', 'GET /admin/cataImport/list'],
        title: '编制表导入',
        noCache: true
      }
    },
    {
      path: 'export',
      component: () => import('@/views/Cataloging/importExport/export'),
      name: 'CatalogingExport',
      meta: {
        perms: ['GET /admin/cataExport/export'],
        title: '编制表导出',
        noCache: true
      },
      hidden: false
    }
    ]
  },
  {
    // path: 'report',
    // component: () => import('@/views/router/index'),
    // redirect: 'noredirect',
    // name: 'CatalogingReport',
    // meta: {
    //   perms: ['GET /admin/cataInfoReport/list', 'GET /admin/cataRequireReport/list'],
    //   title: '目录报告',
    //   noCache: true
    // },
    // hidden: false,
    // children: [{
    path: 'infoCatalogingReport',
    component: () => import('@/views/Cataloging/report/infoCatalogingReport'),
    name: 'infoCatalogingReport',
    meta: {
      perms: ['GET /admin/cataInfoReport/list'],
      title: '信息资源目录报告',
      noCache: true
    }
    // }
    /**,
    {
      path: 'requirementCatalogingReport',
      component: () => import('@/views/Cataloging/report/requirementCatalogingReport'),
      name: 'requirementCatalogingReport',
      meta: {
        perms: ['GET /admin/cataRequireReport/list'],
        title: '信息需求目录报告',
        noCache: true
      }
    }
     */
    // ]
  },
    //  {
    //   path: 'submit',
    //   component: () => import('@/views/file/submit'),
    //   name: 'fileSubmit',
    //   meta: {
    //     perms: ['GET /admin/fileUploadRel/list1'],
    //     title: '文件上报',
    //     noCache: true
    //   },
    //   hidden: false
    // }
  ]
},
{
  path: '/fileReport',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  meta: {
    perms: ['GET /admin/fileUploadRel/list1', 'GET /admin/fileUploadRel/list2'],
    title: '文件上报',
    icon: 'muluGL'
  },
  name: 'fileReport',
  children: [
    {
      path: 'submit',
      component: () => import('@/views/file/submit'),
      name: 'fileSubmit',
      meta: {
        perms: ['GET /admin/fileUploadRel/list1'],
        title: '文件上报',
        noCache: true
      },
      hidden: false
    },
    {
      path: 'fileaudit',
      component: () => import('@/views/file/audit'),
      name: 'fileAudit',
      meta: {
        perms: ['GET /admin/fileUploadRel/list2'],
        title: '文件审核',
        noCache: true
      },
      hidden: false
    }
  ]
},
{
  path: '/catalogingManagement',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  meta: {
    perms: ['GET /admin/cataInfoApprove/cataInfoList', 'GET /admin/cataInfoApprove/approveList'],
    title: '目录管理',
    icon: 'muluGL'
  },
  name: 'catalogingManagement',
  children: [
    // {
    // path: 'audit',
    // component: () => import('@/views/router/index'),
    // redirect: 'noredirect',
    // name: 'catalogingManagementAudit',
    // meta: {
    //   perms: ['GET /admin/cataInfoApprove/cataInfoList', 'GET /admin/cataInfoApprove/approveList', 'POST /admin/infoApprove/approve', 'GET /admin/cataInfoApprove/cataInfoList2', 'GET /admin/cataInfoApprove/approveList2', 'POST /admin/infoApprove/approve2'],
    //   title: '目录审核',
    //   noCache: true
    // },
    // children: [
    {
      path: 'catalogingManagementAuditFirst',
      component: () => import('@/views/Cataloging/catalogingManagement/audit'),
      name: 'catalogingManagementAuditFirst',
      meta: {
        perms: ['POST /admin/cataInfoApprove/approve'],
        title: '目录初审',
        noCache: true
      }
    },
    {
      path: 'catalogingManagementAuditSecond',
      component: () => import('@/views/Cataloging/catalogingManagement/audit2'),
      name: 'catalogingManagementAuditSecond',
      meta: {
        perms: ['POST /admin/cataInfoApprove/approve2'],
        title: '目录终审',
        noCache: true
      },
      hidden: false
    },
    {
      path: 'catalogingManagementAuditDelete',
      component: () => import('@/views/Cataloging/catalogingManagement/auditDelete'),
      name: 'catalogingManagementAuditDelete',
      meta: {
        perms: ['POST /admin/cataInfoApprove/approveDelete'],
        title: '删除审核',
        noCache: true
      },
      hidden: false
    },
    // ]
    // },
    // {
    //   path: 'fileaudit',
    //   component: () => import('@/views/file/audit'),
    //   name: 'fileAudit',
    //   meta: {
    //     perms: ['GET /admin/fileUploadRel/list2'],
    //     title: '文件审核',
    //     noCache: true
    //   },
    //   hidden: false
    // }
  ]
},
{
  path: '/informationCenter',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  meta: {
    perms: ['GET /admin/fileCenterRel/list1', 'GET /admin/fileCenterRel/list2'],
    title: '资料中心',
    icon: 'ziliao'
  },
  hidden: false,
  name: 'informationCenter',
  children: [{
    path: 'informationDownload',
    component: () => import('@/views/information/download'),
    name: 'download',
    meta: {
      perms: ['GET /admin/fileCenterRel/list2'],
      title: '资料下载',
      noCache: true
    }
  },
  {
    path: 'informationPublish',
    component: () => import('@/views/information/publish'),
    name: 'publish',
    meta: {
      perms: ['GET /admin/fileCenterRel/list1'],
      title: '资料发布',
      noCache: true
    }
  }
  ]
},
{
  path: '/system',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  name: 'systemManage',
  meta: {
    title: '系统管理',
    icon: 'xitong'
  },
  children: [{
    path: 'catalogClassify',
    component: () => import('@/views/system/catalogClassify'),
    name: 'catalogClassify',
    meta: {
      perms: ['GET /admin/dictAssetCate/list'],
      title: '目录分类管理',
      noCache: true
    }
  }, {
    path: 'dataEncoding',
    component: () => import('@/views/system/dataEncoding'),
    name: 'dataEncoding',
    meta: {
      perms: ['GET /admin/sysDict/list'],
      title: '数据元标准',
      noCache: true,
    }
  },
  {
    path: 'organization',
    component: () => import('@/views/system/organization'),
    name: 'organization',
    meta: {
      perms: ['GET /admin/org/list'],
      title: '组织机构管理',
      noCache: true
    }
  }, {
    path: 'systemModule',
    component: () => import('@/views/system/systemModule'),
    name: 'systemModule',
    meta: {
      perms: ['GET /admin/sysDict/list'],
      title: '系统模块管理',
      noCache: true
    },
    hidden: true
  }, {
    path: 'informationItem',
    component: () => import('@/views/system/informationItem'),
    name: 'informationItem',
    meta: {
      perms: ['GET /admin/archBusiUviewStrConfig/list'],
      title: '信息项管理',
      noCache: true
    }
  }],
},
{
  path: '/sys',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  name: 'sysManage',
  meta: {
    title: '用户管理',
    icon: 'yonghu'
  },
  children: [{
    path: 'admin',
    component: () => import('@/views/sys/admin'),
    name: 'admin',
    meta: {
      perms: ['GET /admin/user/list', 'POST /admin/user/create', 'POST /admin/user/update', 'POST /admin/user/delete'],
      title: '用户管理',
      noCache: true
    }
  },
  {
    path: 'role',
    component: () => import('@/views/sys/role'),
    name: 'role',
    meta: {
      perms: ['GET /admin/role/list', 'POST /admin/role/create', 'POST /admin/role/update', 'POST /admin/role/delete', 'GET /admin/role/permissions', 'POST /admin/role/permissions'],
      title: '角色管理',
      noCache: true
    }
  }
  ]
},
{
  path: '/log',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  name: 'logManage',
  meta: {
    title: '日志管理',
    icon: 'rizhi'
  },
  children: [{
    path: 'operation',
    component: () => import('@/views/log/operation'),
    name: 'operation',
    meta: {
      perms: ['GET /admin/log/list'],
      title: '操作日志',
      noCache: true
    }
  }]
},
{
  path: '/profile',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  children: [{
    path: 'password',
    component: () => import('@/views/profile/password'),
    name: 'password',
    meta: {
      title: '修改密码',
      noCache: true
    }
  }],
  hidden: true
},

{
  path: '*',
  redirect: '/404',
  hidden: true
}
]
