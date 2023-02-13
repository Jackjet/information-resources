<template>
  <div class="personal-follow-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input placeholder="请输入信息资源代码" v-model="query.uviewNo" clearable>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-input placeholder="请输入信息资源名称" v-model="query.uviewNm" clearable>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-select v-model="query.shareLv" clearable placeholder="请选择状态">
          <el-option label="无条件共享" value="01"> </el-option>
          <el-option label="有条件共享" value="02"> </el-option>
          <el-option label="不予共享" value="03"> </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-button @click="queryClick">查 询</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-col>
    </el-row>
    <div class="catalogue-list">
      <ul class="list-box">
        <div class="data-null" v-if="total === 0">暂 无 数 据</div>
        <li class="list_item" v-for="(item, index) in followList" :key="index">
          <div class="list_item_1">
            <img class="list_item_1_icon" src="@/assets/image/icon/信息资源管理.svg" alt="" />
            <p>{{ item.createTime.split(' ')[0] }}</p>
            <span @click="cancelFollow(item.uviewId)"><img src="@/assets/image/icon/关注1.png" alt="" /> 关注</span>
          </div>
          <div class="list_item_2">
            <h2>{{ item.uviewNm }}</h2>
            <span>{{
              item.shareLv === "01"
                ? "无条件共享"
                : item.shareLv === "02"
                ? "有条件共享"
                : "不予共享"
            }}</span>
            <p>共享条件: {{ item.shareCondition?item.shareCondition:"无" }}</p>
            <p>资源描述: {{ item.uviewDesc }}</p>
          </div>
          <div class="list_item_3">
            <p>资源代码: {{ item.uviewNo }}</p>
            <p>更新周期: {{ item.updateCyc }}</p>
          </div>
          <div class="list_item_4">
            <el-button size="mini" @click="routerDetails(item.uviewId)">进入</el-button>
            <p>
              <img src="@/assets/image/icon/查看 (2).png" alt="" />
              访问：{{ item.visitsCount }}
            </p>
          </div>
        </li>
      </ul>
      <Pagination ref="page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></Pagination>
    </div>
  </div>
</template>
<script>
import Pagination from "com/pagination";
export default {
  components: {
    Pagination,
  },
  data() {
    return {
      input: "",
      followList: [],
      query: {
        uviewNo: "",
        uviewNm: "",
        shareLv: "",
        size: 5,
        page: 1,
      },
      total: 0,
    };
  },
  mounted() {
    this.followListFun(this.query);
  },
  methods: {
    queryClick() {
      this.query.page = 1;
      this.$refs.page.Page(1);
      this.followListFun(this.query);
    },
    resetForm() {
      this.query = {
        uviewNo: "",
        uviewNm: "",
        shareLv: "",
        size: 5,
        page: 1,
      }
      this.followListFun(this.query);
    },
    // 我关注的目录
    followListFun(data) {
      this.myFocusInfoFindAll(data).then((res) => {
        if (res.code === 1) {
          this.followList = res.data.content;
          this.total = res.data.totalElements;
        } else {
          this.$message.error(res.mag);
        }
      });
    },
    routerDetails(uviewId) {
      this.$router.push({ path: "/catalogue/details", query: { id: uviewId } });
    },
    sizeChange(val) {
      this.query.size = val;
      this.followListFun(this.query);
    },
    currentChange(val) {
      this.query.page = val;
      this.followListFun(this.query);
    },
    cancelFollow(id) {
      this.myFocusInfoDelete(id).then((res) => {
        if (res.code === 1) {
          this.$message.success("取消成功！");
          this.followListFun(this.query);
        } else {
          this.$message.error(res.msg);
        }
      });
    },
  },
};
</script>
<style lang="scss" scope>
.personal-follow-page {
  padding: 10px;
  box-sizing: border-box;
  .catalogue-list {
    margin-top: 20px;
    .list-box {
      // height: 456px;
      border-top: 1px solid #ccc;
      // border: 1px solid #ccc;
      // overflow-y: scroll;
      .data-null {
        text-align: center;
        line-height: 100px;
        color: #ccc;
        font-size: 30px;
      }
    }
    .list-box::-webkit-scrollbar {
      display: none;
      width: 0;
    }
    .list-box li {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #ccc;
      padding: 10px 0;
      .list_item_1 {
        width: 120px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .list_item_1_icon {
          width: 50px;
        }
        p {
          font-size: $font-size-small;
          color: $font-color-1;
        }
        span {
          width: 50px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #c3e182;
          font-size: $font-size-small;
          color: #fff;
          cursor: pointer;
          margin-top: 5px;
          img {
            width: 15px;
          }
        }
      }
      .list_item_2 {
        flex: 1;
        color: $font-color-2;
        font-size: $font-size-small;
        line-height: 24px;
        h2 {
          font-size: $font-size-middle;
        }
        span {
          background: #e9f1ff;
          color: #568bd5;
          padding: 2px 3px;
          border-radius: 2px;
        }
      }
      .list_item_3 {
        flex: 1;
        color: $font-color-2;
        font-size: $font-size-middle;
      }
      .list_item_4 {
        width: 100px;
        text-align: center;
        p {
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: $font-size-small;
          color: $font-color-1;
          img {
            width: 20px;
          }
        }
      }
    }
  }
}
</style>