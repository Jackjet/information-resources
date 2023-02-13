<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-form-item>
            <el-date-picker
                v-model="time"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
          </el-form-item>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="goBack" icon="el-icon-back">返回</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center" style="z-index: 1">
      <TableList :table-data='tableData'
        v-loading="isSubmitLoading"
        :table-selection="tableSelection"
        :table-label="tableHeader"
        :table-option="tableOption">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <el-col v-if="resultBox" style="position: absolute;top: 30%;left: 15%;z-index: 2;padding-top: -30px;width: 65%">
      <div name="testResult" style="height: 52vh;width:100%;background-color: #FFFFFF;border-radius: 5px;border:1px solid #b6b5b5;">
        <el-button type="text" icon="el-icon-close" @click="closeThisDiv" style="position: relative;left: 96%;display: inline-block"></el-button>
        <p style="color: black;margin-left: 10px;display: inline-block">{{title}}</p>
        <codemirror
            ref="myCode"
            :value="curCode"
            :options="cmOptions"
            class="code"
        >
        </codemirror>
      </div>
    </el-col>
  </el-main>
</template>
<script>
import {
  apiTestLogList,getContent
} from "@/api/apiTestManage"
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'
import { codemirror } from 'vue-codemirror'
import "codemirror/theme/panda-syntax.css"
require("codemirror/mode/javascript/javascript")

export default {
  components: { TableList, Pagination,codemirror },
  computed: {
    ...mapGetters([
      'permissions'
    ]),
  },
  data() {
    return {
      drawer: false,
      time:[],
      tableSelection: {
        key: false,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        {id: false, type: 'color', label: '调用时间', list: 'requestTime'},
        {id: false, type: 'color', label: '响应状态码', list: 'responseCode'},
        {id: false, type: 'color', label: '请求时长(ms)', list: 'responseTime'}
      ],
      tableOption: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '请求内容',
          key: 0,
          type: 'text',
          icon: 'el-icon-s-order',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.requestContent(row)
          }
        }, {
          label: '响应内容',
          key: 0,
          type: 'text',
          icon: 'el-icon-s-order',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.responseContent(row)
          }
        }]
      },
      SearchItem:{
        apiId:'',
        startTime:'',
        endTime:''
      },
      lastItem: {
        apiId:'',
        startTime:'',
        endTime:''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      title:'',
      message:'',
      resultBox:false,
      curCode:'',
      cmOptions:{
        value:'',
        mode:"text/x-mariadb",
        theme: "panda-syntax",
        readOnly:true,
        lineWrapping: true
      }
    }
  },
  created(){
    this.SearchItem.apiId = this.$route.query.id
    this.findPermission()
    this.fetchData()
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      const that = this
      let props = {
        requestContent: function (value) {
          that.tableOption.options[0].permission = JSON.parse(JSON.stringify(value))
        },
        responseContent: function (value) {
          that.tableOption.options[1].permission = JSON.parse(JSON.stringify(value))
        }
      }

      if(this.permissions.length > 0){
        this.permissions.map(function(v, k){
          if(v['apiTest']){
            for (let i in v['apiTest']) {
              if(props.hasOwnProperty(i)){ props[i](v['apiTest'][i])}
            }
          }
        })
      }
    },

    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      if(that.time !== null &&  that.time !== '' && that.time.length > 0){
        data.startTime = this.time[0]
        data.endTime = this.time[1]
      } else {
        data.startTime = ''
        data.endTime = ''
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await apiTestLogList(data)
        that.isSubmitLoading = false
        if(res.data.code === 1){
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
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },

    // 行高
    cellStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    // 头行高
    headerStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },

    closeThisDiv(){
      const that = this
      that.resultBox = false
    },

    async responseContent(row){
      const  that  = this
      let data = {
        id:row.id,
        type: 1
      }
      try {
        const res = await getContent(data)
        if(res.data.code === 1){
          that.title = '响应结果'
          let tempJson = JSON.parse(res.data.data)
          let tempParams = {}
          if(tempJson.params){
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

    async requestContent(row){
      const  that  = this
      let data = {
        id:row.id,
        type: 0
      }
      try {
        const res = await getContent(data)
        if(res.data.code === 1){
          that.title = '请求内容'
          let tempJson = JSON.parse(res.data.data)
          let tempParams = {}
          if(tempJson.params){
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

    //返回
    goBack(){
      const that = this
      that.$router.push({
        path: '/apiManage/apiTest/apiList'
      })
    },
  }
}
</script>
<style lang="scss" scoped>
  /deep/ .el-button--primary.is-plain:focus, .el-button--primary.is-plain:hover{
    background: #ffffffff
  }
  /deep/ .el-form-item__label{
    color: #fff;
  }
  /deep/ .el-input--medium .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-date-editor .el-range__close-icon{
    line-height: 24px !important;
  }
  /deep/ .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-button--medium{
    padding: 7px 15px;
  }
  /deep/ .el-form-item{
    margin-bottom: 5px;
  }
  /deep/ .el-InputForm .el-range__icon{
    line-height: 25px!important;
  }
  /deep/ .el-input__icon .el-range__icon {
    line-height: 24px!important;
  }
 /deep/ .el-date-editor .el-range-separator{
   line-height: 24px !important;
 }
  .center{
    background: #fff;
    padding: 0px 15px;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 350px;
  }
  .main{
    background: #00000000;
    height: 100%;
    width: 100%;
    padding: 0 30px;
    min-height: 600px;
    margin-top: -240px;
    .el-InputForm{
      background:url('../../../../assets/image/filter.png');
      background-size: 100% 100%;
      padding:15px 0 0 20px;
      min-height: 100px;
    }
  }
  .add{
    width: 25px;
    height: 25px;
    background:url('../../../../assets/image/add.png');
  }
  .work-query {
    display: flex;
    align-items: center;
    padding: 20px;
  }
  .table-list {
    padding: 20px 20px 0 20px;
    box-sizing: border-box;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 400px;
  }
  .table-pagination {
    display: flex;
    justify-content: flex-end;
    padding: 20px;
  }
  .code{
    font-size: 16px;
    width: 100%;
  }

</style>
