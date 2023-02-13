<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入计划名称" prefix-icon="el-icon-search" v-model="SearchItem.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入接口名称" prefix-icon="el-icon-search" v-model="SearchItem.apiName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.status" placeholder="请选择状态">
            <el-option label="已启动" value="started"></el-option>
            <el-option label="已停止" value="stopped"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size='medium' @click="add" icon="el-icon-add">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' @edit="edit" @delete="deleteData" @start="start" @stop="stop" v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <dataServicePlanAdd
      :modal-objjj='title'
      @getList="getList"
      ref="add">
    </dataServicePlanAdd>
  </el-main>
</template>

<script>
import { dataApiPlanList, dataApiPlanDelete, dataApiPlanStop, dataApiPlanStart } from '@/api/dataProcessingService/http'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import dataServicePlanAdd from './dataServicePlanAdd'
export default {
  components: { TableList, Pagination, dataServicePlanAdd },
  data() {
    return {
      tableData: [],
      title: '添加',
      tableHeader: [
        {
          id: false, type: '', label: '计划名称', list: 'name', width: '100'
        },
        {
          id: false, type: '', label: 'Cron表达式', list: 'cron'
        },
        {
          id: false, type: '', label: '接口名称', list: 'apiName'
        },
        {
          id: false, type: '', label: '状态', list: 'status', width: '100'
        },
        {
          id: false, type: '', label: '备注', list: 'remark'
        },
        {
          id: false, type: 'operation', label: '操作', list: 'operation'
        }
      ],
      tableOpction: {},
      isSubmitLoading: false,
      SearchItem: {
        name: '',
        apiName: '',
        status: ''
      },
      lastItem: {
        name: '',
        apiName: '',
        status: ''
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
        name: this.SearchItem.name,
        apiName: this.SearchItem.apiName,
        status: this.SearchItem.status
      }
      try {
        const res = await dataApiPlanList(data)
        res.data.data.content.forEach(item => {
          item.operation = [
            {
              label: '编辑',
              type: 'success',
              State: false,
              click: 'edit'
            },
            {
              label: '删除',
              type: 'danger',
              State: false,
              click: 'delete'
            }
          ]
          if (item.status === 'started') {
            item.status = '已启动'
            item.operation.push({
              label: '停止',
              type: 'danger',
              State: false,
              click: 'stop'
            })
          }
          if (item.status === 'stopped') {
            item.status = '已停止'
            item.operation.push({
              label: '启动',
              type: 'success',
              State: false,
              click: 'start'
            })
          }
        })
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    async stop (row) {
      const that = this
      that.$confirm('确定停止吗?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        const response = await dataApiPlanStop({id: row.id})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          this.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    async start (row) {
      const that = this
      that.$confirm('确定启动吗?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        const response = await dataApiPlanStart({id: row.id})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          this.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
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
        const response = await dataApiPlanDelete({ id: row.id })
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
