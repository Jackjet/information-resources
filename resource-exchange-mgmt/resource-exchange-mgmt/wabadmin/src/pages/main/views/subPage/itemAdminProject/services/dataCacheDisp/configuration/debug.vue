<template>
  <el-dialog
    title="调试画面"
    :visible.sync="isShow"
    :before-close="closeDialog"
    :modal-append-to-body='false'
    width="60%"
    append-to-body
    v-loading='loading'>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" :label-width="formLabelWidth" class="DebugForm">
      <el-form-item label="GET URL" prop="getUrl" class="debugButtonCss">
        <template v-if="write">
          <el-input autocomplete="off" v-model="ruleForm.key">
            <template slot="prepend">/dcrun/cache/get?key=</template>
          </el-input>
        </template>
        <template v-else>
          <el-input autocomplete="off" disabled v-model="ruleForm.getUrl"></el-input>
        </template>
        <el-button type="primary" @click="sendGet" icon="el-icon-s-promotion">发 送</el-button>
      </el-form-item>
      <el-form-item label="SET URL" prop="postUrl" class="debugButtonCss">
        <template v-if="write">
          <el-input autocomplete="off" v-model="ruleForm.key">
            <template slot="prepend">/dcrun/cache/post?key=</template>
          </el-input>
        </template>
        <template v-else>
          <el-input autocomplete="off" disabled v-model="ruleForm.postUrl"></el-input>
        </template>
        <el-button type="primary" @click="sendPost" icon="el-icon-s-promotion">发 送</el-button>
      </el-form-item>
      <el-form-item label="Value" prop="value">
        <el-input type="textarea" rows="5" autocomplete="off" v-model="ruleForm.value"></el-input>
      </el-form-item>
      <el-form-item label="请求结果" prop="result">
        <el-input type="textarea" rows="5" autocomplete="off" v-model="ruleForm.result"></el-input>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      id: '',
      sid: '',
      url: '',
      isShow: false,
      data: '',
      title: '',
      loading: false,
      sysServiceId: '',
      ruleForm: {
        key: '',
        value: '',
        result: ''
      },
      rules: {
        expire: [{
          required: true,
          message: '请输入过期时间',
          trigger: ['blur', 'change']
        }],
        key: [{
          required: true,
          message: '请输入Key',
          trigger: ['blur', 'change']
        }]
      },
      formLabelWidth: '120px',
      write: false
    }
  },
  methods: {
    async initial (data, sid, key, value) {
      this.data = data
      this.sid = sid
      this.ruleForm.key = key
      this.write = false
      if (this.ruleForm.key.indexOf('*') > -1) {
        this.write = true
      }
      this.ruleForm.value = value
      await this.getAppInfo()
      this.ruleForm.getUrl = this.url + '/dcrun/cache/get?key=' + key
      this.ruleForm.postUrl = this.url + '/dcrun/cache/post?key=' + key
      this.isShow = true
    },
    async getAppInfo () {
      const that = this
      try {
        let findUrl = that.Interface.sysServiceConfig.find
        let parameter = {appid: that.sid}
        const response = await that.request.dataGet(that, findUrl, parameter)
        if (response.data.code === 1) {
          that.url = response.data.data.url
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },
    async sendGet () {
      const that = this
      try {
        const response = await that.request.dataGetWithAppid(that, that.url + '/dcrun/cache/get?key=' + that.ruleForm.key, that.sid)
        let result = '{' + '\n'
        result += '  code:' + response.data.code + '\n'
        result += '  msg:' + response.data.msg + '\n'
        result += '  data:' + response.data.data + '\n'
        result += '}'
        that.ruleForm.result = result
      } catch (e) {
        that.$message.error('调试失败')
      }
    },
    async sendPost () {
      const that = this
      try {
        if (!that.ruleForm.value || that.ruleForm.value === '') {
          that.$message.error('value值不能为空')
          return
        }
        const response = await that.request.dataPostWithAppid(that, that.url + '/dcrun/cache/post?key=' + that.ruleForm.key, that.sid, that.ruleForm.value)
        let result = '{' + '\n'
        result += '  code:' + response.data.code + '\n'
        result += '  msg:' + response.data.msg + '\n'
        result += '  data:' + response.data.data + '\n'
        result += '}'
        that.ruleForm.result = result
      } catch (e) {
        that.$message.error('调试失败')
      }
    },
    closeDialog () {
      const that = this
      that.isShow = false
      this.id = ''
      this.title = ''
      that.$refs['ruleForm'].resetFields()
    },
    async save (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        if (this.isAdd) {
          that.add(that.ruleForm)
        } else {
          that.edit(that.ruleForm)
        }
      })
    }
  }
}
</script>

<style lang="scss">
  .DebugForm {
    width: 80%;
  }
  .InpitWidth {
    width: 90%;
    min-width: 302px;
  }
  .debugButtonCss {
    position: relative;
    .el-button {
      position: absolute;
      left: 105%;
      top: 0;
    }
  }
</style>
