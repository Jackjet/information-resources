<template>
  <div class="personal-apiList-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input placeholder="请输入云数据名称" v-model="query.sourceApiName" clearable>
        </el-input>
      </el-col>
      <el-col :span="6">
        <!-- <el-input placeholder="请输入提供单位" v-model="query.provOrgName" clearable>
        </el-input> -->
        <el-select size="medium" style="width:100%" v-model="query.provOrgName" clearable placeholder="请输入提供单位">
          <el-option v-for="item in acceptDeptOption" :key="item.id" :label="item.name" :value="item.name">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select v-model="query.status" clearable placeholder="请选择审核状态">
          <el-option label="审核中" value="0"> </el-option>
          <el-option label="审核通过" value="1"> </el-option>
          <el-option label="审核驳回" value="2"> </el-option>
          <el-option label="审核失败" value="3"> </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-button @click="queryClick">查 询</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-col>
    </el-row>
    <div class="query-list">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="sourceApiName" label="云数据名称">
        </el-table-column>
        <!-- <el-table-column prop="sourceApiDesc" label="云数据描述">
        </el-table-column> -->
        <el-table-column prop="uviewNm" label="信息资源名称">
        </el-table-column>
        <el-table-column prop="provOrgName" label="提供单位"> </el-table-column>
        <el-table-column prop="createTime" label="提交日期" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.createTime.split(' ')[0]}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态">
          <template slot-scope="scope">
            <span v-if="scope.row.status === '0'">审核中</span>
            <span v-if="scope.row.status === '1'">审核通过</span>
            <span v-if="scope.row.status === '2'">审核驳回</span>
            <span v-if="scope.row.status === '3'">审核失败</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status!=='0'&&scope.row.status !== '2'" @click="handleClick(scope.row)" type="text" size="small">API详情</el-button>
            <el-button type="text" size="small" v-if='scope.row.status == 0 || scope.row.status == 3' @click="deteleClick(scope.row)">删除</el-button>
            <el-button type="text" size="small" v-if='scope.row.status == 2' @click="auditDesc(scope.row.auditDesc)">驳回原因</el-button>
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
    <Pagination :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></Pagination>
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
      query: {
        sourceApiName: "",
        provOrgName: "",
        status: "",
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
    auditDesc(data) {
      this.dialogFormVisible = true
      this.auditDescContent = data
    },
    queryClick() {
      this.tableDataList(this.query);
    },
    resetForm() {
      this.query = {
        sourceApiName: "",
        provOrgName: "",
        status: "",
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
        path: "/catalogue/apiDetails",
        query: {
          id: row.uviewApiId,
          uviewId: row.uviewId,
        },
      });
      // this.$router.push({
      //   path: "/personal/apiListDetails",
      //   query: {
      //     id: row.id,
      //   },
      // });
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