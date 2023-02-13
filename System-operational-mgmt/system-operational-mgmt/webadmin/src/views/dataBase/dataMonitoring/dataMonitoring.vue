<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-input
            clearable
            size="medium"
            placeholder="请输入节点名称"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="medium"
            @click="SearchNoteList"
            icon="el-icon-search"
            >搜索</el-button
          >
          <el-button
            type="primary"
            size="medium"
            @click="add"
            icon="el-icon-add"
            >添加监控</el-button
          >
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList
        :table-data="tableData"
        v-loading="isSubmitLoading"
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
    <drawer
      :modal-objjj="modalObjjj"
      ref="callDetail"
      @Reload="SearchNoteList"
    ></drawer>
  </el-main>
</template>

<script>
import {
  databaseMonitoringList,
  databaseMonitoringDelete
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
          label: "内存阈值（M）",
          list: "ram"
        },
        {
          id: false,
          type: "",
          label: "存储阈值（M）",
          list: "rom"
        },
        {
          id: false,
          type: "",
          label: "告警方式",
          list: "alarmWay"
        },
        {
          id: false,
          type: "",
          label: "通知人",
          list: "peopleNotified"
        }
      ],
      tableOpction: {
        label: "操作",
        width: "300px",
        value: 0,
        options: [
          {
            label: "编辑",
            key: 0,
            type: "success",
            State: false,
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
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObjjj: "",
      SearchItem: {
        name: ""
      },
      lastItem: {
        name: ""
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      RootUrl: ""
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        name: this.SearchItem.name
      };
      try {
        const res = await databaseMonitoringList(data);
        if (res.data.code === 1) {
          res.data.data.content.map(function(v) {
            if (v.alarmWay === "sms") {
              v.alarmWay = "短信";
            } else {
              v.alarmWay = "邮件";
            }
          });
          this.tableData = res.data.data.content;
          this.total = res.data.data.totalElements;
        }
      } catch (even) {
        console.log(even);
        this.$message.error(even.msg);
      }
    },

    edit: function(row) {
      this.modalObjjj = "编辑";
      this.$refs.callDetail.initial(row.id);
    },

    add: function() {
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
      Object.entries(this.SearchItem).map((item) => {
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
          const response = await databaseMonitoringDelete(data);
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
    }
  }
};
</script>
