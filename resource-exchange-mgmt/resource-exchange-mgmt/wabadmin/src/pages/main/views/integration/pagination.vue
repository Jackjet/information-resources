<template>
  <div class="pagination">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.page"
      :page-sizes="pageSizes"
      :page-size="page.limit"
      layout="total, sizes, prev, pager, next, jumper" :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  props: {
    total: {
      type: Number,
      default: () => {}
    }
  },

  data () {
    return {
      pageSizes: [10, 20, 50, 100],
      page: {
        page: 1,
        limit: 20
      }
    }
  },

  methods: {
    handleSizeChange (val) {
      this.page.limit = val
      this.page.page = 1
      this.$emit('pageChange', this.page)
    },

    handleCurrentChange (val) {
      this.page.page = val
      this.$emit('pageChange', this.page)
    },

    changePage () {
      this.page.page = 1
    }
  }
}
</script>

<style scoped lang="scss">
  .pagination {
    float: right;
    padding: 1% 2% 4% 0px;
  }
</style>
