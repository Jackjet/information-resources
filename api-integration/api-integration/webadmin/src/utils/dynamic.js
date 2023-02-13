
import axios from 'axios'
import { Loading, MessageBox, Message  } from 'element-ui'

import transmit from '@/utils/transmit'

const service = axios.create({
  baseURL: process.env.VUE_APP_KONG_HTTP_API,
  timeout: 100000
})
// request interceptor
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    Loading.service({ fullscreen: true, text: '加载中' }).close()
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    // if(res.code === 401){
    //   transmit.$emit('logout')
    // }
    return response
  },
  error => {
    let errorData = JSON.stringify(error)
    // 错误抛到业务代码
    // if (JSON.parse(errorData).message.indexOf('401') !== -1) {
    //   Message.error("您的账号已被禁用或已失效, 请联系管理员或重新登录")
    //   transmit.$emit('logout')
    // }else{
    error.data = {}
    error.data.msg = '请求超时或服务器异常，请检查网络或联系管理员！'
    // }
    return Promise.resolve(error)
  }
)

export default service
