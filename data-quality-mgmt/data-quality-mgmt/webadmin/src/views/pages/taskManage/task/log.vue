<template>
  <el-main class="main">
    <el-col :span="24">
      <div class="top">
        <div>
          <i class="el-icon-s-data" style="margin:0 10px"></i>
          {{name}}
        </div>
        <el-button plain size='medium' @click="goBack">返回列表</el-button>
      </div>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
      </div>
      <TableList :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { taskLogFindAll } from "@/api/task.js"

import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
import { outExcel } from '@/utils/export'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      time: [],
      name: '',
      id: '',
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      query: '',
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        { label: '执行时间', list: 'executionTime' },
        { label: '执行结果', list: 'executionResult' },
        { label: '检查数据', list: 'dataSize' },
        { label: '问题数据', list: 'errorDataSize' },
        { label: '耗时', list: 'time' },
      ],
      tableOpction: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [{
          label: '查看明细',
          key: 0,
          type: 'text',
          State: true,
          method: (row) => {
            this.handleReport(row)
          }
        }]
      },
      SearchItem: {
        taskId: '',
        page: '',
        size: ''
      },
      lastItem: {
        createByName: '',
        api: '',
        result: '',
        startTime: '',
        endTime: '',
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    if (this.$route.query.name) {
      localStorage.setItem('taskLog', JSON.stringify(this.$route.query))
    }
    this.query = JSON.parse(localStorage.getItem('taskLog'))
    this.name = this.query.name
    this.id = this.query.id
    this.SearchItem.taskId = this.query.id
    this.fetchData()
  },
  methods: {
    goBack() {
      this.$router.push('/taskManage')
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
      console.log(data)
      try {
        that.isSubmitLoading = true
        const res = await taskLogFindAll(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.forEach((v, i) => {
            v.time = v.time + '毫秒'
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
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData('page')
    },
    handleReport(data) {
      const that = this
      that.$router.push({
        path: '/taskManageDetail',
        query: {
          id: data.id,
          name: this.query.name + ' - ' + data.executionTime,
        }
      })
    },

  }
}
</script>
<style lang="scss" scoped>
.top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
