<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="需求标题:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" :disabled="true" placeholder="" v-model="ruleForm.title"></el-input>
        </el-form-item>
        <el-form-item label="受理部门:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.acceptDept"></el-input>
        </el-form-item>

        <el-form-item v-if="ruleForm.acceptDeptId==='39'" label="申请类型:" :label-width="this.formLabelWidth">
          <el-select clearable v-model="ruleForm.type" placeholder="" :disabled="true" style="width:100%">
            <el-option label="云接口" value="1"></el-option>
            <el-option label="云文件" value="2"></el-option>
            <el-option label="云数据" value="3"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="需求类型:" :label-width="this.formLabelWidth">
          <el-select clearable v-model="ruleForm.requestType" placeholder="" :disabled="true" style="width:100%">
            <el-option label="目录资源变更" value="0"></el-option>
            <el-option label="目录资源新增" value="1"></el-option>
            <el-option label="云数据变更" value="2"></el-option>
            <el-option label="云数据新增" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="需求描述:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.describe"></el-input>
        </el-form-item>
        <el-form-item label="缘由:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.reason"></el-input>
        </el-form-item>
        <el-form-item label="缘由说明:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.reasonDesc"></el-input>
        </el-form-item>
        <el-form-item label="联系人:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.contacts"></el-input>
        </el-form-item>
        <el-form-item label="联系电话:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item label="期望解决时间:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="expectTime"></el-input>
        </el-form-item>
        <el-form-item label="电子邮件:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.email"></el-input>
        </el-form-item>
        <el-form-item label="附件:" :label-width="this.formLabelWidth">
          <span v-if="ruleForm.fileUrl.length===0">无附件</span>
          <a v-else size="medium" style="display: block;" v-for="(item,i) in ruleForm.fileUrl" :key="i" :href="theUrl +'/'+ item.url">附件:{{item.name}}</a>
        </el-form-item>
        <el-form-item label="提交人:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createByName"></el-input>
        </el-form-item>
        <el-form-item label="提交部门:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createDeptName"></el-input>
        </el-form-item>
        <el-form-item label="提交时间:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="createTime"></el-input>
        </el-form-item>
        <el-form-item label="受理状态:" :label-width="this.formLabelWidth">
          <el-select clearable v-model="ruleForm.status" placeholder="" :disabled="true" style="width:100%">
            <el-option label="未受理" value="0"></el-option>
            <el-option label="已受理" value="1"></el-option>
            <el-option label="已驳回" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="ruleForm.status!=='0'" label="受理意见:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.opinion"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.status!=='0'" label="受理时间:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="updateTime"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { demandedInfoFind } from "@/api/demandedInfo.js";
export default {
  data() {
    return {
      title: '详情',
      ruleForm: {
        title: '',
        acceptDept: "",
        requestType: '',
        describe: '',
        reason: '',
        reasonDesc: '',
        contacts: '',
        mobilePhone: '',
        expectTime: '',
        email: '',
        remark: '',
        fileUrl: '',
        createById: '',
        createDeptName: '',
        createTime: '',
        status: ''
      },
      formLabelWidth: "120px",
      theUrl: process.env.VUE_APP_BASE_API,
      expectTime: '',
      createTime: '',
      updateTime: '',
    }
  },
  created() {
    this.demandedInfoDetail()
  },
  methods: {
    // 详情
    async demandedInfoDetail() {
      const that = this
      let data = { id: this.$route.query.id }
      that.loading = true
      const response = await demandedInfoFind(data)
      that.loading = false
      if (response.data.code === 1) {
        if (response.data.data.fileUrl === null) {
          response.data.data.fileUrl = "";
        } else {
          response.data.data.fileUrl = JSON.parse(response.data.data.fileUrl)
        }
        that.ruleForm = response.data.data
        this.expectTime = response.data.data.expectTime ? response.data.data.expectTime.split(' ')[0] : "";
        this.createTime = response.data.data.createTime.split(' ')[0];
        this.updateTime = response.data.data.updateTime.split(' ')[0];

      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      this.$router.push('/demandedInfo')
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