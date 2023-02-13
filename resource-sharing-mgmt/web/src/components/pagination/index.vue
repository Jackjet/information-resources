<template>
  <div class="pagination-box">
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.page" :page-sizes="[5, 10, 20]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper" :total="total">
    </el-pagination>
  </div>
</template>
<script>
export default {
  props: {
    total: {
      type: Number,
      default: () => {
        return 0;
      },
    },
  },
  data() {
    return {
      page: {
        page: 1,
        size: 5,
      }
    };
  },
  methods: {
    Page(val) {
      this.page.page = val
    },
    // 每页条数
    handleSizeChange(val) {
      this.page.size = val;
      this.$emit("sizeChange", val);
    },
    // 当前页数
    handleCurrentChange(val) {
      this.page.page = val;
      this.$emit("currentChange", val);
    },
  },
};
</script>
<style lang="scss" scope>
.pagination-box {
  height: 60px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
</style>