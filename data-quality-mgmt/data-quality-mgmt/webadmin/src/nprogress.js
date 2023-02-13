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
import { isAutomaticLogin } from '@/api/user'
import { findCurrentUserButton, findCurrentUserSidebar } from "@/api/role"

async function initRole(id) {
  let buttons = await findCurrentUserButton({ userId: id });
  if (buttons.data.code === 1) {
    sessionStorage.setItem("UserButtons", JSON.stringify(buttons.data.data))
  }
  let menus = await findCurrentUserSidebar({ userId: id })
  if (menus.data.code === 1) {
    let arr = []
    menus.data.data[0].menuTree.forEach((v, i) => {
      if (v.name == '系统管理') {
        let child = []
        v.children.forEach((value, index) => {
          if (value.name != '菜单管理') {
            child.push(value)
          }
        })
        v.children = child
      }
      arr.push(v)
    })
    sessionStorage.setItem("UserMenus", JSON.stringify(arr))
    // this.SetParameters(menus.data.data[0].menuTree)
  }
  if (menus.data.code === 1 && buttons.data.code === 1) {
    return true
  } else {
    return false
  }
}

NProgress.configure({ showSpinner: false }) // NProgress Configuration
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  // if (to.path !== '/login') {
  if (sessionStorage.getItem("UserInfo")) { // 判断当前的token是否存在 ； 登录存入的token
    next()
  } else {
    let rep = await isAutomaticLogin()
    if (rep.data.code === 1) {
      sessionStorage.setItem("UserInfo", JSON.stringify(rep.data.data))
      let isReady = await initRole(rep.data.data.id)
      // console.log(isReady)
      if (isReady) {
        next()
      }
    }
  }
  // } else {
  //   next()
  // }
});
router.afterEach(() => {
  NProgress.done()
})
