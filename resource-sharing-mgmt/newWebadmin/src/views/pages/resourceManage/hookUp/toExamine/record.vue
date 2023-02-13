<template>
  <el-main class="main">
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
import { assetExLogFindAll } from "@/api/fileData.js"
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
          label: "操作备注",
          list: "statusDetail",
          code: (row) => {
            return "<span>" + row.detail + "</span>";
          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            return "<span>" + row.statusDetail + "</span>";
            // switch (row.status) {
            //   case "0":
            //     return "<span>" + "未审核" + "</span>";
            //     break;
            //   case "1":
            //     return "<span>" + "已审核" + "</span>";
            //     break;
            //   case "2":
            //     return "<span>" + "已驳回" + "</span>";
            //     break;
            //   case "3":
            //     return "<span>" + "审批失败" + "</span>";
            //     break;
            //   default:
            //     return "<span>" + "未知" + "</span>";
            //     break;
            // }
          },
        },
        {
          type: "html",
          label: "操作人",
          list: "createByName",
          code: (row) => {
            return "<span>" + row.createByName + "</span>";
          },
        },
        {
          type: "html",
          label: "操作时间",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
      ],
      tableOpction: {
        label: "操作",
        width: "200px",
        value: 1,
        options: [
          // {
          //   label: "详情",
          //   key: 0,
          //   type: "text",
          //   icon: "el-icon-tickets",
          //   State: false,
          //   method: (row) => {
          //     this.handleDetail(row);
          //   },
          // },
        ],
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
    // 获取列表
    async fetchData() {
      let data = {
        assetExId: this.$route.query.id,
        page: this.currentPage,
        size: this.pageSize
      };
      try {
        this.isSubmitLoading = true;
        const res = await assetExLogFindAll(data);
        this.isSubmitLoading = false;
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
    // 翻页
    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      this.fetchData("page");
    },
  },
};
</script>
