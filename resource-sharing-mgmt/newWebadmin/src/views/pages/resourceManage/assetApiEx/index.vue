<template>
  <el-main class="main">
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
          <el-button v-if="permissions.apiDelete" size='medium' @click="deleteApiList" icon="el-icon-search">删除</el-button>
          <el-button v-if="permissions.apiAdd" size='medium' @click="handleAdd" icon="el-icon-refresh-left">继续添加</el-button>
          <el-button size='medium' @click="goback" icon="el-icon-refresh-left">返回资源列表</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { assetApiExFindAll, assetApiExDelete } from "@/api/assetApiEx.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      permissions: {
        apiAdd: false,
        apiDelete: false,
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
            return '<span>' + row.createTime + '</span>'
          }
        },
        {
          type: 'html',
          label: '创建人',
          list: 'createById',
          code: (row) => {
            return '<span>' + row.createById + '</span>'
          }
        }
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
        uviewNm: '',
        name: '',
        path: '',
        host: '',
        createTime: '',
        createByName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      uviewId: '',
      uviewNm: ''
    }
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
    this.uviewId = this.$route.query.uviewId;
    this.uviewNm = this.$route.query.uviewNm;
    this.fetchData(this.uviewId)
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.SearchItem.name = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData(uviewId) {
      const that = this
      let data = {}
      this.SearchItem.uviewId = uviewId;
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await assetApiExFindAll(data)
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
      // let uviewId=this.$route.query.uviewId;
      this.fetchData(this.uviewId)
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
    // 详情
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/archApiExDetail',
        query: {
          id: data.id,
          uviewId: data.uviewId,
          uviewNm: this.uviewNm
        }
      })
    },
    //返回资源列表
    goback() {
      this.$router.push("/resourceManagement");
    },
    //选中
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      that.listiRead = []
      vals.map(function (v, k) {
        that.DeletelistiD.push(v.id);
        // that.listiRead.push({id: v.id, status: v.status})
      })
    },
    //删除
    async deleteApiList() {
      const that = this;
      console.log(that.DeletelistiD.length)
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择云数据')
      }
      //    if(that.DeletelistiD.length != 1){
      //        return that.$message.warning('请选择一条云数据')
      //    }
      console.log(that.DeletelistiD);
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        let ids = that.DeletelistiD
        const response = await assetApiExDelete(ids + '');
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          // let uviewId=this.$route.query.uviewId;
          this.fetchData(this.uviewId)
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 添加
    handleAdd() {
      const that = this
      that.$router.push({
        path: '/archApiExAdd',
        query: {
          type: '新增',
          uviewId: this.uviewId,
          uviewNm: this.uviewNm
        }
      })
    },
    //测试
    handleApiTest(data) {
      const that = this
      that.$router.push({
        path: '/archApiExTest',
        query: {
          id: data.id,
          uviewId: data.uviewId,
          uviewNm: data.uviewNm,
          id: data.sourceApiId,
          name: data.name,
          host: data.host,
          path: data.path,
          method: data.method,
          protocol: data.protocol
        }
      })
    }
  }
}
</script>
<style lang="scss" scope>
.resourceManagement-page {
  display: flex;
  padding: 10px;
  .resourceManagement-left {
    width: 250px;
    height: 654px;
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
</style>
<style>
.el-tree-node__label,
.el-tree-node__expand-icon {
  font-size: 18px;
}
.el-tree-node__content {
  height: 34px;
  line-height: 34px;
}
</style>