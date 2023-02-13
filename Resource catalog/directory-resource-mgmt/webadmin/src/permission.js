import router from './router'
import store from './store'
import { Message, MessageBox } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import { getToken } from '@/utils/auth' // getToken from cookie

NProgress.configure({ showSpinner: false })// NProgress Configuration

// permission judge function
function hasPermission(perms, permissions) {
  if (perms.indexOf('*') >= 0) return true // admin permission passed directly
  if (!permissions) return true
  return perms.some(perm => permissions.indexOf(perm) >= 0)
}

const whiteList = ['/login', '/auth-redirect']// no redirect whitelist
const handleNext = (to, from, next) => {
  if (store.getters.perms.length === 0) { // 判断当前用户是否已拉取完user_info信息
    store.dispatch('GetUserInfo').then(res => { // 拉取user_info
      const perms = res.data.data.perms // note: perms must be a array! such as: ['GET /aaa','POST /bbb']
      const roles = res.data.data.roles //
      store.dispatch('GenerateRoutes', { perms, roles }).then(() => { // 根据perms权限生成可访问的路由表
        router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
        next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
      })
    }).catch((err) => {
      console.log(err)
      // Message.error('验证失败，请重新登录')
    })
  } else {
    // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
    if (hasPermission(store.getters.perms, to.meta.perms)) {
      next()
    } else {
      next({ path: '/401', replace: true, query: { noGoBack: true } })
    }
    // 可删 ↑
  }
}

router.beforeEach((to, from, next) => {
  if ((from.name === 'infoCatalogMaintain' || from.name === 'infoCatalogDetail' || from.name === 'versionManagement' || from.name === 'versionComparison') && to.name !== 'infoCatalogList') {
    store.dispatch('setInfoReBackDeptId', '')
  }
  if ((from.name === 'requirementCatalogMaintain' || from.name === 'requirementCatalogDetail' || from.name === 'relatedBusinessMatters') && to.name !== 'requirementCatalogList') {
    store.dispatch('setReBackDeptId', '')
  }
  NProgress.start() // start progress bar
  if (getToken()) { // determine if there has token
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      handleNext(to, from, next)
    }
  } else if (router.app.$keycloak.authenticated) {
    store.dispatch('LoginByUsername', router.app.$keycloak.token).then(res => {
      handleNext(to, from, next)
    })
  } else {
    /* has no token*/
    const loginUrl = router.app.$keycloak.createLoginUrl()
    window.location.replace(loginUrl)
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
