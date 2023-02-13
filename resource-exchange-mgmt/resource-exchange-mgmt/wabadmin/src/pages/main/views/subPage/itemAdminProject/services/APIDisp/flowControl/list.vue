<template>
  <el-main v-loading="Loading">
    <el-col :span="24">
      <el-col :span="12">
        <el-form ref="form" label-width="100px">
          <el-form-item label="系统服务url">
            <el-input
              disabled
              size="medium"
              autocomplete="off"
              v-model="sysUrl">
            </el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.name"
            placeholder="请输入策略名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select placeholder="请选择限制类型"
            :popper-append-to-body='false'
            clearable v-model="SearchItem.type"
            size="medium">
            <el-option label="限制IP" value="2"></el-option>
            <el-option label="限制开发者" value="1"></el-option>
          </el-select>
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
    <!-- 调用分页 -->
    <el-col :span='24'>
      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
    </el-col>
    <!-- 调用操作弹层 -->
    <drawer
      ref="callDetail"
      @Reload='submitLoading'>
    </drawer>
  </el-main>
</template>

<script>
import tableForm from '../../../../../integration/tableList'
import paging from '../../../../../integration/pagination'
import drawer from './add'

export default {
  components: { tableForm, paging, drawer },

  data () {
    return {
      integrationId: this.common.session('currentUser').id,
      sysUrl: 'http://47.105.140.86:61613',
      tableData: [],
      SearchItem: {
        name: '',
        type: ''
      },
      lastSearch: {
        name: '',
        type: ''
      },
      tableSelection: {
        key: false,
        type: 'selection'
      },
      tableHeader: [{
        id: false,
        type: '',
        label: '策略名称',
        list: 'name'
      },
      {
        id: false,
        type: '',
        label: '限制类型',
        list: 'type_c'
      },
      {
        id: false,
        type: '',
        label: '限制时长(天)',
        list: 'time'
      },
      {
        id: false,
        type: '',
        label: 'API访问限制次数',
        list: 'apiFlowLimit'
      },
      {
        id: false,
        type: '',
        label: '单个IP/账号流量限制次数',
        list: 'xianzhi'
      },
      {
        id: false,
        type: '',
        label: '创建时间',
        list: 'createTime'
      }],
      tableOption: {
        label: '操作',
        value: 0,
        width: '160px',
        options: [{
          label: '编辑',
          key: 0,
          type: 'edit',
          icon: 'el-icon-edit',
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
        }]
      },
      Loading: false,
      total: 0,
      pageSize: 20,
      currentPage: 1
    }
  },

  methods: {
    initial (appid, url) {
      const that = this
      that.appid = appid
      that.sysUrl = url
      that.askInfo()
    },
    // 初始化获取当前全部列表数据
    async askInfo () {
      const that = this
      try {
        let findUrl = this.Interface.flowStrategy.find
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        let askData = {}
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
          askData[item[0]] = that.lastSearch[item[0]]
        })
        askData.integrationId = that.integrationId
        askData.sysServiceId = that.appid
        that.Loading = true
        const response = await this.request.dataGet(that, url, askData)
        that.Loading = false
        if (response.data.code === 1) {
          let Data = response.data.data
          Data.content.forEach((item, index) => {
            if (item.type === '1') {
              item.type_c = '限制开发者'
              item.xianzhi = item.appIdFlowLimit
            } else if (item.type === '2') {
              item.type_c = '限制IP'
              item.xianzhi = item.ipFlowLimit
            } else {
              item.type_c = '-'
            }
            if (item.apiFlowLimit === -1) {
              item.apiFlowLimit = '无限制'
            }
          })
          that.tableData = response.data.data.content
          that.total = response.data.data.totalElements
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.Loading = false
        that.$message.error('数据获取失败')
      }
    },

    submitLoading: function () {
      this.askInfo()
    },

    add: function () {
      this.$refs.callDetail.initial(this.appid)
    },

    edit (row) {
      this.$refs.callDetail.initial(this.appid, row.id)
    },

    // 删除当前单条数据 重载列表
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.flowStrategy.delete
        that.Loading = true
        const response = await this.request.dataDelete(that, url, {id: row.id})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          that.askInfo()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        that.Loading = false
        return false
      })
    },

    // 模糊查询
    search: function () {
      const that = this
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastSearch[item[0]] = that.SearchItem[item[0]]
      })
      that.currentPage = 1
      this.$refs.pager.changePage()
      this.askInfo()
    },

    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.askInfo()
    }
  }
}
</script>
<style lang="scss">
  .sysUrl-box {
    text-align: left;
    height: 36px;
    line-height: 36px;
    font-size: 20px;
    font-weight: bold;
  }
</style>
