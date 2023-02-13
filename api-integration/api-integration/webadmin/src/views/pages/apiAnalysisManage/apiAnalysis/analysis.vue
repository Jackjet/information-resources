<template>
  <el-main class="main">
    <el-row>
      <div>
        <h3 style="display: inline-block;margin-right: 20px">API名称:{{titleApiName}}</h3>
        <h3 style="display: inline-block">应用名称:{{titleAppName}}</h3>
      </div>
      <el-col :span="24" style="height: 50px;margin-bottom: 20px">
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item>
            <el-date-picker v-model="time" value-format="yyyy-MM-dd HH:mm:ss" :picker-options="pickerOptions" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
          </el-form-item>
          <el-form-item style='margin-left: 1%;'>
            <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
            <el-button size='medium' @click="returnBack" icon="el-icon-back">返回</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-row style="background: #FFFFFF">
      <el-col :span="12" class="center">
        <div id="chartLineBox" style="width: 100%;height: 40vh; margin-top: 20px"></div>
      </el-col>
      <el-col :span="12" class="center">
        <div style="padding-top: 70px">
          <el-table :data="ipTableData" style="width: 100%" border="true" :header-cell-style="{background:'#f5f7fa'}">
            <el-table-column prop="name" label="IP地址">
            </el-table-column>
            <el-table-column prop="num" label="访问次数">
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <el-row style="background: #FFFFFF">
      <el-col :span="12">
        <div id="pieChart" style="width: 100%;height: 40vh;"></div>
      </el-col>
      <el-col :span="12" class="center">
        <div>
          <el-table :data="visitNumTableData" style="width: 100%" border="true" :header-cell-style="{background:'#f5f7fa'}">
            <el-table-column prop="totalNum" label="访问总次数">
            </el-table-column>
            <el-table-column prop="successNum" label="成功次数">
            </el-table-column>
            <el-table-column prop="failNum" label="失败次数">
            </el-table-column>
          </el-table>
        </div>
        <div style="margin-top: 20px">
          <el-table :data="codeNumTableData" style="width: 100%" border="true" :header-cell-style="{background:'#f5f7fa'}">
            <el-table-column prop="name" label="状态码">
            </el-table-column>
            <el-table-column prop="num" label="访问次数">
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <el-row style="background: #FFFFFF">
      <el-col :span="12">
        <div id="barChart" style="width: 100%;height: 40vh;"></div>
      </el-col>
      <el-col :span="12" class="center">
        <div>
          <el-table :data="avgTableData" style="width: 100%" border="true" :header-cell-style="{background:'#f5f7fa'}">
            <el-table-column prop="avgNum" label="平均响应时间(单位ms)">
            </el-table-column>
            <el-table-column prop="fastest" label="最快响应时间(单位ms)">
            </el-table-column>
            <el-table-column prop="slowest" label="最慢响应时间(单位ms)">
            </el-table-column>
          </el-table>
        </div>
        <div style="margin-top: 20px">
          <el-table :data="durationNumTableData" style="width: 100%" border="true" :header-cell-style="{background:'#f5f7fa'}">
            <el-table-column prop="name" label="响应时间">
            </el-table-column>
            <el-table-column prop="num" label="访问次数">
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </el-main>
</template>
<script>
import {
  apiVisitNum, ipVisitNum, codeVisitNum, durationNum
} from "@/api/apiAnalysisManage"
import * as echarts from 'echarts'
export default {
  components: { echarts },
  data() {
    return {
      drawer: false,
      isSubmitLoading: false,
      time: [],
      SearchItem: {
        startTime: '',
        endTime: ''
      },
      lastItem: {
        startTime: '',
        endTime: ''
      },
      ipTableData: [],
      visitNumTableData: [],
      durationNumTableData: [],
      codeNumTableData: [],
      avgTableData: [],
      type: '',
      appId: '',
      apiId: '',

      charts: '',
      opinionUnit: [],
      apiVisitNum: [],
      apiFailNum: [],

      pieCharts: '',
      pieOpinion: [],
      pieOpinionData: [],

      barCharts: '',
      barUnit: ['less 1000ms', '100ms-1s', '1-10s', '10-100s', '大于100s'],
      barNum: [],

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
      },
      titleAppName: '',
      titleApiName: ''
    }
  },
  created() {
    this.type = this.$route.query.type
    this.apiId = this.$route.query.apiId
    this.appId = this.$route.query.appId
    this.titleApiName = this.$route.query.apiName
    this.titleAppName = this.$route.query.appName
    this.apiVisitData()
    this.ipData()
    this.codeData()
    this.durationData()
    this.mounted()
  },
  methods: {
    // 获取列表
    async apiVisitData() {
      const that = this
      let data = {}
      if (that.time !== null && that.time !== '' && that.time.length > 0) {
        data.startTime = this.time[0]
        data.endTime = this.time[1]
      } else {
        data.startTime = ''
        data.endTime = ''
      }
      data.appId = that.appId
      data.apiId = that.apiId
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        const res = await apiVisitNum(data)
        if (res.data.code === 1) {
          this.apiVisitDataFormat(res.data.data)
          this.$nextTick(function () {
            this.drawLine('chartLineBox')
          })
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    async ipData() {
      const that = this
      let data = {}
      if (that.time !== null && that.time !== '' && that.time.length > 0) {
        data.startTime = this.time[0]
        data.endTime = this.time[1]
      } else {
        data.startTime = ''
        data.endTime = ''
      }
      data.appId = that.appId
      data.apiId = that.apiId
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        const res = await ipVisitNum(data)
        if (res.data.code === 1) {
          that.ipTableData = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    async codeData() {
      const that = this
      that.visitNumTableData = []
      let data = {}
      if (that.time !== null && that.time !== '' && that.time.length > 0) {
        data.startTime = this.time[0]
        data.endTime = this.time[1]
      } else {
        data.startTime = ''
        data.endTime = ''
      }
      data.appId = that.appId
      data.apiId = that.apiId
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        const res = await codeVisitNum(data)
        if (res.data.code === 1) {
          let tempVisitNum = []
          tempVisitNum.push(res.data.data.visitNum)
          that.visitNumTableData = tempVisitNum
          that.codeNumTableData = res.data.data.codeNum
          this.codeNumDataFormat(that.codeNumTableData)
          this.$nextTick(function () {
            this.drawPie('pieChart')
          })
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    async durationData() {
      const that = this
      that.avgTableData = []
      let data = {}
      if (that.time !== null && that.time !== '' && that.time.length > 0) {
        data.startTime = this.time[0]
        data.endTime = this.time[1]
      } else {
        data.startTime = ''
        data.endTime = ''
      }
      data.appId = that.appId
      data.apiId = that.apiId
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        const res = await durationNum(data)
        if (res.data.code === 1) {
          that.avgTableData.push(res.data.data.avg)
          let tempList = []
          for (let tempInfo of res.data.data.durationNum) {
            if (tempInfo.name === '0') {
              tempInfo.name = '小于100ms'
            } else if (tempInfo.name === '1') {
              tempInfo.name = '100ms-1s'
            } else if (tempInfo.name === '2') {
              tempInfo.name = '1-10s'
            } else if (tempInfo.name === '3') {
              tempInfo.name = '10-100s'
            } else if (tempInfo.name === '4') {
              tempInfo.name = '大于100s'
            }
            tempList.push(tempInfo)
          }
          that.durationNumTableData = tempList
          this.durationNumDataFormat(that.durationNumTableData)
          this.$nextTick(function () {
            this.drawBar('barChart')
          })
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    apiVisitDataFormat(data) {
      let tempVisitNum = []
      let tempFailNum = []
      let visitNum = data.totalNum
      let failNum = data.failNum
      const that = this
      let startTime = that.time[0]
      let endTime = that.time[1]
      if (that.time.length <= 0) {
        startTime = this.getStartTime()
        endTime = this.getEndTime()
      }
      let timeType = this.getTimeType(startTime, endTime)
      if (timeType === '1') {
        let stTime = startTime.substring(0, 10)
        let edTime = endTime.substring(0, 10)
        that.opinionUnit = this.getDays(stTime, edTime)
      } else {
        that.opinionUnit = this.getHours(startTime, endTime)
      }
      for (const tempInfo of that.opinionUnit) {
        let num1 = 0
        if (visitNum) {
          for (const visitInfo of visitNum) {
            if (visitInfo.date === tempInfo) {
              num1 = visitInfo.num
            }
          }
        }
        tempVisitNum.push(num1)
        let num2 = 0
        if (failNum) {
          for (const visitInfo of failNum) {
            if (visitInfo.date === tempInfo) {
              num2 = visitInfo.num
            }
          }
        }
        tempFailNum.push(num2)
      }
      that.apiVisitNum = tempVisitNum
      that.apiFailNum = tempFailNum
    },

    codeNumDataFormat(data) {
      const that = this
      let tempOpinion = []
      let tempData = []
      for (const tempInfo of data) {
        tempOpinion.push(tempInfo.name)
        let tempValue = {}
        tempValue.name = tempInfo.name
        tempValue.value = tempInfo.num
        tempData.push(tempValue)
      }
      that.pieOpinion = tempOpinion
      that.pieOpinionData = tempData
    },

    durationNumDataFormat(data) {
      const that = this
      let tempData = []
      let tempUnit = []
      if (data) {
        for (const tempInfo of data) {
          tempUnit.push(tempInfo.name)
          tempData.push(tempInfo.num)
        }
      }
      that.barNum = tempData
      that.barUnit = tempUnit
    },

    getEndTime() {
      let nowTime = new Date()
      let timeStamp = nowTime.getTime()
      return this.timestampToTime(timeStamp)
    },

    getStartTime() {
      let nowTime = new Date()
      let timestamp = nowTime.getTime() - 86400000
      return this.timestampToTime(timestamp)
    },

    getTimeType(startTime, endTime) {
      let st1 = new Date(startTime).getTime()
      let st2 = new Date(endTime).getTime()
      let time = st2 - st1
      let type = ''
      if (time > 604800000) {
        type = '1'
      } else {
        type = '0'
      }
      return type
    },

    // 获取日期的X轴信息
    getDays(begin, end) {
      let arr1 = begin.split("-");
      let arr2 = end.split("-");
      let arr1_ = new Date();
      let arrTime = [];
      arr1_.setUTCFullYear(arr1[0], arr1[1] - 1, arr1[2]);
      let arr2_ = new Date();
      arr2_.setUTCFullYear(arr2[0], arr2[1] - 1, arr2[2]);
      let unixDb = arr1_.getTime();
      let unixDe = arr2_.getTime();
      for (let k = unixDb; k <= unixDe;) {
        arrTime.push(this.datetimeparse(k, 'yyyy-MM-DD'));
        k = k + 24 * 60 * 60 * 1000;
      }
      return arrTime
    },

    // 日期时间格式处理
    datetimeparse(timestamp, format, prefix) {
      if (typeof timestamp === 'string') {
        timestamp = Number(timestamp)
      }
      //转换时区
      let currentZoneTime = new Date(timestamp);
      let currentTimestamp = currentZoneTime.getTime();
      let offsetZone = currentZoneTime.getTimezoneOffset() / 60;//如果offsetZone>0是西区，西区晚
      let offset = null;
      //客户端时间与服务器时间保持一致，固定北京时间东八区。
      offset = offsetZone + 8;
      currentTimestamp = currentTimestamp + offset * 3600 * 1000

      let newtimestamp = null;
      if (currentTimestamp) {
        if (currentTimestamp.toString().length === 13) {
          newtimestamp = currentTimestamp.toString()
        } else if (currentTimestamp.toString().length === 10) {
          newtimestamp = currentTimestamp + '000'
        } else {
          newtimestamp = null
        }
      } else {
        newtimestamp = null
      }
      ;
      let dateobj = newtimestamp ? new Date(parseInt(newtimestamp)) : new Date()
      let YYYY = dateobj.getFullYear()
      let MM = dateobj.getMonth() > 8 ? dateobj.getMonth() + 1 : '0' + (dateobj.getMonth() + 1)
      let DD = dateobj.getDate() > 9 ? dateobj.getDate() : '0' + dateobj.getDate()
      let HH = dateobj.getHours() > 9 ? dateobj.getHours() : '0' + dateobj.getHours()
      let mm = dateobj.getMinutes() > 9 ? dateobj.getMinutes() : '0' + dateobj.getMinutes()
      let ss = dateobj.getSeconds() > 9 ? dateobj.getSeconds() : '0' + dateobj.getSeconds()
      let output = '';
      let separator = '/'
      if (format) {
        separator = format.match(/-/) ? '-' : '/'
        output += format.match(/yy/i) ? YYYY : ''
        output += format.match(/MM/) ? (output.length ? separator : '') + MM : ''
        output += format.match(/dd/i) ? (output.length ? separator : '') + DD : ''
        output += format.match(/hh/i) ? (output.length ? ' ' : '') + HH : ''
        output += format.match(/mm/) ? (output.length ? ':' : '') + mm : ''
        output += format.match(/ss/i) ? (output.length ? ':' : '') + ss : ''
      } else {
        output += YYYY + separator + MM + separator + DD
      }
      output = prefix ? (prefix + output) : output

      return newtimestamp ? output : ''
    },

    //获取小时的坐标X轴
    getHours(begin, end) {
      let tempBegin = begin.substring(0, 13)
      tempBegin = tempBegin + ':00:00'
      let beginTimestamp = new Date(tempBegin).getTime()
      let tempEnd = end.substring(0, 13)
      tempEnd = tempEnd + ':00:00'
      let endTimestamp = new Date(tempEnd).getTime()
      let temp = true
      let unit = []
      let timestemp = beginTimestamp
      while (temp) {
        timestemp = timestemp + 3600000
        let tempTime = this.timestampToTime(timestemp)
        unit.push(tempTime)
        if (timestemp === endTimestamp) {
          break
        }
      }
      return unit
    },

    timestampToTime(timestamp) {
      var date = new Date(timestamp);
      let Y = date.getFullYear() + '-';
      let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
      let D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
      let h = (date.getHours() < 10 ? '0' + (date.getHours()) : date.getHours()) + ':';
      let m = (date.getMinutes() < 10 ? '0' + (date.getMinutes()) : date.getMinutes()) + ':';
      let s = (date.getSeconds() < 10 ? '0' + (date.getSeconds()) : date.getSeconds());
      return Y + M + D + h + m + s;
    },

    // 搜索
    SearchNoteList() {
      this.apiVisitData()
      this.ipData()
      this.codeData()
      this.durationData()
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

    returnBack() {
      const that = this
      that.SearchItem.startTime = ''
      that.SearchItem.endTime = ''
      let id = ''
      let name = ''
      if (that.type === 'api') {
        id = that.apiId
        name = that.titleApiName
      } else {
        id = that.appId
        name = that.titleAppName
      }
      this.$router.push({
        path: '/apiAnalysisManage/apiAnalysis/visitInfo',
        query: {
          type: that.type,
          id: id,
          name: name
        }
      })
    },

    drawLine(id) {
      this.charts = echarts.init(document.getElementById(id))
      this.charts.setOption({
        title: {
          text: 'API访问次数'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          show: true,
          orient: "horizontal",
          x: 'center',
          y: 'top',
          padding: [20, 0, 0, 0],
          data: ['API调用次数', 'API调用失败次数']
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
          name: 'API调用次数',
          type: 'line',
          stack: 'API调用次数',
          data: this.apiVisitNum
        }, {
          name: 'API调用失败次数',
          type: 'line',
          stack: 'API调用失败次数',
          data: this.apiFailNum
        }]
      })
    },

    drawPie(id) {
      this.pieCharts = echarts.init(document.getElementById(id))
      this.pieCharts.setOption({
        title: {
          text: '响应状态码统计'
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          x: 'center',
          y: 'bottom',
          data: this.pieOpinion
        },
        series: [
          {
            name: '状态码',
            type: 'pie',
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: 'left'
              },
              emphasis: {
                show: false,
                textStyle: {
                  fontSize: '30',
                  fontWeight: 'blod'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.pieOpinionData
          }
        ]
      })
    },

    drawBar(id) {
      this.barCharts = echarts.init(document.getElementById(id))
      this.barCharts.setOption({
        title: {
          text: '平均响应时间'
        },
        tooltip: {},
        xAxis: {
          data: this.barUnit
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '访问次数',
          type: 'bar',
          data: this.barNum,
          barWidth: 40,
          itemStyle: {
            color: function (params) {
              let colorList = [
                '#B5C334', '#FCCE10', '#E87C25', '#C1232B'
              ];
              return colorList[params.dataIndex]
            }
          }
        }]
      })
    },

    mounted() {
      this.$nextTick(function () {
        this.drawLine('chartLineBox')
        this.drawPie('pieChart')
        this.drawBar('barChart')
      })
    }
  }
}
</script>
