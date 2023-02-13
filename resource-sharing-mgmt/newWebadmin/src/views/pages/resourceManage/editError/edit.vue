<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="提交单位名称:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" :disabled="true" placeholder="" v-model="ruleForm.orgName"></el-input>
        </el-form-item>
        <el-form-item label="提交时间:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createTime"></el-input>
        </el-form-item>
        <el-form-item label="信息资源名称:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.uviewNm"></el-input>
        </el-form-item>
        <el-form-item label="信息资源代码:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.uviewNo"></el-input>
        </el-form-item>
        <el-form-item label="内容表述:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.describe"></el-input>
        </el-form-item>
        <el-form-item label="状态:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.status"></el-input>
        </el-form-item>
        <el-form-item label="受理时间:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.updateTime"></el-input>
        </el-form-item>
        <el-form-item label="反馈类别:" :label-width="this.formLabelWidth">
          <el-select v-model="ruleForm.type" :disabled="true" style="width:100%">
            <el-option label="数据与实际情况不符" :value="1"></el-option>
            <el-option label="资源过时" :value="2"></el-option>
            <el-option label="数据无法下载" :value="3"></el-option>
            <el-option label="其他" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="驳回原因:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" v-model="ruleForm.reject"></el-input>
        </el-form-item> -->
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm','1')">通 过</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm','2')">驳 回</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { correctionFind, correctionUpdate } from "@/api/resourceUseInfo.js";
export default {
  data() {
    return {
      title: '审核',
      ruleForm: {

      },
      formLabelWidth: "120px",
      rules: {
        auditDesc: [{
          required: true,
          message: "请输入驳回说明",
          trigger: "change",
        }],
      },
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
      const response = await correctionFind(data)
      that.loading = false
      if (response.data.code === 1) {
        if (response.data.data.status == 0) {
          response.data.data.status = "未处理"
        } else if (response.data.data.status == 1) {
          response.data.data.status = "已处理"
        } else {
          response.data.data.status = "已驳回"
        }
        that.ruleForm = response.data.data;
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    submitForm(ruleForm, status) {
      // 取值
      let data = {};
      data.id = this.ruleForm.id;
      data.status = status;
      // data.reject = this.ruleForm.reject;
      console.log(data)
      if (status == 2) {
        this.$prompt('驳回原因', '审核驳回', {
          inputType: 'textarea',
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /\S/,
          inputErrorMessage: '请输入驳回原因'
        }).then(({ value }) => {
          data.reject = value;
          correctionUpdate(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('驳回成功');
              this.CloseModal();
            } else {
              this.$message.error(res.data.msg);
            }
          });
        }).catch(() => { });
      } else {
        correctionUpdate(data).then((res) => {
          if (res.data.code === 1) {
            this.$message.success('通过成功');
            this.CloseModal();
          } else {
            this.$message.error(res.data.msg);
          }
        });
      }

    },
    resetForm(ruleForm) {
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.$router.push({
        path: '/editError'
      })
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