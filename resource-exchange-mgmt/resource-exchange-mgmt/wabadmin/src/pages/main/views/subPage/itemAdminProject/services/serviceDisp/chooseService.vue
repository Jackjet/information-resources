<template>
  <el-dialog
    title="服务选择"
    :visible.sync="isShow"
    width="80%"
    :modal-append-to-body='false'
    append-to-body
    v-loading='loading'>
    <!-- 调用表格 -->
    <el-col :span="24">
      <tableForm ref="chooseTable" :table-data='tableData'
                 :table-selection="tableSelection"
                 @onHandleTemplateRow="change"
                 :table-label="tableHeader"
                 :table-option="tableOption">
      </tableForm>
    </el-col>
    <span slot="footer" class="dialog-footer">
      <el-button @click="CloseModal" icon="el-icon-close">取 消</el-button>
      <el-button type="primary" @click="Save" icon="el-icon-check">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>

  import tableForm from '../../../../integration/tableList'

  export default {
    components: {tableForm},
    data () {
      return {
        integrationId: this.common.session('currentUser').id,
        id: '',
        chooseArr: '',
        tableData: [],
        templateId: '',
        tableSelection: {
          key: true,
          type: 'radioService'
        },
        tableHeader: [{
          id: false,
          type: '',
          label: '服务名称',
          list: 'name'
        },
          {
            id: false,
            type: '',
            label: '备注',
            list: 'remark'
          }],
        tableOption: {
          label: '操作',
          value: 11,
          options: []
        },
        loading: false,
        isShow: false
      }
    },

    methods: {
      initial (sid, templateId) {
        const that = this
        that.id = sid
        this.templateId = templateId
        that.isShow = true
        that.askInfo()
      },
      // 初始化获取当前全部列表数据
      async askInfo () {
        const that = this
        try {
          let url = this.Interface.SysServiceInstallPack.findAllVersion
          let obj = {
            type: 'webapi'
          }
          that.Loading = true
          const response = await this.request.dataGet(that, url, obj)

          that.Loading = false
          if (response.data.code === 1) {
            let data = response.data.data
            data.forEach((item, index) => {
              if (item.id === that.templateId) {
                this.$refs.chooseTable.chooseRadioService(item.id)
                this.chooseArr = item.id
              }
            })
            that.tableData = response.data.data
          } else {
            that.$message.error('数据获取失败')
          }
        } catch (even) {
          that.Loading = false
          that.$message.error('数据获取失败')
        }
      },

      change (val) {
        console.log(val)
        this.chooseArr = val
      },

      CloseModal () {
        const that = this
        that.isShow = false
        this.$refs.chooseTable.clearRadioService()
        that.chooseArr = ''
      },

      Save () {
        const that = this
        if (that.chooseArr !== '') {
          let data = {
            appid: that.id,
            templateId: that.chooseArr.id
          }
          that.edit(data)
        } else {
          that.$message.warning('最少需要选择一条数据')
        }
      },
      // 修改方法
      async edit (data) {
        const that = this
        try {
          let url = that.Interface.sysServiceConfig.update
          // PUT 请求修改数据
          that.loading = true
          const response = await that.request.dataPut(that, url, data)
          that.loading = false
          if (response.data.code === 1) {
            // 添加成功后 回调效果
            that.$message.success('保存成功')

            that.CloseModal()
            that.$emit('Reload')
          } else {
            // 添加上传失败后 回调失败信息
            that.$message.error(response.data.msg)
            return false
          }
        } catch (even) {
          that.$message.error('数据获取失败')
        }
      },

      // 获取搜索当前分页 页码 条数
      pageChange (item) {
        const that = this
        that.currentPage = item.page
        that.pageSize = item.limit
        that.initial()
      }
    }
  }
</script>

<style lang="scss">
  .InpitWidth {
    width: 90%;
    min-width: 302px;
  }
</style>
