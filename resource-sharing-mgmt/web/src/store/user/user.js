// 用户状态管理
const user = {
    namespaced: true,
    state: {
        token: "",
        name: "",
        id: "",
        orgId: "",
    },
    mutations: {
        userName(state, body) {
            state.name = body;
        },
        userToken(state, body) {
            state.token = body;
        },
        userId(state, body) {
            state.id = body;
        },
        userOrgId(state, body) {
            state.orgId = body;
        }
    },
    actions: {
        removeLogin({ commit }) {
            commit("userName", "");
            commit("userToken", "");
            commit("userId", "");
            // commit("userOrgId", "");
        },
        setName({ commit }, action) {
            commit("userName", action);
        },
        setToken({ commit }, action) {
            commit("userToken", action);
        },
        setUserId({ commit }, action) {
            commit("userId", action);
        },
        setOrgId({ commit }, action) {
            commit("userOrgId", action);
        },
    }
}

export default user;