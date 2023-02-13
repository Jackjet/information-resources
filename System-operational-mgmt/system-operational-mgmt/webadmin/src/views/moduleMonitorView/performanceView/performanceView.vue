<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-button type="primary" size='medium' @click="add" icon="el-icon-add">添加监控</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <performanceAdd
      :modal-objjj='title'
      @getList="getList"
      ref="add">
    </performanceAdd>
  </el-main>
</template>

<script>
import { findAll, deleteData } from '@/api/moduleMonitorView/http'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import performanceAdd from './performanceAdd'
export default {
  components: { TableList, Pagination, performanceAdd },
  data() {
    return {
      tableData: [],
      title: '添加',
      tableHeader: [
        {
          id: false, type: '', label: '用户访问次数阈值/日', list: 'access'
        },
        {
          id: false, type: '', label: '告警方式', list: 'alarmWay'
        },
        {
          id: false, type: '', label: '通知人', list: 'peopleNotified'
        }
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [
          {
            label: '编辑',
            key: 0,
            type: 'success',
            State: false,
            method: (row) => {
              this.edit(row)
            }
          },
          {
            label: '删除',
            key: 0,
            type: 'danger',
            State: false,
            method: (row) => {
              this.deleteData(row)
            }
          }
        ]
      },
      isSubmitLoading: false,
      SearchItem: {
        nodeName: '',
        sysName: ''
      },
      lastItem: {
        nodeName: '',
        sysName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        type: 'performance',
        nodeName: this.SearchItem.nodeName,
        sysName: this.SearchItem.sysName
      }
      try {
        const res = await findAll(data)
        res.data.data.content.forEach(item => {
          switch (item.alarmWay) {
            case 'sms':
              item.alarmWay = '短信'
              break
            case 'email':
              item.alarmWay = '邮件'
              break
            case 'account':
              item.alarmWay = '锁定账号'
              break
          }
        })
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even)
      }
    },
    add: function() {
      this.title = '添加'
      this.$refs.add.initial('')
    },
    edit: function(row) {
      this.title = '编辑'
      this.$refs.add.initial(row.id)
    },
    SearchNoteList () {
      let that = this
      Object.entries(that.SearchItem).map(item => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      this.currentPage = 1
      this.getList()
    },
    deleteData(row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        const response = await deleteData({ id: row.id })
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.getList()
    }
  }
}
</script>
