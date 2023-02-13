<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item  label="姓名">
          <el-input clearable
            size="medium"
            placeholder="请输入姓名"
            v-model="SearchItem.userName">
          </el-input>
        </el-form-item>
        <el-form-item  label="账号">
          <el-input clearable
            size="medium"
            placeholder="请输入账号"
            v-model="SearchItem.userAccount">
          </el-input>
        </el-form-item>
        <el-form-item  label="手机号">
          <el-input clearable
            size="medium"
            placeholder="请输入手机号"
            v-model="SearchItem.userPhone">
          </el-input>
        </el-form-item>
        <el-form-item  label="组织机构">
          <el-input clearable
            size="medium"
            placeholder="请输入组织机构"
            v-model="SearchItem.organizationName">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="SearchNoteList" style="color: #5677DF" icon="el-icon-search">查询</el-button>
          <el-button  size='medium' @click="reset" icon="el-icon-refresh-left" style="color: #5677DF">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center"> 
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
import {
  findRoleId
} from "@/api/role.js";
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '姓名', list: 'userName'},
        {id: false, type: '', label: '账号', list: 'userAccount'},
        {id: false, type: '', label: '手机号', list: 'userPhone'},
        {id: false, type: '', label: '组织机构', list: 'organizationName'},
      ],
      tableOpction: {

      },
      SearchItem: {
        userName: '',
        userAccount: '',
        userPhone: '',
        organizationName: ''
      },
      lastItem: {
        userName: '',
        userAccount: '',
        userPhone: '',
        organizationName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
    }
  },
  created () {
    this.fetchData()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.SearchItem.userName = ''
      that.SearchItem.userAccount = ''
      that.SearchItem.userPhone = ''
      that.SearchItem.organizationName = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData() {
      const that = this
      let data = {}
      data = this.SearchItem,
      data.roleId = this.$route.query.id
      data.page = this.currentPage,
      data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        const res = await findRoleId(data)
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
      this.fetchData()
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
  }
}
</script>