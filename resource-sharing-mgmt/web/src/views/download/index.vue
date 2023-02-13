<template>
  <div class="download-page">
    <Breadcrumb :returnRouter="[{path:'',name:'下载中心'}]"></Breadcrumb>
    <div class="download-box">
      <ul class="left-tabs">
        <li :class="query.type==='1'?'active-class':''" @click="activeFun('1')">通知公告</li>
        <li :class="query.type==='2'?'active-class':''" @click="activeFun('2')">资料下载</li>
        <li :class="query.type==='3'?'active-class':''" @click="activeFun('3')">技术规范</li>
      </ul>
      <div class="list-box">
        <div class="query-title">
          <h3>
            <span v-if="query.type==='1'">通知公告</span>
            <span v-if="query.type==='2'">资料下载</span>
            <span v-if="query.type==='3'">技术规范</span>
          </h3>
          <el-input style="width:300px;" placeholder="请输入文件名称" v-model="query.title" clearable @keyup.enter.native="queryClick" />
        </div>
        <ul class="list-item">
          <li v-for="item in downloadList" :key="item.id">
            <div class="item-name">
              <p style="color:#333;">{{item.fileName}}</p>
              <p style="color:#666;font-size:15px;">发布日期： {{item.createTime.split(' ')[0]}}</p>
              <!-- <p>{{item.fileDesc}}</p> -->
            </div>
            <div class="item-num">
              <p class="download-btn" @click="downloadClick(item.id)"><img src="@/assets/image/icon/下载.png" alt=""> {{item.downCount}} 次</p>
            </div>
          </li>
        </ul>
        <Pagination ref="page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></Pagination>
      </div>
    </div>
  </div>
</template>
<script>
import Breadcrumb from "com/breadcrumb";
import Pagination from "com/pagination";
import config from "../../config/index";
export default {
  components: {
    Breadcrumb,
    Pagination,
  },
  data() {
    return {
      query: {
        title: "",
        type: "1",
        page: 1,
        size: 5,
      },
      downloadList: [],
      total: 0,
    };
  },
  mounted() {
    this.downloadListFun(this.query);
  },
  methods: {
    activeFun(index) {
      this.query.title = "";
      this.query.type = index;
      this.query.page = 1;
      this.$refs.page.Page(1);
      this.downloadListFun(this.query);
    },
    queryClick() {
      this.query.page = 1;
      this.$refs.page.Page(1);
      this.downloadListFun(this.query);
    },
    downloadListFun(data) {
      this.downloadInfoFindAll(data).then((res) => {
        if (res.code === 1) {
          this.downloadList = res.data.content;
          this.total = res.data.totalElements;
        } else {
          this.$message.error(res.mag);
        }
      });
    },
    sizeChange(val) {
      this.query.size = val;
      this.downloadListFun(this.query);
    },
    currentChange(val) {
      this.query.page = val;
      this.downloadListFun(this.query);
    },
    downloadClick(id) {
      window.location.href =
        config.baseURL + "web/downloadInfo/download?id=" + id;
      setTimeout(() => {
        this.downloadListFun(this.query);
      }, 100)
    },
  },
};
</script>
<style lang="scss" scoped>
.download-page {
  width: 90%;
  margin: 0 auto;
  .download-box {
    margin-left: 50px;
    display: flex;
    .left-tabs {
      width: 230px;
      height: 159px;
      margin-right: 20px;
      // box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      li {
        height: 52px;
        line-height: 52px;
        text-align: center;
        cursor: pointer;
        background: #fff;
        // border-bottom: 1px solid #eee;
        position: relative;
        background: url("~@/assets/image/ziyuan/背景.png") no-repeat;
        background-size: 100% 100%;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
        margin-bottom: 10px;
      }
      .active-class {
        color: #1aa2ff;
      }
      li.active-class:after {
        position: absolute;
        left: 0;
        top: 13px;
        content: "";
        display: block;
        width: 2px;
        height: 25px;
        background: #1aa2ff;
      }
      li.active-class:before {
        position: absolute;
        right: 15px;
        top: 20px;
        content: "";
        display: block;
        width: 0;
        height: 0;
        border-top: 7px solid transparent;
        border-bottom: 7px solid transparent;
        border-left: 10px solid #1aa2ff;
      }
    }
    .list-box {
      flex: 1;
      padding: 40px;
      background: #fff;
      min-height: 500px;
      margin-bottom: 30px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      .query-title {
        display: flex;
        border-bottom: 2px solid #1aa2ff;
        justify-content: space-between;
        align-items: center;
        padding: 10px;
        box-sizing: border-box;
        h3 {
          color: #1aa2ff;
          font-weight: bold;
          font-size: 18px;
        }
      }
      .list-item {
        li {
          display: flex;
          justify-content: space-between;
          align-items: center;
          border-bottom: 1px dashed #ccc;
          // padding: 15px 0;
          line-height: 34px;
          .download-btn {
            color: #f7a200;
            cursor: pointer;
            display: flex;
            align-items: center;
            font-size: 14px;
            img {
              width: 20px;
              margin-right: 5px;
            }
          }
        }
      }
    }
  }
}
</style>