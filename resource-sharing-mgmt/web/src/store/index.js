import Vue from 'vue'
import Vuex from 'vuex'
import user from './user/user';
import getters from './getters'
Vue.use(Vuex)

// 全局状态分模块管理有利于维护 举例user用户模块 新建自己的文件引入抛出模块即可

export default new Vuex.Store({
    modules: {
        user
    },
    getters
})