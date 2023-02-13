<template>
  <el-main class="main">
    <el-col :span="24">
      <h1>任务名称:{{taskName}}</h1>
      <h5>(1)当前任务频率: <span style="color: red">{{taskFrequency}}</span></h5>
      <h5>(2)截止目前,数据采集{{logNum}}次, 成功{{successNum}}次, 失败<span style="color: red">{{failNum}}</span>次</h5>
    </el-col>
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-button size='medium' @click="toBack" icon="el-icon-back">返回</el-button>
        </el-form-item>
        <el-form-item>
          <el-select style="width: 100%;" v-model="SearchItem.status" clearable placeholder="请选择执行状态">
            <el-option key='0' label="成功" value='0'/>
            <el-option key='1' label="失败" value='1'/>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
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
import { getTaskLogs,getTaskLogsInfo } from "@/api/task.js";
import TableList from '@/components/table/TableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination},
  data() {
    return {
      permissions: {
      },
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '执行状态', list: 'status'},
        {id: false, type: '', label: '调度时间', list: 'runTime'},
        {id: false, type: '', label: '开始时间', list: 'startTime'},
        {id: false, type: '', label: '结速时间', list: 'endTime'},
        {id: false, type: '', label: '总数据量', list: 'dataSum'},
        {id: false, type: '', label: '耗时', list: 'sumTime'},
        {id: false, type: '', label: '集成模式', list: 'mode'}
      ],
      tableOpction: {
      },
      SearchItem: {
        taskId: '',
        status: '',
      },
      lastItem: {
        taskId: '',
        status: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
      taskName: '',
      groupId: '',
      taskFrequency: '',
      logNum: '',
      successNum: '',
      failNum: '',
    }
  },
  created () {
    let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
    permissionsArr.forEach(item => {
      let itemArr = item.split('_')
      if (('/' + itemArr[0]) === this.$route.path) {
        this.permissions[itemArr[1]] = true
      }
    })
    this.SearchItem.taskId = this.$route.query.id
    this.groupId = this.$route.query.groupId
    this.fetchData()
    this.getLogsInfo()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.SearchItem.status = '',
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
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
        const res = await getTaskLogs(data)
        if(res.data.code === 1){
          res.data.data.content.map(function (v, k) {
            if (v.status === 0) {
              v.status = '成功'
            } else if (v.status === 1) {
              v.status = '失败'
            }
          })
          res.data.data.content.map(function (v, k) {
            if (v.mode === 0) {
              v.mode = '未知'
            } else if (v.mode === 1) {
              v.mode = '定时'
            } else if (v.mode === 2) {
              v.mode = '实时'
            }
          })
          res.data.data.content.map(function (v, k) {
            v.sumTime = v.sumTime+"秒"
          })
          this.tableData = res.data.data.content
          this.total =  res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 获取列表
    async getLogsInfo() {
      let data = {}
      data.taskId = this.SearchItem.taskId
      try {
        const res = await getTaskLogsInfo(data)
        if(res.data.code === 1){
          this.taskName = res.data.data.name
          this.taskFrequency = res.data.data.taskFrequency
          this.logNum = res.data.data.logNum
          this.successNum = res.data.data.successNum
          this.failNum = res.data.data.failNum
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
      this.fetchData()
    },
    toBack() {
      this.$router.push({
        path: '/task',
        query: {
          groupId:this.taskGroupId
        }
      })
    },
  }
}
</script>
<style lang="scss" scoped>

</style>