import Vue from 'vue'
import Vuex from 'vuex'
import { getCookies } from '@/utils/auth';
import { findCurrentUserButton } from "@/api/role";
Vue.use(Vuex)

let stateStore = new Vuex.Store({
    modules: {
        user: {
            state: {
                UserButtons: "",
                theBreadcrumb1: "",
                theBreadcrumb2: "",
                theId: "",
                expand: "",
            },
            // 同步
            mutations: {
                UserButtonsFun(state, data) {
                    state.UserButtons = data;
                },
                theBreadcrumb1Fun(state, data) {
                    state.theBreadcrumb1 = data;
                },
                theBreadcrumb2Fun(state, data) {
                    state.theBreadcrumb2 = data;
                },
                theIdFun(state, id) {
                    state.theId = id;
                },
                expandFun(state, data) {
                    state.expand = data;
                }
            },
            // 异步
            actions: {
                removeLogin({ commit }) {
                    // commit("userinfoName", "");
                },
                setTheBreadcrumb1({ commit }, action) {
                    commit("theBreadcrumb1Fun", action);
                },
                setTheBreadcrumb2({ commit }, action) {
                    commit("theBreadcrumb2Fun", action);
                },
                async setUserButtons({ commit }, action) {
                    let buttons = await findCurrentUserButton({ userId: JSON.parse(getCookies('userInfo')).id });
                    if (buttons.data.code === 1) {
                        commit("UserButtonsFun", JSON.stringify(buttons.data.data));
                    }
                    return JSON.stringify(buttons.data.data)
                },
                setTheId({ commit }, action) {
                    commit("theIdFun", action);
                },
                setExpand({ commit }, action) {
                    commit("expandFun", action);
                }
            }
        }
    },
    getters: {
        UserButtons: state => state.UserButtons,
        theBreadcrumb1: state => state.theBreadcrumb1,
    },
});
export default stateStore;