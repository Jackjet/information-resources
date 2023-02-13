<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="规则名称:" prop="name" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" placeholder="请输入规则名称" v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="脱敏方式:" prop="type" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.type"></el-input>
        </el-form-item>
        <el-form-item label="索引类型:" :label-width="this.formLabelWidth">
          <el-select clearable v-model="ruleForm.indexType" placeholder="请选择索引类型">
            <el-option key="1" label="位数" value="1"></el-option>
            <el-option key="2" label="字符" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起始值:" prop="indexOf" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" placeholder="索引类型位数时为保留开始的位数，符号时保留符号前的字符" v-model="ruleForm.indexOf"></el-input>
        </el-form-item>
        <el-form-item label="结束值:" prop="lastIndexOf" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" placeholder="索引类型位数时为保留结束的位数，符号时保留符号后的字符" v-model="ruleForm.lastIndexOf"></el-input>
        </el-form-item>
        <el-form-item label="替换字符:" prop="replaceValue" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" placeholder="请输入被替换的字符" v-model="ruleForm.replaceValue"></el-input>
        </el-form-item>
        <el-form-item label="描述:" :label-width="this.formLabelWidth">
          <el-input :rows='4' type="textarea" clearable size="medium" v-model="ruleForm.describe"></el-input>
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
import { replaceRuleFind, replaceRuleInsert, replaceRuleUpdate } from "@/api/replaceRule.js";
export default {
  data() {
    return {
      title: '添加',
      ruleForm: {
        type: '掩码'
      },
      rules: {
        name: [{
          required: true,
          message: "请输入系统名称",
          trigger: "change",
        }],
        type: [{
          required: true,
          message: "请输入脱敏方式",
          trigger: "change",
        }],
        indexType: [{
          required: true,
          message: "请输入索引类型",
          trigger: "change",
        }],
        indexOf: [{
          required: true,
          message: "请输入起始值",
          trigger: "change",
        }],
        lastIndexOf: [{
          required: true,
          message: "请输入结束值",
          trigger: "change",
        }],
        replaceValue: [{
          required: true,
          message: "请输入替换字符",
          trigger: "change",
        }]
      },
      formLabelWidth: "120px",
    }
  },
  created() {
    if (this.$route.query.id) {
      this.title = "编辑";
      this.ruleForm.id = this.$route.query.id;
      this.replaceRuleDetail()
    }
  },
  methods: {
    // 详情
    async replaceRuleDetail() {
      const that = this
      let data = { id: this.$route.query.id }
      const response = await replaceRuleFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    submit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.ruleForm.id) {
            replaceRuleUpdate(this.ruleForm).then(res => {
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
            replaceRuleInsert(this.ruleForm).then(res => {
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
      this.$router.push('/replaceRuleList');
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
