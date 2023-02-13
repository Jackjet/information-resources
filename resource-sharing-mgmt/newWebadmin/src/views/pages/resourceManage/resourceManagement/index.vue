<template>
  <div class="resourceManagement-page">
    <div class="resourceManagement-left" v-bind:style="{width:isWitch?'350px':'250px'}">
      <!-- <h1>信息资源分类</h1>
      <div class="left-nav">
        <el-tree ref="tree" :data="treeData" :props="defaultProps" node-key="typId" :default-expanded-keys="key" accordion @node-click="handleNodeClick"></el-tree>
      </div> -->
      <div class="tree_title">
        <span class="tree_desc">信息资源分类</span>
        <i style="cursor: pointer;" :class="isWitch?'el-icon-s-unfold':'el-icon-s-fold'" @click="isWitchClick"></i>
      </div>
      <div class="tree_content">
        <el-tree ref="tree" accordion :data="treeData" highlight-current node-key="typId" :expand-on-click-node="false" :default-expanded-keys="key" :props="defaultProps" @node-click="handleNodeClick">
          <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
            <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
              <span class="overflowEllips"> {{ node.label }} </span>
            </el-tooltip>
            <div />
          </span>
        </el-tree>
      </div>
    </div>
    <div class="resourceManagement-right">
      <el-col :span="24">
        <el-form :inline="true" class="el-InputForm">
          <el-form-item>
            <el-input clearable size="medium" placeholder="请输入信息资源代码" v-model="SearchItem.uviewNo"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input clearable size="medium" placeholder="请输入信息资源名称" v-model="SearchItem.uviewNm"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="SearchItem.isHook" placeholder="请选择挂接状态">
              <el-option key="0" label="未挂接" value="0"></el-option>
              <el-option key="1" label="已挂接" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="SearchItem.hookType" placeholder="请选择挂接类型" @change="hookTypeChange">
              <el-option key="1" label="接口" value="1"></el-option>
              <el-option key="2" label="文件" value="2"></el-option>
              <el-option key="3" label="数据库" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="SearchItem.shareLv" placeholder="请选择共享状态">
              <el-option key="01" label="无条件共享" value="01"></el-option>
              <el-option key="02" label="有条件共享" value="02"></el-option>
              <el-option key="03" label="不予共享" value="03"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="SearchItem.status" placeholder="请选择上架状态">
              <el-option key="0" label="未下架" value="0"></el-option>
              <el-option key="1" label="已下架" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="margin-left: 1%">
            <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
            <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
            <el-button size="medium" @click="timeQueryClick">{{SearchItem.order==="create_time"?"填报时间":"更新时间"}}</el-button>
            <el-button size="medium" @click="sortQuerClick">{{SearchItem.desc==="0"?"顺序":"倒序"}}</el-button>
            <el-button v-if="permissions.synchronization" size="medium" @click="syncData" :disabled='dis'>同步资源目录</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" class="center">
        <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction"></TableList>
      </el-col>
      <el-col :span="24">
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
    </div>
  </div>
</template>
<script>
import {
  archBusiUviewExFindAll,
  archBusiUviewExUpdate,
  dictAssetCateFindAll,
  archBusiUviewExSyncData
} from "@/api/archBusiUviewEx.js";
import { organizationFindAllById } from "@/api/fileData.js";
import TableList from "@/components/table/tableList";
import Pagination from "@/components/table/Pagination";
import { getCookies } from '@/utils/auth';

