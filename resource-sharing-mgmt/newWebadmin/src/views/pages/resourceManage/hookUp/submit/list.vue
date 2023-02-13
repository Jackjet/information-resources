<template>
  <div class="resourceManagement-page">
    <div class="resourceManagement-left" v-bind:style="{width:isWitch?'350px':'250px'}">
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
            <el-input clearable size="medium" placeholder="请输入资源名称" v-model="SearchItem.resourceName"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="SearchItem.type" placeholder="请选择资源类型">
              <el-option label="文件" value="0"></el-option>
              <el-option label="数据库" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="SearchItem.status" placeholder="请选择状态">
              <el-option label="草稿" value="0"></el-option>
              <el-option label="待审核" value="1"></el-option>
              <el-option label="审核通过" value="2"></el-option>
              <el-option label="审核驳回" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="margin-left: 1%">
            <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
            <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
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
import { assetExFindAll, organizationFindAllById, assetExSubmit, assetExExamine } from "@/api/fileData.js";
import TableList from "@/components/table/tableList";
import Pagination from "@/components/table/Pagination";
import { getCookies } from '@/utils/auth';

export default {
  components: { TableList, Pagination },
  data() {
    return {
      dialogFormVisible: false,
      isWitch: false,
      input: "",
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name",
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
          label: "资源类型",
          list: "type",
          code: (row) => {
            if (row.type == '0') {
              return "<span>文件</span>";
            } else {
              return "<span>数据库</span>";
            }
          },
        },
        {
          type: "html",
          label: "资源名称",
          list: "name",
          code: (row) => {
            if (row.type == 1) {
              return "<span>" + row.tableName + "</span>";
            } else {
              return "<span>" + row.name + "</span>";
            }

          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            switch (row.status) {
              case 0:
                return "<span>草稿</span>";
                break;
              case 1:
                return "<span>待审核</span>";
                break;
              case 2:
                return "<span>审核通过</span>";
                break;
              default:
                return "<span>审核驳回</span>";
                break;
            }
          },
        },
        {
          type: "html",
          label: "创建时间",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
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
            label: "提交审核",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show(row) {
              return row.status === 0
            },
            method: (row) => {
              this.submitToExamine(row);
            },
          },
          {
            label: "编辑",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show(row) {
              return row.status === 0 || row.status === 3;
            },
            method: (row) => {
              this.submitEdit(row);
            },
          },
          // {
          //   label: "审核驳回",
          //   key: 0,
          //   type: "text",
          //   icon: "el-icon-tickets",
          //   State: false,
          //   show(row) {
          //     return row.status === 1;
          //   },
          //   method: (row) => {
          //     this.submitExamine(row, 1);
          //   },
          // },
          // {
          //   label: "审核通过",
          //   key: 0,
          //   type: "text",
          //   icon: "el-icon-tickets",
          //   State: false,
          //   show(row) {
          //     return row.status === 1;
          //   },
          //   method: (row) => {
          //     this.submitExamine(row, 0);
          //   },
          // },
          {
            label: "日志",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            method: (row) => {
              this.handleRecord(row);
            },
          },
        ],
      },
      time: [],
      SearchItem: {
        orgId: "",
        typId: "",
        uviewNm: this.$route.query.uviewNm,
        uviewNo: "",
        name: "",
        resourceName: "",
        type: "",
        status: ""
      },
      lastItem: {
        uviewNm: "",
        uviewNo: "",
        name: "",
        resourceName: "",
        type: "",
        status: ""
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      parentArr: [],
      toExamine: {
        id: "",
        detail: "",
        status: "",
      },
    };
  },
  created() {
    this.treeDataListFun();
    // 
  },
  methods: {
    isWitchClick() {
      this.isWitch = !this.isWitch;
    },
    // 获取树结构
    treeDataListFun() {
      this.isSubmitLoading = true;
      let userInfo = JSON.parse(getCookies('userInfo'))
      let i = 0;
      organizationFindAllById({ id: userInfo.organizations[0] }).then((res) => {
        let i = 0;
        if (res.data.code === 1) {
          this.treeData = [res.data.data];
          this.fetchData();
          this.isSubmitLoading = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    handleNodeClick(data, e) {
      this.parentArr = [];
      this.getTreeNode(e.parent);
      // data 当前点击项，this.parentArr  当前点击项的所有父元素
      if (this.parentArr.length > 0) {
        // this.SearchItem.provOrgId = this.parentArr[this.parentArr.length - 1].id;
        this.SearchItem.orgId = data.id;
      } else {
        // this.SearchItem.provOrgId = data.id;
        this.SearchItem.orgId = '';
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
      that.SearchItem.uviewNm = ""
      that.SearchItem.uviewNo = ""
      that.SearchItem.name = ""
      that.SearchItem.resourceName = ""
      that.SearchItem.type = ""
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
        const res = await assetExFindAll(data);
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
    // 编辑
    submitEdit(data) {
      this.$router.push({
        path: "/submitEdit",
        query: {
          id: data.id,
          title: "编辑",
        }
      });
    },
    // 详情
    handleDetail(data) {
      this.$router.push({
        path: "/toExamineDetail",
        query: {
          id: data.id
        }
      });
    },
    // 提交审核
    submitToExamine(data) {
      let text = data.type == 0 ? '文件' : '数据库';
      this.$confirm('此操作将提交审核该' + text + ', 是否继续?', '提交审核', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        assetExSubmit({ id: data.id }).then(res => {
          if (res.data.code === 1) {
            // this.$message.success(res.data.msg);
            this.$message.success('操作成功！');
            this.fetchData();
          } else {
            this.$message.error(res.data.msg);
          }
        })
      }).catch(() => { });
    },
    // 日志
    handleRecord(data) {
      this.$router.push({
        path: "/toExamineRecord",
        query: {
          id: data.id
        }
      });
    },
    submitExamine(data, status) {
      if (status === 0) {
        this.$prompt('此操作审核通过该文件, 是否继续?', '审核通过', {
          inputType: 'textarea',
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /\S/,
          inputErrorMessage: '请输入通过说明'
        }).then(({ value }) => {
          assetExExamine({ id: data.id, status: status, detail: value }).then(res => {
            if (res.data.code === 1) {
              // this.$message.success(res.data.msg);
              this.$message.success('操作成功！');
              this.fetchData();
            } else {
              this.$message.error(res.data.msg);
            }
          })
        }).catch(() => { });
      } else {
        this.$prompt('驳回原因', '审核驳回', {
          inputType: 'textarea',
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /\S/,
          inputErrorMessage: '请输入驳回原因'
        }).then(({ value }) => {
          assetExExamine({ id: data.id, status: status, detail: value }).then(res => {
            if (res.data.code === 1) {
              // this.$message.success(res.data.msg);
              this.$message.success('操作成功！');
              this.fetchData();
            } else {
              this.$message.error(res.data.msg);
            }
          })
        }).catch(() => { });
      }
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