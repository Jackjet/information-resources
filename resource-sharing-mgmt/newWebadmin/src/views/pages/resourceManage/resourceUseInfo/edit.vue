<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">

        <el-form-item v-if="ruleForm.type==='2'" label="文件名称:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.fileName"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='2'" label="描述:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.detail"></el-input>
        </el-form-item>

        <el-form-item label="信息资源名称:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" :disabled="true" placeholder="" v-model="ruleForm.uviewNm"></el-input>
        </el-form-item>
        <el-form-item label="信息资源代码:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.uviewNo"></el-input>
        </el-form-item>
        <el-form-item label="更新周期:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="updateCyc"></el-input>
        </el-form-item>
        <el-form-item label="信息资源摘要:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.uviewDesc"></el-input>
        </el-form-item>
        <el-form-item label="共享类型:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="shareLv"></el-input>
        </el-form-item>
        <el-form-item label="云数据名称:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.sourceApiName"></el-input>
        </el-form-item>
        <el-form-item label="提供单位:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.provOrgName"></el-input>
        </el-form-item>
        <!-- <el-form-item label="提交人:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createById"></el-input>
        </el-form-item> -->
        <el-form-item label="提交日期:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="createTime"></el-input>
        </el-form-item>
        <el-form-item label="资源用途:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.resourceUse"></el-input>
        </el-form-item>
        <el-form-item label="使用期限:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="useStartTime"></el-input>
          至
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="useEndTime"></el-input>
        </el-form-item>
        <el-form-item label="申请说明:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.instructions"></el-input>
        </el-form-item>
        <el-form-item label="联系人:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.contacts"></el-input>
        </el-form-item>
        <el-form-item label="联系电话:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item label="电子邮件:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.email"></el-input>
        </el-form-item>
        <el-form-item label="申请依据:" :label-width="this.formLabelWidth">
          <span v-if="ruleForm.fileUrl.length===0">无申请依据报告</span>
          <a v-else size="medium" style="display: block;" v-for="(item,i) in ruleForm.fileUrl" :key="i" :href="theUrl+'/'+item.url">申请依据报告:{{item.name}}</a>
        </el-form-item>

        <el-form-item v-if="ruleForm.type==='1'" label="加密方式:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="KeyAuth"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='1'" label="流量策略:" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每分钟：</span>
          <el-input style="width: 300px" maxlength='100' clearable size="medium" onkeyup="value = value.replace(/[^\d]/g,'');" v-model.number="perMinute"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='1'" label="" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每小时：</span>
          <el-input style="width: 300px" maxlength='100' clearable size="medium" onkeyup="value = value.replace(/[^\d]/g,'');" v-model.number="everyHour"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='1'" label="" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每天：</span>
          <el-input style="width: 300px" maxlength='100' clearable size="medium" onkeyup="value = value.replace(/[^\d]/g,'');" v-model.number="everyDay"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='1'" label="" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每次：</span>
          <el-input style="width: 300px" maxlength='100' clearable size="medium" onkeyup="value = value.replace(/[^\d]/g,'');" v-model.number="singleSize"></el-input>
        </el-form-item>

        <el-form-item label="审核说明:" :label-width="this.formLabelWidth" prop='auditDesc'>
          <el-input :rows='4' type="textarea" placeholder="" clearable size="medium" v-model="ruleForm.auditDesc"></el-input>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
          <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">通 过</el-button>
          <el-button size="medium" type="primary" v-if="!disabled" @click="rejectForm('ruleForm')">驳 回</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { resourceUseInfoFind, resourceUseInfoUpdate } from "@/api/resourceUseInfo.js";
export default {
  data() {
    return {
      title: '审核',
      ruleForm: {
        createByName: '',
        updateCyc: "",
        verifyMethod: '',
        instructions: '',
        useStartTime: '',
        useEndTime: '',
        resourceUse: '',
        uviewDesc: ''
      },
      formLabelWidth: "120px",
      perMinute: 1000,
      everyHour: 6000,
      everyDay: 10000,
      singleSize: 1,
      KeyAuth: 'Key-Auth',
      rules: {
        auditDesc: [{
          required: true,
          message: "请输入审核说明",
          trigger: "change",
        }],
      },
      theUrl: process.env.VUE_APP_BASE_API,
      updateCyc: '',
      createTime: '',
      useStartTime: '',
      useEndTime: '',
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
      const response = await resourceUseInfoFind(data)
      that.loading = false
      if (response.data.code === 1) {
        response.data.data.fileUrl = JSON.parse(response.data.data.fileUrl)
        // console.log(response.data.data.fileUrl);
        // console.log(response.data.data.resourceUse);
        if (response.data.data.resourceUse === '[]') {
          response.data.data.resourceUse = '';
        }
        that.ruleForm = response.data.data;
        this.createTime = response.data.data.createTime.split(' ')[0];
        this.useStartTime = response.data.data.useStartTime.split(' ')[0];
        this.useEndTime = response.data.data.useEndTime.split(' ')[0];
        switch (response.data.data.shareLv) {
          case "01":
            this.shareLv = "无条件共享";
            break;
          case "02":
            this.shareLv = "有条件共享";
            break
          case "03":
            this.shareLv = "不予共享";
            break;
          default:
            this.shareLv = "未知";
            break;
        }
        switch (response.data.data.updateCyc) {
          case "01":
            this.updateCyc = "实时";
            break;
          case "02":
            this.updateCyc = "每日";
            break;
          case "03":
            this.updateCyc = "每周";
            break;
          case "04":
            this.updateCyc = "每月";
            break;
          case "05":
            this.updateCyc = "每季度";
            break;
          case "06":
            this.updateCyc = "每半年";
            break;
          case "07":
            this.updateCyc = "每年";
            break;
            break;
          case "08":
            this.updateCyc = "其他";
            break;
        }
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = {};
          data.id = this.ruleForm.id;
          data.perMinute = this.perMinute;
          data.everyHour = this.everyHour;
          data.everyDay = this.everyDay;
          data.singleSize = this.singleSize;
          data.auditDesc = this.ruleForm.auditDesc;
          data.status = "1";
          // 编辑
          resourceUseInfoUpdate(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('审核成功');
              this.resetForm('ruleForm')
              this.loading = false;
            } else {
              this.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },
    rejectForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = {};
          data.id = this.ruleForm.id;
          data.perMinute = this.perMinute;
          data.everyHour = this.everyHour;
          data.everyDay = this.everyDay;
          data.singleSize = this.singleSize;
          data.auditDesc = this.ruleForm.auditDesc;
          data.status = "2";
          // 编辑
          resourceUseInfoUpdate(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('驳回成功');
              this.resetForm('ruleForm')
              this.loading = false;
            } else {
              this.$message.error(res.data.msg);
            }
          });
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
      this.PerMinute = "";
      this.EveryHour = "";
      this.EveryDay = "";
      this.SingleSize = "";
      this.auditDesc = "";
      this.$router.push({
        path: '/resourceUseInfo'
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