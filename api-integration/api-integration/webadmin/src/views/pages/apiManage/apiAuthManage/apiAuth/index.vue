<template>
  <el-main class="main">
    <el-tabs v-model="activeName">
      <el-tab-pane label="API授权" name="apiAuth" id="authIndexTab">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入API名称" v-model="apiSearchItem.name">
          </el-input>
        </el-form-item>
        <!-- <el-form-item>
          <el-select size="medium" v-model="apiSearchItem.key" filterable remote reserve-keyword placeholder="请输入标签名" :remote-method="remoteMethod" clearable>
            <el-option v-for="item in tagInfos" :key="item.name" :label="item.name" :value="item.name">
            </el-option>
          </el-select>
        </el-form-item> -->
        <!-- <el-form-item>
          <el-input clearable size="medium" placeholder="请输入标签值" v-model="apiSearchItem.value">
          </el-input>
        </el-form-item> -->
        <el-form-item>
          <el-select v-model="apiSearchItem.container" placeholder="请选择容器" clearable size="medium">
            <el-option v-for="item in containerInfos" :key="item.container" :label="item.name"
                       :value="item.container"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="apiSearchNoteList" icon="el-icon-search">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-permission="apiAuthAll" plain size='medium' @click="apiAllAuth"
                   icon="el-icon-s-check" v-if="permissions.apiManageAuthBatchAuth">批量授权
        </el-button>
      </div>
      <TableList :table-data='apiTableData' v-loading="isSubmitLoading"
                 @onHandleSelectionChange="apiHandleSelectionChange" :table-selection="tableSelection"
                 :table-label="apiTableHeader" :table-option="apiTableOptions">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="apiPage" :total="apiTotal" @pageChange="apiPageChange"></pagination>
    </el-col>
    </el-tab-pane>
    <el-tab-pane label="API组授权" name="apiGroupAuth">
        <el-col :span="24">
          <el-form :inline="true" class='el-InputForm'>
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入组名称" v-model="groupSearchItem.name">
              </el-input>
            </el-form-item>
            <el-form-item style='margin-left: 1%;'>
              <el-button size='medium' @click="groupFetchData" icon="el-icon-search">查询</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24" class="center">
          <TableList :table-data='groupTableData' v-loading="isSubmitLoading" :table-selection="groupTableSelection" :table-label="groupTableHeader" :table-option="groupTableOpction">
          </TableList>
        </el-col>
      </el-tab-pane>
    </el-tabs>
  </el-main>
</template>
<script>
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'

import AutoCompleteInput from '@/components/input/autoCompleteInput.vue'
import {
  apiList,
  apiGroupList
} from "@/api/apiAuthManage";

import {
  findAllMeta,
  findAllContainer,
  findAllApi
} from "@/api/sourceService";

