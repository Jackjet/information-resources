<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="">
          <el-input clearable size="medium" placeholder="请输入规则名称" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item label="">
          <el-input clearable size="medium" placeholder="请输入数据模板名称" v-model="SearchItem.ruleTemplateName">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" plain size='medium' @click="handleAdd" icon="el-icon-plus">新增</el-button>
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
import { qualityFindAll, updateStatus, verifyRuleDelete } from "@/api/quality.js"
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
      tableData: [],
      tableHeader: [
        { label: '规则名称', list: 'name' },
        {
          type: 'btn',
          label: '数据模板',
          list: 'ruleTemplateName',
          method: (row) => {
            this.modefun(row.ruleTemplateId)
          }
        },
        { label: '规则数据源', list: 'sourceName' },
        { label: '规则实体表', list: 'sourceData' },
        {
          type: 'html',
          label: '规则状态',
          list: 'status',
          code: (row) => {
            if (row.status == '1') {
              return '<span style="color: #70B603!important;">生效</span>'
            } else {
              return '<span style="color: #D70C0C!important;">失效</span>'
            }
          }
        },
        { label: '创建人', list: 'createByName' }
      ],
      tableOpction: {
        label: '操作',
        width: '180px',
        value: 0,
        options: [{
          label: '编辑',
          key: 0,
          type: 'text',
          State: true,
          method: (row) => {
            this.handleEdit(row)
          }
        }, {
          label: '启用',
          key: 0,
          type: 'text',
          // 显示隐藏总控制，条件在方法里面写，返回true或者false
          show: (row) => {
            if (row.status == '0') {
              return true
            } else {
              return false
            }
          },
          method: (row) => {
            this.enable(row)
          }
        }, {
          label: '禁用',
          key: 0,
          show: (row) => {
            if (row.status == '1') {
              return true
            } else {
              return false
            }
          },
          type: 'text',
          method: (row) => {
            this.enable(row)
          }
        }, {
          label: '删除',
          key: 0,
          type: 'text',
          State: true,
          method: (row) => {
            this.handleDelete(row)
          }
        }]
      },
      SearchItem: {
        name: '',
        ruleTemplateName: ''
      },
      lastItem: {
        name: '',
        ruleTemplateName: ''
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
      that.SearchItem.name = '',
        that.SearchItem.ruleTemplateName = ''
      this.fetchData()
    },
    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
      try {
        that.isSubmitLoading = true
        const res = await qualityFindAll(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.map(function (v, k) {
            v.sourceName = JSON.parse(v.metadataData).sourceName
            v.sourceData = JSON.parse(v.metadataData).source.name
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
    // 详情
    modefun(ruleTemplateId) {
      this.$router.push({
        path: '/qualityDetail',
        query: {
          id: ruleTemplateId
        }
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
      this.fetchData('page')
    },
    async enable(row) {
      const that = this
      let tips = ''
      let data = {
        status: row.status == 0 ? 1 : 0,
        id: row.id
      }
      if (row.status) {
        tips = '禁用'
      } else {
        tips = '启用'
      }
      that.$confirm('请确认是否' + tips + '?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await updateStatus(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          if (tips == '禁用') {
            that.$message.success('禁用成功')
            this.fetchData()
          } else {
            that.$message.success('启用成功')
            this.fetchData()
          }
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 修改
    handleEdit(data) {
      const that = this
      that.$router.push({
        path: '/qualityEdit',
        query: {
          data: JSON.stringify(data),
          id: data.id,
          type: '编辑'
        }
      })
    },
    // 新增
    handleAdd() {
      const that = this
      that.$router.push({
        path: '/qualityAdd',
        query: {
          type: '新增'
        }
      })
    },
    // 删除
    handleDelete(row) {
      const that = this
      that.$confirm('删除该规则, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let params = {
          id: row.id
        }
        let response = await verifyRuleDelete(row.id)

        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      })
    }
  }
}
</script>
