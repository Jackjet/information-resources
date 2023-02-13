<template>
  <el-main class="main">
    <el-tabs v-model="activeName">
      <el-tab-pane label="按应用分析" name="appAnalysis">
        <el-col :span="24">
          <el-form :inline="true" class='el-InputForm'>
            <el-form-item>
              <el-input clearable
                        size="medium"
                        placeholder="请输入应用ID"
                        v-model="appSearchItem.id">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input clearable
                        size="medium"
                        placeholder="请输入应用名称"
                        v-model="appSearchItem.appName">
              </el-input>
            </el-form-item>
            <el-form-item style='margin-left: 1%;'>
              <el-button size='medium' @click="appSearchNoteList" icon="el-icon-search">查询</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24" class="center">
          <TableList :table-data='appTableData'
                     v-loading="isSubmitLoading"
                     :table-selection="appTableSelection"
                     :table-label="appTableHeader"
                     :table-option="appTableOptions">
          </TableList>
        </el-col>
        <el-col :span='24'>
          <pagination ref="appPage" :total="appTotal" @pageChange="appPageChange"></pagination>
        </el-col>
      </el-tab-pane>
      <el-tab-pane label="按API分析" name="apiAnalysis">
        <el-col :span="24">
          <el-form :inline="true" class='el-InputForm'>
            <el-form-item>
              <el-input clearable
                        size="medium"
                        placeholder="请输入API名称"
                        v-model="apiSearchItem.apiName">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input clearable
                        size="medium"
                        placeholder="请输入路由路径"
                        v-model="apiSearchItem.routeInfo">
              </el-input>
            </el-form-item>
            <el-form-item style='margin-left: 1%;'>
              <el-button size='medium' @click="apiSearchNoteList" icon="el-icon-search">查询</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24" class="center">
          <TableList :table-data='apiTableData'
                     v-loading="isSubmitLoading"
                     :table-selection="apiTableSelection"
                     :table-label="apiTableHeader"
                     :table-option="apiTableOptions">
          </TableList>
        </el-col>
        <el-col :span='24'>
          <pagination ref="apiPage" :total="apiTotal" @pageChange="apiPageChange"></pagination>
        </el-col>
      </el-tab-pane>
    </el-tabs>
  </el-main>
