<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-input
            clearable
            size="medium"
            placeholder="请输入系统名称"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            placeholder="请输入模块名称"
            prefix-icon="el-icon-search"
            v-model="SearchItem.moduleName"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-select
            size="medium"
            v-model="SearchItem.unit"
            placeholder="请选择统计单位"
          >
            <el-option label="时" value="hour"></el-option>
            <el-option label="日" value="day"></el-option>
            <el-option label="周" value="week"></el-option>
            <el-option label="月" value="month"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="valueTime"
            size="medium"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="medium"
            @click="SearchNoteList"
            icon="el-icon-search"
            >搜索</el-button
          >
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList
        :table-data="tableData"
        :table-label="tableHeader"
        :table-option="tableOpction"
      ></TableList>
    </el-col>
    <el-col :span="24">
      <pagination
        ref="page"
        :total="total"
        @pageChange="pageChange"
      ></pagination>
    </el-col>
  </el-main>
</template>

<script>
import { count } from "@/api/moduleMonitorView/http";
import TableList from "@/components/table/TableList.vue";
import Pagination from "@/components/table/Pagination.vue";
export default {
  components: { TableList, Pagination },
  data() {
    return {
      valueTime: "",
      tableData: [],
      tableHeader: [
        {
          id: false,
          type: "",
          label: "系统名称",
          list: "name"
        },
        {
          id: false,
          type: "",
          label: "模块名称",
          list: "moduleName"
        },
        {
          id: false,
          type: "",
          label: "访问次数",
          list: "number"
        },
        {
          id: false,
          type: "",
          label: "访问时间",
          list: "date"
        }
      ],
      tableOpction: {},
      SearchItem: {
        name: "",
        moduleName: "",
        unit: "day",
        startTime: "",
        endTime: ""
      },
      lastItem: {
        name: "",
        moduleName: "",
        unit: "day",
        startTime: "",
        endTime: ""
      },
      total: 0,
      pageSize: "20",
      currentPage: "1"
    };
  },
  created() {
    var date = new Date();
    var year = date.getFullYear() + "";
    var month = date.getMonth() + 1 + "";
    month = month < 10 ? "0" + month : month
    // 本月第一天日期
    var begin = year + "-" + month + "-01";
    // 本月最后一天日期
    var lastDateOfCurrentMonth = new Date(year, month, 0);
    var end = year + "-" + month + "-" + lastDateOfCurrentMonth.getDate();
    year + "-" + month + "-" + lastDateOfCurrentMonth.getDate() + " 59:59:59";
    this.valueTime = [begin, end];
    this.getList();
  },
  methods: {
    async getList() {
      if (this.valueTime) {
        this.SearchItem.startTime = this.valueTime[0] + " 00:00:00";
        this.SearchItem.endTime = this.valueTime[1] + " 23:59:59";
      } else {
        this.SearchItem.startTime = "";
        this.SearchItem.endTime = "";
      }
      if (this.SearchItem.startTime === '') {
        this.$message.error('请选择日期');
        return false
      }
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        name: this.SearchItem.name,
        moduleName: this.SearchItem.moduleName,
        unit: this.SearchItem.unit,
        startTime: this.SearchItem.startTime,
        endTime: this.SearchItem.endTime
      };
      try {
        const res = await count(data);
        if (res.data.code === 1) {
          res.data.data.content.forEach(item => {
            if (this.SearchItem.unit === "hour") {
              item.date = item.date + '时';
            }
          });
          this.tableData = res.data.data.content;
          this.total = res.data.data.totalElements;
          return false;
        }
        this.$message.error(res.data.msg);
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    SearchNoteList() {
      let that = this;
      if (that.valueTime) {
        that.SearchItem.startTime = that.valueTime[0] + " 00:00:00";
        that.SearchItem.endTime = that.valueTime[1] + " 23:59:59";
      } else {
        that.SearchItem.startTime = "";
        that.SearchItem.endTime = "";
      }
      this.currentPage = 1;
      Object.entries(that.SearchItem).map(item => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
      this.getList();
    },
    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.getList();
    }
  }
};
</script>
