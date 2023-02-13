
<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" />
    <SidebarItem  ref="ChildTree"></SidebarItem>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './navMenu'
// import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import { sessionStorageGet } from '@/utils/storage'

export default {
  components: { SidebarItem, Logo },
  computed: {
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
  }
}
</script>
