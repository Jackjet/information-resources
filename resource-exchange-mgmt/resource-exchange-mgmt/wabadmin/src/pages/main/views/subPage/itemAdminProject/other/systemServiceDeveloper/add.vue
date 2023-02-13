<template>
  <el-dialog
    :title="thisTit"
    :visible.sync="isShow"
    width="40%"
    :modal-append-to-body='false'
    append-to-body
    :before-close="CloseModal"
    v-loading='loading'>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" :label-width="formLabelWidth">
      <el-form-item label="开发者名称" prop="name">
        <el-input v-model="ruleForm.name" placeholder = '请输入开发者名称' :disabled = 'disabled' autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="创建方式" prop="isAutomatic" v-if="isAdd">
        <el-select v-model="ruleForm.isAutomatic" @change="selectChange" style='float: left'>
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="开发者id" prop="appid" v-if="!isAdd || (isAdd && ruleForm.isAutomatic === '0')">
        <el-input v-model="ruleForm.appid" placeholder = '请输入开发者id' :disabled = 'disabled' autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="开发者密钥" prop="appkey" v-if="!isAdd || (isAdd && ruleForm.isAutomatic === '0')">
        <el-input v-model="ruleForm.appkey" placeholder = '请输入开发者密钥' :disabled = 'disabled' autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" v-model="ruleForm.remark" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="CloseModal" icon="el-icon-close">取 消</el-button>
      <el-button type="primary" @click="Save('ruleForm')" icon="el-icon-check">保 存</el-button>
    </span>
  </el-dialog>
</template>

<script>

export default {
  data () {
    return {
      thisTit: '',
      ruleForm: {
        id: '',
        integrationId: '',
        name: '',
        appid: '',
        appkey: '',
        remark: '',
        isAutomatic: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入开发者名称',
          trigger: ['blur', 'change']
        }],
        appid: [],
        appkey: [],
        isAutomatic: []
      },
      options: [{
        value: '0',
        label: '手动填写ID和密钥'
      },
      {
        value: '1',
        label: '自动生成ID和密钥'
      }],
      isAdd: '',
      loading: false,
      formLabelWidth: '120px',
      isShow: false,
      disabled: false
    }
  },

  methods: {
    initial (row) {
      const that = this
      if (row) {
        that.thisTit = '编辑开发者'
        that.disabled = true
        that.isAdd = false
        that.rules.isAutomatic = []
        that.askInfo(row)
      } else {
        that.thisTit = '添加开发者'
        that.disabled = false
        that.isAdd = true
        that.rules.isAutomatic = [{
          required: true,
          message: '请选择创建方式',
          trigger: ['blur', 'change']
        }]
      }
      that.isShow = true
    },

    async askInfo (row) {
      const that = this
      try {
        let findUrl = that.Interface.sysUser.findById
        let parameter = {id: row}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          that.ruleForm.id = response.data.data.id
          that.ruleForm.name = response.data.data.name
          that.ruleForm.appid = response.data.data.appid
          that.ruleForm.appkey = response.data.data.appkey
          that.ruleForm.remark = response.data.data.remark
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },

    CloseModal () {
      const that = this
      that.isShow = false
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.id = ''
      that.ruleForm.integrationId = ''
      that.ruleForm.name = ''
      that.ruleForm.appid = ''
      that.ruleForm.appkey = ''
      that.ruleForm.remark = ''
      that.ruleForm.isAutomatic = ''
      that.rules.isAutomatic = []
    },

    Save (ruleForm) {
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
      let url = that.Interface.sysUser.insert
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
        that.CloseModal()
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
      let url = that.Interface.sysUser.update
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
        that.CloseModal()
        // 重新触发父组件加载loading
        that.$emit('Reload')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    // 监听select
    selectChange () {
      if (this.ruleForm.isAutomatic === '0') {
        this.rules.appid = [{
          required: true,
          message: '请输入开发者id',
          trigger: ['blur', 'change']
        }]
        this.rules.appkey = [{
          required: true,
          message: '请输入开发者密钥',
          trigger: ['blur', 'change']
        }]
      } else {
        this.rules.appid = []
        this.rules.appkey = []
      }
    }
  }
}
</script>

<style lang="scss">
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
