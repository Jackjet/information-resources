import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/nprogress' // permission control
import 'babel-polyfill'
import echarts from 'echarts'
import '../theme/index.css'

Vue.config.productionTip = false
Vue.prototype.$echarts = echarts
require('promise.prototype.finally').shim();
new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
