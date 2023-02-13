<template>
  <el-main class="main">
    <el-col :span="24">
      <div>
        <h3>{{title}}</h3>
      </div>
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入API名称" v-model="apiSearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item :label-width="this.formLabelWidth">
          <el-select v-model="apiSearchItem.methods" clearable placeholder="请选择调用方式" size="medium">
            <el-option key='GET' label="GET" value='GET'>
            </el-option>
            <el-option key='POST' label="POST" value='POST'>
            </el-option>
            <el-option key='PUT' label="PUT" value='PUT'>
            </el-option>
            <el-option key='DELETE' label="DELETE" value='DELETE'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="apiSearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="goToAuth" icon="el-icon-s-check">授权</el-button>
          <el-button size='medium' icon="el-icon-back" @click="resetList">返回</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data='apiTableData' v-loading="isSubmitLoading" @onHandleSelectionChange="apiHandleSelectionChange" :table-selection="tableSelection" :table-label="apiTableHeader" :table-option="apiTableOptions">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="apiPage" :total="apiTotal" @pageChange="apiPageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import {
  groupApiList,
  insertAuth
} from "@/api/apiAuthManage"
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },

  data() {
    return {
      drawer: false,
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      apiAuthAll: ['none'],
      insertApi: ['none'],
      isSubmitLoading: false,
      apiIdList: [],
      apiTableData: [],
      apiTableHeader: [
        { id: false, type: 'imgColor', label: 'API名称', list: 'name' },
        { id: false, type: 'color', label: '分组名称', list: 'groupName' },
        { id: false, type: 'color', label: '更新时间', list: 'updateTime' },
        { id: false, type: 'color', label: '更新人', list: 'updateName' }
      ],
      apiTableOptions: {
      },
      apiSearchItem: {
        name: '',
        methods: ''
      },
      apiLastItem: {
        name: '',
        methods: ''
      },
      apiTotal: 0,
      apiPageSize: '20',
      apiCurrentPage: '1',
      title: '',
      groupId: '',

      //容器
      container: ''
    }
  },
  created() {
    // this.title = this.$route.query.name
    this.groupId = this.$route.query.id
    this.apiFetchData()
  },
  mounted() {
    this.title = this.$route.query.name
  },
  methods: {

    // 获取API列表
    async apiFetchData(type) {
      const that = this
      let data = {}
      data = this.apiSearchItem
      data.page = this.apiCurrentPage
      data.size = this.apiPageSize
      data.groupId = that.groupId
      Object.entries(that.apiSearchItem).map((item, index) => {
        that.apiLastItem[item[0]] = that.apiSearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await groupApiList(data)
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
      console.log('that.container', that.container)
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

    // 授权
    async goToAuth() {
      const that = this
      if (that.apiIdList.length === 0) {
        return that.$message.warning('请先选择API')
      }
      that.$router.push({
        path: '/apiManage/apiAuth/groupInsertAuth',
        query: {
          type: '授权应用',
          ids: that.apiIdList,
          groupId: that.groupId,
          groupName: that.title,
          container: that.container
        }
      })
    },

    resetList() {
      const that = this
      that.apiSearchItem.apiName = ''
      that.apiSearchItem.method = ''
      that.apiIdList = []
      this.$router.push({
        path: '/apiManage/apiAuth/apiList',
        query: {
          type: 'apiGroupAuth'
        }
      })
    }
  }
}
</script>
