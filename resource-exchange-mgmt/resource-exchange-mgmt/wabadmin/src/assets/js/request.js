import * as session from '../../assets/js/common'
import {Message} from 'element-ui'

function getToken () {
  let currentUser = session.default.session('currentUser')
  return currentUser ? currentUser.token : ''
}

function ifError(error) {
  let status = error.response.status
  if (status >= 500) {
      Message.error('服务器异常')
  } else if (status >= 400) {
    if (res.status === 401) {
      Message.warning("您的账号登录已失效, 请重新登录")
    } else {
      Message.error("未知错误")
      console.error(error)
    }
  }
}

// 返回相应状态吗
function checkStatus (response) {
  if (response && (response.status === 200 || response.status === 304 || response.status === 400)) {
    return response
  }
  return {
    status: -404,
    msg: '服务器异常'
  }
}

/**
 * 登录请求 无 token
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const signGET = function (that, url, parameters) {
  let config = {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    params: parameters
  }
  return that.$axios.get(url, config).then(
    (response) => {
      return checkStatus(response)
    }
  )
}

/**
 * GET 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataGet = function (that, url, parameters) {
  let config = {
    headers: {
      'Authorization': 'token ' + getToken()
    },
    params: parameters
  }
  return that.$axios.get(url, config).then(
    response => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}
/**
 * GET 请求
 * 特殊get请求，专用于autoInput组件
 */

export const autoInputGet = function (that, url, parameters) {
  let config = {
    headers: {
      'Authorization': 'token ' + getToken()
    },
    params: parameters,
    cancelToken: that.$axios.CancelToken(function executor(c) {
      // executor 函数接收一个 cancel 函数作为参数
      that.cancel = c;
    })
  }
  return that.$axios.get(url, config).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * GET 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataGetWithAppid = function (that, url, appid, parameters) {
  let config = {
    headers: {
      'Authorization': 'token ' + getToken(),
      'appid': appid
    },
    params: parameters
  }
  return that.$axios.get(url, config).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * POST 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataPost = function (that, url, parameters) {
  return that.$axios({
    method: 'post',
    url: url,
    data: parameters,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * POST 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataPostWithAppid = function (that, url, appid, parameters) {
  return that.$axios({
    method: 'post',
    url: url,
    data: parameters,
    headers: {
      'Authorization': 'token ' + getToken(),
      'Content-Type': 'application/json',
      'appid': appid
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * PUT 请求
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataPut = function (that, url, parameters) {
  return that.$axios({
    method: 'put',
    url: url,
    data: parameters,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * DELETE 请求
 * 拼接地址栏方式传递参数
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataDelete = function (that, url, parameters) {
  return that.$axios({
    method: 'delete',
    url: url,
    params: parameters,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * DELETE 请求    /id
 * 拼接地址栏方式传递参数
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataDeleteById = function (that, url) {
  return that.$axios({
    method: 'delete',
    url: url,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * DELETE 请求
 * 以对象的形式传递参数
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 */

export const dataDeleteByObj = function (that, url, parameters) {
  return that.$axios({
    method: 'delete',
    url: url,
    data: parameters,
    headers: {
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      return checkStatus(response)
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

/**
 * 表单下载文件请求（带token）
 * @param {*} that Vue 对象
 * @param {*} url  请求 url
 * @param {*} parameters 请求参数
 * @param {string} file 下载文件名称
 */

export const downloadFile = function (that, url, parameters, file) {
  return that.$axios({
    method: 'get',
    url: url,
    data: parameters,
    responseType: 'blob',
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': 'token ' + getToken()
    }
  }).then(
    (response) => {
      let result = window.URL.createObjectURL(new Blob([response]))
      let link = document.createElement('a')
      link.style.display = 'none'
      link.href = result
      link.setAttribute('download', file)
      document.body.appendChild(link)
      link.click()
    }
  ).catch(error => {
    ifError(error)
    return error
  })
}

export const outExcel = function (that, url, data, info) {
      const time = data;
      let formData = new FormData();
      if (time != null) {
        for (var p in time) {
          formData.append(p, time[p]);
        }
      }
      console.log(url,formData)
      return that.$axios({
        // method: "post",
        // url: url,
        // data: formData,
        // headers: {
        //   Authorization: "token " +  getToken(),
        //   "Content-Type": "multipart/form-data"
        // },
        // responseType: "blob"
        method: 'get',
        url: url,
        data: data,
        responseType: 'blob',
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': 'token ' + getToken()
        }
      })
        .then(data => {
          if (data.data.type === "application/json") {
            var reader = new FileReader();
            reader.onloadend = function() {
              let res = JSON.parse(reader.result);
              if (res && res.msg) {
                Message.warning(res.msg + "," + res.data);
              }
            };
            reader.readAsText(data.data);
            return;
          }
          let url = window.URL.createObjectURL(new Blob([data.data]));
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", info + ".xlsx");
          document.body.appendChild(link);
          link.click();
            Message.success('导出成功')
        })
        .catch(data => {
            Message.error('导出失败')
        });
  
}