export default {
  components: {TableList, Pagination, AutoCompleteInput},

  data() {
    return {
      permissions: {
        apiManageAuthBatchAuth: false,
        apiManageAuthAuth: false,
        apiManageAuthOperate: false
      },


      drawer: false,
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      container: '',


      groupTableSelection: {
        key: false,
        type: 'selection',
        detaile: false
      },
      apiAuthAll: ['none'],
      insertApi: ['none'],
      isSubmitLoading: false,
      apiIdList: [],
      apiTableData: [],
      groupTableData: [],
      apiTableHeader: [
        {id: false, type: 'imgColor', label: 'API名称', list: 'name'},
        {id: false, type: 'color', label: '路由路径', list: 'routeInfo'},
        {id: false, type: 'color', label: '接口资源名称', list: 'sourceName'},
        // { id: false, type: 'color', label: '标签', list: 'metaStr' },
        {id: false, type: 'color', label: '所属容器', list: 'container'},
        {id: false, type: 'color', label: '更新时间', list: 'updateTime'},
      ],
      groupTableHeader: [
        {id: false, type: 'color', label: '组名称', list: 'name'},
        {id: false, type: 'color', label: 'API数量', list: 'count'},
        {id: false, type: 'color', label: '更新时间', list: 'updateTime'}
      ],
      apiTableOptions: {
        label: '操作',
        width: '200px',
        show: false,
        options: [
            {
            label: '授权',
            key: 0,
            type: 'text',
            icon: 'el-icon-s-check',
            State: false,
            show: false,
            method: (row) => {
              this.insertAuth(row)
            }
          },
          {
            label: '已授权应用',
            key: 0,
            type: 'text',
            icon: 'el-icon-tickets',
            State: false,
            show: false,
            method: (row) => {
              this.getAuthAppList(row)
            }
          }]
      },
      groupTableOpction: {
        label: '操作',
        width: '200px',
        show: false,
        options: [{
          label: '授权',
          key: 0,
          type: 'text',
          icon: 'el-icon-s-check',
          State: false,
          method: (row) => {
            this.getGroupApiList(row)
          }
        }]
      },
      apiSearchItem: {
        apiName: '',
        sourceApiName: '',
        key: '',
        value: '',
        container: ''
      },
      apiLastItem: {
        apiName: '',
        sourceApiName: ''
      },
      groupSearchItem: {
        name: '',
        userName: '',
      },
      groupLastItem: {
        name: '',
        userName: '',
      },
      apiTotal: 0,
      apiPageSize: '20',
      apiCurrentPage: '1',
      groupTotal: 0,
      groupPageSize: '20',
      groupCurrentPage: '1',
      activeName: 'apiAuth',

      //标签
      tagInfos: [],
      containerInfos: [],
    }
  },
  created() {
    let tempName = this.$route.query.type
    if (tempName != null) {
      this.activeName = this.$route.query.type
    }
    this.apiFetchData()
    this.groupFetchData()
    this.getContainers()
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

      this.apiTableOptions.show = this.permissions.apiManageAuthOperate
      this.groupTableOpction.show = this.permissions.apiManageAuthOperate
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
        if (res.data.code === 1) {
          this.apiTableData = res.data.data.content
          this.apiTotal = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    // 获取API组列表
    async groupFetchData(type) {
      const that = this
      let data = {}
      data = this.groupSearchItem

      try {
        that.isSubmitLoading = true
        const res = await apiGroupList(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.groupTableData = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    //API列表全选
    apiHandleSelectionChange(valList) {
      const that = this
      that.apiIdList = []

      that.container = ''
      valList.map(function (v, k) {
        if (that.container !== '' && that.container !== v.container) {
          this.$message.error("请选择相同容器的API")
        }
        that.container = v.container
        that.apiIdList.push(v.id)
      })
    },
    // API搜索
    apiSearchNoteList() {
      this.currentPage = 1
      this.$refs.apiPage.Page(1)
      this.apiFetchData()
    },

    // 翻页
    apiPageChange(item) {
      let that = this
      this.apiPageSize = item.limit
      this.apiCurrentPage = item.page
      Object.entries(that.apiSearchItem).map((item, index) => {
        that.apiSearchItem[item[0]] = that.apiLastItem[item[0]]
      })
      this.apiFetchData('page')
    },

    // 翻页
    groupPageChange(item) {
      let that = this
      this.groupPageSize = item.limit
      this.groupCurrentPage = item.page
      Object.entries(that.groupSearchItem).map((item, index) => {
        that.groupSearchItem[item[0]] = that.groupLastItem[item[0]]
      })
      this.groupFetchData('page')
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

    // 批量授权
    async apiAllAuth() {
      const that = this
      if (that.apiIdList.length === 0) {
        return that.$message.warning('请先选择API')
      }
      that.$router.push({
        path: '/apiManage/apiAuth/insertAuth',
        query: {
          type: '授权应用',
          ids: that.apiIdList,
          container: that.container
        }
      })
    },

    //授权
    insertAuth(data) {
      const that = this
      that.apiIdList = []
      that.apiIdList.push(data.id)
      that.$router.push({
        path: '/apiManage/apiAuth/insertAuth',
        query: {
          type: '授权应用',
          ids: that.apiIdList,
          container: data.container
        }
      })
    },

    getAuthAppList(data) {
      const that = this
      that.$router.push({
        path: '/apiManage/apiAuth/appList',
        query: {
          id: data.id,
          // method: data.method,
          name: data.name,
          // container: data.container
        }
      })
    },

    getGroupApiList(row) {
      const that = this
      that.$router.push({
        path: '/apiManage/apiAuth/groupApiList',
        query: {
          id: row.id,
          name: row.name
        }
      })
    },

    //标签查询
    async remoteMethod(query) {
      const that = this
      if (query !== '') {
        let res = await findAllMeta({name: query})
        if (res.data.code == 1) {
          that.tagInfos = res.data.data.content
        }
      }
    },

    //容器查询
    async getContainers() {
      const that = this
      let param = {
        page: 1,
        size: 100000
      }
      let res = await findAllContainer(param)
      if (res.data.code == 1) {
        that.containerInfos = res.data.data.content


        let containerInfos = that.containerInfos

        containerInfos.forEach((item, i) => {
          item.container = item.ip + ':' + item.port
        })

        that.containerInfos = containerInfos
      }
    },
  }
}
</script>
