<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">

        <el-form-item v-if="ruleForm.type==='2'" label="文件名称:" :label-width="this.formLabelWidth">
          <a :href="theUrl+ruleForm.fileDownloadUri">{{ruleForm.fileName}}</a>
          <!-- <el-input clearable size="medium" :disabled="true" v-model="ruleForm.fileName"></el-input> -->
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

        <el-form-item v-if="ruleForm.type==='1'" label="云接口名称:" :label-width="this.formLabelWidth">
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
          <a v-else size="medium" style="display: block;" v-for="(item,i) in ruleForm.fileUrl" :key="i" :href="theUrl+item.url">申请依据报告:{{item.name}}</a>
        </el-form-item>
        <el-form-item label="审核状态:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="status"></el-input>
        </el-form-item>
        <!-- <el-form-item label="加密方式:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.verifyMethod"></el-input>
        </el-form-item> -->

        <el-form-item label="备注:" :label-width="this.formLabelWidth">
          <el-input v-if="ruleForm.type==='2'" clearable size="medium" :disabled="true" v-model="ruleForm.fileDetail"></el-input>
          <el-input v-if="ruleForm.type==='3'" clearable size="medium" :disabled="true" v-model="ruleForm.dataDetail"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='3'" label="数据库ip:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.ip"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='3'" label="数据库端口:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.port"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='3'" label="数据库账号:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='3'" label="数据库密码:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='3'" label="表名称:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.tableName"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type==='3'&&ruleForm.status==='2'" label="审核说明:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.auditDesc"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.status==='3'" label="审核说明:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.rejectDetail"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { resourceUseInfoFind } from "@/api/resourceUseInfo.js";
export default {
  data() {
    return {
      title: '详情',
      ruleForm: {
        createById: '',
        updateCyc: "",
        status: '',
        auditDesc: '',
        instructions: '',
        useStartTime: '',
        useEndTime: '',
        resourceUse: '',
        uviewDesc: ''
      },
      formLabelWidth: "120px",
      status: '',
      shareLv: '',
      updateCyc: '',
      createTime: '',
      useStartTime: '',
      useEndTime: '',
      theUrl: process.env.VUE_APP_BASE_API
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
        that.ruleForm = response.data.data
        this.createTime = response.data.data.createTime.split(' ')[0];
        this.useStartTime = response.data.data.useStartTime.split(' ')[0];
        this.useEndTime = response.data.data.useEndTime.split(' ')[0];
        // switch (response.data.data.status) {
        //   case "0":
        //     this.status = "未审核";
        //     break;
        //   case "1":
        //     this.status = "已审核";
        //     break
        //   case "2":
        //     this.status = "已驳回";
        //     break;
        //   case "3":
        //     this.status = "审批失败";
        //     break;
        //   default:
        //     this.status = "未知";
        //     break;
        // }
        switch (response.data.data.status) {
          case "0":
            this.status = "待审核";
            break;
          case "1":
            this.status = "审核通过待实施";
            break
          case "2":
            this.status = "已实施";
            break;
          case "3":
            this.status = "已驳回";
            break;
          default:
            this.status = "审核失败";
            break;
        }

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
    goBack() {
      // this.$router.push('/resourceUseInfo')
      this.$router.go(-1);
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