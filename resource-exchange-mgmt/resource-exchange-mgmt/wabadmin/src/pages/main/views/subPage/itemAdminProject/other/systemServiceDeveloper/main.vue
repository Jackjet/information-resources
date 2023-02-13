<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.name"
            placeholder="请输入开发者名称">
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

    <!-- 调用操作弹层 -->
    <drawer
      ref="callDetail"
      @Reload = 'getList'>
    </drawer>
  </el-main>
</template>

<script>
import tableForm from '../../../../integration/tableList'
import paging from '../../../../integration/pagination'
import * as session from '../../../../../../../assets/js/common'
import drawer from './add'

export default {
  components: { tableForm, paging, drawer },

  data () {
    return {
      id: '',
      title: '添加',
      isShow: false,
      editIsShow: false,
      form: {
        appid: '',
        appkey: '',
        remark: ''
      },
      PortUrl: '',
      tableData: [],
      SearchItem: {
        name: '',
        integrationId: ''
      },
      lastSearch: {
        appid: ''
      },
      tableSelection: {},
      tableHeader: [
        {
          id: false,
          type: '',
          label: '开发者名称',
          list: 'name'
        },
        {
          id: false,
          type: '',
          label: '开发者id',
          list: 'appid'
        },
        {
          id: false,
          type: '',
          label: '开发者密钥',
          list: 'appkey'
        },
        {
          id: false,
          type: '',
          label: '备注',
          list: 'remark'
        },
        {
          id: false,
          type: '',
          label: '创建时间',
          list: 'createTime'
        }
      ],
      tableOption: {
        label: '操作',
        value: 0,
        width: '140px',
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
            label: '删除',
            key: 0,
            type: 'delete',
            icon: 'el-icon-delete',
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
    initial (id) {
      const that = this
      that.id = id
      that.getList()
    },
    add: function () {
      this.$refs.callDetail.initial()
    },

    edit (row) {
      console.log(row)
      this.$refs.callDetail.initial(row.id)
    },
    closeDialog () {
      this.isShow = false
      this.$refs['form'].resetFields()
      this.form = {
        appid: '',
        appkey: '',
        remark: ''
      }
    },
    closeEditDialog () {
      this.editIsShow = false
      this.$refs['form'].resetFields()
      this.form = {
        appid: '',
        appkey: '',
        remark: ''
      }
    },
    // 列表
    async getList () {
      const that = this
      that.tableData = [
  {
    "id": "0000001l",
    "createById": "admin",
    "createTime": "2021-10-09 16:37:15",
    "updateById": null,
    "updateTime": null,
    "integrationId": "admin",
    "appid": "0000001m",
    "appkey": "54d7e8ab11b440d5b18851aaef577995",
    "name": "发v",
    "remark": ""
  },
  {
    "id": "0000001a",
    "createById": "admin",
    "createTime": "2021-09-22 16:20:30",
    "updateById": null,
    "updateTime": null,
    "integrationId": "admin",
    "appid": "0000001b",
    "appkey": "7b34b8b13bb9427ea6d11c7582c2b181",
    "name": "测试",
    "remark": ""
  }
]
      // try {
      //   let findUrl = this.Interface.sysUser.findAll
      //   let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
      //   let obj = {
      //     name: this.SearchItem.name,
      //     integrationId: session.default.session('currentUser').id
      //   }
      //   const response = await this.request.dataGet(that, url, obj)
      //   that.tableData = response.data.data.content
      //   that.total = response.data.data.totalElements
      // } catch (even) {
      //   that.$message.error('数据获取失败')
      // }
    },

    // 删除
    delete (row) {
      const that = this
      that.$confirm('是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.sysUser.deleteById + '?id=' + row.id
        const response = await this.request.dataDelete(that, url, {})
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

    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.getList()
    }
  }
}
</script>
