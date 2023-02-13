<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="工单名称">
          <el-input clearable size="medium" placeholder="请输入工单标题" v-model="searchItem.title"></el-input>
        </el-form-item>
        <el-form-item label="工单级别">
          <el-select size="medium" clearable placeholder="请选择工单级别" v-model="searchItem.level">
            <el-option label="正常" value="0"></el-option>
            <el-option label="一般" value="1"></el-option>
            <el-option label="关注" value="2"></el-option>
            <el-option label="警告" value="3"></el-option>
            <el-option label="严重警告" value="4"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="search" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-if="permissions.queryAll" plain size='medium' @click="findAllByMy('-1')">我的工单({{ count.total }})</el-button>
        <el-button type="primary" v-if="permissions.queryMy" plain size='medium' @click="findAllByMy('0')">我的待办({{ count.unfinished }})</el-button>
        <el-button type="primary" v-if="permissions.queryFinish" plain size='medium' @click="findAllByMy('1')">我已处理({{ count.finished }})</el-button>
      </div>
      <TableList :table-data='tableData'
                 v-loading="isSubmitLoading"
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
import {workOrderFindAll, workOrderCount, workOrderFindAllByMy} from "@/api/workOrder.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'

export default {
  components: {TableList, Pagination},
  data() {
    return {
      currentUserId: JSON.parse(sessionStorage.getItem("UserInfo")).id,
      permissions: {
        queryAll: true,
        queryMy: true,
        queryFinish: true,
      },
      time: [],
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        {label: '工单标题', list: 'title'},
        {label: '级别', list: 'levelStr'},
        {label: '状态', list: 'statusStr'},
        {label: '解决人', list: 'handlerName'},
        {label: '解决周期', list: 'handlerPeriod', width: '180px'},
        {label: '提交人', list: 'createByName'},
        {label: '提交时间', list: 'createTime', width: '180px'},
      ],
      tableOpction: {
        label: '操作',
        width: '220px',
        value: 0,
        options: [{
          label: '详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: true,
          method: (row) => {
            this.handleDetail(row)
          }
        }, {
          label: '处理',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: true,
          method: (row) => {
            this.handleHandle(row)
          },
          show: (row) => {
            return row.status === 0 && row.handlerId === this.currentUserId
          }
        }]
      },
      searchItem: {
        title: '',
        level: '',
      },
      lastItem: {
        title: '',
        level: '',
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      count: {
        total: 0,
        unfinished: 0,
        finished: 0,
      },

    }
  },
  created() {
    let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
    permissionsArr.forEach(item => {
      let itemArr = item.split('_')
      if (('/' + itemArr[0]) === this.$route.path) {
        this.permissions[itemArr[1]] = true
      }
    })
    this.fetchData()
    this.getCount()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.searchItem.title = ''
      that.searchItem.level = ''
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.searchItem
      data.page = this.currentPage
      data.size = this.pageSize
      if (type !== 'page') {
        if (that.time !== null && that.time !== '' && that.time.length > 0) {
          data.startTime = this.time[0]
          data.endTime = this.time[1]
        } else {
          data.startTime = ''
          data.endTime = ''
        }
      }
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await workOrderFindAll(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.map(function (v, k) {
            v.statusStr = that.getWorkOrderStatus(v.status)
            v.levelStr = that.getWorkOrderLevel(v.level)
          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 获取工单数量
    async getCount() {
      const that = this
      try {
        const res = await workOrderCount()
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.count = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 获取我的相关的工单
    async findAllByMy(status) {
      const that = this
      that.reset()

      let data = {}
      data.status = status
      try {
        that.isSubmitLoading = true
        const res = await workOrderFindAllByMy(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.map(function (v, k) {
            v.statusStr = that.getWorkOrderStatus(v.status)
            v.levelStr = that.getWorkOrderLevel(v.level)

          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    search() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      Object.entries(that.searchItem).map((item, index) => {
        that.searchItem[item[0]] = that.lastItem[item[0]]
      })
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      this.fetchData('page')
    },
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/workOrderDetail',
        query: {
          id: data.id
        }
      })
    },
    handleHandle(data) {
      const that = this
      that.$router.push({
        path: '/workOrderHandle',
        query: {
          id: data.id
        }
      })
    },

    //////

    //0正常、1一般、2关注、3警告、4严重警告
    getWorkOrderLevel(status) {
      switch (status) {
        case 0:
          return "正常"
        case 1:
          return "一般"
        case 2:
          return "关注"
        case 3:
          return "警告"
        case 4:
          return "严重警告"
        default:
          return "未知"
      }
    },
    getWorkOrderStatus(status) {
      switch (status) {
        case 0:
          return "处理中"
        case 1:
          return "已通过"
        case 2:
          return "已驳回"
        default:
          return "未知"
      }
    }
  }
}
</script>
