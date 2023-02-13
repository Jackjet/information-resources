<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { getLoginUser, clearSsoCookie } from "@/api/user.js";
import { getCookies, removeCookies } from '@/utils/auth';
// import bus from '@/utils/bus'
export default {
  name: 'app',
  components: {
  },
  watch: {

    '$route': {
      immediate: true,
      handler() {
        getLoginUser().then(res => {
          if (res.data.code === 1) {
            if (res.data.data.preferred_username !== JSON.parse(getCookies('userInfo')).account) {
              removeCookies('userInfo');
              window.location.reload();
            }
          } else {
            clearSsoCookie().then(req => {
              window.location.reload();
            })
          }
        })
      }
    },
  }
}
</script>

<style lang="scss">
.el-message {
  min-width: 270px !important;
}
</style>
