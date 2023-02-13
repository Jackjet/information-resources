<template>
  <el-main class="main">
    <el-col :span="24" class="center">
      <div style="margin-bottom:20px;">
        <el-button @click="addRouter">添 加</el-button>
      </div>
      <el-table v-loading="isSubmitLoading" class="table-list" :cell-style="{'font-size': '14px','height': '40px', 'padding': '0'}" :header-cell-style="{'font-size': '14px', 'height': '40px', 'padding': '0', 'background-color': '#F5F7FA'}" row-key="id" :data="tableData" style="width: 100%;" stripe="true">
        <el-table-column prop="name" label="系统名称"></el-table-column>
        <el-table-column prop="path" align="center" label="菜单地址"></el-table-column>
        <el-table-column prop="seq" align="center" label="排序">
          <template slot-scope="scope">
            <template v-if="scope.row.seq === total">
              <el-button type="text" size="medium" icon="el-icon-top" @click="menuSeq(0,scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq === 1">
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="menuSeq(1,scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq !== 1 && scope.row.seq !== total">
              <el-button type="text" size="medium" icon="el-icon-top" @click="menuSeq(0,scope.row)"></el-button>
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="menuSeq(1,scope.row)"></el-button>
            </template>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="medium" icon="el-icon-edit-outline" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="medium" icon="el-icon-delete" @click="handleDetail(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-main>
</template>
<script>
import { guideFindAll, guideDelete, guideUp, guideDown } from "@/api/roleManagement.js"
export default {
  data() {
    return {
      isSubmitLoading: false,
      tableData: [],
      total: 0,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    // 上下移动
    async menuSeq(type, row) {
      try {
        this.isSubmitLoading = true;
        let res = null;
        if (type === 0) {
          res = await guideUp({ id: row.id });
        } else {
          res = await guideDown({ id: row.id });
        }
        this.isSubmitLoading = false
        if (res.data.code === 1) {
          this.fetchData()
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.isSubmitLoading = false
        this.$message.error(even.msg)
      }
    },
    handleEdit(data) {
      this.$router.push({
        path: "/serviceGuidelinesAdd",
        query: {
          id: data.id,
          name: data.name,
          path: data.path,
        }
      });
    },
    handleDetail(data) {
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        guideDelete(data.id).then(res => {
          if (res.data.code === 1) {
            this.$message.success(res.data.msg)
            this.fetchData();
          } else {
            this.$message.error(res.data.msg);
          }
        })
      }).catch(() => { });
    },
    addRouter() {
      this.$router.push("/serviceGuidelinesAdd");
    },
    // 获取列表
    async fetchData() {
      let data = {
        page: 1,
        size: 10
      };
      try {
        this.isSubmitLoading = true;
        const res = await guideFindAll(data);
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
  },

};
</script>
