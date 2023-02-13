<template>
  <el-main v-loading="Loading">
    <el-col :span="24">
      <el-button type="primary" style="display: block;margin-bottom: 20px;" @click="back" icon="el-icon-back">返 回</el-button>
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-date-picker
            v-model="valueTime"
            size="medium"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-select placeholder="请选择打包结果"
            :popper-append-to-body='false'
            clearable v-model="SearchItem.status"
            size="medium">
            <el-option label="成功" value="success"></el-option>
            <el-option label="排队中" value="waiting"></el-option>
            <el-option label="打包中" value="building"></el-option>
            <el-option label="失败" value="fail"></el-option>
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
            icon="el-icon-present"
            @click="toPack">打包
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
    <el-dialog
      title="编辑备注"
      :visible.sync="dialogRemark"
      :before-close="closeRemark"
      width="30%">
      <el-form>
        <el-form-item
          prop="remark"
          label="备 注"
          label-width="60px">
          <el-input clearable type="textarea" :rows="3" autocomplete="off" v-model="dialogData.remark" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeRemark">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>

<script>
import tableForm from '../../../../integration/tableList'
import paging from '../../../../integration/pagination'

export default {
  components: { tableForm, paging },

  data () {
    return {
      integrationId: this.common.session('currentUser').id,
      id: '',
      nodeID: '',
      type: '',
      nowNum: '1.0.0',
      valueTime: '',
      dialogRemark: false,
      dialogData: {
        remark: '',
        id: ''
      },
      tableData: [],
      SearchItem: {
        startTime: '',
        endTime: '',
        status: ''
      },
      lastSearch: {
        startTime: '',
        endTime: '',
        status: ''
      },
      tableSelection: {
        key: false,
        type: 'selection'
      },
      tableHeader: [{
        id: false,
        type: '',
        label: '版本号',
        list: 'version'
      },
      {
        id: false,
        type: '',
        label: '打包类别',
        list: 'type'
      },
      {
        id: false,
        type: 'btnToPack',
        label: '打包结果',
        list: 'status',
        method: (row) => {
          this.cause(row)
        }
      },
      {
        id: false,
        type: '',
        label: '开始时间',
        list: 'startTime'
      },
      {
        id: false,
        type: '',
        label: '结束时间',
        list: 'endTime'
      },
      {
        id: false,
        type: 'btn',
        label: '文档',
        list: 'down',
        method: (row) => {
          this.doc(row)
        }
      },
      {
        id: false,
        type: '',
        label: '备注',
        list: 'remark'
      }],
      tableOption: {
        label: '操作',
        value: 0,
        options: [{
          label: '编辑备注',
          key: 0,
          type: 'text',
          icon: 'el-icon-edit',
          State: false,
          method: (row) => {
            this.edit(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          State: false,
          method: (row) => {
            this.delete(row)
          }
        }]
      },
      stopTimer: null,
      Loading: false,
      total: 0,
      pageSize: 20,
      currentPage: 1
    }
  },

  mounted () {
    this.id = this.$route.query.id
    this.nodeID = this.$route.query.nodeID
    this.initial()
  },

  methods: {
    initial () {
      const that = this
      that.askInfo()
      that.stopTimer = setInterval(that.askInfo, 5000)
    },
    // 初始化获取当前全部列表数据
    async askInfo () {
      const that = this
      try {
        let findUrl = this.Interface.apiBuild.find
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
        })
        if (this.SearchItem.startTime) {
          that.valueTime = [this.SearchItem.startTime, this.SearchItem.endTime]
        } else {
          that.valueTime = []
        }
        that.SearchItem.appid = that.id
        that.SearchItem.integrationId = that.integrationId
        that.Loading = true
        const response = await this.request.dataGet(that, url, this.SearchItem)
        that.Loading = false
        if (response.data.code === 1) {
          // that.nowNum = response.data.data.version
          let Data = response.data.data
          Data.content.forEach((item, index) => {
            switch (item.status) {
              case 'success':
                item.status = '成功'
                item.down = '安装文档'
                break
              case 'waiting':
                item.status = '排队中'
                break
              case 'building':
                item.status = '打包中'
                break
              case 'fail':
                item.status = '失败'
                break
              default:
                // statements_def
                break
            }
            item.havedoc = that.type
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

    async toPack () {
      const that = this
      that.$confirm('是否打包?', '提示', {
        type: 'warning'
      }).then(async () => {
        let data = {
          appid: that.id,
          integrationId: that.integrationId
        }
        let url = that.Interface.apiBuild.insert
        const response = await this.request.dataPost(that, url, data)
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          that.askInfo()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

    doc (row) {
      let routeData = this.$router.resolve({
        name: 'documents',
        query: {
          url: row.installUrl
        }
      })
      window.open(routeData.href, '_blank')
    },

    async cause (row) {
      const that = this
      try {
        let url = this.Interface.cmdBuild.failedInfo
        that.Loading = true
        const response = await this.request.dataGet(that, url, {resultUrl: row.resultUrl})
        that.Loading = false
        if (response.data.code === 1) {
          window.open(response.data.data, '_blank')
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.Loading = false
        that.$message.error('数据获取失败')
      }
    },

    // 删除当前单条数据 重载列表
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.apiBuild.delete
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
    back () {
      this.$router.push({
        path: 'node',
        query: {
          id: this.nodeID
        }
      })
    },

    edit (row) {
      this.dialogData.remark = row.remark
      this.dialogData.id = row.id
      this.dialogRemark = true
    },

    async save () {
      const that = this
      let url = that.Interface.apiBuild.update
      // POST请求添加轮播图数据
      that.Loading = true
      const response = await that.request.dataPut(that, url, that.dialogData)
      that.Loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        that.dialogRemark = false
        that.closeRemark()
        that.askInfo()
      } else {
        that.Loading = false
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    closeRemark () {
      this.dialogRemark = false
      this.dialogData.id = ''
      this.dialogData.remark = ''
    },

    // 模糊查询
    search: function () {
      const that = this
      if (that.valueTime) {
        this.SearchItem.startTime = that.valueTime[0]
        this.SearchItem.endTime = that.valueTime[1]
      } else {
        this.SearchItem.startTime = ''
        this.SearchItem.endTime = ''
      }
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
  },
  beforeDestroy: function () {
    clearInterval(this.stopTimer)
  },
  destroyed: function () {
    clearInterval(this.stopTimer)
  }
}
</script>
<style lang="scss">
  .version-box {
    height: 36px;
    line-height: 36px;
    font-size: 20px;
    font-weight: bold;
  }
</style>
