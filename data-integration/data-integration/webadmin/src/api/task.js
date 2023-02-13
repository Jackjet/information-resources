/*
 * @Author: your name
 * @Date: 2020-08-16 16:50:58
 * @LastEditTime: 2020-09-27 11:06:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\api\user.js
 */
import request from '@/utils/request'

// 添加任务
export function add(data) {
  return request({
    url: '/webadmin/task/insert',
    method: 'post',
    data
  })
}

// 删除任务
export function deleteById(id) {
  return request({
    url: '/webadmin/task/deleteById',
    method: 'delete',
    params: { id }
  })
}

// 更新任务
export function update(data) {
  return request({
    url: '/webadmin/task/update',
    method: 'put',
    data
  })
}

// 获取任务列表
export function findAll(params) {
  return request({
    url: '/webadmin/task/findAll',
    method: 'get',
    params
  })
}

// 运行任务
export function runTask(params) {
  return request({
    url: '/webadmin/task/run',
    method: 'get',
    params
  })
}

// 停止任务
export function stopTask(params) {
  return request({
    url: '/webadmin/task/stop',
    method: 'get',
    params
  })
}

// 刷新状态
export function getStatus(params) {
  return request({
    url: '/webadmin/task/getStatus',
    method: 'get',
    params
  })
}

// 任务日志详情
export function getTaskLogsInfo(params) {
  return request({
    url: '/webadmin/task/logs/findInfo',
    method: 'get',
    params
  })
}

// 任务日志
export function getTaskLogs(params) {
  return request({
    url: '/webadmin/task/logs/findAll',
    method: 'get',
    params
  })
}

// 任务日志详情
export function getTaskLogsAnalysis(params) {
  return request({
    url: '/webadmin/task/logs/findAnalysisInfo',
    method: 'get',
    params
  })
}

// 获取任务组列表
export function getTree(params) {
  return request({
    url: '/webadmin/task/group/getTree',
    method: 'get',
    params
  })
}

/*添加任务组*/
export function insertTaskGroup(data) {
  return request({
    url: '/webadmin/task/group/insert',
    method: 'post',
    data
  })
}

/*更新任务组*/
export function updateTaskGroup(data) {
  return request({
    url: '/webadmin/task/group/update',
    method: 'put',
    data
  })
}

// 删除任务
export function deleteTaskGroupById(id) {
  return request({
    url: '/webadmin/task/group/deleteById',
    method: 'delete',
    params: { id }
  })
}

/*导入作业*/
export function uploadTaskKettleFiles(data) {
  return request({
    url: '/webadmin/task/kettleFiles/uploadFile',
    method: 'post',
    data
  })
}

// 下载任务文件
export function downloadTaskKettleFiles(taskId) {
  return request({
    url: '/webadmin/task/kettleFiles/downloadFile',
    method: 'get',
    params:{ taskId }
  })
}

// 删除任务
export function deleteTaskKettleFiles(taskId) {
  return request({
    url: '/webadmin/task/kettleFiles/deleteFile',
    method: 'delete',
    params:{ taskId }
  })
}

//查询任务job文件
export function findByTaskId(params) {
  return request({
    url: '/webadmin/task/kettleFiles/findByTaskId',
    method: 'get',
    params
  })
}

//根据任务ID和类别查找任务的文件
export function findByTaskIdAndType(params) {
  return request({
    url: '/webadmin/task/kettleFiles/findByTaskIdAndType',
    method: 'get',
    params
  })
}

//根据任务ID和名称查找任务的文件
export function findByTaskIdAndName(params) {
  return request({
    url: '/webadmin/task/kettleFiles/findByTaskIdAndName',
    method: 'get',
    params
  })
}

//明细(天)
export function findAnalysisByDay(params) {
  return request({
    url: '/webadmin/task/analysis/findAnalysisByDay',
    method: 'get',
    params
  })
}

//明细(月)
export function findAnalysisByMonth(params) {
  return request({
    url: '/webadmin/task/analysis/findAnalysisByMonth',
    method: 'get',
    params
  })
}