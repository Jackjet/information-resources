<template>
  <el-main>
    <el-col :span='12'>
      <el-form ref="form" label-width="155px" v-show="isTableShow">
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
    <el-col :span="24" v-show="isTableShow">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.topicName"
            placeholder="请输入主题名称">
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
        <!-- <el-form-item>
          <el-button type="primary"
            size='medium'
            icon="el-icon-setting"
            @click="incident">生成文档
          </el-button>
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary"
                     size='medium'
                     icon="el-icon-plus"
                     @click="add">添加
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     size='medium'
                     icon="el-icon-s-promotion"
                     @click="instep">发布
          </el-button>
          <el-tooltip class="item" effect="dark" content="Topic 配置发布是指发布有“访问控制”配置的Topic。发布时如果Topic列表中无一有访问控制配置，则会提示发布失败！" placement="top-start">
            <i class="el-icon-warning-outline"></i>
          </el-tooltip>
        </el-form-item>
      </el-form>
    </el-col>
    <!-- 调用表格 -->
    <el-col :span="24" v-show="isTableShow">
      <tableForm :table-data='tableData'
                 :table-selection="tableSelection"
                 :table-label="tableHeader"
                 :table-option="tableOption">
      </tableForm>
    </el-col>
    <el-col :span='24' v-show="isTableShow">
      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
    </el-col>
    <!-- 访问控制 -->
    <accessControl v-show="!isTableShow" ref="accessControl" @tabTablt="tabTablt"></accessControl>
    <!-- 添加用户方式 -->
    <dialog-form :isShow="isShow" :title="title" @closeDialog="closeDialog" @saveDialog="saveDialog">
      <el-form slot="form" ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="Topic名称" prop="topicName">
          <el-input autocomplete="off" v-model="form.topicName"></el-input>
        </el-form-item>
        <el-form-item label="Topic描述">
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
import accessControl from './accessControl'

export default {
  components: { tableForm, paging, dialogForm, accessControl },

  data () {
    return {
      id: '',
      isTableShow: true,
      title: '添加',
      isShow: false,
      form: {
        topicName: '',
        remark: ''
      },
      rules: {
        topicName: [
          {required: true, message: '请输入Topic名称', trigger: 'blur'}
        ]
      },
      PortUrl: '',
      tableData: [],
      SearchItem: {
        topicName: ''
      },
      lastSearch: {
        topicName: ''
      },
      tableSelection: {},
      tableHeader: [
        {
          id: false,
          type: '',
          label: 'Topic名称',
          list: 'topicName'
        },
        {
          id: false,
          type: '',
          label: 'Topic描述',
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
        width: '160px',
        options: [
          {
            label: '删除',
            key: 0,
            type: 'delete',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.delete(row)
            }
          },
          {
            label: '访问控制',
            key: 0,
            type: 'accessControl',
            icon: 'el-icon-setting',
            State: false,
            method: (row) => {
              this.isTableShow = false
              this.$refs.accessControl.initial(row.id, this.id)
            }
          }
        ]
      },
      total: 0,
      pageSize: 20,
      currentPage: 1,
      isOnline: ''
    }
  },

  methods: {
    // 切换表格
    tabTablt () {
      this.isTableShow = true
      this.getList()
    },
    // 初始化
    initial (id, isOnline) {
      this.id = id
      this.isTableShow = true
      this.isOnline = isOnline
      this.getList()
    },
    add () {
      this.isShow = true
    },
    closeDialog () {
      this.isShow = false
      this.$refs['form'].resetFields()
      this.form = {
        topicName: '',
        remark: ''
      }
    },
    // 保存
    saveDialog () {
      const that = this
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          try {
            let url = that.Interface.topics.insert
            let obj = {
              sysServiceId: this.id,
              topicName: that.form.topicName,
              remark: that.form.remark,
              integrationId: session.default.session('currentUser').id
            }
            let response = await this.request.dataPost(that, url, obj)
            if (response.data.code === 1) {
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
      if (that.isOnline === 'offline') {
        that.$message.error('该系统服务离线，不允许同步Topic')
        return false
      } else if (that.isOnline !== 'online') {
        that.$message.error('该系统服务未安装，不允许同步Topic')
        return false
      }
      that.$confirm('是否发布?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.topics.syncTopicPermission
        let obj = {
          sysServiceId: that.id,
          integrationId: session.default.session('currentUser').id
        }
        const response = await this.request.dataPost(that, url, obj)
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
        let findUrl = this.Interface.topics.findAll
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        let obj = {
          sysServiceId: this.id,
          integrationId: session.default.session('currentUser').id,
          topicName: this.SearchItem.topicName
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
        let url = this.Interface.topics.deleteById + '?id=' + row.id
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
