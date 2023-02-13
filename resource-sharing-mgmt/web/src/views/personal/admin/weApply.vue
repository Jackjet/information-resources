<template>
  <div class="personal-apiList-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input size="medium" placeholder="资源名称" v-model="query.resourceName" clearable>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-input size="medium" placeholder="信息资源名称" v-model="query.uviewNm" clearable>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-select size="medium" style="width:100%" v-model="query.provOrgName" clearable placeholder="请选择单位名称">
          <el-option v-for="item in acceptDeptOption" :key="item.id" :label="item.name" :value="item.name">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select size="medium" v-model="query.status" clearable placeholder="选择审核状态">
          <!-- <el-option key="0" label="未审核" value="0"></el-option>
          <el-option key="1" label="初审通过" value="1"></el-option>
          <el-option key="2" label="审核通过" value="2"></el-option> -->
          <el-option key="0" label="待审核" value="0"></el-option>
          <el-option key="1" label="审核通过待实施" value="1"></el-option>
          <el-option key="2" label="已实施" value="2"></el-option>
          <el-option key="3" label="已驳回" value="3"></el-option>
          <el-option key="4" label="审核失败" value="4"></el-option>
        </el-select>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:10px">
      <el-col :span="8">
        <el-date-picker size="medium" style="width: 100%" v-model="timeData" type="daterange" range-separator="~" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" @change="timechange">
        </el-date-picker>
      </el-col>
      <el-col :span="5">
        <el-button size="medium" @click="queryClick">查 询</el-button>
        <el-button size="medium" @click="resetForm">重置</el-button>
      </el-col>
    </el-row>

    <div class="query-list">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="resourceName" label="资源名称"></el-table-column>
        <el-table-column prop="uviewNm" label="信息资源名称">
        </el-table-column>
        <el-table-column prop="provOrgName" label="提供单位"> </el-table-column>
        <el-table-column prop="type" label="资源类型">
          <template slot-scope="scope">
            <span v-if="scope.row.type==1">云接口</span>
            <span v-if="scope.row.type==2">云文件</span>
            <span v-if="scope.row.type==3">云数据</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交日期" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.createTime.split(' ')[0]}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态">
          <template slot-scope="scope">
            <span v-if="scope.row.status === '0'">待审核</span>
            <span v-if="scope.row.status === '1'">审核通过待实施</span>
            <span v-if="scope.row.status === '2'">已实施</span>
            <span v-if="scope.row.status === '3'">已驳回</span>
            <span v-if="scope.row.status === '4'">审核失败</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">详情</el-button>
            <el-button type="text" size="small" v-if='scope.row.status == 0 || scope.row.status == 3' @click="deteleClick(scope.row)">删除</el-button>
            <el-button type="text" size="small" v-if='scope.row.status == 3' @click="auditDesc(scope.row.rejectDetail)">驳回原因</el-button>
            <el-dialog title="驳回原因" :visible.sync="dialogFormVisible">
              <el-form :model="form">
                <el-form-item label="驳回原因" label-width="120px">
                  <el-input v-model="auditDescContent" :disabled='true' type='textarea' autocomplete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
              </div>
            </el-dialog>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <Pagination ref="page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></Pagination>
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
      dialogFormVisible: false,
      auditDescContent: '',
      timeData: [],
      query: {
        sourceApiName: "",
        resourceName: "",
        provOrgName: "",
        status: "",
        startTime: "",
        endTime: "",
        page: 1,
        size: 5,
      },
      total: 0,
      tableData: [],
      acceptDeptOption: [],
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
    this.tableDataList(this.query);
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
    auditDesc(data) {
      this.dialogFormVisible = true
      this.auditDescContent = data
    },
    queryClick() {
      this.query.page = 1;
      this.$refs.page.Page(1);
      this.tableDataList(this.query);
    },
    resetForm() {
      this.timeData = [];
      this.query = {
        sourceApiName: "",
        resourceName: "",
        provOrgName: "",
        status: "",
        startTime: "",
        endTime: "",
        page: 1,
        size: 5,
      }
      this.tableDataList(this.query);
    },
    tableDataList(data) {
      this.resourceUseInfoFindAll(data).then((res) => {
        this.tableData = res.data.content;
        this.total = res.data.totalElements;
      });
    },
    sizeChange(val) {
      this.query.size = val;
      this.tableDataList(this.query);
    },
    currentChange(val) {
      this.query.page = val;
      this.tableDataList(this.query);
    },
    handleClick(row) {
      this.$router.push({
        path: "/personalDateil",
        query: {
          uviewId: row.uviewId,
          id: row.id,
          type: row.type,
        },
      });
    },
    deteleClick(row) {
      this.$confirm('是否确认删除该数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.resourceUseInfoDelete(row.id).then((res) => {
          if (res.code === 1) {
            this.$message.success("删除成功！");
            this.tableDataList(this.query);
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
.personal-apiList-page {
  padding: 10px;
  box-sizing: border-box;
  .query-list {
    margin-top: 20px;
    // height: 450px;
  }
}
</style>