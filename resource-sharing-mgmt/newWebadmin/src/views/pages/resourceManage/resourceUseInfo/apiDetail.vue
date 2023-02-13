
<template>
  <el-main class="main">
    <div>
      <h4>{{ title }}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="API名称:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength="100" size="medium" :disabled="true" placeholder="" v-model="ruleForm.name"></el-input>
        </el-form-item>
        <!-- <el-form-item label="资源目录:" :label-width="this.formLabelWidth">
          <el-input maxlength="100" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.uviewNm"></el-input>
        </el-form-item> -->
        <el-form-item label="路由:" :label-width="this.formLabelWidth">
          <el-input maxlength="100" placeholder="" size="medium" :disabled="true" v-model="ruleForm.routeInfo"></el-input>
        </el-form-item>
        <el-form-item label="接口方式:" :label-width="this.formLabelWidth">
          <el-input maxlength="100" placeholder="" size="medium" :disabled="true" v-model="ruleForm.method"></el-input>
        </el-form-item>
        <el-form-item label="认证方式:" :label-width="this.formLabelWidth">
          <el-input maxlength="100" placeholder="Key-Auth" size="medium" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="认证条件:" :label-width="this.formLabelWidth">
          <el-input maxlength="100" placeholder="用户授权码" size="medium" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="加密方式:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.verifyMethod"></el-input>
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
        <el-form-item label="云接口名称:" :label-width="this.formLabelWidth">
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
        <el-form-item label="支持业务:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.supportBusiness"></el-input>
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
          <a v-else size="medium" style="display: block;" v-for="(item,i) in ruleForm.fileUrl" :key="i" :href="reqHttp+item.url">申请依据报告:{{item.name}}</a>
        </el-form-item>
        <el-form-item label="审核状态:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' clearable size="medium" :disabled="true" v-model="status"></el-input>
        </el-form-item>
        <el-form-item label="申请说明:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.instructions"></el-input>
        </el-form-item>

        <el-form-item v-if="ruleForm.status==='2'" label="流量策略:" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每分钟：</span>
          <el-input style="width: 300px;" maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.perMinute"></el-input> 次
        </el-form-item>
        <el-form-item v-if="ruleForm.status==='2'" label="" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每小时：</span>
          <el-input style="width: 300px;" maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.everyHour"></el-input> 次
        </el-form-item>
        <el-form-item v-if="ruleForm.status==='2'" label="" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每天：</span>
          <el-input style="width: 300px;" maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.everyDay"></el-input> 次
        </el-form-item>
        <el-form-item v-if="ruleForm.status==='2'" label="" :label-width="this.formLabelWidth">
          <span style="display: inline-block;width: 100px;text-align: right;">每次：</span>
          <el-input style="width: 300px;" maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.singleSize"></el-input> MB
        </el-form-item>
        <el-form-item v-if="ruleForm.status==='2'" label="审核说明:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.auditDesc"></el-input>
        </el-form-item>

        <el-form-item label="请求参数部分说明:" :label-width="this.formLabelWidth">
          <el-input maxlength="100" placeholder="" size="medium" v-model="httpUrl" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="body数据参数" :label-width="this.formLabelWidth">
        </el-form-item>
        <el-form-item>
          <Table :tableData="tableData1" :tableHeader="tableHeader1"></Table>
        </el-form-item>
        <el-form-item label="params数据参数" :label-width="this.formLabelWidth">
        </el-form-item>
        <el-form-item>
          <Table :tableData="tableParamsData" :tableHeader="tableParamsHeader"></Table>
        </el-form-item>
        <el-form-item label="数据返回参数" :label-width="this.formLabelWidth">
        </el-form-item>
        <el-form-item>
          <Table :tableData="tableData2" :tableHeader="tableHeader2"></Table>
        </el-form-item>
        <el-form-item label="常量参数" :label-width="this.formLabelWidth">
        </el-form-item>
        <el-form-item>
          <Table :tableData="tableConstantsData" :tableHeader="tableConstantsHeader"></Table>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>
</template>

<script>
import Table from "@/components/table/detailTable.vue";
// import { assetApiExFind } from "@/api/assetApiEx.js";
import { resourceUseInfoFind } from "@/api/resourceUseInfo.js";
export default {
  components: {
    Table,
  },
  data() {
    return {
      reqHttp: process.env.VUE_APP_BASE_API,
      title: "API详情",
      ruleForm: {
        name: "",
      },
      useStartTime: "",
      useEndTime: "",
      status: "",
      updateCyc: "",
      shareLv: "",
      createTime: "",
      formLabelWidth: "130px",
      httpUrl: '',
      tableData1: [],
      tableHeader1: [
        {
          prop: "name",
          label: "参数名称",
          width: "",
        },
        {
          prop: "type",
          label: "参数位置",
          width: "",
        },
        {
          prop: "isRequired",
          label: "是否必填",
          width: "",
        },
        {
          prop: "defaultValue",
          label: "默认值",
          width: "",
        },
        {
          prop: "regular",
          label: "校验格式",
          width: "",
        },
        {
          prop: "description",
          label: "描述",
          width: "",
        }
      ],
      tableParamsData: [],
      tableParamsHeader: [
        {
          prop: "name",
          label: "参数名称",
          width: "",
        },
        {
          prop: "type",
          label: "参数位置",
          width: "",
        },
        {
          prop: "isRequired",
          label: "是否必填",
          width: "",
        },
        {
          prop: "defaultValue",
          label: "默认值",
          width: "",
        },
        {
          prop: "regular",
          label: "校验格式",
          width: "",
        },
        {
          prop: "description",
          label: "描述",
          width: "",
        }
      ],
      tableData2: [],
      tableHeader2: [
        {
          prop: "name",
          label: "参数名称",
          width: "",
        },
        {
          prop: "type",
          label: "参数类型",
          width: "",
        },
        {
          prop: "description",
          label: "描述",
          width: "",
        },
      ],
      tableConstantsData: [],
      tableConstantsHeader: [
        {
          prop: "name",
          label: "参数名称",
          width: "",
        },
        {
          prop: "type",
          label: "参数类型",
          width: "",
        },
        {
          prop: "description",
          label: "描述",
          width: "",
        },
      ]
    };
  },
  created() {
    this.assetApiExDetail();
  },
  methods: {
    // 详情
    async assetApiExDetail() {
      const that = this;
      let data = { id: this.$route.query.id };
      that.loading = true;
      const response = await resourceUseInfoFind(data);
      that.loading = false;
      if (response.data.code === 1) {
        this.httpUrl = 'http://' + response.data.data.host + response.data.data.path;
        that.ruleForm = response.data.data;
        this.ruleForm.fileUrl = JSON.parse(response.data.data.fileUrl);
        this.createTime = response.data.data.createTime.split(' ')[0];
        this.useStartTime = response.data.data.useStartTime.split(' ')[0];
        this.useEndTime = response.data.data.useEndTime.split(' ')[0];
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

        if (response.data.data.body != "") {
          that.tableData1 = JSON.parse(response.data.data.body);
        }
        if (response.data.data.params != "") {
          that.tableParamsData = JSON.parse(response.data.data.params);
        }
        if (response.data.data.response != "") {
          that.tableData2 = JSON.parse(response.data.data.response);
        }
        if (response.data.data.constants != "") {
          that.tableConstantsData = JSON.parse(response.data.data.constants);
        }
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg);
        return false;
      }
    },
    goBack() {
      // this.$router.push("/resourceUseInfo");
      this.$router.go(-1);
    },
  },
};
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