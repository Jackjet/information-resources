/*
 * @Author: your name
 * @Date: 2020-09-14 16:36:54
 * @LastEditTime: 2020-09-16 16:50:08
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\message.js
 */
import request from '@/utils/dynamic'
// API列表
export function getMethod(routUrl,method,headers,params,body) {
  return request({
    url: routUrl,
    params:params,
    data:body,
    method: method,
    headers: headers
  })
}

