<template>
  <div class="tables">
    <el-table :data="tableList" style="width: 100%">
      <el-table-column align="center" prop="title" label="标题"></el-table-column>
      <el-table-column align="center" prop="categoryName" label="栏目名称"></el-table-column>
      <el-table-column align="center" prop="author" label="作者"></el-table-column>
      <el-table-column align="center" prop="createTime" label="创建时间"></el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button @click="editClick(scope.row)" type="text" icon="el-icon-edit" size="small">编辑</el-button>
          <el-button
            @click="detailsClick(scope.row)"
            type="text"
            icon="el-icon-document"
            size="small"
          >详情</el-button>
          <el-button
            @click="deleteClick(scope.row)"
            type="text"
            icon="el-icon-delete"
            size="small"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
export default {
  props: {
    tableList: Array
  },
  data() {
    return {
      tableData: []
    };
  },
  mounted() {
    console.log(this.tableList, "props");
  },
  methods: {
    // 编辑
    editClick(row) {
      this.$router.push({
        name: "addArticle",
        path: "/addArticle",
        query: this.$route.query,
        params: {
          id: row.id,
          is: "edit"
        }
      });
    },
    // 删除
    deleteClick(row) {
      this.$emit("deleteClick", row.id);
    },
    // 详情
    detailsClick(row) {
      this.$router.push({
        name: "addArticle",
        path: "/addArticle",
        query: this.$route.query,
        params: {
          id: row.id,
          is: "details"
        }
      });
    }
  }
};
</script>
<style lang="scss">
.el-button--text {
  color: #5a6e8a;
}
.el-table thead {
  color: #909399;
  font-weight: 500;
}
</style>