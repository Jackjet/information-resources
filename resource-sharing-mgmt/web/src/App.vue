<template>
  <div id="app">
    <Header></Header>
    <router-view></router-view>
    <Footer></Footer>
  </div>
</template>

<script>
import Header from "com/headerNav";
import Footer from "com/footer";
import bus from '@/utils/bus';
import { setToken, removeToken } from '@/utils/storage';
import config from '@/config/index.js';
export default {
  name: "App",
  components: {
    Header,
    Footer,
  },
  watch: {
    '$route': {
      immediate: true,
      handler(to) {
        this.getLoginUser().then(res => {
          if (res.code === 1) {
            bus.$emit("name", res.data.preferred_username);
            // console.log(res.data.preferred_username, this.$store.state.user.name, "88888888888888888")
            if (res.data.preferred_username !== this.$store.state.user.name) {
              this.$store.dispatch('user/removeLogin');
              removeToken();
              this.isLoginUser().then(req => {
                if (req.code === 1) {
                  this.$store.dispatch('user/setToken', req.data.token);
                  this.$store.dispatch('user/setName', req.data.name);
                  this.$store.dispatch('user/setUserId', req.data.id);
                  this.$store.dispatch('user/setOrgId', req.data.organizations[0]);
                  setToken(req.data.token);
                }
              }).catch(err => {
                console.log(err)
              })
            }
          } else {
            this.clearSsoCookie().then(() => {
              bus.$emit("name", "");
              if (to.path === '/demand' && to.path !== '/login'
                || (to.path.indexOf('/personal') === 0) && to.path !== '/login' ||
                to.path === '/catalogue/apiApplay' && to.path !== '/login') {
                this.$confirm('此操作需要登录, 是否跳转至登录页?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  window.location.href = "login.html?url=" + config.baseURL + "web/index.html"
                }).catch(() => {
                  window.location.href = "index.html";
                });
              }
            })
          }
        })
      }
    },
  }
};
</script>

<style>
#app {
  width: 100%;
  height: 100%;
}
</style>
