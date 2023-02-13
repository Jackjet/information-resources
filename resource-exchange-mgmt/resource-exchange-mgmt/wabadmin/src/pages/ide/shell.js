import i18n from './assets/locales'
import Vue from 'vue'
import Shell from './shell/Shell.vue'
import './style/shell.css'
import axios from 'axios'
import store from './shell/store'
import FileNode from './shell/components/filetree/FileNode.vue'
import FolderNode from './shell/components/filetree/FolderNode.vue'
import Message from 'vue-multiple-message'
import VueCodeMirror from 'vue-codemirror'
import * as http from '../../assets/js/request'
import Interface from '../../../static/interface/data'
import ElementUI from 'element-ui'

Vue.use(VueCodeMirror)
Vue.use(ElementUI)

window.$rxbus = new Vue()

Vue.component('FileNode', FileNode)
Vue.component('FolderNode', FolderNode)

Vue.prototype.Interface = Interface.constApi
Vue.prototype.PortUrl = Interface.PortUrl
Vue.prototype.FileSeparator = Interface.FileSeparator
Vue.prototype.request = http
Vue.prototype.$message = Message
Vue.prototype.$axios = axios

//全局点击事件监听
Vue.prototype.globalClick = function (callback) {
  document.getElementById('main').onclick = function (event) {
    callback(event);
  };
};

new Vue({
  el: '#shell',
  i18n,
  store,
  render: h => h(Shell),
  created () {
    let url = window.location.search.substring(1)
    let str = url.split('&')
    Vue.prototype.appid = str[0].split('=')[1]
    Vue.prototype.integrationId = str[1].split('=')[1]
  }
})
