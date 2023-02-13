<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-select
            clearable
            size="medium"
            v-model="SearchItem.nodeId"
            placeholder="请选择节点名称"
            @change="nodeChange"
          >
            <el-option
              v-for="item in nodeList"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            clearable
            size="medium"
            v-model="SearchItem.sysId"
            placeholder="请选择系统名称"
          >
            <el-option
              v-for="item in sysList"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            clearable
            size="medium"
            v-model="SearchItem.typeStage"
            placeholder="请选择部署状态"
          >
            <el-option label="未部署" :value="['NotDeployed']"></el-option>
            <el-option label="部署中" :value="['Deploying']"></el-option>
            <el-option
              label="已部署"
              :value="['Deployed', 'NotDeployAble', 'InitDeployAble']"
            ></el-option>
          </el-select>
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
            >添加</el-button
          >
          <el-button
            type="primary"
            size="medium"
            @click="deploymentClick('')"
            icon="el-icon-add"
            >批量部署</el-button
          >
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <el-table
        ref="multipleTable"
        :data="table.list.data"
        empty-text="暂无数据"
        class="el_tab_alage"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          :selectable="checkboxT"
          :reserve-selection="true"
          width="50"
          align="center"
        ></el-table-column>
        <template v-for="(item, index) in table.list.header">
          <el-table-column
            fit
            align="center"
            :key="index"
            :label="item.label"
            :width="item.width"
            :prop="item.field"
          >
            <template slot-scope="scope">
              <template v-if="item.field === 'automatedPackPath'">
                <a
                  :href="scope.row.automatedPackPathDownload"
                  style="color: #58c5f5; cursor: pointer;"
                  @click="download(scope.row)"
                >
                  {{ scope.row[item.field] }}
                </a>
              </template>
              <template v-else>
                {{ scope.row[item.field] }}
              </template>
            </template>
          </el-table-column>
        </template>
        <el-table-column align="center" width="300" label="操作">
          <template slot-scope="scope">
            <el-button
              @click="deploymentClick(scope.row)"
              type="success"
              size="small"
              v-if="scope.row.typeStage === 'NotDeployed'"
            >
              部署
            </el-button>

            <el-button
              style="cursor: not-allowed;"
              type="info"
              size="small"
              v-if="scope.row.typeStage === 'Deploying'"
            >
              部署
            </el-button>

            <el-button
              @click="rollbackClick(scope.row)"
              type="success"
              size="small"
              v-if="scope.row.typeStage === 'Deployed'"
            >
              版本回滚
            </el-button>

            <el-button @click="edit(scope.row)" type="success" size="small">
              编辑
            </el-button>

            <el-button
              @click="detalis(scope.row)"
              type="success"
              size="small"
              v-if="
                scope.row.typeStage === 'Deployed' ||
                scope.row.typeStage === 'NotDeployAble' ||
                scope.row.typeStage === 'InitDeployAble'
              "
            >
              日志
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <el-col :span="24">
      <pagination
        ref="page"
        :total="total"
        @pageChange="pageChange"
      ></pagination>
    </el-col>
    <versionAdd :modal-objjj="title" @getList="getList" ref="add"> </versionAdd>
    <deploymentDetalis :modal-objjj="title" @getList="getList" ref="detalis">
    </deploymentDetalis>
  </el-main>
</template>

