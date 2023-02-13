<template>
  <el-dialog
    :title="title"
    :visible.sync="isShow"
    :before-close="closeDialog"
    :modal-append-to-body='false'
    width="40%"
    append-to-body
    v-loading='loading'>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" :label-width="formLabelWidth" style="width: 90%">
      <el-form-item label="过期时间" prop="expire">
        <el-input autocomplete="off" v-model="ruleForm.expire">
          <template slot="append">S</template>
        </el-input>
      </el-form-item>
      <el-form-item label="Key" prop="key">
        <el-input autocomplete="off" v-model="ruleForm.key"></el-input>
      </el-form-item>
      <el-form-item label="Value示例" prop="value">
        <el-input type="textarea" autocomplete="off" v-model="ruleForm.value" placeholder='这里输入value示例和说明' :rows="8"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" icon="el-icon-close">取 消</el-button>
      <el-button type="primary" @click="save('ruleForm')" icon="el-icon-check">保 存</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      id: '',
      isShow: false,
      isAdd: true,
      title: '',
      loading: false,
      sysServiceId: '',
      ruleForm: {
        expire: 0,
        key: '',
        value: ''
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
      formLabelWidth: '120px'
    }
  },
  methods: {
    initial (data, isAdd) {
      const that = this
      that.title = 'Key定义'
      that.isShow = true
      that.isAdd = isAdd
      if (isAdd) {
        that.sysServiceId = data
      } else {
        that.id = data
        that.askInfo(data)
      }
    },
    async askInfo (row) {
      const that = this
      this.id = row
      try {
        let findUrl = that.Interface.keys.findById
        let parameter = {id: that.id}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          that.ruleForm = response.data.data
          if (that.ruleForm.isAutomatic === '0') {
            that.appidAndAppkeyDisabled = false
          }
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
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
    },

    // 添加方法
    async add (data) {
      const that = this
      data.integrationId = that.common.session('currentUser').id
      data.sysServiceId = that.sysServiceId
      let url = that.Interface.keys.insert
      // POST请求添加数据
      that.loading = true
      const response = await that.request.dataPost(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.closeDialog()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    // 修改方法
    async edit (data) {
      const that = this
      let url = that.Interface.keys.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.closeDialog()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    }
  }
}
</script>

<style scoped>

</style>
