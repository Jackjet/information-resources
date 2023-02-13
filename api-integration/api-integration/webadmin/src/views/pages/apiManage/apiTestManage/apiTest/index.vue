<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入API名称"
            v-model="SearchItem.apiName">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable
                    size="medium"
                    placeholder="请输入源API名称"
                    v-model="SearchItem.sourceApiName">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
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
  </el-main>
</template>
<script>
import {
  apiList,hasAuth
} from "@/api/apiTestManage"
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },
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
        {id: false, type: 'imgColor', label: 'API名称', list: 'apiName'},
        {id: false, type: 'color', label: '路由路径', list: 'routeInfo'},
        {id: false, type: 'color', label: '源API名称', list: 'sourceApiName'},
        {id: false, type: 'color', label: '所属提供方', list: 'supplierName'},
        {id: false, type: 'color', label: '更新时间', list: 'updateTime'},
        {id: false, type: 'color', label: '更新人', list: 'userName'}
      ],
      tableOption: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '测试',
          key: 0,
          type: 'text',
          icon: 'el-icon-link',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.testApi(row)
          }
        }, {
          label: '测试记录',
          key: 0,
          type: 'text',
          icon: 'el-icon-s-order',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.testRecord(row)
          }
        }]
      },
      SearchItem: {
        apiName: '',
        sourceApiName: ''
      },
      lastItem: {
        apiName: '',
        sourceApiName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created(){
    this.findPermission()
    this.fetchData()
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      const that = this
      let props = {
        testApi: function (value) {
          that.tableOption.options[0].permission = JSON.parse(JSON.stringify(value))
        },
        testRecord: function (value) {
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
    // 重置
    reset() {
      const that = this
      that.SearchItem.apiName = ''
      that.SearchItem.sourceApiName = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await apiList(data)
        that.isSubmitLoading = false
        if(res.data.code === 1){
          console.log(res.data.data.content)
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

    //测试
    async testApi(row){
      const that = this
      try {
        let data = {
          apiId : row.id
        }
        const res = await hasAuth(data)
        if(res.data.code === 1){
          if (res.data.data === true){
            that.$router.push({
              path: '/apiManage/apiTest/testPage',
              query: {
                id: row.id,
                name: row.name,
                sourceApiId:row.sourceApiId,
                type:'测试'
              }
            })
          }else {
            this.$message.error("该API未授权,请先授权")
          }
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }

    },
    //测试记录
    testRecord(data){
      const that = this
      that.$router.push({
        path: '/apiManage/apiTest/testList',
        query: {
          id: data.id,
          type:'测试记录'
        }
      })
    }
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

</style>
