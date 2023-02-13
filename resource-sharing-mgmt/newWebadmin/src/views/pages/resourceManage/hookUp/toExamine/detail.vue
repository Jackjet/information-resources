<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="信息资源名称:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" :disabled="true" v-model="ruleForm.uviewNm"></el-input>
        </el-form-item>
        <el-form-item label="信息资源代码:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.uviewNo"></el-input>
        </el-form-item>

        <el-form-item label="提供单位:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.orgName"></el-input>
        </el-form-item>
        <el-form-item label="信息资源摘要:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' clearable size="medium" :disabled="true" v-model="ruleForm.uviewDesc"></el-input>
        </el-form-item>

        <el-form-item label="挂接类型:" :label-width="this.formLabelWidth">
          <el-select size="medium" clearable v-model="ruleForm.type" :disabled="true">
            <el-option label="文件" :value="0"></el-option>
            <el-option label="数据库" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <!-- 文件 -->
        <el-form-item v-if="ruleForm.type===0" label="上传文件:" :label-width="this.formLabelWidth">
          <a :href="urlHttp+ruleForm.fileDownloadUri">{{ruleForm.name}}</a>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===0" label="描述:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.detail"></el-input>
        </el-form-item>
        <!-- 数据库 -->
        <el-form-item v-if="ruleForm.type===1" label="数据库ip:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.ip"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库端口:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.port"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库账号:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' clearable size="medium" :disabled="true" v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库密码:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库备注:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.detail"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="表名称:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="ruleForm.tableName"></el-input>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>
</template>

<script>
import { assetExFind } from "@/api/fileData.js";
export default {
  data() {
    return {
      urlHttp: process.env.VUE_APP_BASE_API,
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
      let data = { id: this.$route.query.id }
      that.loading = true
      const response = await assetExFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      // this.$router.push('/toExamineList')
      this.$router.go(-1);
    },
    downfile() {

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