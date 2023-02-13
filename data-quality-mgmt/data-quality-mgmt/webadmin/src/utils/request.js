import axios from 'axios'
import bus from '@/utils/bus'
import { Message } from 'element-ui'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 100000 // request timeout
})

service.interceptors.request.use(
  config => {
    let currentUser = JSON.parse(sessionStorage.getItem("UserInfo"))
    if (currentUser && currentUser.token) {
      config.headers['Authorization'] = 'token ' + currentUser.token
    }
    if (config.method == 'get') {
      config.params = {
        _t: Date.parse(new Date()),
        ...config.params
      }
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    return response
  },
  error => {
    const res = error.response.data
    if (res.status === 401) {
      Message.error("您的账号登录已失效, 请重新登录")
      sessionStorage.removeItem("UserInfo")
      window.location.reload()
      // bus.$emit('logout')
    } else {
      Message.error("未知错误")
      console.error(error)
    }
    return Promise.resolve(error)
  }
)
export default service
