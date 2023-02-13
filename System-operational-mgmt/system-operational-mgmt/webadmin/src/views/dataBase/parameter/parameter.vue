<template>
  <el-main>
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入参数配置名称"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary"  size='medium' @click="add" icon="el-icon-search">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading"
        :table-label="tableHeader"
        :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
     <drawer
     id="call"
      ref="call"
      :drawer-dialog='mode'
      @Reload='initial'>
    </drawer>
  </el-main>
</template>

<script>
import { parameterConfigList, operationinsertInsert, parameterConfigDelete, parameterConfigUpdate } from '@/api/dataBase/dataBase'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import drawer from '@/components/dialog/dialog.vue'
// import drawer from './detail'
export default {
  components: { TableList, Pagination, drawer},

  data() {
      return {
      drawer: false,
      tableData: [],
      tableHeader: [{id: false, type: '', label: '参数配置名称', list: 'name'},
        {id: false, type: '', label: '配置内容', list: 'content'},
        {id: false, type: '', label: '备注', list: 'remark'},
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
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
            this.delete(row)
          }
        }
        ]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      SearchItem: {
        name: '',
      },
      lastItem: {
        name: '',
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      mode: {
        type: 'drawer',
        form: [{
          type: 'input',
          label: '参数配置名称:',
          placeholder: '请输入参数配置名称',
          model: 'name',
          state: ['detail', 'new', 'modify'],
          rules: [{
            required: true, message: '请输入参数配置名称', trigger: 'blur'
          }]
        }, {
          type: 'textarea',
          ps: true,
          label: '配置内容:',
          placeholder: '请输入配置内容',
          model: 'content',
          state: ['detail', 'new', 'modify'],
        }, {
          type: 'textarea',
          label: '备注:',
          placeholder: '请输入备注',
          model: 'remark',
          state: ['detail', 'new', 'modify'],
        }
       ],
        event: [{
          bind: 'increase',
          method: (row) => {
            this.increase(row)
          }
        },
        {
          bind: 'modify',
          method: (row) => {
            this.bindModify(row)
          }
        }]
      }
    }
  },
  created () {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      const data = {
        page: this.currentPage,
				size: this.pageSize,
				name: this.SearchItem.name
      }
      try {
        const res = await parameterConfigList(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    edit(row) {
      this.$refs.call.popup('编辑', 'modify', row)
    },
    add(row) {
      this.$refs.call.popup('添加', 'increase', '')
    },
		SearchNoteList() {
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
			this.fetchData()
		},
		async increase(row) {
			let that = this 
      const data = {
        name: row.data.name,
				content: row.data.content,
				remark: row.data.remark
      }
      try {
        const res = await operationinsertInsert(data)
				if(res.data.code === 1){
					that.$message.success('添加成功')
					that.$refs.call.closeDialog()
          that.fetchData()
          return false
				}
        that.$message.error(res.data.msg)
      } catch (even) {
        that.$message.error(even.msg)
      }
		},
		async bindModify(row) {
			let that = this
      const data = {
				id:row.data.id,
        name: row.data.name,
				content: row.data.content,
				remark: row.data.remark
      }
      try {
        const res = await parameterConfigUpdate(data)
				if(res.data.code === 1){
					that.$message.success('编辑成功')
					that.$refs.call.closeDialog()
          that.fetchData()
				} else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
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
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async() => {
        let data = {
          id:  row.id
        }
        that.Loading = true
        const response = await parameterConfigDelete(data)
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
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

<style>
#call .el-form-item__label{
  padding: 0 20px 0 0;
}
</style>