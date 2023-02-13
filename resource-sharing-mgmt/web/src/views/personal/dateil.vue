<template>
  <div class="api-dateils-page">
    <Breadcrumb :returnRouter="[{path:'/personal/weApply',name:'我的申请'}, {path:'',name:'详情'}]"></Breadcrumb>
    <div class="api-dateils-box">
      <Follow :dataDetails="dataDetails" @updataFun="updataFun"></Follow>
      <!-- api详情 -->
      <div class="api-dateils-list" v-if="apiDateils.type==='1'">
        <ul class="list-item-box">
          <li>
            API名称：{{ apiDateils.name }}
          </li>
          <li v-if="apiDateils.status==='2'">访问地址：http://{{ apiDateils.container.replace('8001','8000') + apiDateils.routeInfo }}</li>
          <li>接口方法：{{ apiDateils.method }}</li>
          <li>流量策略</li>
          <li>每分钟:{{apiDateils.perMinute}}次</li>
          <li>每小时:{{apiDateils.everyHour}}次</li>
          <li>每天:{{apiDateils.everyDay}}次</li>
          <li>每次:{{apiDateils.singleSize}}MB</li>
          <li>审核说明:{{apiDateils.auditDesc}}</li>
          <li>数据参数：</li>
        </ul>
        <Table :tableData="tableData1" :tableHeader="tableHeader1" :tableButton="tableButton"></Table>
        <ul class="list-item-box">
          <li>返回参数说明</li>
          <li>返回示例:</li>
        </ul>
        <Examples :examplesData="examplesData"></Examples>
        <ul class="list-item-box">
          <li>数据参数：</li>
        </ul>
        <Table :tableData="tableData2" :tableHeader="tableHeader2" :tableButton="tableButton"></Table>
      </div>
      <!-- 文件详情 -->
      <div class="api-dateils-list" v-if="apiDateils.type==='2'">
        <ul class="list-item-box">
          <li>
            提交日期：{{ apiDateils.createTime.split(' ')[0] }}
          </li>
          <!-- <li>审核状态：{{ apiDateils.status==='0'? '审核中':apiDateils.status==='1'?'审核通过':apiDateils.status==='2'?'审核驳回':'审核失败'}}</li> -->
          <li>审核状态：
            <!-- <span v-if="apiDateils.status === '0'">未审核</span>
            <span v-if="apiDateils.status === '1'">初审通过</span>
            <span v-if="apiDateils.status === '2'">审核通过</span> -->
            <span v-if="apiDateils.status === '0'">待审核</span>
            <span v-if="apiDateils.status === '1'">审核通过待实施</span>
            <span v-if="apiDateils.status === '2'">已实施</span>
            <span v-if="apiDateils.status === '3'">已驳回</span>
            <span v-if="apiDateils.status === '4'">审核失败</span>
          </li>
          <li>云文件名称：{{ apiDateils.fileName }}</li>
          <li>云文件描述：{{ apiDateils.fileDetail }}</li>
        </ul>
        <ul class="list-item-box">
          <li>附件下载</li>
          <li v-if="apiDateils.status==='2'"><a :href="downloadUrl+apiDateils.fileDownloadUri+'?orgId='+orgId">{{apiDateils.fileName}}</a></li>
        </ul>
      </div>
      <!-- 数据库 -->
      <div class="api-dateils-list" v-if="apiDateils.type==='3'">
        <ul class="list-item-box">
          <li>
            提交日期：{{ apiDateils.createTime.split(' ')[0] }}
          </li>
          <li>审核状态：
            <span v-if="apiDateils.status === '0'">待审核</span>
            <span v-if="apiDateils.status === '1'">审核通过待实施</span>
            <span v-if="apiDateils.status === '2'">已实施</span>
            <span v-if="apiDateils.status === '3'">已驳回</span>
            <span v-if="apiDateils.status === '4'">审核失败</span>
          </li>
          <li v-if="apiDateils.status === '2'">审核描述：{{apiDateils.auditDesc}}</li>
          <li v-if="apiDateils.status === '2'">IP地址：{{ apiDateils.ip }}</li>
          <li v-if="apiDateils.status === '2'">端口：{{ apiDateils.port }}</li>
          <li v-if="apiDateils.status === '2'">账号：{{ apiDateils.username }}</li>
          <li v-if="apiDateils.status === '2'">密码：{{ apiDateils.password }}</li>
          <li v-if="apiDateils.status === '2'">表名：{{ apiDateils.tableName }}</li>
          <li v-if="apiDateils.status === '2'">数据库描述：{{ apiDateils.dataDetail }}</li>
        </ul>
      </div>
    </div>
    <div class="return-page">
      <el-button type="primary" @click="returnRouter">返 回</el-button>
    </div>
  </div>
</template>
<script>
import Breadcrumb from "com/breadcrumb";
import Follow from "com/follow";
import Table from "com/table";
import Examples from "com/examples";
import config from "../../config/index";
export default {
  components: {
    Breadcrumb,
    Follow,
    Table,
    Examples,
  },
  data() {
    return {
      downloadUrl: config.baseURL,
      orgId: this.$store.state.user.orgId,
      type: this.$route.query.type,
      dataDetails: {},
      apiDateils: {},
      examplesData: "",
      tableHeader1: [
        {
          prop: "name",
          label: "参数名",
          width: "",
        },
        {
          prop: "description",
          label: "说明",
          width: "",
        },
        {
          prop: "value",
          label: "类型",
          width: "",
        },
        {
          prop: "type",
          label: "参数位置",
          width: "",
        },
      ],
      tableData1: [],
      tableHeader2: [
        {
          prop: "name",
          label: "参数名",
          width: "",
        },
        {
          prop: "description",
          label: "说明",
          width: "",
        },
        {
          prop: "type",
          label: "说明",
          width: "",
        },
      ],
      tableData2: [],
      tableButton: {
        label: "申请",
        width: "100",
        isFlag: false,
        textBtn: "立即申请",
      },
      hostArr: [],
    };
  },
  mounted() {
    // console.log(this.orgId, "===========")
    this.dataDateilsFun();
  },
  methods: {
    dataDateilsFun() {
      // 基础信息
      this.archBusiUviewExFind({ id: this.$route.query.uviewId }).then((res) => {
        if (res.code === 1) {
          this.dataDetails = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
      this.resourceUseInfoFindById({ id: this.$route.query.id }).then((res) => {
        if (res.code === 1) {
          this.tableData1 = res.data.body ? JSON.parse(res.data.body) : "";
          this.examplesData = res.data.response;
          this.tableData2 = res.data.params
            ? JSON.parse(res.data.params)
            : "";
          this.apiDateils = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });

    },
    updataFun() {
      this.dataDateilsFun();
    },
    returnRouter() {
      this.$router.go(-1);
    },
  },

};
</script>
<style lang="scss" scope>
.api-dateils-page {
  // padding-top: 70px;
  .api-dateils-box {
    border-top: 1px solid #ccc;
    .api-dateils-list {
      width: 80%;
      margin: 20px auto;
      border-top: 1px solid $font-color-header;
      padding-left: 150px;
      box-sizing: border-box;
      .list-item-box {
        line-height: 40px;
        padding: 10px 0;
      }
    }
  }
  .return-page {
    height: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
