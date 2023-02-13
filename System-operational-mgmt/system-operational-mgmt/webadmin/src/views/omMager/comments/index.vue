<template>
  <el-main class="lunbo-main" v-loading="Loading">
    <el-row>
      <el-col class="lunbo-btn" :span="24" style="text-align: left">
        <el-form :inline="true" class="el-InputForm">
          <el-row v-if="tab.type == 0">
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入角色名称" prefix-icon="el-icon-search" v-model="SearchItem.name">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="search">搜索
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="add">添加
              </el-button>
            </el-form-item>
          </el-row>
          <el-row v-if="tab.type == 1">
            <el-form-item :label-width="this.formLabelWidth" class="InpitWidth">
              <el-select clearable v-model="SearchItem.nodeId" :disabled="ProhibitFromm" @change="selectGet" placeholder="请选择节点名称">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入系统名称" prefix-icon="el-icon-search" v-model="SearchItem.name">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入所属平台或系统" prefix-icon="el-icon-search" v-model="SearchItem.systemAndPlatform">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="search">搜索
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="add">添加
              </el-button>
            </el-form-item>
          </el-row>
          <el-row v-if="tab.type == 2">
            <el-form-item :label-width="this.formLabelWidth" class="InpitWidth">
              <el-select clearable v-model="SearchItem.nodeId" :disabled="ProhibitFromm" @change="selectGet" placeholder="请选择节点名称">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入数据库名称" prefix-icon="el-icon-search" v-model="SearchItem.name">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入所属平台或系统" prefix-icon="el-icon-search" v-model="SearchItem.systemAndPlatform">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="search">搜索
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="add">添加
              </el-button>
            </el-form-item>
          </el-row>
          <el-row v-if="tab.type == 3">
            <el-form-item :label-width="this.formLabelWidth" class="InpitWidth">
              <el-input clearable size="medium" placeholder="请输入节点名称" prefix-icon="el-icon-search" v-model="SearchItem.name">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入公网IP" prefix-icon="el-icon-search" v-model="SearchItem.publicIp">
              </el-input>
            </el-form-item>
            <!-- 由于唐山项目使用内网连接数据库，实际做的是使用外网连接为了改动小，将内网和外网IP字段互换 -->
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入内网IP" prefix-icon="el-icon-search" v-model="SearchItem.intranetIp">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="search">搜索
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="medium" icon="el-icon-search" @click="add">添加
              </el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </el-col>
    </el-row>
    <el-tabs class="lunbo-tab" clearable v-model="tab.type" type="border-card" @tab-click="initial()">
      <el-tab-pane v-for="item in typeList" :key="item.value" :label="item.label" :name="item.value">
        <!-- 调用表格 -->
        <el-col :span="24">
          <tableForm :table-data="tableData" v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOption">
          </tableForm>
        </el-col>
        <!-- 调用分页 -->
        <el-col :span="24">
          <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
        </el-col>
      </el-tab-pane>
    </el-tabs>
    <nodedrawer :modal-objj="modalObjj" ref="callDetaill" @Reload="search">
    </nodedrawer>
    <servicedrawer :modal-objjj="modalObjjj" ref="callDetailS" @Reload="search">
    </servicedrawer>
    <databasedrawer :modal-objjf="modalObjjf" ref="callDetailF" @Reload="search">
    </databasedrawer>
    <drawer :modal-objjr="modalObjjr" ref="callDetailR" @Reload="search">
    </drawer>
    <!-- <change ref="ChildChange">
    </change> -->
    <!-- <drawer
      ref="call"
      :drawer-dialog='mode'
      @Reload='initial'>
    </drawer> -->
  </el-main>
</template>

