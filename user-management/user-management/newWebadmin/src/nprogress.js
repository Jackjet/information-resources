/*
 * @Author: your name
 * @Date: 2020-08-12 16:07:56
 * @LastEditTime: 2020-10-12 00:38:16
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\permission.js
 */

import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { isAutomaticLogin } from '@/api/user1'

NProgress.configure({ showSpinner: false }) // NProgress Configuration
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  // if (to.path !== '/login') {
  if (sessionStorage.getItem("UserInfo")) { // 判断当前的token是否存在 ； 登录存入的token
    next()
  } else {
    let rep = await isAutomaticLogin()
    console.log(rep)
    if (rep.data.code === 1) {
      sessionStorage.setItem("UserInfo", JSON.stringify(rep.data.data))
      location.href = "index.html";
      // next()
    }
  }
  // } else {
  //   next();
  // }
});
router.afterEach(() => {
  NProgress.done()
})
