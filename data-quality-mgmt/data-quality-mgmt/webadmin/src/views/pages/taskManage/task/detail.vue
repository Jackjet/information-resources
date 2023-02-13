<template>
  <el-main class="main">
     <el-col :span="24">
      <div class="top">
          <div>
            <i class="el-icon-s-data" style="margin:0 10px"></i>
            {{name}}
          </div>
          <el-button  plain size='medium' @click="goBack">返回列表</el-button>
       </div>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
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
import { taskRuleLogFindAll } from "@/api/task.js"

import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
import { outExcel } from '@/utils/export'
export default {
  components: { TableList, Pagination},
  data() {
    return {
      query:'',
      time: [],
      name:'',
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        {label: '规则名称', list: 'ruleName'},
        {label: '数据模板', list: 'templateName'},
        {label: '规则数据源', list: 'sourceName'},
        {label: '检查数据', list: 'dataSize'},
        {label: '问题数据', list: 'errorDataSize'},
      ],
      tableOpction: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [{
          label: '查看问题',
          key: 0,
          type: 'text',
          State: true,
          method: (row) => {
            this.handleReport(row)
          }
        },{
          label: '生成工单',
          key: 1,
          type: 'text',
          State: true,
          method: (row) => {
            this.handleReport1(row)
          }
        }]
      },
      SearchItem: {
        taskLogId:''
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
    if(this.$route.query.name){
      localStorage.setItem('taskDetail',JSON.stringify(this.$route.query))
    }
    this.query = JSON.parse(localStorage.getItem('taskDetail'))
    this.SearchItem.taskLogId = this.query.id
    this.name = this.query.name
    this.fetchData()
  },
  methods: {
     goBack() {
      const that = this
      that.$router.push({
          path: 'taskManageLog'
      })
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      try {
        that.isSubmitLoading = true
        const res = await taskRuleLogFindAll(data)
        that.isSubmitLoading = false
        if(res.data.code === 1) {
          this.tableData = res.data.data.content
          this.total =  res.data.data.totalElements
          this.tableData.forEach((v,i)=>{
            v.sourceName = JSON.parse(v.metadataData).sourceName
          })
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

    handleReport(data){
      const that = this
      that.$router.push({
        path: '/taskManageProblem',
        query: {
          id: data.id
        }
      })
    },
    handleReport1(data){
      const that = this
      that.$router.push({
        path: '/taskManageOrder',
        query: {
          id: data.ruleId,
          ruleName:data.ruleName,
          templateName:data.templateName,
          templateId:data.templateId,
          createTime:data.createTime
        }
      })
    },


  }
}
</script>
<style lang="scss" scoped>
  .top{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
</style>
