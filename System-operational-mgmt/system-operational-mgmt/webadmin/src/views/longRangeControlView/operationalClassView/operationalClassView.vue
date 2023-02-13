<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入类型名称" prefix-icon="el-icon-search" v-model="SearchItem.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size='medium' @click="add" icon="el-icon-add">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <operationalClassAdd
      :modal-objjj='title'
      @getList="getList"
      ref="add">
    </operationalClassAdd>
  </el-main>
</template>

<script>
import { commandTypeFindAll, commandTypeDelete } from '@/api/longRangeControlView/http'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import operationalClassAdd from './operationalClassAdd'
export default {
  components: { TableList, Pagination, operationalClassAdd },
  data() {
    return {
      tableData: [],
      title: '添加',
      tableHeader: [
        {
          id: false, type: '', label: '类型名称', list: 'name'
        },
        {
          id: false, type: '', label: '备注', list: 'remark'
        },
        {
          id: false, type: '', label: '权限', list: 'roleNames', width: '500'
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
        name: ''
      },
      lastItem: {
        name: ''
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
        name: this.SearchItem.name
      }
      try {
        const res = await commandTypeFindAll(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
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
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.getList()
    },
    deleteData(row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        const response = await commandTypeDelete({ id: row.id })
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
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.getList()
    }
  }
}
</script>
