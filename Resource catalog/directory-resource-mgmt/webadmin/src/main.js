import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import Element from 'element-ui'
import $ from 'jquery'
import './styles/element-variables.scss'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control
// permission control

import * as filters from './filters' // global filters

import permission from '@/directive/permission/index.js' // 权限判断指令

import Print from '@/utils/print' // 打印
import echarts from 'echarts'

import axios from "axios";

import VueKeycloakJs from '@dsb-norge/vue-keycloak-js'
Vue.use(Print)

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

// vuekeycloak
Vue.use(VueKeycloakJs, {
  init: {
    // Use 'login-required' to always require authentication
    // If using 'login-required', there is no need for the router guards in router.js
    //  onLoad: 'login-required'
    onLoad: 'login-required',
    checkLoginIframe: false//防止登陆后重复刷新
  },
  logout: {
    redirectUri: window.location.protocol + "//" + window.location.hostname + ":" + window.location.port
  },
  config: {
    // url: 'http://8.140.114.226:9001/auth/',
    url: 'http://10.113.0.45:9001/auth/',
    clientId: 'resource_catalog_js',
    realm: 'tianjin'
  },
  onReady: (keycloak) => {
    keycloak.loadUserProfile().success((data) => {
      if (JSON.parse(localStorage.getItem('userinfo')).adminInfo.nickName !== keycloak.tokenParsed.preferred_username) {
        axios({
          method: "post",
          // url: "http://8.140.114.226:8084/admin/auth/login",
          url: "http://10.113.0.31:5080/admin/auth/login",
          data: {
            access_token: keycloak.token
          }
        }).then(res => {
          localStorage.setItem('token', res.data.data.token)
          localStorage.setItem('userinfo', JSON.stringify(res.data.data))
          store.dispatch('GetUserInfo').then(res => { // 拉取user_info
            const perms = res.data.data.perms // note: perms must be a array! such as: ['GET /aaa','POST /bbb']
            const roles = res.data.data.roles //
            store.dispatch('GenerateRoutes', { perms, roles }).then(() => { // 根据perms权限生成可访问的路由表
              router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
              // next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
            })
          }).catch((err) => {
            console.log(err)
            // Message.error('验证失败，请重新登录')
          })
        })
      }
    });
    var vm = new Vue({
      el: '#app',
      router,
      store,
      render: h => h(App)
    })
  }
})


Vue.directive('permission', permission)
// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false
Vue.prototype.$echarts = echarts

/*
var vm = new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
*/
