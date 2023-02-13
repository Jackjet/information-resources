<template>
  <el-dialog
    title="选择开发者"
    :visible.sync="isShow"
    width="80%"
    :modal-append-to-body='false'
    append-to-body
    v-loading='Loading'>
    <!-- 调用表格 -->
    <el-col :span="24">
      <el-table ref="chooseTable" :data="tableData" tooltip-effect="dark" style="width: 100%" @selection-change="change">
        <el-table-column type="selection">
        </el-table-column>
        <el-table-column prop="appid" label="开发者appid">
        </el-table-column>
        <el-table-column prop="appkey" label="开发者密钥">
        </el-table-column>
        <el-table-column prop="remark" label="备注">
        </el-table-column>
      </el-table>
    </el-col>
    <span slot="footer" class="dialog-footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="Save('ruleForm')">保 存</el-button>
    </span>
  </el-dialog>
</template>

<script>

import tableForm from '../../../../../integration/tableList'

export default {
  components: { tableForm },
  data () {
    return {
      integrationId: this.common.session('currentUser').id,
      appid: '',
      chooseArr: [],
      tableData: [],
      Loading: false,
      isShow: false
    }
  },

  methods: {
    initial (row, appid) {
      const that = this
      that.isShow = true
      that.appid = appid
      that.askInfo(row)
    },
    // 初始化获取当前全部列表数据
    async askInfo (row) {
      const that = this
      let hasSelectList = row.split(',')
      try {
        let url = this.Interface.sysUser.findAll + '?page=1&size=1000'
        that.Loading = true
        const response = await this.request.dataGet(that, url, {integrationId: that.integrationId, sysServiceId: that.appid})
        that.Loading = false
        if (response.data.code === 1) {
          that.tableData = response.data.data.content
          that.$nextTick(() => {
            that.tableData.forEach(m => {
              if (hasSelectList.indexOf(m.appid) >= 0) {
                this.$refs.chooseTable.toggleRowSelection(m, true)
              }
            })
          })
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.Loading = false
        that.$message.error('数据获取失败')
      }
    },

    change (val) {
      this.chooseArr = val
    },

    CloseModal () {
      const that = this
      that.isShow = false
    },

    Save (ruleForm) {
      const that = this
      if (that.chooseArr.length > 0) {
        let val = []
        that.chooseArr.forEach((a, b) => {
          val.push(a.appid)
        })
        that.CloseModal()
        that.$emit('Reload', val.join(','))
      } else {
        that.$message.warning('最少需要选择一条数据')
      }
    }
  }
}
</script>

<style lang="scss">
  .routeForm {
    width: 70%;
  }
  .InpitWidth {
    width: 90%;
    min-width: 302px;
  }
  .selectCss {
    position: relative;

    .el-select {
      width: 50%;
      position: absolute;
      left: 105%;
      top: 0;
    }
  }
</style>
