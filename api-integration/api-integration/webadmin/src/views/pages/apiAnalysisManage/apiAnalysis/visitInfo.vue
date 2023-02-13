<template>
  <el-main class="main">
    <el-row>
      <el-col :span="24" style="height: 50px">
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item>
            <el-date-picker v-model="time" value-format="yyyy-MM-dd HH:mm:ss" :picker-options="pickerOptions" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
          </el-form-item>
          <el-form-item style='margin-left: 1%;'>
            <el-button size='medium' @click="getStatistic" icon="el-icon-search">查询</el-button>
            <el-button size='medium' @click="returnBack" icon="el-icon-back">返回</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" style="margin-top: 10px;">
        <div style="margin-bottom: -50px">
          <h4>访问量</h4>
        </div>
        <div id="chartLineBox" style="width: 100%;height: 40vh;"> </div>
      </el-col>
    </el-row>
    <el-row v-if="type === 'app'">
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm'>
          <div>
            <h4>授权的API</h4>
          </div>
          <el-form-item>
            <el-input clearable size="medium" placeholder="请输入API名称" v-model="apiSearchItem.apiName">
            </el-input>
          </el-form-item>
          <el-form-item style='margin-left: 1%;'>
            <el-button size='medium' @click="apiSearchNoteList" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" style="margin-top: 10px;">
        <TableList :table-data='apiTableData' v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="apiTableHeader" :table-option="tableOption">
        </TableList>
      </el-col>
      <el-col :span='24'>
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
    </el-row>
    <el-row v-if="type === 'api'">
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm'>
          <div>
            <h4>授权的应用</h4>
          </div>
          <el-form-item>
            <el-input clearable size="medium" placeholder="请输入应用ID" v-model="appSearchItem.appId">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input clearable size="medium" placeholder="请输入应用名称" v-model="appSearchItem.appName" icon="el-icon-search">
            </el-input>
          </el-form-item>
          <el-form-item style='margin-left: 1%;'>
            <el-button size='medium' @click="appSearchNoteList" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" class="center">
        <TableList :table-data='appTableData' v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="appTableHeader" :table-option="tableOption">
        </TableList>
      </el-col>
      <el-col :span='24'>
        <pagination ref="page" :total="total" @pageChange="appPageChange"></pagination>
      </el-col>
    </el-row>
  </el-main>
