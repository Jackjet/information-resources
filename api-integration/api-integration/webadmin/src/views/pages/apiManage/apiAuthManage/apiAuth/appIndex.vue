<template>
  <el-main class="main">
    <el-col :span="24">
      <div>
        <h3>
          <el-tag v-if="method === 'GET'" color="#993300" effect="dark" size="medium">GET</el-tag>
          <el-tag v-else-if="method === 'POST'" size="medium" effect="dark" color="#336699">POST</el-tag>
          <el-tag v-else-if="method === 'PUT'" size="medium" effect="dark" color="#FCA130">PUT</el-tag>
          <el-tag v-else-if="method === 'DELETE'" size="medium" effect="dark" color="#F93E3E">DELETE</el-tag>
          <!-- <el-tag v-else-if="method" color="#109612" effect="dark" size="medium">MULT</el-tag> -->
          {{title}}
        </h3>
      </div>
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入应用ID" v-model="SearchItem.appId">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入应用名称" v-model="SearchItem.appName">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' icon="el-icon-back" @click="resetList">返回</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-permission="deleteAuthAll" plain size='medium' @click="authDeleteAll" icon="el-icon-delete">批量取消授权</el-button>
      </div>
      <TableList :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOption">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import {
  apiAuthAppList, authDelete
} from "@/api/apiAuthManage"
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'
import { groupApiDelete } from "@/api/apiGroupManage";
export default {
  components: { TableList, Pagination },
  computed: {
    ...mapGetters([
      'permissions'
    ]),
  },
  data() {
    return {
      container: '',
      drawer: false,
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      deleteAuthAll: ['none'],
      isSubmitLoading: false,
      tableIdList: [],
      tableData: [],
      tableHeader: [
        { id: false, type: 'color', label: '应用ID', list: 'appId' },
        { id: false, type: 'color', label: '应用名称', list: 'appName' },
        { id: false, type: 'color', label: '白名单', list: 'white' },
        { id: false, type: 'color', label: '黑名单', list: 'black' },
        { id: false, type: 'color', label: '授权时间', list: 'createTime' }
      ],
      tableOption: {
        label: '操作',
        width: '200px',
        value: 0,
        show: true,
        options: [{
          label: '取消授权',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.deleteAppAuth(row)
          }
        }, {
          label: '测试',
          key: 0,
          type: 'text',
          icon: 'el-icon-view',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.testApi(row)
          }
        }]
      },
      SearchItem: {
        appId: '',
        appName: ''
      },
      lastItem: {
        appId: '',
        appName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      title: '',
      method: '',
      apiId: ''
    }
  },
  created() {
    this.method = this.$route.query.method
    this.title = this.$route.query.name
    this.apiId = this.$route.query.id
    this.container = this.$route.query.container
    console.log('this.container11111111', this.container)
    this.fetchData()
  },
  methods: {

    // 获取列表
    async fetchData(type) {
      this.tableData = []
      const that = this
      let data = {}
      data = this.SearchItem
      data.apiId = that.apiId
      data.page = this.currentPage
      data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await apiAuthAppList(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          let content = res.data.data.content
          for (const appInfo of content) {
            let tempInfo = {}
            tempInfo.id = appInfo.id
            tempInfo.appId = appInfo.appId
            tempInfo.appName = appInfo.appName
            tempInfo.createTime = appInfo.createTime
            tempInfo.key = appInfo.key
            let type = appInfo.type
            tempInfo.white = 'N/A'
            tempInfo.black = 'N/A'
            if (type === 1) {
              tempInfo.white = appInfo.content
            }
            if (type === 0) {
              tempInfo.black = appInfo.content
            }
            this.tableData.push(tempInfo)
          }
          this.total = res.data.data.totalElements
        } else {
          let msg = res.data.msg
          if (msg.indexOf("API暂无授权的应用!")) {
            this.$message.warning(msg.substring(3))
          } else {
            this.$message.error(msg)
          }
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

    // 行高
    cellStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    // 头行高
    headerStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    // 删除授权
    async deleteAppAuth(row) {
      const that = this
      that.$confirm('请确认是否删除授权?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        that.tableIdList.push(row.id)
        const response = await authDelete(that.tableIdList + '')
        that.isSubmitLoading = false
        that.tableIdList = []
        if (response.data.code === 1) {
          that.$message.success('取消授权成功')
          await this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

    //批量取消授权
    authDeleteAll() {
      const that = this
      if (that.tableIdList.length === 0) {
        return that.$message.warning('请选择APP')
      }
      that.$confirm('请确认是否取消授权?', '提示', {
        type: 'warning'
      }).then(async () => {
        console.log(that.tableIdList)
        that.isSubmitLoading = true
        const response = await authDelete(that.tableIdList + '')
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('取消授权成功')
          that.tableIdList = []
          await this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })

    },

    testApi(row) {
      const that = this
      console.log('that.container', that.container)
      that.$router.push({
        path: '/apiManage/apiAuth/testPage',
        query: {
          id: that.apiId,
          key: row.key,
          container: that.container,
          appId: row.appId
        }
      })
    },

    //全选
    handleSelectionChange(vals) {
      const that = this
      that.tableIdList = []
      vals.map(function (v, k) {
        that.tableIdList.push(v.id)
      })
    },

    resetList() {
      const that = this
      that.SearchItem.appId = ''
      that.SearchItem.apiId = ''
      that.SearchItem.appName = ''
      that.tableData = []
      this.$router.push({
        path: '/apiManage/apiAuth/apiList',
        query: {
          type: 'apiAuth'
        }
      })
    }
  }
}
</script>
