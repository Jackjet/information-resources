<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入文件名称" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <!-- <el-form-item>
          <el-input clearable size="medium" placeholder="请输入文件格式" v-model="SearchItem.fileType">
          </el-input>
        </el-form-item> -->
        <!-- <el-form-item>
          <el-select clearable v-model="SearchItem.status" placeholder="请选择审核状态">
            <el-option label="已挂接" value="0"></el-option>
            <el-option label="未挂接" value="1"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item style="margin-left: 1%">
          <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { assetFileExFindAll, assetFileExDelete } from "@/api/fileData.js";
import TableList from "@/components/table/tableList";
import Pagination from "@/components/table/Pagination";
export default {
  components: { TableList, Pagination },
  data() {
    return {
      tableSelection: {
        key: true,
        type: "",
        detaile: false,
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        {
          type: "html",
          label: "文件名称",
          list: "name",
          code: (row) => {
            return "<a href=" + process.env.VUE_APP_BASE_API + row.fileDownloadUri + ">" + row.name + "</a>";
          },
        },
        // {
        //   type: "html",
        //   label: "文件格式",
        //   list: "fileType",
        //   code: (row) => {
        //     return "<span>" + row.fileType + "</span>";
        //   },
        // },
        {
          type: "html",
          label: "挂接时间",
          list: "createDeptName",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "创建人",
          list: "provOrgName",
          code: (row) => {
            return "<span>" + row.createById + "</span>";

          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            return "<span>已挂接</span>";
          }
        },
      ],
      tableOpction: {
        label: "操作",
        width: "200px",
        value: 0,
        options: [
          {
            label: "删除",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            method: (row) => {
              this.handleDetele(row);
            },
          },
        ],
      },
      SearchItem: {
        name: "",
        fileType: "",
      },
      lastItem: {
        name: "",
        fileType: "",
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    // 重置
    reset() {
      const that = this;
      that.SearchItem.name = ""
      that.SearchItem.fileType = ""
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
    },
    // 获取列表
    async fetchData() {
      const that = this;
      let data = {};
      data = this.SearchItem
      data.uviewId = this.$route.query.uviewId;
      data.page = this.currentPage
      data.size = this.pageSize
      try {
        that.isSubmitLoading = true;
        const res = await assetFileExFindAll(data);
        that.isSubmitLoading = false;
        if (res.data.code === 1) {
          this.tableData = res.data.data.content;
          this.total = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    // 搜索
    SearchNoteList() {
      this.currentPage = 1;
      this.$refs.page.Page(1);
      this.fetchData();
    },
    // 翻页
    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      this.fetchData("page");
    },
    // 删除
    handleDetele(data) {
      this.$confirm("确定删除此数据吗？继续操作请确认", {
        type: "warning"
      }).then(async () => {
        assetFileExDelete(data.id).then(res => {
          if (res.data.code === 1) {
            this.$message.success(res.data.msg);
            this.fetchData();
          } else {
            this.$message.error(res.data.msg);
          }
        })
      }).catch(() => {
        return false;
      });

    },
  },
};
</script>
