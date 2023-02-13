<template>
  <el-main v-loading="loading">
    <el-col :span="24" v-show="isTableShow">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="searchItem.key"
            placeholder="请输入key">
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
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-plus"
            @click="add">添加
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-s-promotion"
            @click="syncConfig">发布
          </el-button>
        </el-form-item>
<!--        <el-form-item>-->
<!--          <el-button-->
<!--            type="primary"-->
<!--            size='medium'-->
<!--            @click="apiFile">API文档-->
<!--          </el-button>-->
<!--        </el-form-item>-->
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
    <!-- 调用分页 -->
    <el-col :span='24' v-show="isTableShow">
      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
    </el-col>
    <!-- 调用操作弹层 -->
    <add ref="callDetail"
      @Reload = 'getList'>
    </add>

    <!-- 调用操作弹层 -->
    <debugDialog ref="debugDialog"
         @Reload = 'getList'>
    </debugDialog>

    <accessControl v-show="!isTableShow" ref="accessControl" @tabTablt="tabTablt"></accessControl>

  </el-main>
</template>

<script>
import tableForm from './tableList'
import add from './add'
import debugDialog from './debug'
import * as session from '../../../../../../../../assets/js/common'
import paging from '../../../../../integration/pagination'
import accessControl from './accessControl'
export default {
  components: { add, tableForm, accessControl, debugDialog, paging },
  data () {
    return {
      loading: false,
      tableData: [],
      searchItem: {
        integrationId: '',
        sysServiceId: '',
        key: ''
      },
      tableHeader: [{
        id: false,
        type: '',
        label: 'Key',
        list: 'key'
      },
      {
        id: false,
        type: '',
        label: 'Value示例',
        list: 'value'
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
        options: [{
          label: '编辑',
          key: 0,
          type: 'edit',
          icon: 'el-icon-document',
          State: false,
          method: (row) => {
            this.edit(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'delete',
          icon: 'el-icon-document',
          State: false,
          method: (row) => {
            this.delete(row)
          }
        },
        {
          label: '调试',
          key: 0,
          type: 'debug',
          icon: 'el-icon-document',
          State: false,
          method: (row) => {
            this.debug(row)
          }
        },
        {
          label: '访问控制',
          key: 0,
          type: 'accessControl',
          icon: 'el-icon-document',
          State: false,
          method: (row) => {
            this.isTableShow = false
            this.accessControl(row)
          }
        }],
        title: '',
        isShow: false,
        rules: {
        }
      },
      Loading: false,
      total: 0,
      pageSize: 20,
      currentPage: 1,
      tableSelection: {},
      isTableShow: true,
      isOnline: ''
    }
  },

  methods: {
    async initial (sid, isOnline) {
      this.searchItem.sysServiceId = sid
      this.searchItem.integrationId = session.default.session('currentUser').id
      this.isOnline = isOnline
      await this.getList()
    },

    async search () {
      await this.getList()
    },

    async getList () {
      const that = this
      try {
        let findUrl = that.Interface.keys.findAll
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        that.loading = true
        const response = await that.request.dataGet(that, url, that.searchItem)
        that.loading = false
        if (response.data.code === 1) {
          that.tableData = response.data.data.content
          that.total = response.data.data.totalElements
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (e) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },
    add () {
      this.$refs.callDetail.initial(this.searchItem.sysServiceId, true)
    },
    edit (row) {
      this.$refs.callDetail.initial(row.id, false)
    },
    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.getList()
    },
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let findUrl = that.Interface.keys.delete + '/' + row.id
        const response = await that.request.dataDeleteById(that, findUrl)
        if (response.data.code === 1) {
          that.$message.success('数据删除成功')
          this.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    debug (row) {
      this.$refs.debugDialog.initial(row.id, this.searchItem.sysServiceId, row.key, row.value)
    },
    accessControl (row) {
      console.log(row)
      this.$refs.accessControl.initial(row.key, this.searchItem.sysServiceId, parseInt(row.expire))
    },
    // 切换表格
    tabTablt () {
      this.isTableShow = true
      this.getList()
    },
    async syncConfig () {
      const that = this
      if (that.isOnline === 'offline') {
        that.$message.error('该系统服务离线，不允许同步key')
        return false
      } else if (that.isOnline !== 'online') {
        that.$message.error('该系统服务未安装，不允许同步key')
        return false
      }
      try {
        let url = that.Interface.keyConfig.syncKeys
        let parameter = {
          integrationId: that.searchItem.integrationId,
          sysServiceId: that.searchItem.sysServiceId
        }
        const response = await that.request.dataPut(that, url, parameter)
        if (response.data.code === 1) {
          that.$message.success('同步成功')
          this.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (e) {
        that.$message.error('同步失败')
      }
    },
    apiFile () {

    }
  }
}
</script>

<style scoped>

</style>
