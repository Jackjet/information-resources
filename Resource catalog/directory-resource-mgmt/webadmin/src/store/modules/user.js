import { loginByUsername, logout, getUserInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { Message, MessageBox } from 'element-ui'
import Vue from 'vue'

const user = {
  state: {
    user: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    portalUrl: '',
    avatar: '',
    introduction: '',
    roles: [],
    perms: [],
    userid: '',
    propertyId: '',
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PORTALURL: (state, portalUrl) => {
      state.portalUrl = portalUrl
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMS: (state, perms) => {
      state.perms = perms
    },
    SET_USERID: (state, id) => {
      state.userid = id
    },
    SET_PROPERTYId: (state, propertyId) => {
      state.propertyId = propertyId
    }
  },

  actions: {
    //keycloakLogin action keycloakLogin action主要是在Keycloak登录成功后设置token
    keycloakLogin({ dispatch, commit }, accessToken) {
      return new Promise((resolve, reject) => {
        //commit('SET_TOKEN', accessToken)
        //setToken(accessToken)
        resolve()
      })
    },
    //keyLoadLogOut action是通过Keycloak进行登出操作，成功后便清除本地保存的信息
    // isError true:为请求出错等 非用户手动推出
    keyLoadLogOut({ dispatch, commit, state }, isError) {
      return new Promise((resolve, reject) => {
        if (isError) {
          // 注释更换Cookies为sessionStorage
          commit('SET_TOKEN', '')
          commit('SET_NAME', '')
          commit('SET_ROLES', [])
          commit('SET_PERMS', [])
          removeToken()
          resolve()
          // localStorage.removeItem("UserButtons")
          // localStorage.removeItem("UserMenus")
          localStorage.removeItem("userinfo")
          localStorage.removeItem("UserInfo")
          localStorage.removeItem('token')
          localStorage.clear();
          window.location.reload();
        } else {
          dispatch('LogOut').then(res => {
            // 注释更换Cookies为sessionStorage
            commit('SET_TOKEN', '')
            commit('SET_NAME', '')
            commit('SET_ROLES', [])
            commit('SET_PERMS', [])
            removeToken()
            resolve()
            // localStorage.removeItem("UserButtons")
            // localStorage.removeItem("UserMenus")
            localStorage.removeItem("userinfo")
            localStorage.removeItem("UserInfo");
            localStorage.removeItem('token')
            localStorage.clear();
            window.location.reload();
          })
        }
      })
    },

    // 用户名登录
    LoginByUsername({ commit }, accessToken) {
      return new Promise((resolve, reject) => {
        loginByUsername(accessToken).then(response => {
          const token = response.data.data.token
          commit('SET_TOKEN', token)
          // 注释更换Cookies为sessionStorage
          // setToken(token)
          localStorage.setItem('token', token)
          localStorage.setItem('userinfo', JSON.stringify(response.data.data))
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
          const data = response.data.data
          console.log(data, "用户信息")
          if (data.perms && data.perms.length > 0 && data.roles && data.roles.length > 0) { // 验证返回的perms是否是一个非空数组
            commit('SET_PERMS', data.perms)
          } else {
            MessageBox.alert('您没有任何权限或被禁用，无法登录', '警告', {
              confirmButtonText: '确定',
              type: 'error'
            })
            reject('getInfo: perms must be a non-null array !')
          }
          commit('SET_USERID', data.id)
          commit('SET_PROPERTYId', data.propertyId)
          commit('SET_ROLES', data.roles)
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_PORTALURL', data.portalUrl)
          commit('SET_INTRODUCTION', data.introduction)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMS', [])
          removeToken()
          resolve()
          // sessionStorage.removeItem("UserButtons");
          // sessionStorage.removeItem("UserMenus");
          localStorage.removeItem("userinfo");
          localStorage.removeItem("UserInfo");
          localStorage.removeItem("token");
          localStorage.clear();
          window.location.reload();
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    // FedLogOut({ commit }) {
    //   return new Promise(resolve => {
    //     commit('SET_TOKEN', '')
    //     removeToken()
    //     resolve()
    //   })
    // },

    // 动态修改权限
    ChangeRoles({ commit, dispatch }, role) {
      return new Promise(resolve => {
        commit('SET_TOKEN', role)
        setToken(role)
        getUserInfo(role).then(response => {
          const data = response.data
          commit('SET_ROLES', data.roles)
          commit('SET_PERMS', data.perms)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_PORTALURL', data.portalUrl)
          commit('SET_INTRODUCTION', data.introduction)
          dispatch('GenerateRoutes', data) // 动态修改权限后 重绘侧边菜单
          resolve()
        })
      })
    }
  }
}

export default user
