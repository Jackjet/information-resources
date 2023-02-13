<template>
  <div class="InputForm">
    <el-form :inline="true" class="el-InputForm">
      <el-form-item>
        <el-input
          clearable
          size="medium"
          :placeholder="placeholder"
          prefix-icon="el-icon-search"
          v-model="SearchData"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="medium" icon="el-icon-search" @click="SearchNoteList">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="medium" icon="el-icon-plus" @click="add">添加</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  props: {
    placeholder: String
  },
  data() {
    return {
      SearchData: ""
    };
  },
  methods: {
    SearchNoteList() {
      this.$emit("searchClick", this.SearchData);
    },
    add() {
      this.$emit("addClick");
    }
  }
};
</script>
<style lang="scss">
.el-InputForm {
  padding: 5px;
  background: #f2f2f2;
  display: inline-block;
  border-radius: 4px;
  width: 98%;
  margin-bottom: 20px;
  -webkit-box-shadow: 0 3px 8px #e2dfdf;
  box-shadow: 0 3px 8px #e2dfdf;
  border: 1px solid #ecedf0;
}
.el-InputForm .el-form-item {
  float: left;
  margin: 15px 5px !important;
}
.el-button--primary {
  color: #fff;
  background-color: #5a6e8a;
  border-color: #5a6e8a;
}
.el-button--primary:hover {
  background: #7b8ba1;
  border-color: #7b8ba1;
  color: #fff;
}
</style>