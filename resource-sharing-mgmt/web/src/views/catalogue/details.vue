<template>
  <div class="catalogue-details-page">
    <Breadcrumb :returnRouter="[{path:'/catalogue',name:'资源目录'}, {path:'',name:'详情'}]"></Breadcrumb>
    <div class="catalogue-details-box">
      <Follow :dataDetails="dataDetails" @updataFun="updataFun"></Follow>
      <div class="details-boxlist">
        <!-- <el-button class="data-btn" type="text" @click="routerClick">云数据申请</el-button> -->
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="信息项" name="first"></el-tab-pane>
          <el-tab-pane label="云接口" name="second"></el-tab-pane>
          <el-tab-pane label="云文件" name="yunwen"></el-tab-pane>
          <el-tab-pane label="云数据" name="yunshuju"></el-tab-pane>
        </el-tabs>
        <Table :tableData="tableData" :tableDetail="tableDetail" :tableHeader="tableHeader" :tableButton="tableButton"></Table>
        <Pagination v-if="paginationFang" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></Pagination>
      </div>
    </div>
  </div>
</template>
<script>
import Breadcrumb from "com/breadcrumb";
import Follow from "com/follow";
import Pagination from "com/pagination";
import Table from "com/table";
export default {
  components: {
    Breadcrumb,
    Follow,
    Pagination,
    Table,
  },
  data() {
    return {
      paginationFang: true,
      activeName: "first",
      tableDetail: {
        isFlag: false,
      },
      tableHeader: [],
      tableData: [],
      tableButton: {
        label: "申请",
        width: "100",
        isFlag: true,
        textBtn: "立即申请",
      },
      visitsCount: '',
      dataDetails: {

      },
      tabsQuery: {
        uviewId: "",
        page: 1,
        size: 5,
      },
      total: 0,
    };
  },
  mounted() {
    this.visitsCountFun()
    this.detailsApiFun();
  },
  methods: {
    routerClick() {
      this.$router.push({
        path: "/catalogue/apiApplay",
        query: {
          id: this.dataDetails.id,
          Id: this.$route.query.id,
          type: "云数据"
        }
      })
    },
    updataFun() {
      this.detailsApiFun();
    },
    detailsApiFun() {
      this.archBusiUviewExFind({ id: this.$route.query.id }).then((res) => {
        if (res.code === 1) {
          this.dataDetails = res.data;
          // this.dataDetails.visitsCount = this.visitsCount
          this.tabsQuery.uviewId = res.data.uviewId;
          this.archBusiUviewStrExFindAllFun(this.tabsQuery);
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    visitsCountFun() {
      this.archBusiUviewExFindVisits({ id: this.$route.query.id }).then((res) => {
        if (res.code === 1) {
          this.dataDetails.visitsCount = res.data
          // this.visitsCount = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 信息项
    archBusiUviewStrExFindAllFun(data) {
      this.archBusiUviewStrExFindAll(data).then((res) => {
        if (res.code === 1) {
          this.tableHeader = [
            {
              prop: "srcField",
              label: "信息项名称",
              width: "",
            },
            {
              prop: "engCd",
              label: "信息项标识",
              width: "",
            },
            {
              prop: "srcDataTyp",
              label: "数据类型",
              width: "",
            },
            {
              prop: "dataLen",
              label: "长度",
              width: "",
            },
          ];
          this.tableDetail.isFlag = false;
          this.tableButton.isFlag = false;
          this.tableData = res.data.content;
          this.total = res.data.totalElements;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 云接口
    assetApiExFindAllFun(data) {
      this.assetApiExFindAll(data).then((res) => {
        if (res.code === 1) {
          this.tableDetail = {
            isFlag: true,
            prop: "name",
            label: "名称",
            width: "",
          };
          this.tableHeader = [
            // {
            //   prop: "detail",
            //   label: "描述",
            //   width: "",
            // },
            {
              prop: "createTime",
              label: "发布日期",
              width: "",
            },
          ];
          this.tableButton.isFlag = true;
          this.tableButton.type = "云接口";
          this.tableData = res.data.content;
          this.total = res.data.totalElements;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 云文件
    fileApiFun(data) {
      this.assetFileExFindAll(data).then(res => {
        if (res.code === 1) {
          this.tableDetail = {
            isFlag: true,
            prop: "name",
            label: "名称",
            width: "",
          };
          this.tableHeader = [
            {
              prop: "detail",
              label: "描述",
              width: "",
            },
            {
              prop: "createTime",
              label: "发布日期",
              width: "",
            },
          ];
          this.tableButton.type = "云文件";
          this.tableButton.isFlag = true;
          this.tableData = res.data.content;
          this.total = res.data.totalElements;
        } else {
          this.$message.error(res.msg);
        }
      })
    },
    // 云数据
    dataListFun(data) {
      this.assetDataExFindAll(data).then(res => {
        if (res.code === 1) {
          this.tableDetail = {
            isFlag: true,
            prop: "tableName",
            label: "名称",
            width: "",
          };
          this.tableHeader = [
            {
              prop: "detail",
              label: "描述",
              width: "",
            },
            {
              prop: "createTime",
              label: "发布日期",
              width: "",
            },
          ];
          this.tableButton.type = "云数据";
          this.tableButton.isFlag = true;
          this.tableData = res.data.content;
          this.total = res.data.totalElements;
        } else {
          this.$message.error(res.msg);
        }
      })
    },
    handleClick() {
      this.paginationFang = false;
      this.tabsQuery.page = 1;
      this.tabsQuery.size = 5;
      if (this.activeName === "first") {
        this.archBusiUviewStrExFindAllFun(this.tabsQuery);
      } else if (this.activeName === "second") {
        this.assetApiExFindAllFun(this.tabsQuery);
      } else if (this.activeName === 'yunwen') {
        this.fileApiFun(this.tabsQuery);
      } else if (this.activeName === 'yunshuju') {
        this.dataListFun(this.tabsQuery);
      }
      this.paginationFang = true;
    },
    sizeChange(val) {
      this.tabsQuery.size = val;
      if (this.activeName === "first") {
        this.archBusiUviewStrExFindAllFun(this.tabsQuery);
      } else if (this.activeName === "second") {
        this.assetApiExFindAllFun(this.tabsQuery);
      } else if (this.activeName === 'yunwen') {
        this.fileApiFun(this.tabsQuery);
      } else if (this.activeName === 'yunshuju') {
        this.dataListFun(this.tabsQuery);
      }
    },
    currentChange(val) {
      this.tabsQuery.page = val;
      if (this.activeName === "first") {
        this.archBusiUviewStrExFindAllFun(this.tabsQuery);
      } else if (this.activeName === "second") {
        this.assetApiExFindAllFun(this.tabsQuery);
      } else if (this.activeName === 'yunwen') {
        this.fileApiFun(this.tabsQuery);
      } else if (this.activeName === 'yunshuju') {
        this.dataListFun(this.tabsQuery);
      }
    },
  },
};
</script>
<style lang="scss" scope>
.catalogue-details-page {
  // padding-top: 70px;
  .catalogue-details-box {
    border-top: 1px solid #ccc;
    .details-boxlist {
      width: 80%;
      margin: 30px auto;
      position: relative;
      .data-btn {
        position: absolute;
        right: 0;
        top: 0;
        z-index: 100;
      }
    }
  }
}
</style>