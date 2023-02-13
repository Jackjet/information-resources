import Cookies from 'js-cookie'

const app = {
  state: {
    sidebar: {
      opened: !+Cookies.get('sidebarStatus'),
      withoutAnimation: false
    },
    device: 'desktop',
    language: Cookies.get('language') || 'en',
    size: Cookies.get('size') || 'medium',
    breadcrumb: [],
    rebackdeptid: '',
    infoReBackDeptId: '',
    OSReBackDeptId: ''
  },
  mutations: {
    TOGGLE_SIDEBAR: state => {
      if (state.sidebar.opened) {
        Cookies.set('sidebarStatus', 1)
      } else {
        Cookies.set('sidebarStatus', 0)
      }
      state.sidebar.opened = !state.sidebar.opened
      state.sidebar.withoutAnimation = false
    },
    CLOSE_SIDEBAR: (state, withoutAnimation) => {
      Cookies.set('sidebarStatus', 1)
      state.sidebar.opened = false
      state.sidebar.withoutAnimation = withoutAnimation
    },
    TOGGLE_DEVICE: (state, device) => {
      state.device = device
    },
    SET_LANGUAGE: (state, language) => {
      state.language = language
      Cookies.set('language', language)
    },
    SET_SIZE: (state, size) => {
      state.size = size
      Cookies.set('size', size)
    },
    SET_ReBackDeptId: (state, deptid) => {
      state.rebackdeptid = deptid
      // Cookies.set('size', size)
    },
    SET_InfoReBackDeptId: (state, deptid) => {
      state.infoReBackDeptId = deptid
      // Cookies.set('size', size)
    },
    SET_OSReBackDeptId: (state, deptid) => {
      state.OSReBackDeptId = deptid
      // Cookies.set('size', size)
    },
    Set_BreadCrumb: (state, breadcrumb) => {
      state.breadcrumb = breadcrumb
      // Cookies.set('size', size)
    }
  },
  actions: {
    toggleSideBar({ commit }) {
      commit('TOGGLE_SIDEBAR')
    },
    closeSideBar({ commit }, { withoutAnimation }) {
      commit('CLOSE_SIDEBAR', withoutAnimation)
    },
    toggleDevice({ commit }, device) {
      commit('TOGGLE_DEVICE', device)
    },
    setLanguage({ commit }, language) {
      commit('SET_LANGUAGE', language)
    },
    setSize({ commit }, size) {
      commit('SET_SIZE', size)
    },
    setReBackDeptId({ commit }, deptid) {
      commit('SET_ReBackDeptId', deptid)
    },
    setInfoReBackDeptId({ commit }, deptid) {
      commit('SET_InfoReBackDeptId', deptid)
    },
    setReBackOSId({ commit }, deptid) {
      commit('SET_OSReBackDeptId', deptid)
    },
    setBreadCrumb({ commit }, arr) {
      commit('Set_BreadCrumb', arr)
    }
  }
}

export default app
