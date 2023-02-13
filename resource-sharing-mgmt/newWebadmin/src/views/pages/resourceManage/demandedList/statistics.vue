<template>
  <div class="resourceManagement-page">
    <div class="resourceManagement-left" v-bind:style="{width:isWitch?'350px':'250px'}">
      <div class="tree_title">
        <span class="tree_desc">申请单位</span>
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
          <el-form-item label="申请日期">
            <el-date-picker size="medium" v-model="time" value-format="yyyy-MM-dd HH:mm:ss" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
          </el-form-item>
          <el-form-item style="margin-left: 1%">
            <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
            <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
            <el-button size="medium" @click="outUser" icon="el-icon-refresh-left">导出</el-button>
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
  findDemandedInfoCountByProvOrgId,
  exportDemandedInfoCountByProvOrgId
} from "@/api/summary.js";
import {
  findResourceUseInfoByProvOrgId
} from "@/api/archBusiUviewEx.js";
import { organizationFindAllById } from "@/api/fileData.js"
import { outExcel1 } from '@/utils/export'
import TableList from "@/components/table/tableList";
import Pagination from "@/components/table/Pagination";
import { getCookies } from '@/utils/auth';

export default {
  components: { TableList, Pagination },
  data() {
    return {
      isWitch: false,
      dis: false,
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
          label: "序号",
          list: "sort",
          code: (row) => {
            return "<span>" + row.sort + "</span>";
          },
        },
        {
          type: "html",
          label: "申请单位",
          list: "createDeptName",
          code: (row) => {
            return "<span style='text-align: left;display: block;'>" + row.createDeptName + "</span>";
          },
        },
        {
          type: "html",
          label: "申请的需求资源数量",
          width: "160px",
          list: "demandedNum",
          code: (row) => {
            if (row.demandedNum === null) {
              return "";
            } else {
              return "<span>" + row.demandedNum + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "已受理数量",
          list: "processed",
          code: (row) => {
            if (row.processed === null) {
              return "";
            } else {
              return "<span>" + row.processed + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "未受理数量",
          list: "untreated",
          code: (row) => {
            if (row.untreated === null) {
              return "";
            } else {
              return "<span>" + row.untreated + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "已驳回数量",
          list: "reject",
          code: (row) => {
            if (row.reject === null) {
              return "";
            } else {
              return "<span>" + row.reject + "</span>";
            }
          },
        },
      ],
      tableOpction: {
        label: "操作",
        width: "200px",
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
        ],
      },
      time: [],
      SearchItem: {

      },
      lastItem: {

      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      parentArr: [],
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
      let userInfo = JSON.parse(getCookies('userInfo'));
      let i = 0;
      organizationFindAllById({ id: userInfo.organizations[0] }).then((res) => {
        let i = 0;
        if (res.data.code === 1) {
          this.treeData = [res.data.data];
          this.fetchData('');
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
        // this.SearchItem.createDeptId = data.id;
        // this.SearchItem.typId = data.typId;
        this.SearchItem.createDeptId = data.id;
      } else {
        // this.SearchItem.createDeptId = data.id;
        // this.SearchItem.typId = '';
        this.SearchItem.createDeptId = '';
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
      (that.SearchItem.startTime = ''),
        (that.SearchItem.endTime = ''),
        (that.time = []),
        that.SearchItem.status = "";
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
    },
    // 获取列表
    async fetchData(type) {
      const that = this;
      let data = {};
      (data = this.SearchItem),
        (data.page = this.currentPage),
        (data.size = this.pageSize);
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
      if (type !== 'page') {
        if (that.time !== null && that.time !== '' && that.time.length > 0) {
          data.startTime = this.time[0],
            data.endTime = this.time[1]
        } else {
          data.startTime = '',
            data.endTime = ''
        }
      }
      try {
        that.isSubmitLoading = true;
        const res = await findDemandedInfoCountByProvOrgId(data);
        that.isSubmitLoading = false;
        if (res.data.code === 1) {
          this.tableData = res.data.data.content;
          this.tableData.forEach((v, i) => {
            v.sort = i + 1
          })
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
      this.fetchData('');
    },
    // 翻页
    pageChange(item) {
      let that = this;
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]];
      });
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      this.fetchData("page");
    },
    // 详情
    handleDetail(data) {
      const that = this;
      that.$router.push({
        path: "/demandedStatisticsDetail",
        query: {
          id: data.createDeptId,
          queryTime: this.time,
        }
      });
    },
    // 导出模板
    outUser() {
      let that = this
      let data = {};
      (data = this.SearchItem)
      if (that.time !== null && that.time !== '' && that.time.length > 0) {
        data.startTime = this.time[0],
          data.endTime = this.time[1]
      } else {
        data.startTime = '',
          data.endTime = ''
      }
      outExcel1('请确认是否导出需求统计?', "webadmin/demandedInfo/exportDemandedInfoCountByProvOrgId", data, '需求统计')
    },
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