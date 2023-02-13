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
      <el-form-item label="父级组织机构:":label-width="this.formLabelWidth">
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          disabled="true"
          placeholder=""
          v-model="ruleForm.parentName"
        ></el-input>
      </el-form-item>
      <el-form-item label="层级:" :label-width="this.formLabelWidth">
        <el-input  maxlength = '100' clearable size="medium" disabled="true" v-model="ruleForm.levelMsg"></el-input>
      </el-form-item>
      <el-form-item label="组织机构名称:" :label-width="this.formLabelWidth" prop='name'>
        <el-input  maxlength = '100' placeholder="请输入组织机构名称" clearable size="medium"  v-model="ruleForm.name"></el-input>
      </el-form-item>
       <div class="demo-drawer__footer drawer_footer">
        <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
        <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>
</template>

<script>
import { organizationInsert, organizationUpdate, } from "@/api/organization.js"
export default {
  components: { },
  data() {
    return {
      title: '新增',
      disabled: false,
      ruleForm: {
        level: '',
        name: "",
        parentName: '',
        parentId: '',
        levelMsg: ''
      },
      formLabelWidth: "120px",
      rules: {
        name: [
          {
            required: true,
            message: "请输入组织机构名称",
            trigger: "change",
          },
        ],
      },
    }
  },
  created () {
    if (this.$route.query.type === '新增') {
      this.title = this.$route.query.type
      this.ruleForm.name = ''
      this.ruleForm.parentId = this.$route.query.id
      this.ruleForm.parentName = this.$route.query.name
      this.ruleForm.level = this.$route.query.level
      this.ruleForm.levelMsg = this.$route.query.levelMsg
    }else if(this.$route.query.type === '编辑'){
      this.title = this.$route.query.type
      this.ruleForm.id = this.$route.query.id
      this.ruleForm.parentId = this.$route.query.parentId
      this.ruleForm.parentName = this.$route.query.parentName
      this.ruleForm.name = this.$route.query.name
      this.ruleForm.level = this.$route.query.level
      this.ruleForm.levelMsg = this.$route.query.levelMsg
    }
  },
  methods: {
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // this.loading = true;
          // 取值
          let data = this.ruleForm
          if (this.$route.query.type === '编辑') {
            // 编辑
            organizationUpdate(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('编辑成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            organizationInsert(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('新增成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.disabled = false;
      this.ruleForm.parentId = "";
      this.ruleForm.parentName = "";
      this.ruleForm.name = "";
      this.ruleForm.levelName = "";
      this.ruleForm.level = "";
      this.$router.push({ 
        path: '/organization'
      })
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
    background-color: #fff;
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