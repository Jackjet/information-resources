import axios from 'axios'
import { Loading } from 'element-ui'
// import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

const Status = (status) => {
  let message = ''
  // 这一坨代码可以使用策略模式进行优化
  switch (status) {
    case 400:
      message = '请求错误(400)'
      break
    case 401:
      message = '未授权，请重新登录(401)'
      break
    case 403:
      message = '拒绝访问(403)'
      break
    case 404:
      message = '请求出错(404)'
      break
    case 408:
      message = '请求超时(408)'
      break
    case 500:
      message = '服务器错误(500)'
      break
    case 501:
      message = '服务未实现(501)'
      break
    case 502:
      message = '网络错误(502)'
      break
    case 503:
      message = '服务不可用(503)'
      break
    case 504:
      message = '网络超时(504)'
      break
    case 505:
      message = 'HTTP版本不受支持(505)'
      break
    default:
      message = `连接出错(${status})!`
  }
  return `${message}，请检查网络或联系管理员！`
}
// request interceptor
service.interceptors.request.use(
  config => {
    // Loading.service({ fullscreen: true, text: '加载中' })
    if (getToken()) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['Authorization'] = 'token ' + getToken()
    }
    return config
  },
  error => {
    Loading.service({ fullscreen: true, text: '加载中' }).close()
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    // Loading.service({ fullscreen: true, text: '加载中' }).close()
    const res = response.data
    let msg = ''
    // if (res.code < 200 || res.code >= 300 && res.code !== 1) {
    //   // 处理http错误，抛到业务代码
    //   msg = Status(status)
    //   if (typeof response.data === 'string') {
    //     response.data = { msg }
    //   } else {
    //     response.data.msg = msg
    //   }
    // }
    if (res.code !== 1) {
      // 处理http错误，抛到业务代码
      // msg = Status(status)
      msg = res.msg
      if (typeof response.data === 'string') {
        response.data = { msg }
      } else {
        response.data.msg = msg
      }
    }
    return response
  },
  error => {
    // 错误抛到业务代码
    // if (error.indexof('401') !== -1) {
    //   store.dispatch('user/resetToken')
    //   // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    // }
    error.data = {}
    error.data.msg = '请求超时或服务器异常，请检查网络或联系管理员！'
    return Promise.resolve(error)
  }
)

export default service
