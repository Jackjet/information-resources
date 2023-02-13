<template>
  <el-dialog
    :title="thisTit"
    :visible.sync="isShow"
    width="60%"
    :modal-append-to-body='false'
    append-to-body
    :before-close="CloseModal"
    v-loading='loading'>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="flowForm">
      <el-form-item
        prop="name"
        label="策略名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.name" placeholder="请输入策略名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="time"
        label="限制时长"
        :label-width="this.formLabelWidth"
        class='InpitWidth danwei'>
        <el-input clearable autocomplete="off" v-model="ruleForm.time" placeholder="请输入天数"></el-input>
        <div class="tian">天</div>
      </el-form-item>
      <el-form-item
        prop="apiFlowLimit"
        label="API访问限制次数"
        :label-width="this.formLabelWidth"
        class='InpitWidth danwei'>
        <el-input clearable autocomplete="off" v-model="ruleForm.apiFlowLimit" placeholder="-1为不限次数"></el-input>
        <div class="tian">次</div>
      </el-form-item>
      <el-form-item
        prop="type"
        label="限制类型"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select clearable v-model="ruleForm.type" placeholder='请选择限制类型' style='width: 100%; float: left' @change="flowType">
          <el-option label='请选择限制类型' value='0'></el-option>
          <el-option label='限制IP' value='2'></el-option>
          <el-option label='限制开发者' value='1'></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="ipFlowLimit"
        label="单个IP流量限制次数"
        v-show="isIP&&isFlowLimit"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.ipFlowLimit" placeholder="请输入限制次数"></el-input>
      </el-form-item>
      <el-form-item
        prop="appIdFlowLimit"
        label="单个账号流量限制次数"
        v-show="!isIP&&isFlowLimit"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.appIdFlowLimit" placeholder="请输入限制次数"></el-input>
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
      integrationId: this.common.session('currentUser').id,
      appid: '',
      thisTit: '',
      isIP: false,
      ruleForm: {
        name: '',
        time: '',
        apiFlowLimit: '',
        type: '',
        ipFlowLimit: '',
        appIdFlowLimit: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入策略名称',
          trigger: ['blur', 'change']
        }],
        time: [{
          required: true,
          message: '请输入限制时长',
          trigger: ['blur', 'change']
        }],
        apiFlowLimit: [{
          required: true,
          message: '请输入API访问限制次数',
          trigger: ['blur', 'change']
        }]
      },
      isAdd: '',
      loading: false,
      formLabelWidth: '160px',
      isShow: false,
      isFlowLimit: false
    }
  },

  methods: {
    initial (appid, row) {
      const that = this
      that.appid = appid
      if (row) {
        that.thisTit = '编辑流量控制策略'
        that.isAdd = true
        that.askInfo(row)
      } else {
        that.thisTit = '添加流量控制策略'
        that.isAdd = false
        that.isFlowLimit = false
      }
      that.isShow = true
    },

    async askInfo (row) {
      const that = this
      try {
        let findUrl = that.Interface.flowStrategy.findById
        let parameter = {id: row}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          that.isFlowLimit = true
          if (response.data.data.type === '1') {
            that.isIP = false
          } else if (response.data.data.type === '2') {
            that.isIP = true
          } else {
            that.isIP = false
            that.isFlowLimit = false
          }
          that.ruleForm = response.data.data
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },

    flowType (a) {
      const that = this
      that.isFlowLimit = true
      if (a === '1') {
        that.isIP = false
      } else if (a === '2') {
        that.isIP = true
      } else {
        that.isIP = false
        that.isFlowLimit = false
      }
    },

    CloseModal () {
      const that = this
      that.isShow = false
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.name = ''
      that.ruleForm.time = ''
      that.ruleForm.apiFlowLimit = ''
      that.ruleForm.type = ''
      that.ruleForm.ipFlowLimit = ''
      that.ruleForm.appIdFlowLimit = ''
      that.ruleForm.integrationId = ''
    },

    Save (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        if (!this.isAdd) {
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
      let url = that.Interface.flowStrategy.insert
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
      let url = that.Interface.flowStrategy.update
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
  .flowForm {
    width: 90%;
    margin: 0 auto;
  }
  .InpitWidth {
    width: 90%;
    min-width: 302px;
  }
  .danwei {
    position: relative;

    .tian {
      text-align: left;
      position: absolute;
      left: 105%;
      top: 0;
    }
  }
</style>
