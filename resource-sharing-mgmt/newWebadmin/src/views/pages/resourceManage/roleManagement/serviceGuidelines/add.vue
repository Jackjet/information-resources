<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="系统名称:" prop="name" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="系统地址:" prop="path" :label-width="this.formLabelWidth">
          <el-input :rows='4' type="textarea" clearable size="medium" v-model="ruleForm.path"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" @click="submit('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { guideInsert, guideUpdate } from "@/api/roleManagement.js";
export default {
  data() {
    return {
      title: '添加',
      ruleForm: {
        name: "",
        path: "",
      },
      rules: {
        name: [{
          required: true,
          message: "请输入系统名称",
          trigger: "change",
        }],
        path: [{
          required: true,
          message: "请输入系统地址",
          trigger: "change",
        }]
      },
      formLabelWidth: "120px",
    }
  },
  mounted() {
    if (this.$route.query.id) {
      this.title = "编辑";
      this.ruleForm.id = this.$route.query.id;
      this.ruleForm.name = this.$route.query.name;
      this.ruleForm.path = this.$route.query.path;
    }
  },
  methods: {
    submit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.ruleForm.id) {
            guideUpdate(this.ruleForm).then(res => {
              if (res.data.code === 1) {
                this.$message.success(res.data.msg);
                this.goBack();
              } else {
                this.$message.error(res.data.msg)
                return false
              }
              this.loading = false;
            })
          } else {
            guideInsert(this.ruleForm).then(res => {
              if (res.data.code === 1) {
                this.$message.success(res.data.msg);
                this.goBack();
              } else {
                this.$message.error(res.data.msg)
                return false
              }
              this.loading = false;
            })
          }

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    goBack() {
      this.$router.push('/serviceGuidelinesList');
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