</template>
<script>
import {
  apiList,
  appList
} from "@/api/apiAnalysisManage"
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },

  data() {
    return {
      permissions:{
        apiManageApiAnalysisOperate:false
      },


      drawer: false,
      apiTableSelection: {
        key: false,
        type: 'selection',
        detaile: false
      },
      appTableSelection:{
        key: false,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      apiTableData: [],
      appTableData:[],
      apiTableHeader: [
        {id: false, type: 'color', label: 'API名称', list: 'apiName'},
        {id: false, type: 'color', label: '路由路径', list: 'routeInfo'},
        {id: false, type: 'color', label: '授权次数', list: 'authNum'},
        {id: false, type: 'color', label: '访问次数', list: 'visitNum'}
      ],
      appTableHeader:[
        {id: false, type: 'color', label: '应用ID', list: 'id'},
        {id: false, type: 'color', label: '应用名称', list: 'appName'},
        {id: false, type: 'color', label: '授权个数', list: 'authNum'},
        {id: false, type: 'color', label: '访问次数', list: 'visitNum'}
      ],
      apiTableOptions: {
        label: '操作',
        width: '200px',
        show: false,
        options: [{
          label: '访问详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-chat-line-square',
          State: false,
          method: (row) => {
            this.apiVisitRecord(row)
          }
        }, {
          label: '日志详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-document',
          State: false,
          method: (row) => {
            this.apiLogRecord(row)
          }
        }]
      },
      appTableOptions: {
        label: '操作',
        width: '200px',
        show: false,
        options: [{
          label: '访问详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-chat-line-square',
          State: false,
          method: (row) => {
            this.appVisitRecord(row)
          }
        }, {
          label: '日志详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-document',
          State: false,
          method: (row) => {
            this.appLogRecord(row)
          }
        }]
      },
      apiSearchItem: {
        apiName: '',
        routeInfo: ''
      },
      apiLastItem: {
        apiName: '',
        routeInfo: ''
      },
      appSearchItem:{
        id:'',
        appName:'',
      },
      appLastItem:{
        id:'',
        appName:'',
      },
      apiTotal: 0,
      apiPageSize: '20',
      apiCurrentPage: '1',
      appTotal: 0,
      appPageSize: '20',
      appCurrentPage: '1',
      activeName:'appAnalysis'
    }
  },
  created(){
    let tempName = this.$route.query.type
    if (tempName != null){
      this.activeName = this.$route.query.type
    }
    this.apiFetchData()
    this.appFetchData()
    this.findPermission()
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
      permissionsArr.forEach(item => {
        let itemArr = item.split('_')
        this.permissions[itemArr[0]] = true
      })

      this.apiTableOptions.show = this.permissions.apiManageApiAnalysisOperate
      this.appTableOptions.show = this.permissions.apiManageApiAnalysisOperate
    },

    // 获取API列表
    async apiFetchData(type) {
      const that = this
      let data = {}
      data = this.apiSearchItem
      data.page = this.apiCurrentPage
      data.size = this.apiPageSize
      Object.entries(that.apiSearchItem).map((item, index) => {
        that.apiLastItem[item[0]] = that.apiSearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await apiList(data)
        that.isSubmitLoading = false
        if(res.data.code === 1){
          this.apiTableData = res.data.data.content
          this.apiTotal =  res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    // 获取app列表
    async appFetchData(type) {
      const that = this
      let data = {}
      data = this.appSearchItem
      data.page = this.appCurrentPage
      data.size = this.appPageSize
      Object.entries(that.appSearchItem).map((item, index) => {
        that.appLastItem[item[0]] = that.appSearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await appList(data)
        that.isSubmitLoading = false
        if(res.data.code === 1){
          this.appTableData = res.data.data.content
          this.appTotal =  res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    // API搜索
    apiSearchNoteList() {
      this.currentPage = 1
      this.$refs.apiPage.Page(1)
      this.apiFetchData()
    },

    //app搜索
    appSearchNoteList() {
      this.apiCurrentPage = 1
      this.$refs.appPage.Page(1)
      this.appFetchData()
    },

    // API翻页
    apiPageChange(item) {
      let that = this
      this.apiPageSize = item.limit
      this.apiCurrentPage = item.page
      Object.entries(that.apiSearchItem).map((item, index) => {
        that.apiSearchItem[item[0]] = that.apiLastItem[item[0]]
      })
      this.apiFetchData('page')
    },

    // APP翻页
    appPageChange(item) {
      let that = this
      this.appPageSize = item.limit
      this.appCurrentPage = item.page
      Object.entries(that.appSearchItem).map((item, index) => {
        that.appSearchItem[item[0]] = that.appLastItem[item[0]]
      })
      this.appFetchData('page')
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
    //API访问日志
    apiVisitRecord(row) {
      const that = this
      that.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/visitInfo',
        query: {
          id: row.id,
          title:'访问详情',
          type:'api',
          name:row.apiName
        }
      })
    },
    //APP访问日志
    appVisitRecord(row) {
      const that = this
      that.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/visitInfo',
        query: {
          id: row.id,
          title:'访问详情',
          type:'app',
          name:row.appName
        }
      })
    },
    //API日志详情
    apiLogRecord(row) {
      const that = this
      that.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/logRecord',
        query: {
          id: row.id,
          title:'日志详情',
          type:'api',
          name:row.apiName
        }
      })
    },
    //APP日志详情
    appLogRecord(row) {
      const that = this
      that.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/logRecord',
        query: {
          id: row.id,
          title:'日志详情',
          type:'app',
          name:row.appName
        }
      })
    }
  }
}
</script>

