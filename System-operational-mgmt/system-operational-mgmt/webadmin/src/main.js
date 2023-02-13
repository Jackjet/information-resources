import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css


import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/assets/iconfont/iconfont.css'
import '@/permission' // permission control
import '@babel/polyfill'
import common from '@/utils/common'
// import kk from 'keycloak-js'
// import axios from "axios";
// import { sessionStorageSet } from '@/utils/storage'
// import { setToken, getToken, removeToken } from "@/utils/auth";

// setToken('7a56d073bc8a4006a39402226bcdf9ed')

// const initOptions = {
//   url: process.env.VUE_APP_SSO_API,
//   realm: 'share',
//   clientId: 'yxglpt',
//   onLoad: 'login-required'
// }
// let keycloak = kk(initOptions)
// keycloak.init({ onLoad: initOptions.onLoad, promiseType: 'native' })
//   .then((authenticated) => {
//     console.log(keycloak)
//     console.log(authenticated)
//     axios({
//       method: "post",
//       url: process.env.VUE_APP_BASE_API + "/webadmin/user/ssoSignIn",
//       data: {
//         ssoToken: keycloak.token
//       }
//     }).then(row => {
//       if (row.data.code === 1) {
//         sessionStorageSet('id', row.data.data.id)
//         sessionStorageSet('name', row.data.data.name)
//         setToken(row.data.data.token)
//         router.push({ path: '/dashboard' })
//       } else {
//         Vue.prototype.$message({
//           type: "error",
//           message: row.data.msg
//         });
//         setTimeout(() => {
//           let url = process.env.VUE_APP_SSOLOGOUT_API;
//           sessionStorageSet("id", undefined);
//           sessionStorageSet("name", undefined);
//           removeToken();
//           window.open(url, "_self");
//         }, 500)
//       }
//     }).catch(data => {
//       console.log(data);
//     });
//   }).catch(error => {
//     console.log('Authenticated Failed', error)
//   })

Vue.use(ElementUI)
Vue.prototype.common = common
Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
