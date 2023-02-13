
import axios from 'axios'
import { Loading, MessageBox, Message  } from 'element-ui'

import transmit from '@/utils/transmit'

const service = axios.create({
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
    let temp = service.interceptors.response
    let errorData = JSON.stringify(error, null, "\t");
    // 错误抛到业务代码
    // if (JSON.parse(errorData).message.indexOf('401') !== -1) {
    //   Message.error("您的账号已被禁用或已失效, 请联系管理员或重新登录")
    //   transmit.$emit('logout')
    // }else{
    // }
    return Promise.resolve(errorData)
  }
)

export default service
