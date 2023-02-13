<template>
  <el-main class="main">
    <el-col :span="24" class="center">
      <el-table ref="table_name" v-loading="isSubmitLoading" class="table-list" :cell-style="{'font-size': '14px','height': '40px', 'padding': '0'}" :header-cell-style="{'font-size': '14px', 'height': '40px', 'padding': '0', 'background-color': '#F5F7FA'}" row-key="id" :data="tableData" @selection-change="handleSelectionChange" style="width: 100%;" stripe="true">
        <el-table-column type="selection" width="50" align="center" :reserve-selection="true"></el-table-column>
        <el-table-column prop="name" label="系统名称"></el-table-column>
        <el-table-column prop="path" align="center" label="菜单地址"></el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 20px;">
        <el-button @click="cancelClick">取 消</el-button>
        <el-button @click="submitClick">保 存</el-button>
      </div>
    </el-col>
  </el-main>
</template>
<script>
import { guideRoleInsert, guideRoleGet } from "@/api/roleManagement.js";
export default {
  data() {
    return {
      isSubmitLoading: false,
      tableData: [],
      DeletelistiD: [],
      chack: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        this.isSubmitLoading = true;
        let res = await guideRoleGet({ roleId: this.$route.query.id });
        this.isSubmitLoading = false;
        if (res.data.code === 1) {
          this.tableData = res.data.data.wayEntities;

          this.$nextTick(() => {
            res.data.data.choose.forEach(item => {
              res.data.data.wayEntities.forEach(key => {
                if (item === key.id) {
                  this.$refs.table_name.toggleRowSelection(key, true);
                  this.DeletelistiD.push(key.id);
                }
              })
            })
          })
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    //全选
    handleSelectionChange(vals) {
      this.DeletelistiD = [];
      vals.map((v, k) => {
        this.DeletelistiD.push(v.id)
      })
    },
    // 取消
    cancelClick() {
      // this.DeletelistiD = [];
      // this.$refs.table_name.clearSelection();
      this.$router.go(-1);
    },
    // 保存
    submitClick() {
      guideRoleInsert({
        // roleId: JSON.parse(sessionStorage.getItem('UserInfo')).id,
        roleId: this.$route.query.id,
        wayIds: this.DeletelistiD
      }).then(res => {
        if (res.data.code === 1) {
          this.$message.success(res.data.msg);
          this.$router.push("/role");
        } else {
          this.$message.error(res.data.msg)
          return false
        }
      })
    }
  }
};
</script>
