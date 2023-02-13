import Cookies from 'js-cookie'
import Vue from 'vue'

const TokenKey = 'X-Resourcecatalog-Admin-Token'
// const TokenKey = `${Vue.prototype.$keycloak.token}` 

export function getToken() {
  // 注释更换Cookies为sessionStorage
  // return Cookies.get(TokenKey)
  return localStorage.getItem('token');
}

export function setToken(token) {
  // 注释更换Cookies为sessionStorage
  // return Cookies.set(TokenKey, token)
  return localStorage.setItem('TokenKey', token)
}

export function removeToken() {
  // 注释更换Cookies为sessionStorage
  // return Cookies.remove(TokenKey)
  return localStorage.removeItem('TokenKey')
}
