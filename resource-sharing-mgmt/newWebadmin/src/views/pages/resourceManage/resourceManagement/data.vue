<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="demo-ruleForm">
        <el-form-item label="数据库ip:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.ip"></el-input>
        </el-form-item>
        <el-form-item label="数据库端口:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.port"></el-input>
        </el-form-item>
        <el-form-item label="数据库账号:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item label="数据库密码:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item label="数据库备注:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.detail"></el-input>
        </el-form-item>
        <el-form-item label="表名称:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.tableName"></el-input>
        </el-form-item>
      </el-form>
    </el-col>
    <div class="demo-drawer__footer drawer_footer">
      <el-button size="medium" @click="goBack">返回</el-button>
    </div>
  </el-main>
</template>

<script>
import { assetDataExFindByUviewId } from "@/api/fileData.js";
export default {
  data() {
    return {
      title: '详情',
      ruleForm: {},
      formLabelWidth: "120px",
    }
  },
  created() {
    this.resourceUseInfoDetail()
  },
  methods: {
    // 详情
    async resourceUseInfoDetail() {
      const that = this
      let data = { id: this.$route.query.uviewId }
      that.loading = true
      const response = await assetDataExFindByUviewId(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
      } else {
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      this.$router.go(-1)
    },
  }
}
</script>
<style lang="scss" scoped>
.main {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;
  .main-center {
    padding: 20px;
    border-radius: 5px;
    width: 65%;
    margin: 0 auto;
  }
}
.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}
</style>