<script>
import {
  getNodeList,
  deNodeModel,
  getServiceList,
  deServiceModel,
  getRoleList,
  deDatabase,
  getDatabaseList,
  deleteRole,
  getRoleId,
  insertRole,
  updateRole,
  findAllNodes
} from "@/api/omManger/comments";
import tableForm from "@/components/table/TableList.vue";
import paging from "@/components/table/Pagination.vue";
import nodedrawer from "./node";
import servicedrawer from "./service";
import databasedrawer from "./database";
import drawer from "./role";
export default {
  components: {
    tableForm,
    paging,
    nodedrawer,
    servicedrawer,
    databasedrawer,
    drawer
  },
  data() {
    return {
      tableData: [],
      tableSelection: {
        key: true,
        type: "",
        detaile: false
      },
      options: [],
      SearchItem: {
        name: "",
        nodeId: "",
        systemAndPlatform: "",
        publicIp: "",
        intranetIp: ""
      },
      lastItem: {
        name: "",
        nodeId: "",
        systemAndPlatform: "",
        publicIp: "",
        intranetIp: ""
      },
      tab: {
        type: ""
      },
      tableHeader: [],
      editableTabsValue: "0",
      tableOption: "",
      typeList: [
        {
          value: "0",
          label: "角色管理"
        },
        {
          value: "3",
          label: "节点管理"
        },
        {
          value: "1",
          label: "系统管理"
        },
        {
          value: "2",
          label: "数据库管理"
        }
      ],
      searchObj: {},
      QiniuToken: "",
      Loading: false,
      modalObjj: "", // 弹层操作标题
      modalObjjj: "", // 弹层操作标题
      modalObjjf: "",
      modalObjjr: "",
      isSubmitLoading: false, // 表格加载效果
      deleteList: [], // 批量删除数据集
      total: 0, // 总条数
      currentPage: 1, // 起始页
      pageSize: 20 // 条数
    };
  },

  mounted() {
    // console.log(this.$route.path, "==========")
    if (this.$route.path === '/omMager/comments') {
      this.tab.type = "3"
      this.typeList = [
        {
          value: "3",
          label: "节点管理"
        },
        {
          value: "1",
          label: "系统管理"
        },
        {
          value: "2",
          label: "数据库管理"
        }
      ]
    } else if (this.$route.path === '/system/permissionView') {
      this.tab.type = "0"
      this.typeList = [
        {
          value: "0",
          label: "角色管理"
        },
      ]
    }
    this.findNodes();
    this.getList();
  },

  methods: {
    initial() {
      this.SearchItem = {
        name: "",
        nodeId: "",
        systemAndPlatform: "",
        publicIp: "",
        intranetIp: ""
      };
      this.findNodes();
      this.getList();
    },
    async getList() {
      this.tableOption = {
        label: "操作",
        width: "200px",
        value: 0,
        options: [
          {
            label: "编辑",
            key: 0,
            type: "success",
            State: false,
            method: row => {
              this.editT(row);
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
      };
      if (this.tab.type === "3") {
        this.tableHeader = [];
        this.tableHeader.push(
          {
            // sort: false,
            id: false,
            type: "",
            label: "节点名称",
            list: "name"
          },
          {
            id: false,
            type: "",
            label: "公网IP",
            list: "publicIp"
          },
          {
            id: false,
            type: "",
            label: "内网IP",
            list: "intranetIp"
          },
          {
            id: false,
            type: "",
            label: "用途",
            list: "purpose"
          },
          {
            id: false,
            type: "",
            label: "配置",
            list: "configuration"
          },
          {
            id: false,
            type: "",
            label: "备注",
            list: "remark"
          }
        );
      } else if (this.tab.type === "1") {
        this.tableHeader = [];
        this.tableHeader.push(
          {
            // sort: false,
            id: false,
            type: "",
            label: "节点名称",
            list: "nodeName"
          },
          {
            id: false,
            type: "",
            label: "系统名称",
            list: "name"
          },
          {
            id: false,
            type: "",
            label: "进程名",
            list: "processName"
          },
          {
            id: false,
            type: "",
            label: "占用端口",
            list: "port"
          },
          {
            id: false,
            type: "",
            label: "所属系统或平台",
            list: "systemAndPlatform"
          },
          {
            id: false,
            type: "",
            label: "备注",
            list: "remark"
          }
        );
      } else if (this.tab.type === "2") {
        this.tableHeader = [];
        this.tableHeader.push(
          {
            // sort: false,
            id: false,
            type: "",
            label: "节点名称",
            list: "nodeName"
          },
          {
            id: false,
            type: "",
            label: "数据库名称",
            list: "name"
          },
          {
            id: false,
            type: "",
            label: "数据库类型",
            list: "type"
          },
          {
            id: false,
            type: "",
            label: "进程名",
            list: "processName"
          },
          {
            id: false,
            type: "",
            label: "占用端口",
            list: "port"
          },
          {
            id: false,
            type: "",
            label: "所属系统或平台",
            list: "systemAndPlatform"
          },
          {
            id: false,
            type: "",
            label: "用户名",
            list: "username"
          },
          {
            id: false,
            type: "",
            label: "密码",
            list: "password"
          },
          {
            id: false,
            type: "",
            label: "备注",
            list: "remark"
          }
        );
      } else if (this.tab.type === "0") {
        this.tableOption = {};
        this.tableOption = {
          label: "操作",
          width: "300px",
          value: 3,
          options: [
            {
              label: "编辑",
              key: 0,
              type: "success",
              State: false,
              method: row => {
                this.editT(row);
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
            },
            {
              label: "分配权限",
              key: 0,
              type: "info",
              State: false,
              method: row => {
                this.$router.push({
                  path: "roleList",
                  query: {
                    id: row.id,
                    name: row.name
                  }
                });
              }
            }
          ]
        };
        this.tableHeader = [];
        this.tableHeader.push(
          {
            // sort: false,
            id: false,
            type: "",
            label: "角色名称",
            list: "name"
          },
          {
            id: false,
            type: "",
            label: "描述",
            list: "remark"
          },
          {
            id: false,
            type: "",
            label: "创建时间",
            list: "createTime"
          }
        );
      }
      const that = this;
      try {
        let findUrl = null;
        let data = this.SearchItem;
        data.page = this.currentPage;
        data.size = this.pageSize;
        switch (this.tab.type) {
          case "0":
            findUrl = getRoleList(data);
            break;
          case "1":
            findUrl = getServiceList(data);
            break;
          case "2":
            findUrl = getDatabaseList(data);
            break;
          case "3":
            findUrl = getNodeList(data);
            break;
        }
        const response = await findUrl;
        that.tableData = response.data.data.content;
        that.total = response.data.data.totalElements;
      } catch (even) {
        that.$message.error("数据获取失败");
      }
    },
    search() {
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.getList();
    },
    add: function () {
      let that = this;
      if (that.tab.type === "3") {
        this.modalObjj = "添加";
        this.$refs.callDetaill.initial(false);
      } else if (that.tab.type === "1") {
        that.modalObjjj = "添加";
        that.$refs.callDetailS.initial(false);
      } else if (that.tab.type === "2") {
        that.modalObjjf = "添加";
        that.$refs.callDetailF.initial(false);
      } else if (that.tab.type === "0") {
        that.modalObjjr = "添加";
        that.$refs.callDetailR.initial(false);
      }
    },
    editT: function (row) {
      let that = this;
      if (that.tab.type === "3") {
        that.modalObjj = "编辑";
        that.$refs.callDetaill.initial(row.id);
      } else if (that.tab.type === "1") {
        that.modalObjjj = "编辑";
        that.$refs.callDetailS.initial(row.id);
      } else if (that.tab.type === "2") {
        that.modalObjjf = "编辑";
        that.$refs.callDetailF.initial(row.id);
      } else if (that.tab.type === "0") {
        that.modalObjjr = "编辑";
        that.$refs.callDetailR.initial(row.id);
      }
    },
    role(data) {
      this.$router.push({ path: "roleList" });
    },
    success(data) {
      let that = this;
      if (data.code === 1) {
        that.$message.success(data.msg);
        that.getList();
      } else {
        that.$message.error(data.msg);
      }
    },

    delete(row) {
      const that = this;
      let findUrll = "";
      switch (this.tab.type) {
        case "0":
          findUrll = deleteRole;
          break;
        case "1":
          findUrll = deServiceModel;
          break;
        case "2":
          findUrll = deDatabase;
          break;
        case "3":
          findUrll = deNodeModel;
          break;
      }
      // alert(findUrl)
      let urll = findUrll;
      that
        .$confirm("此操作将永久删除数据, 是否继续?", "提示", {
          type: "warning"
        })
        .then(async () => {
          that.Loading = true;
          const response = await urll(row.id);
          that.Loading = false;
          if (response.data.code === 1) {
            that.$message.success("删除成功");
            that.getList();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },
    async findNodes() {
      const that = this;
      that.loading = true;
      const response = await findAllNodes();
      that.loading = false;
      if (response.data.code === 1) {
        that.options = [];
        response.data.data.map(function (v) {
          that.options.push({ value: v.id, label: v.name });
        });
        console.log(that.options);
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg);
        return false;
      }
    },
    pageChange(item) {
      this.currentPage = item.page;
      this.pageSize = item.limit;
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.getList();
    }
  }
};
</script>

<style lang="scss" scoped>
.lunbo-tab {
  margin-top: 10vh;
}
.lunbo-main {
  position: relative;

  .lunbo-btn {
    position: absolute;
    top: 0;
    left: 0px;
  }
}
</style>
