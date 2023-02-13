<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入用户姓名" prefix-icon="el-icon-search" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入手机号" prefix-icon="el-icon-search" v-model="SearchItem.tel">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable size="medium" width="50" placeholder="请输入单位" prefix-icon="el-icon-search" v-model="SearchItem.organization">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.roleId" placeholder="请选择角色">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.status" placeholder="请选择状态">
            <el-option label="正常" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="medium" @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <!-- <el-button
            type="primary"
            size="medium"
            @click="add"
            icon="el-icon-add"
            >添加</el-button
          > -->
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList @edit="edit" @delete="deleteData" @ban="ban" @reset="reset" :table-data="tableData" v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <drawer :modal-objj="modalObjj" ref="callDetaill" @Reload="SearchNoteList">
    </drawer>
  </el-main>
</template>

<script>
import {
  getList,
  roleList,
  deUser,
  updateStatus,
  setPasswd
} from "@/api/omManger/user";
import TableList from "@/components/table/TableList.vue";
import Pagination from "@/components/table/Pagination.vue";
import drawer from "./add";
import md5 from "js-md5";
export default {
  components: { TableList, Pagination, drawer },
  data() {
    return {
      drawer: false,
      tableData: [],
      tableHeader: [
        {
          id: false,
          type: "",
          label: "用户姓名",
          list: "name"
        },
        {
          id: false,
          type: "",
          label: "手机号",
          list: "tel"
        },
        {
          id: false,
          type: "",
          label: "单位",
          list: "organization"
        },
        {
          id: false,
          type: "",
          label: "角色",
          list: "roleName"
        },
        {
          id: false,
          type: "",
          label: "状态",
          list: "status"
        },
        {
          id: false,
          type: "",
          label: "备注",
          list: "remark"
        },
        {
          id: false,
          type: "operation",
          label: "操作",
          list: "operation",
          width: "350"
        }
      ],
      tableOpction: [],
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObjj: "",
      SearchItem: {
        name: "",
        organization: "",
        roleId: "",
        status: "",
        tel: ""
      },
      lastItem: {
        name: "",
        organization: "",
        roleId: "",
        status: "",
        tel: ""
      },
      options: [],
      total: 0,
      pageSize: "20",
      currentPage: "1",
      RootUrl: ""
    };
  },
  created() {
    this.findNodes();
    this.fetchData();
  },
  methods: {
    async fetchData() {
      const data = this.SearchItem;
      data.page = this.currentPage;
      data.size = this.pageSize;
      try {
        const res = await getList(data);
        if (res.data.code === 1) {
          res.data.data.content.forEach(item => {
            item.operation = [
              {
                label: "编辑",
                type: "success",
                State: false,
                click: "edit"
              },
              // {
              //   label: "删除",
              //   type: "danger",
              //   State: false,
              //   click: "delete"
              // },
              // {
              //   label: "重置密码",
              //   type: "info",
              //   State: false,
              //   click: "reset"
              // }
            ];
            if (item.status === "0") {
              item.status = "禁用";
              // item.operation.push({
              //   label: "启用",
              //   type: "warning",
              //   State: false,
              //   click: "ban"
              // });
            } else {
              item.status = "正常";
              // item.operation.push({
              //   label: "禁用",
              //   type: "warning",
              //   State: false,
              //   click: "ban"
              // });
            }
            if (item.id === "admin") {
              item.operation = [
                {
                  label: "编辑",
                  type: "success",
                  State: false,
                  click: "edit"
                }
              ];
            }
          });
          this.tableData = res.data.data.content;
          this.total = res.data.data.totalElements;
        } else {
          this.$message.error(even.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    SearchNoteList() {
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.fetchData();
    },
    add: function () {
      this.modalObjj = "添加";
      this.$refs.callDetaill.initial(false);
    },
    edit: function (row) {
      this.modalObjj = "编辑";
      this.$refs.callDetaill.initial(row.id);
    },
    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.fetchData();
    },

    // 删除当前单条数据 重载列表
    deleteData(row) {
      const that = this;
      that
        .$confirm("此操作将永久删除数据, 是否继续?", "提示", {
          type: "warning"
        })
        .then(async () => {
          that.Loading = true;
          const response = await deUser(row.id);
          that.Loading = false;
          if (response.data.code === 1) {
            that.$message.success("删除成功");
            this.fetchData();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },
    // 删除当前单条数据 重载列表
    ban(row) {
      const that = this;
      that
        .$confirm("此操作将" + (row.status === "禁用" ? "启用" : "禁用") + "账号, 是否继续?", "提示", {
          type: "warning"
        })
        .then(async () => {
          that.Loading = true;
          let status = "";
          if (row.status === "禁用") {
            status = 1;
          } else {
            status = 0;
          }
          let data = {
            id: row.id,
            status: status
          };
          const response = await updateStatus(data);
          that.Loading = false;
          if (response.data.code === 1) {
            that.$message.success(row.status === "禁用" ? "启用成功" : "禁用成功");
            this.fetchData();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },

    reset(row) {
      const that = this;
      that
        .$confirm("此操作将重置密码, 是否继续?", "提示", {
          type: "warning"
        })
        .then(async () => {
          that.Loading = true;
          let data = {
            id: row.id
          };
          const response = await setPasswd(data);
          that.Loading = false;
          if (response.data.code === 1) {
            that.$message.success(response.data.msg);
            this.fetchData();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },
    async findNodes(data) {
      const that = this;
      that.loading = true;
      const response = await roleList();
      that.loading = false;
      if (response.data.code === 1) {
        that.options = [];
        response.data.data.content.map(function (v, k) {
          that.options.push({ value: v.id, label: v.name });
        });
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg);
        return false;
      }
    }
  }
};
</script>




