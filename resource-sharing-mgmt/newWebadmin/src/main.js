import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '../theme/index.css'
import '@/nprogress' // permission control
import 'babel-polyfill'
import store from './store'

require('promise.prototype.finally').shim();

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')

Vue.config.productionTip = false
