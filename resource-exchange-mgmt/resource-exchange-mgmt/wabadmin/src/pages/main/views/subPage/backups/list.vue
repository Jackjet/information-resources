<template>
  <el-main>
    <el-col :span="24" v-loading="loading">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.name"
            placeholder="请输入备份名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-search"
            @click="search">查询
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!-- 调用表格 -->
    <el-col :span="24">
      <tableForm :table-data='tableData'
                 :table-selection="tableSelection"
                 :table-label="tableHeader"
                 :table-option="tableOption">
      </tableForm>
    </el-col>
    <el-col :span='24'>
      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
    </el-col>
  </el-main>
</template>

<script>
import tableForm from '../../integration/tableList'
import paging from '../../integration/pagination'

export default {
  components: {tableForm, paging},
  data () {
    return {
      loading: false,
      tableData: [],
      SearchItem: {
        name: ''
      },
      lastSearch: {
        name: ''
      },
      tableSelection: {},
      tableHeader: [
        { id: false, type: '', label: '备份名称', list: 'name' },
        { id: false, type: '', label: '备份时间', list: 'createTime' }
      ],
      tableOption: {
        label: '操作',
        value: 0,
        options: [
          {
            label: '下载',
            key: 0,
            icon: 'el-icon-document',
            State: false,
            method: (row) => {
              this.download(row)
            }
          }, {
            label: '删除',
            key: 0,
            icon: 'el-icon-document',
            State: false,
            method: (row) => {
              this.delete(row)
            }
          }
        ]
      },
      total: 0,
      pageSize: 20,
      currentPage: 1
    }
  },
  mounted () {
    this.getList()
  },
  methods: {
    // 列表
    async getList () {
      const that = this
      try {
        let findUrl = this.Interface.backups.find
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
        })
        let obj = {
          name: that.SearchItem.name
        }
        let response = await this.request.dataGet(that, url, obj)
        let theData = response.data.data
        that.tableData = theData.content
        that.total = theData.totalElements
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    download (row) {
      window.open(row.url, '_blank')
    },
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.backups.delete
        that.loading = true
        const response = await this.request.dataDelete(that, url, {id: row.di})
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          that.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        that.loading = false
        return false
      })
    },
    // 查询
    search: function () {
      const that = this
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastSearch[item[0]] = that.SearchItem[item[0]]
      })
      that.currentPage = 1
      this.$refs.pager.changePage()
      this.getList()
    },
    // 分页
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.getList()
    }
  }
}
</script>
