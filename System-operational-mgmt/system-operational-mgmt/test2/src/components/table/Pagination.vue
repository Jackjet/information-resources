<template>
  <div class="pagination">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.page"
      :page-sizes="pageSizes"
      :page-size="page.limit"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
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
  data() {
    return {
      pageSizes: [5, 10, 20],
      page: {
        page: 1,
        limit: 20
      }
    }
  },

  methods: {
    Page(val) {
      this.page.page = val
    },
    handleSizeChange(val) {
      this.page.limit = val
      this.$emit('pageChange', this.page)
    },
    handleCurrentChange(val) {
      this.page.page = val
      this.$emit('pageChange', this.page)
    }
  }
}
</script>

<style scoped lang="scss">
  .pagination {
    float: right;
    padding: 1% 4% 2% 0px;
  }
</style>

