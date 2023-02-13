<template>
  <el-main class="main">
    <el-row :gutter="12">
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>总体信息</span>
          </div>
          <div class="text item">
            <h6>任务数：{{logsAnalysisInfo.taskNum}}</h6>
            <h6>任务调度成功数：{{logsAnalysisInfo.successNum}}</h6>
            <h6>同步数据量：{{logsAnalysisInfo.dataSum}}</h6>
            <h6 v-if="logsAnalysisInfo.rateOfSuccess>50"><span style="color: #02830F">成功率：{{logsAnalysisInfo.rateOfSuccess}}%</span></h6>
            <h6 v-else-if="logsAnalysisInfo.rateOfSuccess<=50"><span style="color: red">成功率：{{logsAnalysisInfo.rateOfSuccess}}%</span></h6>
            <h6 v-else><span style="color: red">成功率：{{logsAnalysisInfo.rateOfSuccess}}</span></h6>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>定时信息</span>
          </div>
          <div class="text item">
            <h6>任务数：{{logsAnalysisTiming.taskNum}}</h6>
            <h6>任务调度成功数：{{logsAnalysisTiming.successNum}}</h6>
            <h6>同步数据量：{{logsAnalysisTiming.dataSum}}</h6>
            <h6 v-if="logsAnalysisTiming.rateOfSuccess>50"><span style="color: #02830F">成功率：{{logsAnalysisTiming.rateOfSuccess}}%</span></h6>
            <h6 v-else-if="logsAnalysisTiming.rateOfSuccess<=50"><span style="color: red">成功率：{{logsAnalysisTiming.rateOfSuccess}}%</span></h6>
            <h6 v-else><span style="color: red">成功率：{{logsAnalysisTiming.rateOfSuccess}}</span></h6>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>实时信息</span>
          </div>
          <div class="text item">
            <h6>任务数：{{logsAnalysisRealTime.taskNum}}</h6>
            <h6>任务调度成功数：{{logsAnalysisRealTime.successNum}}</h6>
            <h6>同步数据量：{{logsAnalysisRealTime.dataSum}}</h6>
            <h6 v-if="logsAnalysisRealTime.rateOfSuccess>50"><span style="color: #02830F">成功率：{{logsAnalysisRealTime.rateOfSuccess}}%</span></h6>
            <h6 v-else-if="logsAnalysisRealTime.rateOfSuccess<=50"><span style="color: red">成功率：{{logsAnalysisRealTime.rateOfSuccess}}%</span></h6>
            <h6 v-else><span style="color: red">成功率：{{logsAnalysisRealTime.rateOfSuccess}}</span></h6>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-col :span="24" class="center">
      <el-tabs v-model="activeName">
        <el-tab-pane label="当月明细（天）" name="day">
          <el-col :span="24">
            <TableList :table-data='tableDayData'
                       v-loading="isSubmitLoading"
                       :table-selection="tableSelection"
                       :table-label="tableHeader"
                       :table-option="tableOpction">
            </TableList>
          </el-col>
          <el-col :span='24'>
            <pagination ref="page" :total="dayTotal" @pageChange="pageDayChange"></pagination>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="明细（月）" name="month">
          <el-col :span="24">
            <TableList :table-data='tableMonthData'
                       v-loading="isSubmitLoading"
                       :table-selection="tableSelection"
                       :table-label="tableHeader"
                       :table-option="tableOpction">
            </TableList>
          </el-col>
          <el-col :span='24'>
            <pagination ref="page" :total="monthTotal" @pageChange="pageMonthChange"></pagination>
          </el-col>
        </el-tab-pane>
      </el-tabs>
    </el-col>

  </el-main>
</template>
<script>
import { findAnalysisByDay, findAnalysisByMonth, getTaskLogsAnalysis } from "@/api/task.js";
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
      tableDayData: [],
      tableMonthData: [],
      tableHeader: [
        {id: false, type: '', label: '定时任务采集数据', list: 'timingTaskNum'},
        {id: false, type: '', label: '定时数据量', list: 'timingTaskDataNum'},
        {id: false, type: '', label: '实时数据量', list: 'realTimeTaskNum'},
        {id: false, type: '', label: '数据采集成功次数', list: 'taskSuccessNum'},
        {id: false, type: '', label: '数据采集失败次数', list: 'taskFailNum'},
        {id: false, type: '', label: '日期', list: 'taskDate'}
      ],
      tableOpction: {
      },
      dayTotal: 0,
      pageDaySize: '20',
      currentDayPage: '1',
      monthTotal: 0,
      pageMonthSize: '20',
      currentMonthPage: '1',
      activeName: 'day',
      logsAnalysisInfo:{
        taskNum: '',
        logNum:'',
        successNum: '',
        dataSum: '',
        rateOfSuccess: ''
      },
      logsAnalysisTiming:{
        taskNum: '',
        logNum:'',
        successNum: '',
        dataSum: '',
        rateOfSuccess: ''
      },
      logsAnalysisRealTime:{
        taskNum: '',
        logNum:'',
        successNum: '',
        dataSum: '',
        rateOfSuccess: ''
      }
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
    this.fetchData()
  },
  methods: {
    // 获取列表
    fetchData() {
      this.dayData()
      this.monthData()
      this.findLogsAnalysis()
    },
    //获取天
    async dayData(){
      let dayData = {}
      dayData.page = this.currentDayPage
      dayData.size = this.pageDaySize
      try {
        const res = await findAnalysisByDay(dayData)
        if (res.data.code === 1) {
          this.tableDayData = res.data.data.content
          this.dayTotal = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //获取月
    async monthData(){
      let monthData = {}
      monthData.page = this.currentMonthPage
      monthData.size = this.pageMonthSize
      try {
        const res = await findAnalysisByMonth(monthData)
        if (res.data.code === 1) {
          this.tableMonthData = res.data.data.content
          this.monthTotal = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //获取日志统计信息
    async findLogsAnalysis(){
      let dayData = {}
      for (let i=0;i<3;i++)
      {
        dayData.mode = i
        try {
          const res = await getTaskLogsAnalysis(dayData)
          if (res.data.code === 1) {
            if(i===0){
              this.logsAnalysisInfo = res.data.data
            }else if(i===1){
              this.logsAnalysisTiming = res.data.data
            }else if(i===2){
              this.logsAnalysisRealTime = res.data.data
            }
          } else {
            this.$message.error(res.data.msg)
          }
        } catch (even) {
          this.$message.error(even.msg)
        }
      }
    },
    // 日翻页
    pageDayChange(item) {
      console.log(item)
      this.pageDaySize = item.limit
      this.currentDayPage = item.page
      this.dayData()
    },
    // 月翻页
    pageMonthChange(item) {
      this.pageMonthSize = item.limit
      this.currentMonthPage = item.page
      this.monthData()
    }
  }
}
</script>
<style lang="scss" scoped>
.center{
  margin-top: 20px;
}
</style>