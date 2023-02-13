<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
     <el-col class="main-center">
      <el-form
      :model="ruleForm"
      :rules="rules"
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
       <div class="demo-drawer__footer drawer_footer">
        <el-button size="medium" @click="goBack">返回列表</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>
  
</template>

<script>
import { operationLogFind } from "@/api/log.js"
export default {
  data() {
    return {
      title: '详情',
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
      const response = await operationLogFind(data)
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
      this.$router.push('/operationLogs')
    }
  }
}
</script>
<style lang="scss" scoped>
 .main{
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;
  .main-center{
    padding: 20px;
    border-radius: 5px;
    width: 65%;
    margin: 0 auto;
  }
 }
 .demo-drawer__footer{
   margin-top: 80px;
   text-align: center;
 }
</style>