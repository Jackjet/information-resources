<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入API名称"
            v-model="SearchItem.apiName">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable
                    size="medium"
                    placeholder="请输入源API名称"
                    v-model="SearchItem.sourceApiName">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button  size='medium' v-permission="groupAddApiPermission" @click="groupAddApi" icon="el-icon-plus">继续添加</el-button>
          <el-button  size='medium' @click="resetList" icon="el-icon-back">返回</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-permission="groupDeleteApiAllPermission"  plain size='medium' @click="groupDeleteApiAll" icon="el-icon-download">批量删除API</el-button>
      </div>
      <TableList :table-data='tableData'
        v-loading="isSubmitLoading"
        @onHandleSelectionChange="handleSelectionChange"
        :table-selection="tableSelection"
        :table-label="tableHeader"
        :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import {
  groupApiList,
  groupApiDelete
} from "@/api/apiGroupManage"
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },
  computed: {
    ...mapGetters([
      'permissions'
    ]),
  },
  data() {
    return {
      drawer: false,
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      groupDeleteApiAllPermission: ['none'],
      groupAddApiPermission: ['none'],
      isSubmitLoading: false,
      DeletelistiD: [],
      tableData: [],
      tableHeader: [
        {id: false, type: 'imgColor', label: 'API名称', list: 'apiName'},
        {id: false, type: 'color', label: '路由路径', list: 'routeInfo'},
        {id: false, type: 'color', label: '源API名称', list: 'sourceApiName'},
        {id: false, type: 'color', label: '所属提供方', list: 'supplierName'},
        {id: false, type: 'color', label: '更新时间', list: 'updateTime'},
        {id: false, type: 'color', label: '更新人', list: 'userName'}
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          permission: ['none'],
          State: false,
          method: (row) => {
            this.groupApiDelete(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        groupId:'',
        apiName: '',
        sourceApiName: ''
      },
      lastItem: {
        groupId:'',
        apiName: '',
        sourceApiName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      groupId:''
    }
  },
  created(){
    this.SearchItem.groupId = this.$route.query.id
    this.groupId = this.$route.query.id
    this.findPermission()
    this.fetchData()
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      const that = this
      let props = {
        groupApiDelete: function (value) {
          that.tableOpction.options[0].permission = JSON.parse(JSON.stringify(value))
        },
        groupAddApi: function (value) {
          that.groupAddApiPermission = JSON.parse(JSON.stringify(value))
        },
        groupDeleteApiAll:function (value) {
          that.groupDeleteApiAllPermission = JSON.parse(JSON.stringify(value))
        },
      }
      if(this.permissions.length > 0){
        this.permissions.map(function(v, k){
          if(v['apiGroup']){
            for (let i in v['apiGroup']) {
              if(props.hasOwnProperty(i)){ props[i](v['apiGroup'][i])}
            }
          }
        })
      }
    },
    // 重置
    reset() {
      const that = this
      that.SearchItem.apiName = ''
      that.SearchItem.sourceApiName = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await groupApiList(data)
        that.isSubmitLoading = false
        if(res.data.code === 1){
          this.tableData = res.data.data.content
          this.total =  res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //全选
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      that.listiRead = []
      vals.map(function(v, k){
        that.DeletelistiD.push(v.id)
        that.listiRead.push({id: v.id, status: v.status})
      })
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
      if(that.lastItem.startTime === '' || that.lastItem.startTime === null){
        that.time = []
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },

    // 行高
    cellStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    // 头行高
    headerStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    //
    async groupApiDelete(row) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        let ids = []
        ids.push(row.id)
        const response = await groupApiDelete(ids+'')
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          await this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

    //批量删除
    groupDeleteApiAll(){
      const that = this
      if(that.DeletelistiD.length === 0){
        return that.$message.warning('请先选择API')
      }
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await groupApiDelete(that.DeletelistiD+'')
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          await this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

    //新增
    groupAddApi(){
      const that = this
      that.$router.push({
        path: '/apiManage/apiGroup/apiAdd',
        query: {
          id: that.groupId,
        }
      })
    },

    resetList() {
      const that = this
      this.$router.push({
        path: '/apiManage/apiGroup/groupList'
      })
    }
  }
}
</script>
<style lang="scss" scoped>
  /deep/ .el-button--primary.is-plain:focus, .el-button--primary.is-plain:hover{
    background: #ffffffff
  }
  /deep/ .el-form-item__label{
    color: #fff;
  }
  /deep/ .el-input--medium .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-date-editor .el-range__close-icon{
    line-height: 24px !important;
  }
  /deep/ .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-button--medium{
    padding: 7px 15px;
  }
  /deep/ .el-form-item{
    margin-bottom: 5px;
  }
  /deep/ .el-InputForm .el-range__icon{
    line-height: 25px!important;
  }
  /deep/ .el-input__icon .el-range__icon {
    line-height: 24px!important;
  }
 /deep/ .el-date-editor .el-range-separator{
   line-height: 24px !important;
 }
  .center{
    background: #fff;
    padding: 0px 15px;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 350px;
  }
  .main{
    background: #00000000;
    height: 100%;
    width: 100%;
    padding: 0 30px;
    min-height: 600px;
    margin-top: -240px;
    .el-InputForm{
      background:url('../../../../assets/image/filter.png');
      background-size: 100% 100%;
      padding:15px 0 0 20px;
      min-height: 100px;
    }
  }
  .add{
    width: 25px;
    height: 25px;
    background:url('../../../../assets/image/add.png');
  }
  .work-query {
    display: flex;
    align-items: center;
    padding: 20px;
  }
  .table-list {
    padding: 20px 20px 0 20px;
    box-sizing: border-box;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 400px;
  }
  .table-pagination {
    display: flex;
    justify-content: flex-end;
    padding: 20px;
  }

</style>
