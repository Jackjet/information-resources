<template>
  <el-main class="main">
    <el-col :span='24'>
      页面已跳转
      <!-- <ifr :item="item"></ifr> -->
    </el-col>
  </el-main>
</template>

<script>
import { mapGetters } from 'vuex'
import ifr from '@/components/iframe/iframe'

export default {
  name: 'iframe',
  components: {
    ifr,

  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  mounted() {
    window.open(process.env.VUE_APP_DSS_API);
  },
  data() {
    return {
      item: {
       url: 'https://www.baidu.com/',
       isScroll: false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .main{
    height:100%;
  }

</style>
