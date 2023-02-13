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
      <el-form-item label="消息主题:" :label-width="this.formLabelWidth">
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="true"
          placeholder=""
          v-model="ruleForm.messageTitle"
        ></el-input>
      </el-form-item>
      <el-form-item label="来源:" :label-width="this.formLabelWidth" >
        <el-input  maxlength = '100'  placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.messageModule"></el-input>
      </el-form-item>
      <el-form-item label="内容:" :label-width="this.formLabelWidth">
        <el-input :rows='4' maxlength = '200' type="textarea"  placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.messageContent"></el-input>
      </el-form-item>
      <el-form-item label="接收时间:" :label-width="this.formLabelWidth" >
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
import { userMessagefind } from "@/api/message.js";
export default {
  data() {
    return {
      title: '详情',
      ruleForm: {
        createByName: '',
        messageTitle: "",
        messageContent: '',
        messageModule: '',
        status: '',
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
      const response = await userMessagefind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
        that.ruleForm.result = that.ruleForm.status === 0 ? '未读': '已读'
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      this.$router.push('/messages')
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