export default {
  components: { TableList, Pagination },
  data() {
    return {
      permissions: {
        synchronization: false,
        hookUp: false,
        top: false,
        bottom: false,
        api: false,
        data: false,
        file: false,
      },
      isWitch: false,
      dis: false,
      input: "",
      treeData: [],
      defaultProps: {
        children: "children",
        label: "typNm",
      },
      tableSelection: {
        key: true,
        type: "",
        detaile: false,
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: "html",
          label: "信息资源代码",
          list: "uviewNo",
          code: (row) => {
            return "<span>" + row.uviewNo + "</span>";
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "同步时间",
          list: "updateTime",
          code: (row) => {
            if (row.updateTime === null) {
              return "";
            } else {
              return "<span>" + row.updateTime.split(' ')[0] + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "挂接时间",
          list: "hookTime",
          code: (row) => {
            if (row.hookTime === null) {
              return "";
            } else {
              return "<span>" + row.hookTime.split(' ')[0] + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "共享条件",
          list: "shareLv",
          code: (row) => {
            switch (row.shareLv) {
              case "01":
                return "<span>" + "无条件共享" + "</span>";
                break;
              case "02":
                return "<span>" + "有条件共享" + "</span>";
                break;
              case "03":
                return "<span>" + "不予共享" + "</span>";
                break;
              default:
                return "<span>" + "未知" + "</span>";
                break;
            }
          },
        },
        {
          type: "html",
          label: "状态",
          list: "hookStatus",
          code: (row) => {
            if (row.hookStatus === '1' || row.fileHookStatus === '1' || row.dataHookStatus === '1') {
              return "<span>已挂接</span>";
            } else {
              return "<span>未挂接</span>";
            }
          },
        },
        {
          type: "html",
          label: "挂接类型",
          list: "hookStatus",
          code: (row) => {
            if (row.hookStatus === '1' && row.fileHookStatus === '1' && row.dataHookStatus === '1') {
              return "<span>云接口,云文件,云数据</span>";
            } else if (row.hookStatus === '1' && row.fileHookStatus === '1') {
              return "<span>云接口,云文件</span>";
            } else if (row.fileHookStatus === '1' && row.dataHookStatus === '1') {
              return "<span>云文件,云数据</span>";
            } else if (row.hookStatus === '1' && row.dataHookStatus === '1') {
              return "<span>云接口,云数据</span>";
            } else {
              if (row.hookStatus === '1') {
                return "<span>云接口</span>";
              } else if (row.fileHookStatus === '1') {
                return "<span>云文件</span>";
              } else if (row.dataHookStatus === '1') {
                return "<span>云数据</span>";
              } else {
                return "<span>未挂接</span>";
              }
            }
          },
        },
      ],
      tableOpction: {
        label: "操作",
        width: "250px",
        value: 0,
        options: [
          {
            label: "详情",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            method: (row) => {
              this.handleDetail(row);
            },
          },
          {
            label: "云接口",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show: (row) => {
              if (row.hookStatus === "1" && this.permissions.api) {
                return true;
              } else {
                return false;
              }
            },
            method: (row) => {
              this.handleApiDetail(row);
            },
          },
          {
            label: "云文件",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show: (row) => {
              return row.fileHookStatus === '1' && this.permissions.file;
            },
            method: (row) => {
              this.fileDetail(row);
            },
          },
          {
            label: "云数据",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show: (row) => {
              return row.dataHookStatus === '1' && this.permissions.data;
            },
            method: (row) => {
              this.dataDetail(row);
            },
          },
          {
            label: "资源数据挂接",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show: (row) => {
              if (row.hookStatus === "0" && this.permissions.hookUp) {
                return true;
              } else {
                return false;
              }
            },
            method: (row) => {
              this.handleApiList(row);
            },
          },
          {
            label: "下架",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show: (row) => {
              if (row.status === "0" && this.permissions.bottom) {
                return true;
              } else {
                return false;
              }
            },
            method: (row) => {
              this.handleUpdate(row, '1');
            },
          },
          {
            label: "上架",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show: (row) => {
              if (row.status === "1" && this.permissions.top) {
                return true;
              } else {
                return false;
              }
            },
            method: (row) => {
              this.handleUpdate(row, '0');
            },
          },
        ],
      },
      time: [],
      SearchItem: {
        provOrgId: "",
        typId: "",
        uviewNm: "",
        uviewNo: "",
        shareLv: "",
        hookStatus: "",
        status: "",
        order: "create_time",
        desc: "1",
      },
      lastItem: {
        uviewNm: "",
        uviewNo: "",
        shareLv: "",
        hookStatus: "",
        status: ""
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      parentArr: [],
    };
  },
  created() {
    this.$store.dispatch('setUserButtons').then(res => {
      let permissionsArr = JSON.parse(res);
      permissionsArr.forEach(item => {
        let itemArr = item.split('_')
        if (('/' + itemArr[0]) === this.$route.path) {
          this.permissions[itemArr[1]] = true;
        }
      })
    })
    this.treeDataListFun();
  },
  methods: {
    timeQueryClick() {
      this.SearchItem.order = this.SearchItem.order === "create_time" ? "update_time" : "create_time";
      this.fetchData();
    },
    sortQuerClick() {
      this.SearchItem.desc = this.SearchItem.desc === "0" ? "1" : "0";
      this.fetchData();
    },
    hookTypeChange() {
      if (!this.SearchItem.isHook) {
        this.$message("请先选择挂接状态");
        this.SearchItem.hookType = "";
      }
    },
    isWitchClick() {
      this.isWitch = !this.isWitch;
    },
    // 获取树结构
    treeDataListFun() {
      this.isSubmitLoading = true;
      let userId = JSON.parse(getCookies('userInfo')).organizations[0];
      organizationFindAllById({ id: userId }).then(res => {
        if (res.data.code === 1) {
          let arrTree = res.data.data.level == 1 ? [res.data.data] : res.data.data.children;
          dictAssetCateFindAll().then(ress => {
            if (ress.data.code === 1) {
              arrTree.forEach(item => {
                item.children = ress.data.data;
                item.typNm = item.name;
                item.typId = item.idLink
              })
              this.treeData = arrTree;
              this.fetchData();
              this.isSubmitLoading = false;
            } else {
              this.$message.error(res.data.msg);
            }
          })
        } else {
          this.$message.error(res.data.msg);
        }
      })

    },
    handleNodeClick(data, e) {
      this.parentArr = [];
      this.getTreeNode(e.parent);
      // data 当前点击项，this.parentArr  当前点击项的所有父元素
      if (this.parentArr.length > 0) {
        this.SearchItem.provOrgId = this.parentArr[this.parentArr.length - 1].id;
        this.SearchItem.typId = data.typId;
      } else {
        this.SearchItem.provOrgId = data.id;
        this.SearchItem.typId = '';
      }
      this.SearchNoteList();
    },
    getTreeNode(node) {
      //获取当前树节点和其父级节点
      if (node) {
        if (node.label !== undefined) {
          this.parentArr.push(node.data);
          this.getTreeNode(node.parent); //递归
        }
      }
    },
    // 重置
    reset() {
      const that = this;
      (that.SearchItem.uviewNm = ""),
        (that.SearchItem.uviewNo = ""),
        (that.SearchItem.shareLv = ""),
        (that.SearchItem.hookStatus = "");
      that.SearchItem.isHook = "";
      that.SearchItem.hookType = "";
      that.SearchItem.status = "";
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
    },
    // 获取列表
    async fetchData() {
      const that = this;
      let data = {};
      (data = this.SearchItem),
        (data.page = this.currentPage),
        (data.size = this.pageSize);
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
      try {
        that.isSubmitLoading = true;
        const res = await archBusiUviewExFindAll(data);
        that.isSubmitLoading = false;
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
    // 搜索
    SearchNoteList() {
      this.currentPage = 1;
      this.$refs.page.Page(1);
      this.fetchData();
    },
    // 翻页
    pageChange(item) {
      let that = this;
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]];
      });
      this.fetchData("page");
    },
    // 详情
    handleDetail(data) {
      const that = this;
      that.$router.push({
        path: "/archBusiUviewDetail",
        query: {
          id: data.id
        }
      });
    },
    //云接口
    handleApiDetail(data) {
      const that = this;
      that.$router.push({
        path: "/archApiExIndex",
        query: {
          uviewId: data.id,
          uviewNm: data.uviewNm
        }
      });
    },
    //挂接云接口
    handleApiList(row) {
      const that = this;
      that.$router.push({
        path: "/archBusiUviewAdd",
        query: {
          uviewId: row.id,
          uviewNm: row.uviewNm
        }
      });
    },
    // 云文件
    fileDetail(row) {
      this.$router.push({
        path: "/fileList",
        query: {
          uviewId: row.id,
          uviewNm: row.uviewNm
        }
      });
    },
    // 云数据
    dataDetail(row) {
      this.$router.push({
        path: "/dataManagement",
        query: {
          uviewId: row.id,
          uviewNm: row.uviewNm
        }
      });
    },
    //下架
    handleUpdate(data, status) {
      const that = this;
      let msg = '上架'
      if (status == 0) {
        msg = '上架'
      } else {
        msg = '下架'
      }
      that.$confirm("请确定此资源目录在门户端" + msg, "提示", {
        type: "warning"
      })
        .then(async () => {
          that.isSubmitLoading = true;
          let data1 = {
            id: data.id,
            status: status,
            hookStatus: ""
          };
          console.log(data.id);
          const response = await archBusiUviewExUpdate(data1);
          that.isSubmitLoading = false;
          if (response.data.code === 1) {
            that.$message.success(msg + "成功");
            this.fetchData();
          } else {
            that.$message.error(response.data.msg);
          }
        })
        .catch(() => {
          return false;
        });
    },
    //同步资源目录数据
    syncData() {
      this.dis = true
      this.$message.success("正在同步，请稍候");
      archBusiUviewExSyncData().then(res => {
        if (res.data.code === 1) {
          this.dis = false
          this.$message.success("同步成功");
          this.fetchData();
        } else {
          this.$message.error(res.data.msg);
        }
      });
    }
  }
};
</script>
<style lang="scss" scope>
.resourceManagement-page {
  display: flex;
  padding: 10px;
  .resourceManagement-left {
    // width: 250px;
    // height: 654px;
    height: 100%;
    border: 1px solid #ccc;
    h1 {
      text-align: center;
      border-bottom: 1px solid #ccc;
      margin: 0;
      padding: 0;
      line-height: 50px;
    }
    .left-nav {
      height: 604px;
      overflow-y: scroll;
    }
    .left-nav::-webkit-scrollbar {
      display: none;
    }
  }
  .resourceManagement-right {
    flex: 1;
    margin-left: 20px;
    // border: 1px solid #ccc;
  }
}
// 后期样式修改
.tree_title {
  border-bottom: 1px solid #ccc;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
}
.tree_content {
  min-height: 650px;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
<style>
.el-tree-node__label,
.el-tree-node__expand-icon {
  font-size: 18px;
}

/* .el-tree-node__content {
  height: 34px;
  line-height: 34px;
} */
</style>