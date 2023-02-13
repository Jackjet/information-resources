<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="接收时间">
          <el-date-picker size="medium" v-model="time" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="消息主题">
          <el-input clearable size="medium" placeholder="请输入消息主题" v-model="SearchItem.messageTitle">
          </el-input>
        </el-form-item>
        <el-form-item label="来源">
          <el-input clearable size="medium" placeholder="请输入来源" v-model="SearchItem.messageModule">
          </el-input>
        </el-form-item>
        <el-form-item label="是否已读">
          <el-select size="medium" clearable v-model="SearchItem.status" placeholder="请选择是否已读">
            <el-option key="0" label="否" value="0">
            </el-option>
            <el-option key="1" label="是" value="1">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-permission="allRead" plain size='medium' @click="allStatusAll" icon="el-icon-bottom-left">全部已读</el-button>
        <el-button type="primary" v-permission="batchRead" plain size='medium' @click="status" icon="el-icon-top-right">批量已读</el-button>
        <el-button type="primary" v-permission="batchDelete" plain size='medium' @click="handleDelete" icon="el-icon-delete">批量删除</el-button>
      </div>
      <TableList :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { userMessageFinall, userMessageDelete, userMessageDeleteAll, updateStatus, updateStatusAll } from "@/api/message.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: 'html',
          label: '状态',
          list: '',
          code: (row) => {
            if (row.status !== 1) {
              return '<div style= "display: flex; justify-content: center; align-items: center;">' +
                '<svg t="1601279073578" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3588" width="20" height="20"><path d="M499.2 92.16l-409.6 230.4v486.4c0 56.32 46.08 102.4 102.4 102.4h614.4c56.32 0 102.4-46.08 102.4-102.4v-486.4l-409.6-230.4z m-358.4 716.8V407.04l232.96 184.32-230.4 230.4c-2.56-2.56-2.56-7.68-2.56-12.8z m40.96 48.64l235.52-235.52 84.48 56.32 84.48-56.32 235.52 235.52h-640z m675.84-48.64c0 5.12 0 10.24-2.56 15.36l-230.4-230.4 232.96-184.32v399.36z m-358.4-189.44l-358.4-273.92 358.4-197.12 358.4 197.12-358.4 273.92z" p-id="3589" fill="#ebb563"></path></svg>' +
                '<span style="cursor: pointer; padding-left: 3px; color: #ebb563" >未读</span>' +
                '</div>'
            } else {
              return '<div style= "display: flex; justify-content: center; align-items: center;">' +
                '<svg t="1601279073578" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3588" width="20" height="20"><path d="M499.2 92.16l-409.6 230.4v486.4c0 56.32 46.08 102.4 102.4 102.4h614.4c56.32 0 102.4-46.08 102.4-102.4v-486.4l-409.6-230.4z m-358.4 716.8V407.04l232.96 184.32-230.4 230.4c-2.56-2.56-2.56-7.68-2.56-12.8z m40.96 48.64l235.52-235.52 84.48 56.32 84.48-56.32 235.52 235.52h-640z m675.84-48.64c0 5.12 0 10.24-2.56 15.36l-230.4-230.4 232.96-184.32v399.36z m-358.4-189.44l-358.4-273.92 358.4-197.12 358.4 197.12-358.4 273.92z" p-id="3589" fill="#a2a2a2"></path></svg>' +
                '<span style="cursor: pointer; padding-left: 3px;" >已读</span>' +
                '</div>'
            }
          }
        },
        {
          type: 'html',
          label: '消息主题',
          list: 'messageTitle',
          code: (row) => {
            return '<div style= "display: flex; justify-content: center; align-items: center;">' +
              '<span style="cursor: pointer; padding-left: 3px;" >' + row.messageTitle + '</span>' +
              '</div>'
          }
        },
        {
          type: 'html',
          label: '来源',
          list: 'messageModule',
          code: (row) => {
            if (row.status !== 1) {
              return '<span>' + row.messageModule + '</span>'
            } else {
              return '<span style="color: #9c9898 !important;">' + row.messageModule + '</span>'
            }
          }
        },
        {
          type: 'html',
          label: '内容',
          list: 'messageContent',
          code: (row) => {
            if (row.status !== 1) {
              return '<span>' + row.messageContent + '</span>'
            } else {
              return '<span style="color: #9c9898 !important;">' + row.messageContent + '</span>'
            }
          }
        },
        {
          type: 'html',
          label: '接收时间',
          list: 'createTime',
          code: (row) => {
            if (row.status !== 1) {
              return '<span>' + row.createTime + '</span>'
            } else {
              return '<span style="color: #9c9898 !important;">' + row.messageModule + '</span>'
            }
          }
        }
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '详情',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: false,
          method: (row) => {
            this.handleDetail(row)
          }
        }, {
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          State: false,
          method: (row) => {
            this.delete(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        messageTitle: '',
        messageModule: '',
        startTime: '',
        endTime: '',
        status: ''
      },
      lastItem: {
        messageTitle: '',
        messageModule: '',
        startTime: '',
        endTime: '',
        status: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.SearchItem.messageTitle = '',
        that.SearchItem.messageModule = '',
        that.SearchItem.startTime = '',
        that.SearchItem.endTime = '',
        that.SearchItem.status = '',
        that.time = []
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
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
        const res = await userMessageFinall(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.map(function (v, k) {
            if (v.messageContent.length > 11) { v.messageContent = v.messageContent.substr(0, 11) + '...' }
          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //全选
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      that.listiRead = []
      vals.map(function (v, k) {
        that.DeletelistiD.push(v.id)
        that.listiRead.push({ id: v.id, status: v.status })
      })
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
    // 详情
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/messageDetail',
        query: {
          id: data.id
        }
      })
    },
    // 删除当前数据 重载列表
    async handleDelete(data) {
      const that = this
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择消息')
      }
      that.$confirm('请确认是否批量删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await userMessageDeleteAll(that.DeletelistiD + '')
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('批量删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 启用 重载列表
    async delete(row) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await userMessageDelete(row.id)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 批量已读
    async status() {
      const that = this
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择消息')
      }
      let num = 0
      that.listiRead.map(function (v, k) {
        if (v.status === 0) {
          num++
        }
      })
      if (num === 0) {
        return that.$message.warning('没有未读消息')
      }
      let data = {
        ids: that.DeletelistiD
      }
      that.$confirm('请确认是否批量已读?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await updateStatus(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('批量已读成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 全部已读
    async allStatusAll(type) {
      const that = this
      let data = {
        ids: that.DeletelistiD
      }
      that.$confirm('请确认是否全部已读?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await updateStatusAll(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('全部已读成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    }
  }
}
</script>
