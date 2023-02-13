<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  // watch: {
  //   '$route': {
  //     immediate: true,
  //     handler() {
  //       console.log(this.$keycloak, "8888888888")
  //       this.$keycloak.loadUserProfile().then(res => {
  //         console.log(res, "========")
  //       }).catch(err => {
  //         // location.reload();
  //       })
  //     }
  //   },
  // }
}
</script>
