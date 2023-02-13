<template>
  <div class="personal-demand-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input size="medium" placeholder="请输入需求标题" v-model="query.title" clearable>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-select size="medium" style="width:100%" v-model="query.acceptDept" clearable placeholder="请选择受理单位">
          <el-option v-for="item in acceptDeptOption" :key="item.id" :label="item.name" :value="item.name">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select size="medium" v-model="query.requestType" clearable placeholder="请选择需求类型">
          <el-option label="资源目录变更" value="0"> </el-option>
          <el-option label="资源目录新增" value="1"> </el-option>
          <el-option label="资源数据变更" value="2"> </el-option>
          <el-option label="资源数据新增" value="3"> </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select size="medium" v-model="query.status" clearable placeholder="请选择受理状态">
          <el-option label="未受理" value="0"> </el-option>
          <el-option label="已受理" value="1"> </el-option>
          <el-option label="已驳回" value="2"> </el-option>
        </el-select>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:10px;">
      <el-col :span="8">
        <el-date-picker size="medium" style="width: 100%" v-model="timeData" type="daterange" range-separator="~" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" @change="timechange">
        </el-date-picker>
      </el-col>
      <el-col :span="6">
        <el-button size="medium" @click="queryClick">查 询</el-button>
        <el-button size="medium" @click="resetForm">重置</el-button>
      </el-col>
    </el-row>
    <div class="query-list">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="title" label="需求标题"> </el-table-column>
        <el-table-column prop="acceptDept" label="受理部门"> </el-table-column>
        <el-table-column prop="requestType" label="需求类型">
          <template slot-scope="scope">
            <span v-if="scope.row.requestType === '0'">资源目录变更</span>
            <span v-if="scope.row.requestType === '1'">资源目录新增</span>
            <span v-if="scope.row.requestType === '2'">资源数据变更</span>
            <span v-if="scope.row.requestType === '3'">资源数据新增</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <span v-if="scope.row.status === '0'">未受理</span>
            <span v-if="scope.row.status === '1'">已受理</span>
            <span v-if="scope.row.status === '2'">已驳回</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提出日期">
          <template slot-scope="scope">
            <span>{{scope.row.createTime.split(' ')[0]}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">详情</el-button>
            <el-button v-if="scope.row.status !== '1'" type="text" size="small" @click="deleteClick(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
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
      acceptDeptOption: [],
      total: 0,
      timeData: [],
      tableData: [],
      query: {
        title: "",
        acceptDept: "",
        requestType: "",
        status: "",
        startTime: "",
        endTime: "",
        size: 5,
        page: 1,
      },
    };
  },
  mounted() {
    this.organizationFindAll().then((res) => {
      if (res.code === 1) {
        this.acceptDeptOption = res.data.children;
      } else {
        this.$message.error(res.msg);
      }
    });
    this.demandedInfoFindAllList(this.query);
  },
  methods: {
    timechange(time) {
      if (time) {
        this.query.startTime = time[0];
        this.query.endTime = time[1];
      } else {
        this.query.startTime = "";
        this.query.endTime = "";
      }
    },
    queryClick() {
      this.query.page = 1;
      this.$refs.page.Page(1);
      this.demandedInfoFindAllList(this.query);
    },
    resetForm() {
      this.timeData = [];
      this.query = {
        title: "",
        acceptDept: "",
        requestType: "",
        status: "",
        startTime: "",
        endTime: "",
        size: 5,
        page: 1,
      }
      this.demandedInfoFindAllList(this.query);
    },
    demandedInfoFindAllList(data) {
      this.demandedInfoFindAll(data).then((res) => {
        this.tableData = res.data.content;
        this.total = res.data.totalElements;
      });
    },
    sizeChange(val) {
      this.query.size = val;
      this.demandedInfoFindAllList(this.query);
    },
    currentChange(val) {
      this.query.page = val;
      this.demandedInfoFindAllList(this.query);
    },
    handleClick(row) {
      this.$router.push({
        path: "/personal/demandDetails",
        query: {
          id: row.id,
        },
      });
    },
    deleteClick(row) {
      this.$confirm('是否确认删除该需求?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.demandedInfoDelete(row.id).then((res) => {
          if (res.code === 1) {
            this.$message.success("删除成功！");
            this.demandedInfoFindAllList(this.query);
          } else {
            this.$message.error(res.msg);
          }
        });
      }).catch(() => {

      });

    },
  },
};
</script>
<style lang="scss" scope>
.personal-demand-page {
  padding: 10px;
  box-sizing: border-box;
  .query-list {
    margin-top: 20px;
  }
}
</style>