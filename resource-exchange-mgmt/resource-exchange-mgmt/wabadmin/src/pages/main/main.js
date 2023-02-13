import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import axios from 'axios'
import cookies from 'js-cookie'
import kk from 'keycloak-js'
import '@babel/polyfill'

import Interface from '../../../static/interface/data'
import * as util from '../../assets/js/common'
import * as http from '../../assets/js/request'
import store from './store/store'

import '../../assets/scss/global.scss'
import '../../assets/iconFont/iconfont.css'
import record from "./views/subPage/itemAdminProject/services/record";

/**
 * 钩子函数 判断路由是否通过登录页面.
 * @param route 即将进入的路由对象.
 * @param routeFrom 当前导航即将离开的路由.
 * @param routeNext 终止导航.
 */
router.beforeEach((route, routeFrom, routeNext) => {
  console.log(route)
  if (route.matched.some(record => record.meta.requireAuth)) {
    // 抓取登录状态如果未登录返回登录页面
    if (!util.default.session('currentUser')) {
      if (util.default.session('type')) {
        switch (Number(util.default.session('type'))) {
          case 1:
            routeNext('/admin')
            break
          case 2:
            routeNext('/tenant')
            break
          case 3:
            routeNext('/login')
            break
          default:
            routeNext('/')
            break
        }
      } else {
        routeNext('/')
      }
    } else {
      routeNext()
    }
  } else {
    axios({
      method: "get",
      url: Interface.constApi.UserLogin.login,
      params: {
        type: 'integration'
      }
    }).then(row => {
      if (row.data.code === 1) {
        util.default.session('currentUser', row.data.data)
        util.default.session('type', 3)
        router.push({ path: '/index' })
      } else {
        console.log(row.data.data.msg);
      }
    }).catch(data => {
      console.log(data);
    });
  }
})
Vue.prototype.$axios = axios
Vue.prototype.Interface = Interface.constApi
Vue.prototype.PortUrl = Interface.PortUrl
Vue.prototype.common = util.default
Vue.prototype.request = http
Vue.prototype.keycloak = kk
Vue.prototype.cookie = cookies
Vue.config.productionTip = false
Vue.use(ElementUI)

new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
})
