<template>
  <el-main class="main">
    <div><h4>{{ title }}</h4></div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm" v-loading="loading">
        <el-form-item label="报告标题:" :label-width="this.formLabelWidth" prop='name'>
          <el-input clearable maxlength='100' size="medium" :disabled="disabled" placeholder="请输入报告标题" v-model="ruleForm.name"/>
        </el-form-item>
        <el-form-item label="开始时间:" :label-width="this.formLabelWidth"  prop='startTime'>
         <el-date-picker
          v-model="ruleForm.startTime"
          type="month"
          format='yyyy-MM'
          value-format='yyyy-MM'
           :disabled="disabled"
          placeholder="选择开始时间">
        </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间:" :label-width="this.formLabelWidth"  prop='endTime'>
         <el-date-picker
          v-model="ruleForm.endTime"
          type="month"
          :disabled="disabled"
          format='yyyy-MM'
          value-format='yyyy-MM'
          placeholder="选择结束时间">
        </el-date-picker>
        </el-form-item>
        <el-form-item label="报告描述:" :label-width="this.formLabelWidth">
          <el-input maxlength='200' type="textarea" :rows="3" placeholder="请输入报告描述" clearable size="medium" :disabled="disabled" v-model="ruleForm.description"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="avatar" alt="">
    </el-dialog>
  </el-main>
</template>
<script>

import { qualityReportInsert,qualityReportFind } from "@/api/qualityReport.js"

export default {
  data() {
    return {
      title: '新增',
      rule:'完整性非空约束',
      id:'',
      disabled: false,
      disabledo: false,
      ruleForm: {
        name:'',
        startTime:'',
        endTime:'',
      },
      rules: {
        name: [{
          required: true,
          message: "请输入报告标题",
          trigger: "change",
        }],
        startTime: [{
          required: true,
          message: "请选择开始时间",
          trigger: "change",
        }],
        endTime: [{
          required: true,
          message: '请选择结束时间',
          trigger: 'change'
        }]
      },
    }
  },
  created() {
    this.title = this.$route.query.type
     if (this.$route.query.type === '详情') {
      this.disabled = true
      this.id = this.$route.query.id
      this.userDetail()

    }
  },
  methods: {
    
    // 详情
    async userDetail() {
      const that = this
      let data = {id: this.$route.query.id}
      that.loading = true
      const response = await qualityReportFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
      } else {
        that.$message.error(response.data.msg)
        return false
      }
    },
    submitForm(ruleForm) {
      const that = this
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          that.loading = true;
          let data = that.ruleForm
            // 新增
            qualityReportInsert(data).then((res) => {
              that.loading = false
              if (res.data.code === 1) {
                that.$message.success('新增成功')
                that.goBack()
              } else {
                that.$message.error(res.data.msg)
              }
            })
        } else {
          return false;
        }
      });
    },
    goBack() {
      this.$router.push('/qualityReport')
    },
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

    .avatar-uploader {
      width: 64px;
      height: 64px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .avatar-uploader-icon {
      box-sizing: border-box;
      font-size: 28px;
      color: #b7b7b7;
      width: 64px;
      height: 64px;
      line-height: 64px;
      text-align: center;
      &:hover {
        border: 1px dashed #b7b7b7;
        color: #fff;
        background-color: rgba(0, 0, 0, .3);
      }
    }
    .avatar {
      position: relative;
      width: 64px;
      height: 64px;
      display: block;
      border-radius: 50%;
    }
    .el-upload-action {
      position: absolute;
      top: 0;
      left: 0;
      display: block;
      width: 100%;
      height: 100%;
      font-size: 0;
      color: #fff;
      text-align: center;
      line-height: 64px;
      &:hover {
        font-size: 20px;
        background-color: #000;
        background-color: rgba(0, 0, 0, .3);
        border-radius: 50%;
      }
    }
  }
 }
 .demo-drawer__footer{
   margin-top: 80px;
   text-align: center;
 }
 .local{
   color: #7F7F7F;
   display: flex;
   line-height: 16px;
   font-size: 14px;
   align-items: center;
   div{
     display: flex;
     align-items: flex-start;
   }
 }
 .el-date-editor.el-input, .el-date-editor.el-input__inner {
    width: 100%;
}
</style>
