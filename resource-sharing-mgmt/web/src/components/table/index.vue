<template>
  <div class="table-box">
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column v-if="tableDetail.isFlag" :prop="tableDetail.prop" :label="tableDetail.label" :width="tableDetail.width">
        <template slot-scope="scope">
          <span v-if="scope.row.name" style="
              color: #568bd5;
              border-bottom: 1px solid #568bd5;
              cursor: pointer;
            " @click="dateilsClick(scope.row)">{{ scope.row.name }}</span>
          <span v-if="scope.row.tableName" style="
              color: #568bd5;
              border-bottom: 1px solid #568bd5;
              cursor: pointer;
            " @click="dateilsClick(scope.row)">{{ scope.row.tableName }}</span>
        </template>
      </el-table-column>
      <el-table-column v-for="(item, index) in tableHeader" :key="index" :prop="item.prop" :label="item.label" :width="item.width">
      </el-table-column>
      <el-table-column v-if="tableButton.isFlag" :label="tableButton.label" :width="tableButton.width">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">{{
            tableButton.textBtn
          }}</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import { getToken } from '@/utils/storage.js';
export default {
  props: {
    tableDetail: {
      type: Object,
      default: () => {
        return {};
      },
    },
    tableHeader: {
      type: Array,
      default: () => {
        return [];
      },
    },
    tableData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    tableButton: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {};
  },
  methods: {
    dateilsClick(row) {
      let type = this.tableButton.type === "云接口" ? '1' : this.tableButton.type === "云文件" ? '2' : '3';
      this.$router.push({
        path: "/catalogue/apiDetails",
        query: {
          id: row.id,
          uviewId: row.uviewId,
          type: type,
        },
      });
    },
    handleClick(row) {
      if (!getToken()) {
        this.$confirm('此操作需要登录, 是否跳转至登录页?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          location.href = "login.html?url=" + location.href;
        }).catch(() => {
          return false;
        });
      } else {
        this.$router.push({
          path: "/catalogue/apiApplay",
          query: {
            id: row.id,
            Id: this.$route.query.id,
            type: this.tableButton.type,
          },
        });
      }
    },
  },
};
</script>