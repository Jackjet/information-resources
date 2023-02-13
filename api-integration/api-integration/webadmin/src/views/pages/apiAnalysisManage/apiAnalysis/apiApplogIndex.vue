<template>
  <el-main class="main">
    <el-col :span="24">
      <div>
        <h3 style="display: inline-block;margin-right: 20px">API名称:{{titleApiName}}</h3>
        <h3 style="display: inline-block">应用名称:{{titleAppName}}</h3>
      </div>
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="请求时间">
          <el-date-picker v-model="time" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="returnBack" icon="el-icon-back">返回</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <el-col v-if="resultBox" style="position: absolute;top: 30%;left: 15%;z-index: 2;padding-top: -30px;width: 65%">
      <div name="testResult" style="height: 52vh;width:100%;background-color: #FFFFFF;border-radius: 5px;border:1px solid #b6b5b5;">
        <el-button type="text" icon="el-icon-close" @click="closeThisDiv" style="position: relative;left: 96%;display: inline-block"></el-button>
        <p style="color: black;margin-left: 10px;display: inline-block">{{title}}</p>
        <codemirror ref="myCode" :value="curCode" :options="cmOptions" class="code">
        </codemirror>
      </div>
    </el-col>
  </el-main>
</template>
<script>
import {
  getContent,
  logRecord
} from "@/api/apiAnalysisManage"
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListJiLu.vue'
import Pagination from '@/components/table/Pagination.vue'
import { codemirror } from 'vue-codemirror'
import "codemirror/theme/panda-syntax.css"
require("codemirror/mode/javascript/javascript")
export default {
  components: { TableList, Pagination, codemirror },
  computed: {
    ...mapGetters([
      'permissions'
    ]),
  },
  data() {
    return {
      drawer: false,
      tableSelection: {
        key: false,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        { id: false, type: 'color', label: '应用名称', list: 'appName' },
        { id: false, type: 'color', label: 'API名称', list: 'apiName' },
        { id: false, type: 'color', label: '请求方式', list: 'method' },
        { id: false, type: 'color', label: '请求时间', list: 'requestTime' },
        { id: false, type: 'color', label: '响应状态', list: 'responseCode' },
        { id: false, type: 'color', label: '响应时间', list: 'responseTime' }
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '请求内容',
          key: 0,
          type: 'text',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.getRequestContent(row)
          }
        }, {
          label: '响应结果',
          key: 0,
          type: 'text',
          permission: ['none'],
          State: false,
          method: (row) => {
            this.getResponseContent(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        startTime: '',
        endTime: ''
      },
      lastItem: {
        startTime: '',
        endTime: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      type: '',
      apiId: '',
      appId: '',
      resultBox: false,
      title: '',
      curCode: '',
      cmOptions: {
        value: '',
        mode: "text/x-mariadb",
        theme: "panda-syntax",
        readOnly: true,
        lineWrapping: true
      },
      titleApiName: '',
      titleAppName: ''
    }
  },
  created() {
    this.titleApiName = this.$route.query.apiName
    this.titleAppName = this.$route.query.appName
    this.type = this.$route.query.type
    this.apiId = this.$route.query.apiId
    this.appId = this.$route.query.appId
    this.fetchData()
  },
  methods: {
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      data.apiId = this.apiId
      data.appId = this.appId
      if (type !== 'page') {
        if (that.time !== null && that.time !== '' && that.time.length > 0) {
          data.startTime = this.time[0],
            data.endTime = this.time[1]
        } else {
          data.startTime = '',
            data.endTime = ''
        }
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await logRecord(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
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
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
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


    async getResponseContent(row) {
      const that = this
      let data = {
        id: row.id,
        type: 1
      }
      try {
        const res = await getContent(data)
        if (res.data.code === 1) {
          that.title = '响应结果'
          let tempJson = JSON.parse(res.data.data)
          let tempParams = {}
          if (tempJson.params) {
            tempParams = JSON.parse(tempJson.params)
            tempJson.params = tempParams
          }
          that.curCode = JSON.stringify(tempJson, null, "\t");
          that.resultBox = true
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    async getRequestContent(row) {
      const that = this
      let data = {
        id: row.id,
        type: 0
      }
      try {
        const res = await getContent(data)
        if (res.data.code === 1) {
          that.title = '请求内容'
          let tempJson = JSON.parse(res.data.data)
          let tempParams = {}
          if (tempJson.params) {
            tempParams = JSON.parse(tempJson.params)
            tempJson.params = tempParams
          }
          that.curCode = JSON.stringify(tempJson, null, "\t");
          that.resultBox = true
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    returnBack() {
      const that = this
      that.SearchItem.appId = ''
      that.SearchItem.apiId = ''
      that.SearchItem.startTime = ''
      that.SearchItem.endTime = ''
      let name = ''
      let id = ''
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

    closeThisDiv() {
      const that = this
      that.resultBox = false
    }
  }
}
</script>
