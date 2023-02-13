<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="数据模板:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" :disabled="true" placeholder="" v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="数据模板描述:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.description"></el-input>
        </el-form-item>
        <el-form-item label="创建人:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createById"></el-input>
        </el-form-item>
        <el-form-item label="创建时间:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createTime"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回列表</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { ruleTemplateFindOne } from "@/api/quality.js"
export default {
  data() {
    return {
      title: '详情',
      ruleForm: {},
      formLabelWidth: "120px"
    }
  },
  created() {
    this.userDetail()
  },
  methods: {
    // 详情
    async userDetail() {
      const res = await ruleTemplateFindOne({ id: this.$route.query.id });
      if (res.data.code === 1) {
        this.ruleForm = res.data.data;
      } else {
        this.$message.error(res.data.msg)
      }
    },
    goBack() {
      this.$router.push('/quality')
    }
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