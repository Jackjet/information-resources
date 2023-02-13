import 'babel-polyfill';
import Vue from 'vue'
import App from './App.vue'

Vue.use(Api);
import store from './store' //全局状态 vuex
// 挂载全局请求方法
import Api from './api/index';
import axios from 'axios';
import './assets/style/common.css' // 公共样式
import router from './router'; //路由
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'swiper/dist/css/swiper.min.css';
import 'swiper/dist/js/swiper.min.js';
import './utils/bus.js';
import { setToken, getToken } from './utils/storage';
import config from './config/index.js';

router.beforeEach(async (to, from, next) => {
  if (getToken()) {
    next();
  } else {
    axios({
      method: "get",
      // url: "http://8.140.114.226:8085/web/system/webUser/login",
      // url: "http://10.113.0.20:5080/web/system/webUser/login",
      url: config.baseURL + 'web/system/webUser/login',
      params: {},
    }).then(rep => {
      if (rep.data.code === 1) {
        store.dispatch('user/setName', rep.data.data.name);
        store.dispatch('user/setToken', rep.data.data.token);
        store.dispatch('user/setUserId', rep.data.data.id);
        store.dispatch('user/setOrgId', rep.data.data.organizations[0]);
        setToken(rep.data.data.token);
        window.location.reload();
        // next()
      } else {
        next()
      }
    }).catch(err => {
      console.log(err)
    })
  }
});

Vue.config.productionTip = false;
Vue.use(ElementUI);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
