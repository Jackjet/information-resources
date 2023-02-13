import axios from 'axios'
import bus from '@/utils/bus'
import {Message} from 'element-ui'

const service = axios.create({
    timeout: 100000 // request timeout
})

service.interceptors.request.use(
    config => {
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// response interceptor
service.interceptors.response.use(
    response => {
        return response
    },
    error => {
        return Promise.resolve(error)
    }
)
export default service
