<template>
  <div class="api-dateils-page">
    <Breadcrumb :returnRouter="[{path:'/catalogue',name:'资源目录'}, {path:'',name:'详情'}]"></Breadcrumb>
    <div class="api-dateils-box">
      <Follow :dataDetails="dataDetails" @updataFun="updataFun"></Follow>
      <!-- api详情 -->
      <div class="api-dateils-list" v-if="type==='1'">
        <ul class="list-item-box">
          <li>
            API名称：{{ apiDateils.name }}
            <!-- <el-button type="primary" style="margin-left: 30px" @click="testBtnFun">测 试</el-button> -->
          </li>
          <!-- <li>访问地址：<span v-for="(item,index) in hostArr" :key="index">{{item.target}}</span></li> -->
          <li v-if="apiDateils.container&&apiDateils.routeInfo">访问地址：http://{{ apiDateils.container.replace('8001','8000') + apiDateils.routeInfo }}</li>
          <li>接口方法：{{ apiDateils.method }}</li>
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
      <div class="api-dateils-list" v-if="type==='2'">
        <ul class="list-item-box">
          <li>
            挂接日期：{{ apiDateils.createTime.split(' ')[0] }}
          </li>
          <li>云文件名称：{{ apiDateils.name }}</li>
          <li>云文件描述：{{ apiDateils.detail }}</li>
        </ul>
      </div>
      <!-- 数据库 -->
      <div class="api-dateils-list" v-if="type==='3'">
        <ul class="list-item-box">
          <li>
            挂接日期：{{ apiDateils.createTime.split(' ')[0] }}
          </li>
          <li>表名：{{ apiDateils.tableName }}</li>
          <li>描述：{{ apiDateils.detail }}</li>
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
// import { get } from "../../utils/request.js";
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
    this.dataDateilsFun();
  },
  methods: {
    testBtnFun() {
      let host = ''
      if (this.apiDateils.host) {
        host = JSON.parse(this.apiDateils.host)[0].target;
      }
      window.open(
        this.apiDateils.protocol + "://" + host + this.apiDateils.path
      );
      // if (this.apiDateils.method === "GET") {
      //   get(this.apiDateils.protocol + host + this.apiDateils.path, null).then(
      //     (res) => {
      //       if (res.code === 1) {
      //         this.$message.success("测试成功");
      //       } else {
      //         this.$message.error(res.msg);
      //       }
      //     }
      //   );
      // }
    },
    dataDateilsFun() {
      // 基础信息
      this.archBusiUviewExFind({ id: this.$route.query.uviewId }).then((res) => {
        if (res.code === 1) {
          this.hostArr = this.apiDateils.host ? JSON.parse(this.apiDateils.host) : [];
          this.dataDetails = res.data;
          // this.apiDateils = Object.assign(this.dataDetails, res.data);
        } else {
          this.$message.error(res.msg);
        }
      });

      if (this.$route.query.type === '1') {
        this.assetApiExFind({ id: this.$route.query.id }).then((res) => {
          if (res.code === 1) {
            if (res.data) {
              this.tableData1 = res.data.body ? JSON.parse(res.data.body) : "";
              this.examplesData = res.data.response;
              this.tableData2 = res.data.params
                ? JSON.parse(res.data.params)
                : "";
              this.apiDateils = res.data;
            }
          } else {
            this.$message.error(res.msg);
          }
        });
      } else if (this.$route.query.type === '2') {
        this.assetFileExFind({ id: this.$route.query.id }).then(res => {
          if (res.code === 1) {
            // this.apiDateils = Object.assign(this.dataDetails, res.data);
            this.apiDateils = res.data;
          } else {
            this.$message.error(res.msg);
          }
        })
      } else if (this.$route.query.type === '3') {
        this.assetDataExFind({ id: this.$route.query.id }).then(res => {
          if (res.code === 1) {
            // this.apiDateils = Object.assign(this.dataDetails, res.data);
            this.apiDateils = res.data;
          } else {
            this.$message.error(res.msg);
          }
        })
      }
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