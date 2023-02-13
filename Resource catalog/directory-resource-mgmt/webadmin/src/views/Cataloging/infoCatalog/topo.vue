<template>
  <el-container class="wrapper">
    <el-container>
      <el-main style="background-color: #ffffff">
        <AllView ref="allView" :current-id="currentId" :show-type="showType" :topo-svg-id="topoSvgId" :all-res-btn-id="allResBtnId" :part-res-btn-id="partResBtnId" />
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import AllView from '@/components/allView/AllView'
export default {
  components: {
    AllView
  },
  data() {
    return {
      showType: 'first',
      topoSvgId: 'topoSvgId1',
      allResBtnId: 'allResBtnId1',
      partResBtnId: 'partResBtnId1',
      departs: [],
      categorys: [],
      currentId: '',
      items: [{ 'name': '按部门' }],
      isTopo: true
    }
  },
  watch: {
  },
  created() {
    if (this.$route.params.currentId) {
      this.currentId = this.$route.params.currentId
    }
    if (this.$route.params.showType) {
      this.showType = this.$route.params.showType
    }
    //   this.getDepartCategry()
  },
  mounted() {
  },
  methods: {
    getDepartCategry() {
      const _this = this
      _this.departs = departs.result
      // this.categorys = category.result
      var allData = list
      var count = 0
      _this.departs.forEach(function(depart) {
        count = 0
        allData.forEach(function(res) {
          if (depart.dep_code === res.department.typeId) {
            count++
          }
        })
        depart.dep_name = depart.dep_name + '(' + count + ')'
      })
      this.categorys.forEach(function(category) {
        count = 0
        allData.forEach(function(res) {
          if ((category.categoryId + '') === res.serviceCategory.typeId) {
            count++
          }
        })
        category.categoryName = category.categoryName + '(' + count + ')'
      })
    }
  }
}
</script>
<style >

</style>
