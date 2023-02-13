
import router from './router'
// import store from './store'
// import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken, setToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { isAutomaticLogin } from '@/api/user1'

// NProgress.configure({ showSpinner: false }) // NProgress Configuration

// const whiteList = ['/'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  document.title = getPageTitle(to.meta.title)
  const hasToken = getToken()
  if (to.path !== '/login') {
    // debugger;
    if (sessionStorage.getItem("UserInfo")) { // 判断当前的token是否存在 ； 登录存入的token
      next()
    } else {
      let rep = await isAutomaticLogin()
      // console.log(rep, "========")
      if (rep.data.code === 1) {
        localStorage.setItem("token", rep.data.data.token)
        sessionStorage.setItem("UserInfo", JSON.stringify(rep.data.data))
        setToken(rep.data.data.token)
        next()
      }
    }
  } else {
    next();
  }
  // if (hasToken) {
  //   if (to.path === '/') {
  //     next({ path: '/dashboard' })
  //     NProgress.done()
  //   } else {
  //     next()
  //   }
  // }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
