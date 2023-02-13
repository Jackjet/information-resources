<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-input
            clearable
            size="medium"
            placeholder="请输入模型名称"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.type" placeholder="请选择模型类型">
            <el-option label="独立服务模型" value="independentService"></el-option>
            <el-option label="复合服务模型" value="compositeService"></el-option>
            <el-option label="编排服务模型" value="choreographyServices"></el-option>
            <el-option label="规则模型" value="rules"></el-option>
            <el-option label="标准规范模型" value="standardSpecification"></el-option>
            <el-option label="页面组件模型" value="pageComponents"></el-option>
            <el-option label="XML结构模型" value="xmlStructural"></el-option>
            <el-option label="模式匹配模型" value="modeMatching"></el-option>
            <el-option label="模式识别模型" value="modeRecognition"></el-option>
            <el-option label="其它模型" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="medium" @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size="medium" @click="add" icon="el-icon-add">添加</el-button>
        </el-form-item>
        <div class="right-button" @click="modelingClick">
          <img src="@/assets/image/modeling.png" alt />数学模型建模下载地址
        </div>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList
        :table-data="tableData"
        v-loading="isSubmitLoading"
        :table-label="tableHeader"
        :table-option="tableOpction"
      ></TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <drawer :modal-obj="modalObj" ref="callDetail" @Reload="fetchData"></drawer>
  </el-main>
</template>

<script>
import { getList, deModel } from "@/api/knowledgeBase/model";
import TableList from "@/components/table/TableList.vue";
import Pagination from "@/components/table/Pagination.vue";
// import drawer from '@/components/dialog/dialog.vue'
import drawer from "./detail";
export default {
  components: { TableList, Pagination, drawer },
  data() {
    return {
      drawer: false,
      tableData: [],
      tableHeader: [
        {
          id: false,
          type: "",
          label: "模型名称",
          list: "name",
        },
        {
          id: false,
          type: "",
          label: "模型类型",
          list: "type",
        },
        {
          id: false,
          type: "",
          label: "备注",
          list: "remark",
        },
        {
          id: false,
          type: "",
          label: "创建时间",
          list: "createTime",
        },
      ],
      tableOpction: {
        label: "操作",
        width: "300px",
        value: 0,
        options: [
          {
            label: "编辑",
            key: 0,
            type: "success",
            State: false,
            method: (row) => {
              this.edit(row);
            },
          },
          {
            label: "删除",
            key: 0,
            type: "danger",
            State: false,
            method: (row) => {
              this.delete(row);
            },
          },
        ],
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObj: "",
      SearchItem: {
        name: "",
        type: "",
      },
      lastItem: {
        name: "",
        type: "",
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      RootUrl: "",
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    modelingClick() {
      this.$confirm("您是否下载 “数学模型建模工具” ?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          window.location.href =
            process.env.VUE_APP_RES_API + "/win-unpacked.zip";
        })
        .catch(() => {
          // this.$message({
          //   type: "info",
          //   message: "已取消下载",
          // });
        });
    },
    async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        name: this.SearchItem.name,
        type: this.SearchItem.type,
      };
      try {
        const res = await getList(data);
        this.tableData = res.data.data.content;
        res.data.data.content.map(function (v, k) {
          switch (v.type) {
            case "independentService":
              v.type = "独立服务模型";
              break;
            case "compositeService":
              v.type = "复合服务模型";
              break;
            case "choreographyServices":
              v.type = "编排服务模型";
              break;
            case "rules":
              v.type = "规则模型";
              break;
            case "standardSpecification":
              v.type = "标准规范模型";
              break;
            case "pageComponents":
              v.type = "页面组件模型";
              break;
            case "xmlStructural":
              v.type = "XML结构模型";
              break;
            case "modeMatching":
              v.type = "模式匹配模型";
              break;
            case "modeRecognition":
              v.type = "模式识别模型";
              break;
            case "other":
              v.type = "其它模型";
              break;
          }
        });
        this.total = res.data.data.totalElements;
      } catch (even) {
        this.$message.error(even.msg);
      }
    },

    edit: function (row) {
      this.modalObj = "编辑";
      this.$refs.callDetail.initial(row.id);
    },

    add: function () {
      this.modalObj = "添加";
      this.$refs.callDetail.initial(false);
    },
    SearchNoteList() {
      this.currentPage = 1;
      Object.entries(this.SearchItem).map((item) => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.fetchData();
    },

    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(this.SearchItem).map((item) => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.fetchData();
    },

    // 删除当前单条数据 重载列表
    delete(row) {
      const that = this;
      that
        .$confirm("此操作将永久删除数据, 是否继续?", "提示", {
          type: "warning",
        })
        .then(async () => {
          that.Loading = true;
          const response = await deModel(row.id);
          that.Loading = false;
          if (response.data.code === 1) {
            that.$message.success("删除成功");
            this.fetchData();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },
  },
};
</script>

<style lang="scss" scope>
.right-button {
  color: #60a0fb;
  display: flex;
  align-items: center;
  cursor: pointer;
  float: right;
  margin: 10px;
  img {
    width: 16px;
    margin-right: 10px;
  }
}
</style>