/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

/*获取树列表*/
export function getGroupList (params) {
  return request({
    url: '/webadmin/groupInfo/getGroupList',
    method: 'get',
    params
  })
}


/*获取树列表*/
export function getTree (params) {
  return request({
    url: '/webadmin/groupInfo/getTree',
    method: 'get',
    params
  })
}

/*添加*/
export function insertGroup(data) {
  return request({
    url: '/webadmin/groupInfo/insert',
    method: 'post',
    data
  })
}

/*更新*/
export function updateGroup(data) {
  return request({
    url: '/webadmin/groupInfo/update',
    method: 'put',
    data
  })
}

/*删除*/
export function deleteGroup(id) {
  return request({
    url: '/webadmin/groupInfo/delete',
    method: 'delete',
    params: { id }
  })
}
