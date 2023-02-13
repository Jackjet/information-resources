<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入节点名称" prefix-icon="el-icon-search" v-model="SearchItem.nodeName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="valueTime" size="medium" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="日志开始日期" end-placeholder="日志结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size='medium' @click="add" icon="el-icon-add">设置预警</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <environmentAdd :modal-objjj='title' ref="add">
    </environmentAdd>
  </el-main>
</template>

<script>
import { getList } from '@/api/monitoringView/http'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import environmentAdd from './environmentAdd'
export default {
  components: { TableList, Pagination, environmentAdd },
  data() {
    return {
      valueTime: '',
      lastTime: '',
      tableData: [],
      title: '预警通知',
      tableHeader: [
        {
          id: false, type: '', label: '日志记录时间', list: 'createTime'
        },
        {
          id: false, type: '', label: '节点名称', list: 'nodeName'
        },
        {
          id: false, type: '', label: '状态', list: 'status'
        },
        {
          id: false, type: '', label: '网络', list: 'network'
        },
        {
          id: false, type: 'html', label: '数据库', list: 'db', html: (row) => { return '正常' }
        },
        {
          id: false, type: 'html', label: '系统软件', list: 'systemSoftware', html: (row) => { return '正常' }
        }
      ],
      tableOpction: {},
      isSubmitLoading: false,
      SearchItem: {
        nodeName: '',
        beginTime: '',
        endTime: ''
      },
      lastItem: {
        nodeName: '',
        beginTime: '',
        endTime: ''
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
        nodeName: this.SearchItem.nodeName,
        beginTime: this.SearchItem.beginTime,
        endTime: this.SearchItem.endTime
      }
      try {
        const res = await getList(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    add: function () {
      this.$refs.add.initial('environment')
    },
    SearchNoteList() {
      let that = this
      if (that.valueTime) {
        that.SearchItem.beginTime = that.valueTime[0] + ' 00:00:00'
        that.SearchItem.endTime = that.valueTime[1] + ' 23:59:59'
      } else {
        that.SearchItem.beginTime = ''
        that.SearchItem.endTime = ''
      }
      this.currentPage = 1
      Object.entries(that.SearchItem).map(item => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      this.lastTime = this.valueTime
      this.getList()
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(this.SearchItem).map((item) => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.valueTime = this.lastTime
      this.getList()
    }
  }
}
</script>
