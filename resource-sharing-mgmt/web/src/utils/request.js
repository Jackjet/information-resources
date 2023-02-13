// 引用环境全局环境
import config from '../config/index.js';
import axios from 'axios'
import { getToken, removeToken } from './storage.js';
import store from '../store';
// 创建axios请求实例
const request = axios.create({
    baseURL: config.baseURL, // 设置跨域代理接口统一的前置地址
    timeout: 50000,
    headers: {
        'Content-Type': 'application/json; charset=utf-8',
    }
})
// 添加请求拦截器
request.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    if (getToken()) {
        config.headers['Authorization'] = 'token ' + getToken()
    }
    return config
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
})

// 添加响应拦截器
request.interceptors.response.use(function (response) {
    // 只返回数据
    return response.data
}, function (error) {
    console.log(error, '拦截器')
    const status = error.response.status

    if (status >= 500) {
        alert('服务繁忙请稍后再试')
    }
    if (status === 401) {
        alert("您的账号登录已失效, 请重新登录")
        store.dispatch('user/removeLogin');
        removeToken();
        window.location.href = "login.html?url=" + config.baseURL + "web/index.html";
    }
    // else if (status >= 400) {
    //     alert(error.response.data.message)
    // }
    // 对响应错误做点什么
    return Promise.reject(error)
})


const get = (url, data) => request.get(url, {
    params: data ? Object.assign(data, { time: "time" + Date.now() }) : { time: "time" + Date.now() }
})
const post = (url, data) => request.post(url, data)
const DELETE = (url, data) => request.delete(url, data)
const head = (url, data) => request.head(url, data)
const options = (url, data) => request.options(url, data)
const put = (url, data) => request.put(url, data)
const patch = (url, data) => request.patch(url, data)

export {
    get,
    post,
    DELETE,
    put,
    head,
    options,
    patch
}