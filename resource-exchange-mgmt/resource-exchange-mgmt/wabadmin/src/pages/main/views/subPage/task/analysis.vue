<template>
  <el-main>
    <el-row :gutter="12" class="car-box">
      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>总体信息</span>
          </div>
          <div>
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
        <el-card>
          <div slot="header">
            <span>定时信息</span>
          </div>
          <div>
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
        <el-card>
          <div slot="header">
            <span>实时信息</span>
          </div>
          <div>
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
        <el-tab-pane label="明细（天）" name="day">
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
import TableList from '../../integration/tableList'
import Pagination from '../../integration/pagination'
export default {
  components: { TableList, Pagination},
  data() {
    return {
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
      const that = this
      let dayData = {}
      dayData.page = that.currentDayPage
      dayData.size = that.pageDaySize
      try {
        let url = that.Interface.analysis.getDay
        let res = await that.request.dataGet(that, url, dayData)
        if (res.data.code === 1) {
          that.tableDayData = res.data.data.content
          that.dayTotal = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //获取月
    async monthData(){
      const that = this
      let monthData = {}
      monthData.page = this.currentMonthPage
      monthData.size = this.pageMonthSize
      try {
        let url = that.Interface.analysis.getMonth
        let res = await that.request.dataGet(that, url, monthData)
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
      const that = this
      let dayData = {}
      for (let i=0;i<3;i++) {
        dayData.mode = i
        try {
          let url = that.Interface.analysis.getTaskLogs
          let res = await that.request.dataGet(that, url, dayData)
          if (res.data.code === 1) {
            if(i===0){
              that.logsAnalysisInfo = res.data.data
            }else if(i===1){
              that.logsAnalysisTiming = res.data.data
            }else{
              that.logsAnalysisRealTime = res.data.data
            }
          } else {
            that.$message.error(res.data.msg)
          }
        } catch (even) {
          that.$message.error(even.msg)
        }
      }
    },
    // 日翻页
    pageDayChange(item) {
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
.car-box {
  h6 {
    margin: 2em auto;
    text-align: left;
  }
}
.center{
  margin-top: 20px;
}
</style>
