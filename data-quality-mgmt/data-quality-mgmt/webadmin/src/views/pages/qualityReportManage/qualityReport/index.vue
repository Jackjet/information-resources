<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item  label="">
          <el-input clearable
            size="medium"
            placeholder="请输入报告名称"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item  label="">
          <el-input clearable
            size="medium"
            placeholder="请输入创建人名称"
            v-model="SearchItem.createByName">
          </el-input>
        </el-form-item>

        <el-form-item  label="创建时间" >
          <el-date-picker
                  v-model="time"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="创建时间开始"
                  end-placeholder="创建时间结束"
                  :default-time="['00:00:00', '23:59:59']"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
          <el-button  type="primary" plain  size='medium' @click="handleAdd" icon="el-icon-plus">新增</el-button>
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
import { qualityReportFindAll,qualityReportDelete} from "@/api/qualityReport.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination},
  data() {
    return {
      time: [],
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        {label: '报告名称', list: 'name'},
        {label: '开始时间', list: 'startTime'},
        {label: '结束时间', list: 'endTime'},
        {label: '备注', list: 'description'},
        {label: '创建人', list: 'createByName'},
        {label: '创建时间', list: 'createTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [{
          label: '完善报告',
          key: 0,
          type: 'text',
          State: true,
          method: (row) => {
            this.handleReport(row)
          }
        },{
          label: '详情',
          key: 0,
          type: 'text',
          show: (row) => {
            if (row.closing && row.foreword) {
              return true
            } else {
              return false
            }
          },
          State: true,
          method: (row) => {
            this.handleDetail(row)
          }
        },{
          label: '删除',
          key: 0,
          type: 'text',
          State: true,
          method: (row) => {
            this.handleDelete(row)
          }
        }]
      },
      SearchItem: {
        createByName: '',
        name: '',
        startTime: '',
        endTime: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.SearchItem.createByName = '',
      that.SearchItem.name = '',
      that.SearchItem.startTime = '',
              that.SearchItem.endTime = '',
              that.time = []
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem,
      data.page = this.currentPage,
      data.size = this.pageSize
      if(type !== 'page') {
        if(that.time !== null &&  that.time !== '' && that.time.length > 0){
          data.startTime = this.time[0],
                  data.endTime = this.time[1]
        } else {
          data.startTime = '',
                  data.endTime = ''
        }
      }
      try {
        that.isSubmitLoading = true
        const res = await qualityReportFindAll(data)
        that.isSubmitLoading = false
        if(res.data.code === 1) {
          this.tableData = res.data.data.content
          this.total =  res.data.data.totalElements
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
      if(that.lastItem.startTime === '' || that.lastItem.startTime === null){
        that.time = []
      }
      this.fetchData('page')
    },
    // 详情
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/qualityReportWord',
        query: {
          id: data.id,
          startTime:data.startTime,
          endTime:data.endTime,
          foreword:data.foreword,
          closing:data.closing,
          type:'detail'
        }
      })
    },
    handleReport(data){
      const that = this
      that.$router.push({
        path: '/qualityReportWord',
        query: {
          id: data.id,
          startTime:data.startTime,
          endTime:data.endTime,
          foreword:data.foreword,
          closing:data.closing,
          type:'report'
        }
      })
    },
     // 删除
    handleDelete(row){
      const that = this
      that.$confirm('删除该报告, 是否继续?', '提示', {
          type: 'warning'
      }).then(async () => {
          let params = {
              id: row.id
          }
          let response = await qualityReportDelete(row.id)
          if(response.data.code === 1) {
              that.$message.success('删除成功')
            this.fetchData()
          } else {
              that.$message.error(response.data.msg)
          }
      })
    },
    // 新增
    handleAdd() {
      const that = this
      that.$router.push({
        path: '/qualityReportAdd',
        query: {
          type:'新增'
        }
      })
    },

  }
}
</script>
