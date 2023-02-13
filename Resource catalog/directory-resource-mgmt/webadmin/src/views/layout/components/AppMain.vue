<template>
  <section class="app-main">
    <!-- <breadcrumb v-if="showBreadcrumb" class="breadcrumb-container" /> -->
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
// import Breadcrumb from '@/components/Breadcrumb'
export default {
  name: 'AppMain',
  components: {
    //Breadcrumb
  },
  data() {
    return {
    }
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.fullPath
    },
    // showBreadcrumb() {
    //   return this.$route.name !== 'Dashboard'
    // }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 65px);
  // height: 100%;
  padding: 20px;
  /*padding-top: 22px;*/
  width: 100%;
  position: relative;
  overflow: hidden;
  background: #EFF0F5;
}
.fixed-header+.app-main {
  padding-top: 50px;
}
.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }
  .fixed-header+.app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>

