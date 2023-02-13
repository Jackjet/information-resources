<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="模板名称">
          <el-input clearable size="medium" placeholder="请输入数据模板名称" v-model="searchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="search" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
          <el-button size='medium' @click="handleAdd" icon="el-icon-plus">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <!--<div style="padding-top: 20px;">
        <el-button type="primary" v-if="permissions.add" plain size='medium' @click="handleAdd" icon="el-icon-plus">新增</el-button>
      </div>-->
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { ruleTemplateFindAll, ruleTemplateUpdateStatus, ruleTemplateDelete } from "@/api/ruleTemplate.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'

export default {
  components: { TableList, Pagination },
  data() {
    return {
      permissions: {
        add: true,
      },
      time: [],
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [
        { label: '模板名称', list: 'name', width: '180', textAlign: "left" },
        {
          type: 'html',
          label: '模板类型',
          list: 'type',
          width: '80',
          textAlign: "left",
          code: (row) => {
            if (row.type === 1) {
              return '<span style="color: #000000!important; font-size: 13px;">自定义</span>'
            } else {
              return '<span style="color: #C0C4CC!important; font-size: 13px;">内置</span>'
            }
          }
        },
        { label: '模板描述', list: 'description', textAlign: "left" },
        { label: '引用次数', list: 'useCount', width: '180', textAlign: "left" },
        {
          type: 'html',
          label: '状态',
          list: 'status',
          width: '80',
          textAlign: "center",
          code: (row) => {
            if (row.status === 1) {
              return '<span style="color: #70B603!important; font-size: 13px;">启用</span>'
            } else {
              return '<span style="color: #D70C0C!important; font-size: 13px;">禁用</span>'
            }
          }
        },
      ],
      tableOpction: {
        label: '操作',
        width: '220px',
        value: 0,
        options: [{
          label: '禁用',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: true,
          method: (row) => {
            this.handleEnable(row.id, 0)
          },
          show: (row) => {
            return row.type !== 0 && row.status === 1
          }
        }, {
          label: '启用',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: true,
          method: (row) => {
            this.handleEnable(row.id, 1)
          },
          show: (row) => {
            return row.type !== 0 && row.status === 0
          }
        }, {
          label: '编辑',
          key: 0,
          type: 'text',
          icon: 'el-icon-edit-outline',
          State: true,
          method: (row) => {
            this.handleEdit(row)
          },
          /*show: (row) => {
            console.log(row)
            return row.type !== 0
          },*/
        }, {
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          State: true,
          method: (row) => {
            this.handleDelete(row.id)
          },
          /*show: (row) => {
            return row.type !== 0
          },*/
        }]
      },
      searchItem: {
        name: '',
      },
      lastItem: {
        name: '',
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
    permissionsArr.forEach(item => {
      let itemArr = item.split('_')
      if (('/' + itemArr[0]) === this.$route.path) {
        this.permissions[itemArr[1]] = true
      }
    })
    this.fetchData()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.searchItem.name = ''
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.searchItem
      data.page = this.currentPage
      data.size = this.pageSize
      if (type !== 'page') {
        if (that.time !== null && that.time !== '' && that.time.length > 0) {
          data.startTime = this.time[0]
          data.endTime = this.time[1]
        } else {
          data.startTime = ''
          data.endTime = ''
        }
      }
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await ruleTemplateFindAll(data)
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
    search() {
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
      Object.entries(that.searchItem).map((item, index) => {
        that.searchItem[item[0]] = that.lastItem[item[0]]
      })
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      this.fetchData('page')
    },
    // 编辑
    handleEdit(data) {
      const that = this
      that.$router.push({
        path: '/ruleTemplateEdit',
        query: {
          id: data.id,
          name: data.name,
          description: data.description,
          type: 'edit'
        }
      })
    },
    // 添加
    handleAdd(data) {
      const that = this
      that.$router.push({
        path: '/ruleTemplateAdd',
        query: {
          type: 'add'
        }
      })
    },
    // 删除当前数据 重载列表
    async handleDelete(id) {
      const that = this
      that.$confirm('删除模板数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await ruleTemplateDelete(id)
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
    // 启用 重载列表
    async handleEnable(id, status) {
      const that = this
      let tips = status === 0 ? "禁用" : "启用"

      let data = {
        id: id,
        status: status
      }
      that.$confirm('请确认是否' + tips + '?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await ruleTemplateUpdateStatus(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success(tips + '成功')
          that.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

  }
}
</script>
