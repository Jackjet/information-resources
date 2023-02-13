<template>
  <el-dialog
    :title="thisTit"
    :visible.sync="isShow"
    width="60%"
    :modal-append-to-body='false'
    append-to-body
    :before-close="CloseModal"
    v-loading='loading'>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
      <el-form-item
        prop="name"
        label="路由名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.name" placeholder="请输入路由名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="sourcePath"
        label="路由地址"
        :label-width="this.formLabelWidth"
        class='InpitWidth selectCss'>
        <el-input clearable autocomplete="off" v-model="ruleForm.sourcePath" placeholder="请输入路由地址"></el-input>
      </el-form-item>
      <el-form-item
        prop="sourceMethod"
        label="路由请求类型"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select clearable v-model="ruleForm.sourceMethod" placeholder='请选择路由请求类型' class='SelectWidth' style='width: 100%; float: left'>
          <el-option label='GET' value='GET'></el-option>
          <el-option label='PUT' value='PUT'></el-option>
          <el-option label='DELETE' value='DELETE'></el-option>
          <el-option label='POST' value='POST'></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="targetPath"
        label="服务地址"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.targetPath" placeholder="例:http://xxxxxxx，# 为非法字符"></el-input>
      </el-form-item>
      <el-form-item
        prop="targetMethod"
        label="服务请求类型"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select clearable v-model="ruleForm.targetMethod" placeholder='请选择服务请求类型' class='SelectWidth' style='width: 100%; float: left'>
          <el-option label='GET' value='GET'></el-option>
          <el-option label='PUT' value='PUT'></el-option>
          <el-option label='DELETE' value='DELETE'></el-option>
          <el-option label='POST' value='POST'></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="accessStrategyId"
        label="访问控制策略"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
         <el-select placeholder="请选择访问控制策略" clearable v-model="ruleForm.accessStrategyId" class='SelectWidth' style='width: 100%; float: left'>
          <el-option
            v-for="item in visitList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="flowStrategyId"
        label="流量控制策略"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
         <el-select placeholder="请选择流量控制策略" clearable v-model="ruleForm.flowStrategyId" class='SelectWidth' style='width: 100%; float: left'>
          <el-option
            v-for="item in flowList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="remark"
        label="描述"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable type="textarea" :rows="3" autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注信息"></el-input>
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
    let isHTTP = (rule, value, callback) => {
      const idReg = /^http(s)?:\/\/.+/
      if (!value) {
        return callback(new Error('服务地址不能为空'))
      }
      setTimeout(() => {
        if (idReg.test(value)) {
          callback()
        } else {
          callback(new Error('服务地址需要以http://开头'))
        }
      }, 100)
    }
    return {
      integrationId: this.common.session('currentUser').id,
      appid: '',
      thisTit: '',
      visitList: [],
      flowList: [],
      ruleForm: {
        name: '',
        sourcePath: '',
        sourceMethod: '',
        targetPath: '',
        targetMethod: '',
        accessStrategyId: '',
        flowStrategyId: '',
        remark: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入路由名称',
          trigger: ['blur', 'change']
        }],
        sourceMethod: [{
          required: true,
          message: '请选择路由方法',
          trigger: ['blur', 'change']
        }],
        sourcePath: [{
          required: true,
          message: '请输入路由地址',
          trigger: ['blur', 'change']
        }],
        targetMethod: [{
          required: true,
          message: '请选择服务方法',
          trigger: ['blur', 'change']
        }],
        targetPath: [{
          required: true,
          validator: isHTTP,
          trigger: 'blur'
        }],
        accessStrategyId: [{
          required: true,
          message: '请选择访问控制策略',
          trigger: ['blur', 'change']
        }],
        flowStrategyId: [{
          required: true,
          message: '请选择流量控制策略',
          trigger: ['blur', 'change']
        }]
      },
      isAdd: '',
      loading: false,
      formLabelWidth: '150px',
      isShow: false
    }
  },

  methods: {
    initial (appid, row) {
      const that = this
      that.appid = appid
      that.askFlow()
      that.askVisit()
      if (row) {
        that.thisTit = '编辑路由'
        that.isAdd = true
        that.askInfo(row)
      } else {
        that.thisTit = '添加路由'
        that.isAdd = false
      }
      that.isShow = true
    },

    async askInfo (row) {
      const that = this
      try {
        let findUrl = that.Interface.apiRoute.findById
        let parameter = {id: row}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          that.ruleForm = response.data.data
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },

    async askFlow () {
      const that = this
      try {
        let url = this.Interface.flowStrategy.find + '?page=1&size=1000'
        that.Loading = true
        const response = await this.request.dataGet(that, url, {integrationId: that.integrationId, sysServiceId: that.appid})
        that.Loading = false
        if (response.data.code === 1) {
          that.flowList = response.data.data.content
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },

    async askVisit () {
      const that = this
      try {
        let url = this.Interface.accessStrategy.find + '?page=1&size=1000'
        that.loading = true
        const response = await this.request.dataGet(that, url, {integrationId: that.integrationId, sysServiceId: that.appid})
        that.loading = false
        if (response.data.code === 1) {
          that.visitList = response.data.data.content
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
      that.ruleForm.name = ''
      that.ruleForm.sourcePath = ''
      that.ruleForm.sourceMethod = ''
      that.ruleForm.targetPath = ''
      that.ruleForm.targetMethod = ''
      that.ruleForm.accessStrategyId = ''
      that.ruleForm.flowStrategyId = ''
      that.ruleForm.remark = ''
      that.ruleForm.integrationId = ''
    },

    Save (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        let pos = that.ruleForm.targetPath.indexOf("#")
        if (pos > -1) {
          this.$message.error("服务地址中包含非法字符")
          return false
        }
        if (!that.isAdd) {
          that.ruleForm.integrationId = that.integrationId
          that.add(that.ruleForm)
        } else {
          that.edit(that.ruleForm)
        }
      })
    },

    // 添加方法
    async add (data) {
      const that = this
      data.sysServiceId = that.appid
      let url = that.Interface.apiRoute.insert
      // POST请求添加轮播图数据
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
      let url = that.Interface.apiRoute.update
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
