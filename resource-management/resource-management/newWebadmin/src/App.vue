<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { getLoginUser, clearSsoCookie } from '@/api/login.js';
export default {
  name: 'app',
  watch: {
    '$route': {
      immediate: true,
      handler() {
        getLoginUser().then(res => {
          if (res.data.code === 1) {
            if (res.data.data.preferred_username !== JSON.parse(sessionStorage.getItem('UserInfo')).name) {
              sessionStorage.removeItem('UserInfo');
              location.href = process.env.VUE_APP_NAV_API;
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
