<template>
  <el-main>
    <el-col :span="18">
      <!-- <div>测试数据:{{ msg }}</div> -->
      <el-form ref="form" label-width="240px">
        <el-form-item v-for="(item, index) in form" :key="index" :label="item.configName">
          <el-input autocomplete="off" v-model="item.configValue"></el-input>
        </el-form-item>
        <el-form-item class="otherBtnBox">
          <el-button @click="close" icon="el-icon-close">取 消</el-button>
          <el-button type="primary" @click="save()" icon="el-icon-check">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-main>
</template>

<script>
export default {

  data () {
    return {
      msg: '',
      form: []
    }
  },
  mounted () {
    this.getList()
  },
  methods: {
    async getList () {
      const that = this
      try {
        let url = this.Interface.managementConfig.findAll
        const response = await this.request.dataGet(that, url, {})
        that.form = response.data.data
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    close () {
      const that = this
      that.$confirm('是否取消?', '提示', {
        type: 'warning'
      }).then(() => {
        this.getList()
      }).catch(() => {
        return false
      })
    },
    async save () {
      let configKey = ''
      let that = this
      let object = {}
      try {
        this.form.forEach(item => {
          if (!item.configValue) {
            configKey = item.configKey
            throw new Error('configKey')
          }
          object[item.configKey] = item.configValue
        })
      } catch (e) {
        if (e.message !== 'configKey') throw e
      };
      if (configKey) {
        this.$message.error('请填写' + configKey)
        return false
      }
      try {
        let url = that.Interface.managementConfig.update
        let response = await this.request.dataPut(that, url, object)
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)

          that.getList()
          return false
        }
        that.$message.error(response.data.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    }
  }
}
</script>
<style lang='scss'>
  .otherBtnBox {
    .el-form-item__content {
      margin-left: 0 !important;
    }
  }
</style>
