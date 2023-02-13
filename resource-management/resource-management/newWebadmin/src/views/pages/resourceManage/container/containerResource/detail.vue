<template>
  <el-main class="main">
    <apiDetail v-if="this.type==='3'"
    :group-id="groupId"
    :id="id"
    >
    </apiDetail>
    <apiDesignDetail v-if="this.type==='0'"
               :group-id="groupId"
               :id="id"
    >
    </apiDesignDetail>
    <dataDetail v-if="this.type==='1'"
               :group-id="groupId"
               :id="id"
    >
    </dataDetail>
  </el-main>
</template>

<script>
import apiDetail from './assembly/api'
import apiDesignDetail from './assembly/apiDesign'
import dataDetail from './assembly/data'

export default {
  components: {apiDetail,apiDesignDetail,dataDetail},
  data() {
    return{
      groupId: '',
      id:'',
      type:''
    }
  },
  created () {
    this.groupId = this.$route.query.groupId
    this.id = this.$route.query.id
    this.type = this.$route.query.type
  },
}
</script>
<style lang="scss" scoped>
</style>
