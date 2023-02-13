import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import Vue from 'vue'
import router from "../router";

// create an axios instance
const service = axios.create({
  baseURL: location.protocol + '//' + window.location.host + '/admin', // api 的 base_url
  timeout: 30000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // Do something before request is sent
    /*if (store.getters.token) {
      // 让每个请求携带token-- ['X-Litemall-Admin-Token']为自定义key 请根据实际情况自行修改
      config.headers['X-Resourcecatalog-Admin-Token'] = getToken()
    }*/
    config.headers['X-Resourcecatalog-Admin-Token'] = getToken()
    config.headers['Keycloak-Token'] = `${Vue.prototype.$keycloak.token}`
    config.headers.Authorization = `Bearer ${Vue.prototype.$keycloak.token}`
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.errno === 900) {
      MessageBox.alert('资源目录系统中未找到该用户，请退出切换账号', '错误', {
        confirmButtonText: '确定',
        type: 'error'
      }).then(() => {
        store.dispatch('keyLoadLogOut', true).then(() => {
          router.app.$keycloak.token ? router.app.$keycloak.logoutFn() : window.location.replace(router.app.$keycloak.createLoginUrl())
        })
      })
      return Promise.reject('error')
    } else if (res.errno === 501 || res.errno === 401) {
      // MessageBox.alert('系统未登录，请重新登录', '错误', {
      //   confirmButtonText: '确定',
      //   type: 'error'
      // }).then(() => {
      store.dispatch('keyLoadLogOut', true).then(() => {
        router.app.$keycloak.token ? router.app.$keycloak.logoutFn() : window.location.replace(router.app.$keycloak.createLoginUrl())
      })
      // })
      return Promise.reject('error')
    } else if (res.errno === 502) {
      MessageBox.alert('系统内部错误，请联系管理员维护', '错误', {
        confirmButtonText: '确定',
        type: 'error'
      })
      return Promise.reject('error')
    } else if (res.errno === 503) {
      MessageBox.alert('请求业务目前未支持', '警告', {
        confirmButtonText: '确定',
        type: 'error'
      })
      return Promise.reject('error')
    } else if (res.errno === 504) {
      MessageBox.alert('更新数据已经失效，请刷新页面重新操作', '警告', {
        confirmButtonText: '确定',
        type: 'error'
      })
      return Promise.reject('error')
    } else if (res.errno === 505) {
      MessageBox.alert('更新失败，请再尝试一次', '警告', {
        confirmButtonText: '确定',
        type: 'error'
      })
      return Promise.reject('error')
    } else if (res.errno === 506) {
      MessageBox.alert('没有操作权限，请联系管理员授权', '错误', {
        confirmButtonText: '确定',
        type: 'error'
      })
      return Promise.reject('error')
    } else if (res.errno && res.errno !== 0) {
      /* Message.error(res.errmsg || '出错了')*/
      // 非5xx的错误属于业务错误，留给具体页面处理
      return Promise.reject(response)
    } else if (!res.errno) {
      return response
    } else {
      return response
    }
  }, error => {
    console.log('err' + error)// for debug
    Message({
      message: '登录连接超时（后台不能连接，请联系系统管理员）',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  })

export default service