<script>
import {
  deploymentFindAll,
  deploymentApi,
  rollback,
} from "@/api/deploymentView/http";
import { findAllNodes, findAllServices } from "@/api/moduleMonitorView/http";
import TableList from "@/components/table/TableList.vue";
import Pagination from "@/components/table/Pagination.vue";
import versionAdd from "./versionAdd";
import deploymentDetalis from "../deploymentLogView/deploymentDetalis";
let idNum = 0;
let myTime = null;
export default {
  components: { TableList, Pagination, versionAdd, deploymentDetalis },
  data() {
    return {
      nodeList: [],
      sysList: [],
      tableArr: [],
      title: "添加",
      table: {
        list: {
          header: [
            {
              label: "版本号",
              field: "versionNumber",
              width: "70",
            },
            {
              label: "节点名称",
              field: "nodeName",
              width: "110",
            },
            {
              label: "系统名称",
              field: "sysName",
              width: "110",
            },
            {
              label: "部署包",
              field: "automatedPackPath",
              width: "110",
            },
            {
              label: "部署接口URL",
              field: "apiUrl",
              width: "110",
            },
            {
              label: "部署状态",
              field: "typeStageText",
              width: "110",
            },
            {
              label: "升级说明",
              field: "remark",
            },
          ],

          data: [],
        },
      },
      SearchItem: {
        nodeId: "",
        sysId: "",
        typeStage: "",
      },
      lastItem: {
        nodeId: "",
        sysId: "",
        typeStage: "",
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
    };
  },
  created() {
    this.getList();
    this.getNodeList();
    myTime = setInterval(() => {
      this.getList();
    },  10000)
  },
  beforeDestroy() {
    clearInterval(myTime)
  },
  methods: {
    checkboxT(row, index) {
      if (row.typeStage === "NotDeployed") {
        return 1;
      } else {
        return 0;
      }
    },
    getRowKeys(row) {
      return row.id;
    },
    handleSelectionChange(row) {
      // 只要id
      // let arr = [];
      // row.forEach((item) => {
      //   arr.push(item.id);
      // });
      this.tableArr = row;
    },
    async getList() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        nodeId: this.SearchItem.nodeId,
        sysId: this.SearchItem.sysId,
        type: "deployment",
        typeStageIn: this.SearchItem.typeStage + "",
      };
      try {
        const res = await deploymentFindAll(data);
        res.data.data.content.forEach((item) => {
          item.automatedPackPathDownload =
            process.env.VUE_APP_FILE_API +
            item.automatedPackPath +
            "?type=deployment";
          switch (item.typeStage) {
            case "NotDeployed":
              item.typeStageText = "未部署";
              break;
            case "Deploying":
              item.typeStageText = "部署中";
              break;
            case "Deployed":
              item.typeStageText = "已部署";
              break;
            case "NotDeployAble":
              item.typeStageText = "已部署";
              break;
            case "InitDeployAble":
              item.typeStageText = "已部署";
              break;
          }
        });
        this.table.list.data = res.data.data.content;
        this.total = res.data.data.totalElements;
      } catch (even) {
        console.log(even);
        this.$message.error(even.msg);
      }
    },
    async getNodeList() {
      const res = await findAllNodes();
      res.data.data.forEach((item) => {
        this.nodeList.push({
          id: item.id,
          name: item.name,
        });
      });
    },
    nodeChange() {
      this.SearchItem.sysId = "";
      this.sysList = [];
      this.getSysList();
    },
    async getSysList() {
      let data = {
        nodeId: this.SearchItem.nodeId,
      };
      const res = await findAllServices(data);
      res.data.data.forEach((item) => {
        this.sysList.push({
          id: item.id,
          name: item.name,
        });
      });
    },
    add() {
      this.title = "添加";
      this.$refs.add.initial("");
    },
    edit(row) {
      this.title = "编辑";
      this.$refs.add.initial(row);
    },
    detalis(row) {
      this.title = "日志";
      this.$refs.detalis.initial(row.id, true);
    },
    SearchNoteList() {
      this.currentPage = 1;
      Object.entries(this.SearchItem).map((item) => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.getList();
    },
    // 部署
    deploymentClick(row) {
      const that = this;
      if (row) {
        this.tableArr = []
        this.tableArr.push(row);
      }
      if (this.tableArr.length === 0) {
        this.$message.error("请选择版本");
        return false;
      }
      // 判断是否为同一系统
      let arr = []
      this.tableArr.forEach(item => {
        arr.push(item.sysId)
      })
      if (new Set(arr).size !== arr.length) {
        this.$message.error("同一系统服务的不同版本不能批量部署");
        return false;
      }
      that
        .$confirm("确定部署吗?", "提示", {
          type: "warning",
        })
        .then(() => {
          try {
            this.tableArr.forEach(async (item) => {
              const response = await deploymentApi({ id: item.id });
              if (response.data.code === 1) {
                idNum++;
                if (this.tableArr.length === idNum) {
                  this.$message.success('正在部署...');
                }
                this.tableArr = [];
                this.getList();
              } else {
                that.$message.error(response.data.msg);
                if (row) {
                  this.tableArr = [];
                }
              }
            });
            setTimeout(() => {
              this.$refs.multipleTable.clearSelection()
            }, 200)
          } catch (even) {
            console.log(even);
          }
        })
        .catch(() => {
          return false;
        });
    },
    // 版本回滚
    rollbackClick(row) {
      const that = this;
      that
        .$confirm("确定版本回滚吗?", "提示", {
          type: "warning",
        })
        .then(async () => {
          const response = await rollback({ id: row.id });
          if (response.data.code === 1) {
            that.$message.success(response.data.msg);
            this.getList();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },

    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(this.SearchItem).map((item) => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.getList();
    },
  }
};
</script>
