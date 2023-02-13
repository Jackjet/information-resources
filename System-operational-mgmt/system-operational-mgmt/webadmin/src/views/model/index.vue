<template>
  <el-main>
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入访客"
            prefix-icon="el-icon-search"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入访客手机号"
            prefix-icon="el-icon-search"
            v-model="SearchItem.phone">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable
            size="medium"
            width='50'
            placeholder="请输入被访人"
            prefix-icon="el-icon-search"
            v-model="SearchItem.userName">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="SearchItem.Timeframe"
            size="medium"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
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
      ref="call"
      :drawer-dialog='mode'
      @Reload='initial'>
    </drawer>
  </el-main>
  
</template>

<script>
import { getList } from '@/api/knowledgeBase/model'
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
      tableHeader: [{id: false, type: '', label: '模型名称', list: 'name'},
        {id: false, type: '', label: '模型类型', list: 'type'},
        {id: false, type: '', label: '创建时间', list: 'createTime'},
        {id: false, type: '', label: '备注', list: 'remark'}
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
            // this.detail(row)
          }
        }
        ]
      },
      isSubmitLoading: false,
      DeletelistiD: [],
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
      RootUrl: '',
      mode: {
        type: 'drawer',
        form: [{
          type: 'input',
          label: '模型名称:',
          placeholder: '请输入模型名称',
          model: 'value',
          state: ['detail', 'new', 'modify'],
          rules: [{
            required: true, message: '请输入模型名称', trigger: 'blur'
          }]
        },
        {
          type: 'select',
          label: '模型类型:',
          placeholder: '请输入模型名称',
          model: 'value',
          option:[
            {
              value: '选项1',
              label: '黄金糕'
            }, {
              value: '选项2',
              label: '双皮奶'
            }
          ],
          state: ['detail', 'new', 'modify'],
          rules: [{
            required: true, message: '请输入模型名称', trigger: 'blur'
          }]
        }],

        event: [{
          bind: 'increase',
          method: (row) => {
            this.increase(row)
          }
        },
        {
          bind: 'modify',
          method: (row) => {
            console.log('aaaaaaaaa')
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
        size: this.pageSize
      }
      try {
        const res = await getList(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    edit (row) {
      this.$refs.call.popup('商品编辑', 'modify', row)
    },
    // detail (row) {
    //   this.$refs.call.popup('商品详情', 'details', row)
    // },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },
    // 删除当前单条数据 重载列表
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.areaIncome.deleteById + '?id=' + row.id
        that.Loading = true
        const response = await this.request.dataDelete(that, url, {})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          that.initial()
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
