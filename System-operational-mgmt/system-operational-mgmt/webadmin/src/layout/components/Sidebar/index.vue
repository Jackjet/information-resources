
<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" />
    <SidebarItem ref="ChildTree"></SidebarItem>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './navMenu'
// import SidebarItem from './SidebarItem'
// import variables from '@/styles/variables.scss'
import { PermissionFindAll } from '@/api/user'
import { sessionStorageGet } from '@/utils/storage'

export default {
  components: { SidebarItem, Logo },
  computed: {
    // ...mapGetters([
    //   'sidebar'
    // ]),
    // routes() {
    //   return this.$router.options.routes
    // },
    // activeMenu() {
    //   const route = this.$route
    //   const { meta, path } = route
    //   // if set path, the sidebar will highlight the path you set
    //   if (meta.activeMenu) {
    //     return meta.activeMenu
    //   }
    //   return path
    // },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
  },
  mounted() {
    this.getMenuData()
  },
  methods: {
    async getMenuData() {
      let that = this
      let menu = []
      let parameters = { userId: sessionStorageGet('id') }
      const response = await PermissionFindAll(parameters)
      let Data = response.data.data
      if (response.data.code === 1) {
        Data.map(function (v, k) {
          let sub = []
          v.childs.map(function (v1) {
            if (v1.childs.length > 0) {
              let subs = []
              v1.childs.forEach((v2) => {
                subs.push({ id: v2.entity.id, index: v2.entity.route, title: v2.entity.name })
              })
              sub.push({ id: v1.entity.id, index: v1.entity.route, title: v1.entity.name, subs: subs })
            } else {
              if (v1.entity.id !== '1-4') {
                sub.push({ id: v1.entity.id, index: v1.entity.route, title: v1.entity.name })
              }
            }
          })
          if (v.entity.name === '首页') {
            menu.push({ id: v.entity.id, icon: v.entity.icon, index: v.entity.route, title: v.entity.name })
          } else {
            if (v.entity.name !== '系统管理') {
              menu.push({ id: v.entity.id, icon: v.entity.icon, index: v.entity.route, title: v.entity.name, subs: sub })
            }
            // menu.push({ id: v.entity.id, icon: v.entity.icon, index: v.entity.route, title: v.entity.name, subs: sub })
          }
        })
        that.$refs.ChildTree.SetParameters(menu)
      }
    }
  },
}
</script>
