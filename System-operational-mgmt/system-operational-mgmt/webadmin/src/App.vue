<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { getLoginUser, clearSsoCookie } from '@/api/user'
import { removeToken } from "@/utils/auth";
export default {
  name: 'App',
  watch: {
    '$route': {
      immediate: true,
      handler() {
        getLoginUser().then(res => {
          if (res.data.code === 1) {
            // console.log(res.data.data.preferred_username, JSON.parse(sessionStorage.getItem('UserInfo').name), "0000000000000")
            if (res.data.data.preferred_username !== JSON.parse(sessionStorage.getItem('UserInfo')).name) {
              localStorage.removeItem('token');
              sessionStorage.removeItem('UserInfo');
              removeToken();
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
