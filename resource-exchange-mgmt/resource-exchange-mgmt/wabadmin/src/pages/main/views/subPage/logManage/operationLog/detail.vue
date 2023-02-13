<template>
  <el-main>
     <el-col class="main-center">
      <el-form
      :model="ruleForm"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
     >
      <el-form-item label="操作人:" :label-width="this.formLabelWidth">
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="true"
          placeholder=""
          v-model="ruleForm.createByName"
        ></el-input>
      </el-form-item>
      <el-form-item label="操作模块:" :label-width="this.formLabelWidth">
        <el-input  maxlength = '100'  placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.module"></el-input>
      </el-form-item>
      <el-form-item label="操作类型:" :label-width="this.formLabelWidth" >
        <el-input  maxlength = '100'  placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.api"></el-input>
      </el-form-item>
      <el-form-item label="操作内容:" :label-width="this.formLabelWidth">
        <el-input  maxlength = '200' :rows='4' type="textarea"  placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.contentMsg"></el-input>
      </el-form-item>
      <el-form-item label="操作结果:" :label-width="this.formLabelWidth" >
        <el-input  maxlength = '100'  placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.result"></el-input>
      </el-form-item>
      <el-form-item label="操作时间:" :label-width="this.formLabelWidth" >
        <el-input  maxlength = '100'  placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createTime"></el-input>
      </el-form-item>
       <div style="margin-bottom: 20px;">
        <el-button size="medium" @click="goBack">返回列表</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>
  
</template>

<script>
// import { operationLogFind } from "@/api/log.js"
export default {
  data() {
    return {
      ruleForm: {
        createByName: '',
        api: "",
        contentMsg: '',
        result: '',
        createTime: ''
      },
      formLabelWidth: "120px"
    }
  },
  created () {
    this.userDetail()
  },
  methods: {
    // 详情
    async userDetail() {
      const that = this
      let data = { id: this.$route.query.id }
      that.loading = true
      let url = this.Interface.logs.operationLogfind
      let response = await this.request.dataGet(that, url, data)
      // const response = await operationLogFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
        that.ruleForm.result = that.ruleForm.result === 0 ? '失败': '成功'
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      this.$router.push('/index/operationLog')
    }
  }
}
</script>
<style lang="scss" scoped>
 .demo-ruleForm {
  width: 700px;
  margin: 0 auto;
 }
</style>