</template>
<script>
import {
  authApiList, authAppList, statistic
} from "@/api/apiAnalysisManage"
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'
import * as echarts from 'echarts'
export default {
  name: '',
  components: { TableList, Pagination, echarts },
  data() {
    return {
      drawer: false,
      tableSelection: {
        key: false,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      apiTableData: [],
      appTableData: [],
      apiTableHeader: [
        { id: false, type: 'color', label: 'API名称', list: 'apiName' },
        { id: false, type: 'color', label: '路由信息', list: 'routeInfo' },
        { id: false, type: 'color', label: '授权时间', list: 'createTime' }
      ],
      appTableHeader: [
        { id: false, type: 'color', label: '应用ID', list: 'appId' },
        { id: false, type: 'color', label: '应用名称', list: 'appName' },
        { id: false, type: 'color', label: '授权时间', list: 'createTime' }
      ],

      tableOption: {
        label: '操作',
        width: '200px',
        value: 0,
        show: true,
        options: [{
          label: 'API日志统计',
          key: 0,
          type: 'text',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.apiLogAnalysis(row)
          }
        }, {
          label: '日志详情',
          key: 0,
          type: 'text',
          permission: ['none'],
          State: false,
          method: (row) => {
            this.apiLogRecord(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        id: '',
        startTime: '',
        endTime: ''
      },
      lastItem: {
        id: '',
        startTime: '',
        endTime: ''
      },
      apiSearchItem: {
        apiName: '',
        startTime: '',
        endTime: ''
      },
      apiLastItem: {
        startTime: '',
        endTime: '',
        apiName: ''
      },
      appSearchItem: {
        appId: '',
        appName: '',
        startTime: '',
        endTime: ''
      },
      appLastItem: {
        appId: '',
        appName: '',
        startTime: '',
        endTime: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      type: '',
      tempName: '',
      apiId: '',
      appId: '',
      //折线图用数据
      charts: '',
      opinionUnit: [],
      opinionData: [],

      pickerMinDate: null,
      pickerMaxDate: null,
      day31: 31 * 24 * 3600 * 1000,
      pickerOptions: {
        onPick: ({ maxDate, minDate }) => {
          if (minDate && this.pickerMinDate) {
            this.pickerMinDate = null;
          } else if (minDate) {
            this.pickerMinDate = minDate.getTime();
          }
        },
        disabledDate: (time) => {
          if (this.pickerMinDate) {
            return (time.getTime() > (this.pickerMinDate + this.day31)) || (time.getTime() < (this.pickerMinDate - this.day31));
          }
          return false;
        }
      }
    }
  },
  created() {
    this.type = this.$route.query.type
    this.tempName = this.$route.query.name
    if (this.type === 'api') {
      this.apiId = this.$route.query.id
      this.appFetchData()
    }
    if (this.type === 'app') {
      this.appId = this.$route.query.id
      this.apiFetchData()
    }
    this.getStatistic()
    this.mounted()
  },
  methods: {
    //获取API列表
    async apiFetchData(type) {
      const that = this
      let data = {}
      data = this.apiSearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      data.appId = this.appId
      Object.entries(that.apiSearchItem).map((item, index) => {
        that.apiLastItem[item[0]] = that.apiSearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await authApiList(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.apiTableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    //获取APP列表
    async appFetchData(type) {
      const that = this
      let data = {}
      data = this.appSearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      data.apiId = this.apiId
      Object.entries(that.appSearchItem).map((item, index) => {
        that.appLastItem[item[0]] = that.appSearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await authAppList(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.appTableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    //获取访问量图表数据
    async getStatistic(type) {
      const that = this
      let data = {}
      if (that.time !== null && that.time !== '' && that.time.length > 0) {
        data.startTime = this.time[0]
        data.endTime = this.time[1]
      } else {
        data.startTime = ''
        data.endTime = ''
      }
      if (that.type === 'api') {
        data.id = that.apiId
        data.type = 1
      }
      if (that.type === 'app') {
        data.id = that.appId
        data.type = 0
      }
      try {
        const res = await statistic(data)
        if (res.data.code === 1) {
          this.processingDataFormats(res.data.data)
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    processingDataFormats(content) {
      let tag = []
      let data = []
      for (const param of content) {
        tag.push(param.date)
        data.push(param.num)
      }
      const that = this
      that.opinionUnit = tag
      that.opinionData = data
      this.mounted()
    },

    //API日志统计
    apiLogAnalysis(row) {
      const that = this
      let type = that.type
      let appName = ''
      let apiName = ''
      if (type === 'api') {
        apiName = that.tempName
        appName = row.appName
      } else {
        apiName = row.apiName
        appName = that.tempName
      }
      that.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/analysis',
        query: {
          apiId: row.apiId,
          appId: row.appId,
          title: 'API日志统计',
          type: type,
          apiName: apiName,
          appName: appName
        }
      })
    },

    //日志详情
    apiLogRecord(row) {
      const that = this
      let type = that.type
      let apiName = ''
      let appName = ''
      if (type === 'api') {
        apiName = that.tempName
        appName = row.appName
      } else {
        apiName = row.apiName
        appName = that.tempName
      }
      that.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/appApiLog',
        query: {
          apiId: row.apiId,
          appId: row.appId,
          title: '日志详情',
          type: type,
          apiName: apiName,
          appName: appName
        }
      })
    },

    apiSearchNoteList() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.apiFetchData()
    },

    appSearchNoteList() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.appFetchData()
    },


    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      if (that.apiLastItem.startTime === '' || that.apiLastItem.startTime === null) {
        that.time = []
      }
      Object.entries(that.apiSearchItem).map((item, index) => {
        that.apiSearchItem[item[0]] = that.apiLastItem[item[0]]
      })
      this.apiFetchData('page')
    },

    // 翻页
    appPageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      if (that.appLastItem.startTime === '' || that.appLastItem.startTime === null) {
        that.time = []
      }
      Object.entries(that.appSearchItem).map((item, index) => {
        that.appSearchItem[item[0]] = that.appLastItem[item[0]]
      })
      this.appFetchData('page')
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
    //返回上一级
    returnBack() {
      const that = this
      that.apiSearchItem.apiName = ''
      that.appSearchItem.appId = ''
      that.appSearchItem.appName = ''
      that.SearchItem.startTime = ''
      that.SearchItem.endTime = ''
      let tempType = ''
      if (that.type === 'api') {
        tempType = 'apiAnalysis'
      }
      if (that.type === 'app') {
        tempType = 'appAnalysis'
      }
      this.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/apiList',
        query: {
          type: tempType
        }
      })
    },

    drawLine(id) {
      this.charts = echarts.init(document.getElementById(id))
      this.charts.setOption({
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.opinionUnit
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '访问量',
          type: 'line',
          stack: '总量',
          data: this.opinionData
        }]
      })
    },
    mounted() {
      this.$nextTick(function () {
        this.drawLine('chartLineBox')
      })
    }
  }
}
</script>

