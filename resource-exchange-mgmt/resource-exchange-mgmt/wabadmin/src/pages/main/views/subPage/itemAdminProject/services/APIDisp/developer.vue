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
            icon="el-icon-plus"
            @click="add">添加
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
            size='medium'
            icon="el-icon-s-promotion"
            @click="syncUser">同步开发者
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
    <el-dialog
      title="添加开发者"
      :visible.sync="dialogEdit"
      :before-close="closeEdit"
      width="40%">
      <el-form :model="ruleForm" ref="ruleForm" :rules="rules">
        <el-form-item
          prop="appid"
          label="开发者id"
          label-width="120px">
          <el-input clearable autocomplete="off" v-model="ruleForm.appid" placeholder="请输入开发者id"></el-input>
        </el-form-item>
        <el-form-item
          prop="appkey"
          label="开发者密钥"
          label-width="120px">
          <el-input clearable autocomplete="off" v-model="ruleForm.appkey" placeholder="请输入开发者密钥"></el-input>
        </el-form-item>
        <el-form-item
          prop="remark"
          label="备 注"
          label-width="120px">
          <el-input clearable type="textarea" :rows="3" autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEdit">取 消</el-button>
        <el-button type="primary" @click="save('ruleForm')">保 存</el-button>
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
      sysUrl: 'http://47.105.140.86:61613',
      appid: '',
      dialogEdit: false,
      ruleForm: {
        appid: '',
        appkey: '',
        remark: ''
      },
      rules: {
        appid: [{
          required: true,
          message: '请输入开发者id',
          trigger: ['blur', 'change']
        }],
        appkey: [{
          required: true,
          message: '请输入开发者密钥',
          trigger: ['blur', 'change']
        }]
      },
      tableData: [],
      SearchItem: {
        appid: ''
      },
      lastSearch: {
        appid: ''
      },
      tableSelection: {
        key: false,
        type: 'selection'
      },
      tableHeader: [{
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
      }],
      tableOption: {
        label: '操作',
        value: 0,
        options: [{
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
        let findUrl = this.Interface.apiRouteUser.find
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
          that.tableData = Data.content
          that.total = Data.totalElements
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.Loading = false
        that.$message.error('数据获取失败')
      }
    },

    add: function () {
      this.dialogEdit = true
    },

    save (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate(async (index) => {
        if (index === false) {
          return false
        }
        let url = that.Interface.apiRouteUser.insert
        let data = {
          appid: that.ruleForm.appid,
          appkey: that.ruleForm.appkey,
          remark: that.ruleForm.remark,
          integrationId: that.integrationId,
          sysServiceId: that.appid
        }
        // POST请求添加轮播图数据
        that.Loading = true
        const response = await that.request.dataPost(that, url, data)
        that.Loading = false
        if (response.data.code === 1) {
          // 添加成功后 回调效果
          that.$message.success(response.data.msg)
          that.closeEdit()
          that.askInfo()
        } else {
          that.Loading = false
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      })
    },

    closeEdit () {
      const that = this
      that.dialogEdit = false
      that.$refs['ruleForm'].resetFields()
      that.id = ''
      that.key = ''
      that.remark = ''
    },

    // 删除当前单条数据 重载列表
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.apiRouteUser.delete
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

    async syncUser () {
      const that = this
      try {
        let url = this.Interface.apiRouteUser.syncApiRouteUser
        that.Loading = true
        const response = await this.request.dataPut(that, url, {integrationId: that.integrationId, sysServiceId: that.appid})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('同步成功')
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.Loading = false
        that.$message.error('数据获取失败')
      }
    },

    more (row) {
      window.open(row.link + '/readme.md', '_blank')
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
