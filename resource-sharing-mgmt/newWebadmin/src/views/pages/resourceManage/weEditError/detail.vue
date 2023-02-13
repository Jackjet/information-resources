<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="提交单位名称:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.orgName"></el-input>
        </el-form-item>
        <el-form-item label="提交时间:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="createTime"></el-input>
        </el-form-item>
        <el-form-item label="信息资源名称:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.uviewNm"></el-input>
        </el-form-item>
        <el-form-item label="信息资源代码:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' clearable size="medium" :disabled="true" v-model="ruleForm.uviewNo"></el-input>
        </el-form-item>
        <el-form-item label="反馈类别:" :label-width="this.formLabelWidth">
          <el-select v-model="ruleForm.type" clearable size="medium" :disabled="true" style="width:100%">
            <el-option label="数据与实际情况不符" :value="1"></el-option>
            <el-option label="资源过时" :value="2"></el-option>
            <el-option label="数据无法下载" :value="3"></el-option>
            <el-option label="其他" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据描述:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.describe"></el-input>
        </el-form-item>
        <el-form-item label="状态:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.status"></el-input>
        </el-form-item>
        <el-form-item label="受理时间:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="updateTime"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.status==='已驳回'" label="驳回原因:" :label-width="this.formLabelWidth">
          <el-input :rows='4' type="textarea" clearable size="medium" :disabled="true" v-model="ruleForm.reject"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { correctionFind } from "@/api/resourceUseInfo.js";
export default {
  data() {
    return {
      title: '详情',
      ruleForm: {
      },
      formLabelWidth: "120px",
      status: '',
      shareLv: '',
      theUrl: process.env.VUE_APP_BASE_API,
      createTime: '',
      updateTime: '',
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
        that.ruleForm = response.data.data
        this.createTime = response.data.data.createTime.split(' ')[0];
        this.updateTime = response.data.data.updateTime.split(' ')[0];
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      this.$router.push('/weEditError')
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