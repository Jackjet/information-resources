<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入申请人姓名" prefix-icon="el-icon-search" v-model="SearchItem.applicant">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="valueTime" size="medium" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-select style="width: 100%;" clearable size="medium" v-model="SearchItem.status" placeholder="请选择状态">
            <el-option label="待审批" value="stay"></el-option>
            <el-option label="已驳回" value="reject"></el-option>
            <el-option label="已同意" value="agree"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>

<script>
import { getAppoveList, updateAppove } from '@/api/omManger/appove'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      drawer: false,
      valueTime: '',
      tableData: [],
      tableHeader: [{
        id: false, type: '', label: '申请人姓名', list: 'applicant', width: '100'
      }, {
        id: false, type: '', label: '手机号', list: 'tel'
      }, {
        id: false, type: '', label: '单位', list: 'organization', width: '100'
      }, {
        id: false, type: '', label: '申请类型', list: 'type', width: '90'
      }, {
        id: false, type: '', label: '申请时间', list: 'createTime'
      }, {
        id: false, type: '', label: '申请内容', list: 'content'
      }, {
        id: false, type: '', label: '状态', list: 'status', width: '90'
      },
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 1,
        options: [{
          label: '同意',
          key: 0,
          type: 'success',
          State: false,
          method: (row) => {
            this.agree(row, 2)
          }
        }, {
          label: '驳回',
          key: 1,
          type: 'danger',
          State: false,
          method: (row) => {
            this.agree(row, 1)
          }
        }]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObj: '',
      SearchItem: {
        applicant: '',
        status: '',
        startTime: '',
        endTime: ''
      },
      lastItem: {
        applicant: '',
        startTime: '',
        status: '',
        endTime: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        applicant: this.SearchItem.applicant,
        status: this.SearchItem.status,
        startTime: this.SearchItem.startTime,
        endTime: this.SearchItem.endTime
      }
      try {
        const res = await getAppoveList(data)
        res.data.data.content.map(function (v) {
          if (v.type === 'sql') {
            v.type = '远程SQL'
          }
          if (v.type === 'control') {
            v.type = '远程控制'
          }
          if (v.status === 'stay') {
            v.status = '待审批'
          }
          if (v.status === 'reject') {
            v.status = '已驳回'
          }
          if (v.status === 'agree') {
            v.status = '已同意'
          }
        })
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    agree(row, type) {
      const that = this
      that.$confirm('此操作' + (type === 2 ? '同意' : '驳回') + '审批, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let status = ""
        if (type === 1) {
          status = 'reject'
        } else if (type === 2) {
          status = 'agree'
        }
        let data = {
          id: row.id,
          status: status
        }
        that.Loading = true
        const response = await updateAppove(data)
        that.Loading = false
        if (response.data.code === 1) {
          // that.$message.success(type === 2 ? '同意执行指令申请' : '驳回执行指令申请')
          that.$message.success('执行成功！')
          that.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

    SearchNoteList() {
      let that = this
      if (that.valueTime) {
        that.SearchItem.startTime = that.valueTime[0] + ' 00:00:00'
        that.SearchItem.endTime = that.valueTime[1] + ' 23:59:59'
      } else {
        that.SearchItem.startTime = ''
        that.SearchItem.endTime = ''
      }
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.fetchData()
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.fetchData()
    },
  }
}
</script>
<style lang="scss" scoped>
.el-input__inner {
  height: 36px;
  line-height: 36px;
}
</style>