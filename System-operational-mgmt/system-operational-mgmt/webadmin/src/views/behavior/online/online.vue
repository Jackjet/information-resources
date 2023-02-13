<template>
  <el-main>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
     <drawer :modal-obj='modalObj' ref="callDetail" @Reload="SearchNoteList"></drawer>
  </el-main>
</template>

<script>
import { online } from '@/api/behavior/loginInfo'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
// import drawer from '@/components/dialog/dialog.vue'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      drawer: false,
      tableData: [],
      tableHeader: [{
        id: false, type: '', label: '用户', list: 'name'
      }, {
        id: false, type: '', label: '首次登录时间', list: 'firstLoginTime'
      }, {
        id: false, type: '', label: '最近登录时间', list: 'loginTime'
      }],
      tableOpction: {},
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObj: '',
      SearchItem: {
        name: '',
        type: ''
      },
      lastItem: {
        name: '',
        type: ''
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
        name: this.SearchItem.name,
        type: this.SearchItem.type
      }
      try {
        const res = await online(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    edit: function(row) {
      this.modalObj = '编辑'
      this.$refs.callDetail.initial(row.id)
    },

    add: function() {
      this.modalObj = '添加'
      this.$refs.callDetail.initial(false)
    },
    SearchNoteList(){
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

    // 删除当前单条数据 重载列表
    delete(row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        const response = await deModel(row.id)
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
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
