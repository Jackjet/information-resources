<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-select clearable v-model="SearchItem.nodeId" @change="nodeChange" placeholder="请选择节点名称">
            <el-option v-for="item in nodeList" :key="item.value" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.databaseId" placeholder="请选择数据库名称">
            <el-option v-for="item in databasesList" :key="item.value" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="medium" @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size="medium" @click="add" icon="el-icon-add">添加备份</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <drawer :modal-objjj="modalObjjj" ref="callDetail" @Reload="SearchNoteList"></drawer>
  </el-main>
</template>

<script>
import {
  databaseBackupList,
  databaseBackupDelete,
  databaseBackupUpdate,
  getFindAllDatabases,
  getFindAllNodes
} from "@/api/dataBase/dataBase";
import TableList from "@/components/table/TableList.vue";
import Pagination from "@/components/table/Pagination.vue";
import drawer from "./detail";
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
          label: "节点名称",
          list: "nodeName"
        },
        {
          id: false,
          type: "",
          label: "数据库名称",
          list: "databaseName"
        },
        {
          id: false,
          type: "",
          label: "备份时间",
          list: "createTime"
        },
        {
          id: false,
          type: "",
          label: "备份位置",
          list: "location"
        },
        {
          id: false,
          type: "",
          label: "备份结果",
          list: "result"
        },
        {
          id: false,
          type: "",
          label: "备注",
          list: "remark"
        }
      ],
      tableOpction: {
        label: "操作",
        width: "300px",
        value: 0,
        options: [
          {
            label: "恢复",
            key: 0,
            type: "success",
            State: false,
            isDisabled: (row) => {
              if (row.result === '成功') {
                return false;
              } else {
                return true;
              }
            },
            method: row => {
              this.edit(row);
            }
          },
          {
            label: "删除",
            key: 0,
            type: "danger",
            State: false,
            method: row => {
              this.delete(row);
            }
          }
        ]
      },
      nodeList: [],
      databasesList: [],
      optionsss: [],
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObjjj: "",
      SearchItem: {
        name: "",
        nodeId: "",
        databaseId: "",
        instanceId: ""
      },
      lastItem: {
        name: "",
        nodeId: "",
        databaseId: ""
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      RootUrl: ""
    };
  },
  created() {
    this.fetchData();
    // this.findList()
    this.findNodes();
  },
  methods: {
    async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        name: this.SearchItem.name,
        nodeId: this.SearchItem.nodeId,
        databaseId: this.SearchItem.databaseId,
        instanceId: this.SearchItem.instanceId
      };
      try {
        const res = await databaseBackupList(data);
        res.data.data.content.map(function (v, k) {
          if (v.result === "1") {
            v.result = "成功";
          } else {
            v.result = "失败";
          }
        });
        this.tableData = res.data.data.content;
        this.total = res.data.data.totalElements;
      } catch (even) {
        this.$message.error(even.msg);
      }
    },

    add: function () {
      this.modalObjjj = "添加";
      this.$refs.callDetail.initial(false);
    },
    SearchNoteList() {
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.fetchData();
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
    delete(row) {
      const that = this;
      that
        .$confirm("此操作将永久删除数据, 是否继续?", "提示", {
          type: "warning"
        })
        .then(async () => {
          that.Loading = true;
          let data = {
            id: row.id
          };
          const response = await databaseBackupDelete(data);
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
    edit(row) {
      const that = this;
      that
        .$confirm("此操作将恢复数据, 是否继续?", "提示", {
          type: "warning"
        })
        .then(async () => {
          // that.$message.success("恢复成功");
          that.Loading = true;
          let data = {
            id: row.id
          };
          const response = await databaseBackupUpdate(data);
          that.Loading = false;
          if (response.data.code === 1) {
            that.$message.success("恢复成功");
            this.fetchData();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },
    async findNodes() {
      const res = await getFindAllNodes()
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    nodeChange() {
      this.SearchItem.databaseId = ''
      this.databasesList = []
      this.findDatabases()
    },
    async findDatabases() {
      let data = {
        nodeId: this.SearchItem.nodeId
      }
      const res = await getFindAllDatabases(data)
      res.data.data.forEach(item => {
        this.databasesList.push({
          id: item.id,
          name: item.name
        })
      })
    }
  }
};
</script>
