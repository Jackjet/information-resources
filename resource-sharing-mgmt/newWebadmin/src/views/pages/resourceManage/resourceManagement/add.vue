<template>
  <el-main class="resourceManagement-page">
    <div class="resourceManagement-left" v-bind:style="{width:isWitch?'350px':'250px'}">
      <!-- <h1>分类</h1>
      <div class="left-nav">
        <el-tree ref="tree" :data="treeData" :props="defaultProps" node-key="id" :default-expanded-keys="key" accordion @node-click="handleNodeClick"></el-tree>
      </div> -->
      <div class="tree_title">
        <span class="tree_desc">分类</span>
        <i style="cursor: pointer;" :class="isWitch?'el-icon-s-unfold':'el-icon-s-fold'" @click="isWitchClick"></i>
      </div>
      <div class="tree_content">
        <el-tree ref="tree" accordion :data="treeData" highlight-current node-key="typId" :expand-on-click-node="false" :default-expanded-keys="key" :props="defaultProps" @node-click="handleNodeClick">
          <span slot-scope="{ node,data}" class="custom-tree-node overflowEllips">
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
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item>
            <el-input clearable size="medium" placeholder="请输入接口名称" v-model="SearchItem.name">
            </el-input>
          </el-form-item>
          <el-form-item style='margin-left: 1%;'>
            <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
            <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item style='margin-left: 1%;'>
            <el-button size='medium' @click="submitForm" icon="el-icon-search">确 认</el-button>
            <el-button size='medium' @click="goBack" icon="el-icon-search">返 回</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" class="center">
        <TableList :table-data='tableData' :row-key="getRowKey" v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
        </TableList>
      </el-col>
      <el-col :span='24'>
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
    </div>
  </el-main>
</template>
<script>
import { assetApiExFindAllApi, assetApiExAdd, findGroupTree } from "@/api/assetApiEx.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      isWitch: false,
      treeData: [],
      defaultProps: {
        children: "child",
        label: "name",
      },
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: 'html',
          label: '接口名称',
          list: 'name',
          code: (row) => {
            return '<span>' + row.name + '</span>'
          }
        },
        {
          type: 'html',
          label: 'host',
          list: 'host',
          code: (row) => {
            let jHost = JSON.parse(row.host);
            let shost = '';
            jHost.forEach(item => {
              shost += item.target + ","
            });
            if (shost.length > 0) {
              shost = shost.substring(0, shost.length - 1);
            }
            return '<span>' + shost + '</span>'
          }
        },
        {
          type: 'html',
          label: 'path',
          list: 'path',
          code: (row) => {
            return '<span>' + row.path + '</span>'
          }
        },
        {
          type: 'html',
          label: '挂接时间',
          list: 'createTime',
          code: (row) => {
            return '<span>' + row.createTime.split(' ')[0] + '</span>'
          }
        }
        // {
        //     type: 'html',
        //     label: '创建人',
        //     list: 'createById',
        //     code: (row) => {
        //         return '<span>' + row.createById + '</span>'
        //     }
        // }
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: false,
          method: (row) => {
            this.handleDetail(row)
          }
        },
        {
          label: '测试',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: false,
          method: (row) => {
            this.handleApiTest(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        name: ''
      },
      lastItem: {
        name: '',
        path: '',
        host: '',
        createTime: '',
        createByName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      uviewId: "",
      uviewNm: ""
    }
  },
  created() {
    this.uviewId = this.$route.query.uviewId;
    this.uviewNm = this.$route.query.uviewNm;
    this.treeDataListFun()
    this.fetchData()
  },
  methods: {
    isWitchClick() {
      this.isWitch = !this.isWitch;
    },
    // 获取树结构
    treeDataListFun() {
      this.isSubmitLoading = true;
      let i = 0;
      findGroupTree().then((res) => {
        let i = 0;
        if (res.data.code === 1) {

          this.treeData = res.data.data;
          // this.SearchItem.groupId = this.treeData[0].id
          this.key = [this.treeData[0].id]
          this.isSubmitLoading = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    // 重置
    reset() {
      const that = this
      that.SearchItem.name = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    handleNodeClick(data, e) {
      this.parentArr = [];
      this.getTreeNode(e.parent);
      // data 当前点击项，this.parentArr  当前点击项的所有父元素
      if (this.parentArr.length > 0) {
        this.SearchItem.groupId = this.parentArr[this.parentArr.length - 1].id;
        this.SearchItem.typId = data.typId;
      } else {
        this.SearchItem.groupId = data.id;
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
    // 获取列表
    async fetchData() {
      const that = this
      let data = {}
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await assetApiExFindAllApi(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 搜索
    SearchNoteList() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },
    // api详情
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/archBusiUviewApiDetail',
        query: {
          id: data.id
        }
      })
    },
    //api测试
    handleApiTest(data) {
      const that = this
      that.$router.push({
        path: '/archBusiUviewApiTest',
        query: {
          id: data.id,
          name: data.name,
          host: data.host,
          path: data.path,
          method: data.method,
          protocol: data.protocol
        }
      })
    },
    goBack() {
      this.$router.push("/resourceManagement");
    },
    //选择
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      that.listiRead = []
      vals.map(function (v, k) {
        that.DeletelistiD.push(vals)
        // that.listiRead.push({id: v.id, status: v.status})
      })
    },
    getRowKey(row) {
      return row.id;
    },
    // 新增
    submitForm() {
      const that = this
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择云数据')
      }
      // if(that.DeletelistiD.length !=1){
      //     return that.$message.warning('请选择一条云数据')
      // }
      let data = {};
      data.uviewId = this.uviewId;
      data.uviewNm = this.uviewNm;
      let sourceApis = [];
      that.DeletelistiD[0].forEach((v, i) => {
        let obj = {}
        obj.sourceApiId = v.id;
        obj.name = v.name;
        obj.host = v.host;
        obj.loadBalancing = v.loadBalancing;
        obj.groupId = v.groupId;
        obj.path = v.path;
        obj.protocol = v.protocol;
        obj.method = v.method;
        obj.formatType = v.formatType;
        obj.body = v.body;
        obj.params = v.params;
        obj.response = v.response;
        obj.constants = v.constants;
        obj.tagName = v.tagName;
        obj.tagValue = v.tagValue;
        obj.detail = v.detail;
        sourceApis.push(obj)
      })
      data.sourceApis = sourceApis
      that.loading = true;
      assetApiExAdd(data).then((res) => {
        that.loading = false;
        if (res.data.code === 1) {
          that.$message.success('挂接成功');
          that.goBack()
        } else {
          that.$message.error(res.data.msg);
        }
      });
    }
  }
}
</script>
<style lang="scss" scope>
.resourceManagement-page {
  display: flex;
  padding: 10px;
  .resourceManagement-left {
    // width: 250px;
    // height: 654px;
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