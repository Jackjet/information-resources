import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default
new Vuex.Store({
  state: {
    isLoading: true,
    activedFile: null,
    projectComponentList: [],
    componentsHints: {},
    needSaveFlag:false,
    rootFile:[]
  },

  mutations: {
    activeFile (state, file) {
      state.activedFile = file
    },

    projectChange (state, project) {
      state.project = project
    },

    themeChange (state, theme) {
      state.theme = theme
    },

    isLoading (state, isLoading) {
      state.isLoading = isLoading
    },
    projectComponentList (state, projectComponentList) {
      state.projectComponentList = projectComponentList
    },
    componentsHints (state, componentsHints) {
      state.componentsHints = componentsHints
    },
    needSaveFlag (state,needSaveFlag){
      state.needSaveFlag = needSaveFlag
    },
    rootFile (state,rootFile){
      state.rootFile = rootFile
    }
  },

  actions: {
  }
})
