<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.code"
            placeholder="请输入用户账号">
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
        <el-form-item>
          <el-button type="primary"
            size='medium'
            icon="el-icon-plus"
            @click="add">添加
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
    <!-- 添加 -->
    <dialog-form :isShow="isShow" :title="title" @closeDialog="closeDialog" @saveDialog="saveDialog">
      <el-form slot="form" ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="用户账号" prop="code">
          <el-input autocomplete="off" v-model="form.code" :disabled="id ? true : false"></el-input>
        </el-form-item>
        <el-form-item label="用户appid" v-show="id ? true : false">
          <el-input autocomplete="off" v-model="form.appid" :disabled="id ? true : false"></el-input>
        </el-form-item>
        <el-form-item label="用户appkey" v-show="id ? true : false">
          <el-input autocomplete="off" v-model="form.appkey" :disabled="id ? true : false"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>
      </el-form>
    </dialog-form>
  </el-main>
</template>

<script>
import dialogForm from '../../../../integration/dialogForm'
import tableForm from '../../../../integration/tableList'
import paging from '../../../../integration/pagination'

export default {
  components: { tableForm, paging, dialogForm },

  data () {
    return {
      id: '',
      title: '添加用户',
      isShow: false,
      form: {
        code: '',
        remark: ''
      },
      rules: {
        code: [
          {required: true, message: '请输入用户账号', trigger: 'blur'}
        ]
      },
      PortUrl: this.PortUrl,
      tableData: [],
      SearchItem: {
        name: '',
        code: ''
      },
      lastSearch: {
        name: '',
        code: ''
      },
      tableSelection: {},
      tableHeader: [
        {
          id: false,
          type: '',
          label: '用户账号',
          list: 'code'
        },
        {
          id: false,
          type: '',
          label: '备注',
          list: 'remark'
        }
      ],
      tableOption: {
        label: '操作',
        value: 0,
        width: '150px',
        options: [
          {
            label: '编辑',
            key: 0,
            type: 'edit',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.edit(row)
            }
          },
          {
            label: '重置密码',
            key: 0,
            type: 'reset',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.resetPassword(row)
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
    add () {
      this.title = '添加用户'
      this.isShow = true
    },
    closeDialog () {
      this.isShow = false
      this.id = ''
      this.$refs['form'].resetFields()
      this.form = {
        code: '',
        remark: ''
      }
    },
    saveDialog () {
      const that = this
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          try {
            let url = that.Interface.integrationProjectManage.insert
            let obj = {
              code: that.form.code,
              remark: that.form.remark
            }
            let response = null
            if (that.id) {
              url = that.Interface.integrationProjectManage.update
              obj.id = that.id
              delete obj.code
              response = await this.request.dataPut(that, url, obj)
            } else {
              response = await this.request.dataPost(that, url, obj)
            }
            if (response.data.code === 1) {
              that.isShow = false
              that.$message.success(response.data.msg)
              that.closeDialog()
              that.getList()
              return false
            }
            that.$message.error(response.data.msg)
          } catch (even) {
            that.$message.error('数据获取失败')
          }
        }
      })
    },
    // 编辑
    async edit (row) {
      const that = this
      try {
        let findUrl = that.Interface.integrationProjectManage.findById
        let parameter = {id: row.id}
        const response = await that.request.dataGet(that, findUrl, parameter)
        if (response.data.code === 1) {
          that.title = '编辑用户'
          that.id = row.id
          that.isShow = true
          that.form = response.data.data
          return false
        }
        that.$message.error(response.data.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    // 列表
    async getList () {
      const that = this
      try {
        let findUrl = this.Interface.integrationProjectManage.findAll
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        let obj = {
          code: this.SearchItem.code
        }
        const response = await this.request.dataGet(that, url, obj)
        that.tableData = response.data.data.content
        that.total = response.data.data.totalElements
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    // 重置
    resetPassword (row) {
      const that = this
      that.$confirm('是否重置?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.integrationProjectManage.resetPassword + '?id=' + row.id
        const response = await this.request.dataPut(that, url, {})
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          that.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
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
