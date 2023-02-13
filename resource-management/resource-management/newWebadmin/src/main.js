import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/nprogress' // permission control
import 'babel-polyfill'
import VueCodeMirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import '../theme/index.css'

Vue.config.productionTip = false
Vue.use(VueCodeMirror)
require('promise.prototype.finally').shim();
new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
