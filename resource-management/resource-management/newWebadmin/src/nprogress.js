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

import { userLogin } from '@/api/login.js';

NProgress.configure({ showSpinner: false }) // NProgress Configuration
router.beforeEach((to, from, next) => {
  let search = location.search.split('?UserInfo=1&')[1]
  if (search) {
    let obj = {}
    search.split('&').forEach((v, i) => {
      let key = v.split('=')[0]
      obj[key] = key === 'roles' ? v.split('=')[1].split(',') : v.split('=')[1]
    })
    sessionStorage.setItem('UserInfo', JSON.stringify(obj))
  }
  NProgress.start()
  // if (to.path !== '/login') {
  if (sessionStorage.getItem("UserInfo")) { // 判断当前的token是否存在 ； 登录存入的token
    next();
  } else {
    userLogin().then(res => { })
    // next({path: '/login'})
  }
  // } else {
  //   console.log(5)
  //   next();
  // }
});
router.afterEach(() => {
  NProgress.done()
})
