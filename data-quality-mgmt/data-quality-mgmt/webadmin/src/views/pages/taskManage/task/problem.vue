<template>
  <el-main class="main">
    <el-col :span="24" class="center">
      <div><h4>{{ title }}</h4></div>      
      <p style="color:#8080FF;margin:40px 0">{{data.ruleName}}</p>
      <p style="margin:30px 0">数据模板：{{data.templateName}}<span style="margin-left:50px;color:#7F7F7F;font-size:14px"><img src="../../../../assets/image/local.png" alt="">{{data.templateDescription}}</span></p>
      <p style="margin:30px 0">元数据：{{keyData}}</p>
      <p style="margin:30px 0">描述：{{data.ruleDescription}}</p>
      <p style="color:#F59A23;margin:40px 0">问题数据示例：</p>
      <TableList :table-data='tableData'
        v-loading="isSubmitLoading"
        @onHandleSelectionChange="handleSelectionChange"
        :table-selection="tableSelection"
        :table-label="tableHeader"
        :table-option="tableOpction">
      </TableList>
      <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回</el-button>
        </div>
    </el-col>
    
  </el-main>
</template>
<script>
import { findTaskRuleLogById } from "@/api/task.js"
import TableList from '@/components/table/tableList'
export default {
  components: { TableList},
  data() {
    return {
      title:'问题数据',
      data:{},
      keyData:'',
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [],
      tableOpction: {},
      SearchItem: {
        id:''
      },
    }
  },
  created() {
    this.SearchItem.id = this.$route.query.id
    this.fetchData()
  },
  methods: {
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      try {
        that.isSubmitLoading = true  
        const res = await findTaskRuleLogById(data)
        that.isSubmitLoading = false  
        if(res.data.code === 1) {
          this.data = res.data.data
          if(this.data.errorData){
            this.data.errorData = JSON.parse(this.data.errorData)
            let title = this.data.errorData[0]
            this.tableHeader = []
            for(let key in title){
              let obj = {label:key,list:key}
              this.tableHeader.push(obj)
            }
            this.tableData = this.data.errorData
          }
          
          let fields = JSON.parse(this.data.metadataData).source.fields
          let keyData = []
          fields.forEach((v,i)=>{
            keyData.push(v.name+'('+v.type+')')
          })
          this.keyData = keyData.join(' , ')
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    goBack() {
      const that = this
      that.$router.push({ 
          path: '/taskManageDetail'
      })
    },
  }
}
</script>
<style lang="scss" scoped>
  .drawer_footer{
    text-align:center;
  }
</style>