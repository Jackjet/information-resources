<template>
  <el-main class="main">
    <el-col :span="24">
      <el-button size="medium" @click="outUser" icon="el-icon-refresh-left">报表导出</el-button>
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
import { resourceUseLogFindAll } from "@/api/summary.js";
import { outExcel1 } from '@/utils/export';
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
          label: "操作类型",
          list: "processName",
          code: (row) => {
            return "<span>" + row.processName + "</span>";
          },
        },
        {
          type: "html",
          label: "审核时间",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime + "</span>";
          },
        },
        {
          type: "html",
          label: "审核人",
          list: "createByName",
          code: (row) => {
            return "<span>" + row.createByName + "</span>";
          },
        },
        {
          type: "html",
          label: "审核意见",
          list: "auditDesc",
          code: (row) => {
            if (row.auditDesc) {
              return "<span>" + row.auditDesc + "</span>";
            } else {
              return "<span></span>";
            }
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
        resourceId: this.$route.query.id,
        page: this.currentPage,
        size: this.pageSize
      };
      try {
        this.isSubmitLoading = true;
        const res = await resourceUseLogFindAll(data);
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
    outUser() {
      //   let that = this
      let data = {
        resourceId: this.$route.query.id,
      };
      //   (data = this.SearchItem)
      //   if (that.time !== null && that.time !== '' && that.time.length > 0) {
      //     data.startTime = this.time[0],
      //       data.endTime = this.time[1]
      //   } else {
      //     data.startTime = '',
      //       data.endTime = ''
      //   }
      outExcel1('请确认是否导出资源使用日志?', "webadmin/resourceUseLog/exportByProvOrgId", data, '资源使用日志')
    },
  },
};
</script>
