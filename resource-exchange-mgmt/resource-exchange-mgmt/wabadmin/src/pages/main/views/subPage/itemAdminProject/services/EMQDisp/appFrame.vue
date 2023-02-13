<template>
  <el-main>
    <el-col :span='12'>
      <el-form ref="form" label-width="155px">
        <el-form-item label="数据消息队列链接地址">
          <el-input
            disabled
            size="medium"
            autocomplete="off"
            v-model="PortUrl">
          </el-input>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.appid"
            placeholder="请输入开发者id">
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
            icon="el-icon-sort"
            @click="instep">同步开发者
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
        <el-form-item label="开发者id" prop="appid">
          <el-input autocomplete="off" v-model="form.appid"></el-input>
        </el-form-item>
        <el-form-item label="开发者密钥" prop="appkey">
          <el-input autocomplete="off" v-model="form.appkey"></el-input>
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
import * as session from '../../../../../../../assets/js/common'

export default {
  components: { tableForm, paging, dialogForm },

  data () {
    return {
      id: '',
      title: '添加',
      isShow: false,
      form: {
        appid: '',
        appkey: '',
        remark: ''
      },
      rules: {
        appid: [
          {required: true, message: '请输入开发者id', trigger: 'blur'}
        ],
        appkey: [
          {required: true, message: '请输入开发者密钥', trigger: 'blur'}
        ]
      },
      PortUrl: '',
      tableData: [],
      SearchItem: {
        appid: ''
      },
      lastSearch: {
        appid: ''
      },
      tableSelection: {},
      tableHeader: [
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
        options: [
          {
            label: '删除',
            key: 0,
            type: 'text',
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

  methods: {
    initial (id) {
      const that = this
      that.id = id
      that.getList()
    },
    add () {
      this.isShow = true
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
    // 保存
    saveDialog () {
      const that = this
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          try {
            let url = that.Interface.emqUser.insert
            let obj = {
              sysServiceId: this.id,
              appid: that.form.appid,
              appkey: that.form.appkey,
              remark: that.form.remark,
              integrationId: session.default.session('currentUser').id
            }
            let response = await this.request.dataPost(that, url, obj)
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
    // 同步
    async instep () {
      const that = this
      that.$confirm('是否同步?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.emqUser.syncEmqUser
        let obj = {
          sysServiceId: that.id,
          integrationId: session.default.session('currentUser').id
        }
        const response = await this.request.dataPut(that, url, obj)
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
    // 列表
    async getList () {
      const that = this
      try {
        let findUrl = this.Interface.emqUser.findAll
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        let obj = {
          sysServiceId: this.id,
          appid: this.SearchItem.appid,
          integrationId: session.default.session('currentUser').id
        }
        const response = await this.request.dataGet(that, url, obj)
        that.tableData = response.data.data.content
        that.total = response.data.data.totalElements
        that.getUrl()
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    async getUrl () {
      const that = this
      try {
        let findUrl = that.Interface.sysService.findById
        let parameter = {id: that.id}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          that.PortUrl = response.data.data.url
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },

    // 删除
    delete (row) {
      const that = this
      that.$confirm('是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.emqUser.deleteById + '?id=' + row.id
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
