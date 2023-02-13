<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
     <el-col class="main-center">
      <el-form
       v-loading="loading"
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
     >
      <el-form-item label="角色权限名称:" :label-width="this.formLabelWidth" prop='name'>
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          placeholder="请输入角色权限名称"
          v-model="ruleForm.name"
        ></el-input>
      </el-form-item>
      <el-form-item label="备注:" :label-width="this.formLabelWidth">
        <el-input  maxlength = '200' type="textarea"  placeholder="请输入备注" clearable size="medium" v-model="ruleForm.remark"></el-input>
      </el-form-item>
       <div class="demo-drawer__footer drawer_footer">
        <el-button size="medium" @click="goBack">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitForm('ruleForm')">保 存</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>
</template>
<script>
import { roleInsert, roleUpdate } from "@/api/role.js";
export default {
  components: { },
  data() {
    return {
      title: '新增',
      loading: false,
      ruleForm: {
        name: "",
        remark: ''
      },
      formLabelWidth: "120px",
      rules: {
        name: [
          {
            required: true,
            message: "请输入角色权限名称",
            trigger: "change",
          },
        ]
      }
    }
  },
  created () {
    this.title = this.$route.query.type
    if(this.$route.query.type === '编辑'){
      this.ruleForm.id = this.$route.query.id
      this.ruleForm.name = this.$route.query.name
      this.ruleForm.remark = this.$route.query.remark
    }
  },
  methods: {
    submitForm(ruleForm) {
      const that = this
      that.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          let data = that.ruleForm
          that.loading = true;
          if (that.$route.query.type === '编辑') {
            // 编辑
            roleUpdate(data).then((res) => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success('编辑成功');
                that.goBack()
              } else {
                that.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            roleInsert(data).then((res) => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success('新增成功');
                that.goBack()
              } else {
                that.$message.error(res.data.msg);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    goBack() {
      this.$router.push('/role')
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